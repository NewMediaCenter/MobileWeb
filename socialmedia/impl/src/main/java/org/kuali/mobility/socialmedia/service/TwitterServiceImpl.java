/**
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.mobility.socialmedia.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.kuali.mobility.socialmedia.entity.Tweet;
import org.kuali.mobility.socialmedia.entity.TweetEntity;
import org.kuali.mobility.socialmedia.entity.TwitterFeed;
import org.springframework.stereotype.Service;

@Service
public class TwitterServiceImpl implements TwitterService {
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(TwitterServiceImpl.class);
	
	private static final long FEED_UPDATE_INTERVAL = 1000 * 60 * 5; //5 min
	private static final SimpleDateFormat twitterDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	
	private static ConcurrentMap<String, TwitterFeed> twitterFeeds;
	
	static {
		twitterFeeds = new ConcurrentHashMap<String, TwitterFeed>();
	}
	
	@Override
	public List<Tweet> retrieveCombinedFeeds(List<String> publicIds) {
		List<Tweet> tweets = new ArrayList<Tweet>();
		
		TwitterFeed feed;
		for (String id : publicIds) {
			feed = retrieveFeed(id);
			tweets.addAll(feed.getTweets());
		}
		
		Collections.sort(tweets);
		return tweets;
	}
	
	private TwitterFeed retrieveFeed(String publicId) {
		TwitterFeed feed = twitterFeeds.get(publicId);
		if (feed == null) {
			feed = new TwitterFeed();
			feed.setTwitterId(publicId);
			TwitterFeed existing = twitterFeeds.putIfAbsent(publicId, feed);
			if (existing == null) {
				updateFeed(feed);
			} else {
				feed = existing;
			}
		}
		Calendar now = Calendar.getInstance();
		if (now.getTimeInMillis() - feed.getLastUpdated() > FEED_UPDATE_INTERVAL) {
			updateFeed(feed);
		}
		return feed;
	}
	
	@SuppressWarnings("unchecked")
	private TwitterFeed updateFeed(TwitterFeed feedToUpdate) {
		try {
			LOG.info("Updating Twitter feed: " + feedToUpdate.getTwitterId());
			URL url = new URL("http://api.twitter.com/1/statuses/user_timeline.json?screen_name=" + feedToUpdate.getTwitterId() + "&exclude_replies=true&include_entities=true");
			URLConnection conn = url.openConnection();
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null)
			{
				sb.append(line);
			}
			rd.close();
			String json = sb.toString();
	
			JSONArray tweetArray = (JSONArray) JSONSerializer.toJSON(json);
			List<Tweet> tweets = new ArrayList<Tweet>();
			for (Iterator<JSONObject> iter = tweetArray.iterator(); iter.hasNext();) {
				try {
					JSONObject tweetObject = iter.next();
					JSONObject user = tweetObject.getJSONObject("user");
					JSONObject entities = tweetObject.getJSONObject("entities");
					JSONArray hashTags = entities.getJSONArray("hashtags");
					JSONArray urls = entities.getJSONArray("urls");
					JSONArray mentions = entities.getJSONArray("user_mentions");
					
					Tweet tweet = new Tweet();	
					tweet.setProfileImageUrl(user.getString("profile_image_url_https"));
					tweet.setScreenName(user.getString("screen_name"));
					tweet.setUserName(user.getString("name"));
					tweet.setId(tweetObject.getString("id_str"));
					
					String text = tweetObject.getString("text");
					text = fixEntities(text, hashTags, urls, mentions);
					tweet.setText(text);
					
					String publishDate = tweetObject.getString("created_at").replace("+0000", "GMT");
					tweet.setTimestamp(twitterDateFormat.parse(publishDate).getTime());
					
					tweets.add(tweet);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
			if (!tweets.isEmpty()) {
				feedToUpdate.setTweets(tweets);
				Calendar currentDate = Calendar.getInstance();
				feedToUpdate.setLastUpdated(currentDate.getTimeInMillis());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
		return feedToUpdate;
	}
	
	@SuppressWarnings("unchecked")
	private String fixEntities(String message, JSONArray hashTags, JSONArray urls, JSONArray mentions) {
		List<TweetEntity> entities = new ArrayList<TweetEntity>();
		for (Iterator<JSONObject> iter = hashTags.iterator(); iter.hasNext();) {
			JSONObject hashTag = iter.next();
			TweetEntity entity = new TweetEntity();
			
			JSONArray indices = hashTag.getJSONArray("indices");
			entity.setStartIndex(indices.getInt(0));
			entity.setEndIndex(indices.getInt(1));
			entity.setDisplayText(hashTag.getString("text"));
			entity.setLinkText("<a href=\"https://twitter.com/search?q=%23" + entity.getDisplayText() + "\">&#35;" + entity.getDisplayText() + "</a>");
			
			entities.add(entity);
		}
		for (Iterator<JSONObject> iter = urls.iterator(); iter.hasNext();) {
			JSONObject url = iter.next();
			TweetEntity entity = new TweetEntity();
			
			JSONArray indices = url.getJSONArray("indices");
			entity.setStartIndex(indices.getInt(0));
			entity.setEndIndex(indices.getInt(1));
			entity.setDisplayText(url.getString("url"));
			entity.setLinkText("<a href=\"" + entity.getDisplayText() + "\">" + entity.getDisplayText() + "</a>");

			entities.add(entity);
		}
		for (Iterator<JSONObject> iter = mentions.iterator(); iter.hasNext();) {
			JSONObject mention = iter.next();
			TweetEntity entity = new TweetEntity();
			
			JSONArray indices = mention.getJSONArray("indices");
			entity.setStartIndex(indices.getInt(0));
			entity.setEndIndex(indices.getInt(1));
			entity.setDisplayText(mention.getString("screen_name"));
			entity.setLinkText("<a href=\"http://twitter.com/" + entity.getDisplayText() + "\">&#64;" + entity.getDisplayText() + "</a>");
			entities.add(entity);
		}
		
		Collections.sort(entities);
		StringBuffer sb = new StringBuffer();
		int currentIndex = 0;
		for (TweetEntity entity : entities) {
			sb.append(message.substring(currentIndex, entity.getStartIndex()));
			sb.append(entity.getLinkText());
			currentIndex = entity.getEndIndex();
		}
		sb.append(message.substring(currentIndex));
		return sb.toString();
	}
	
//	public List<Tweet> getAllTweets(){
//		//
//	}
}
