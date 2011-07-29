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
			twitterFeeds.put(publicId, feed);
			updateFeed(feed);
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
			URL url = new URL("http://api.twitter.com/1/statuses/user_timeline.json?screen_name=" + feedToUpdate.getTwitterId() + "&exclude_replies=true&include_entities=false");
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
					Tweet tweet = new Tweet();
					
					JSONObject user = tweetObject.getJSONObject("user");
					tweet.setProfileImageUrl(user.getString("profile_image_url_https"));
					tweet.setScreenName(user.getString("screen_name"));
					tweet.setUserName(user.getString("name"));
					tweet.setId(tweetObject.getString("id_str"));
					tweet.setText(tweetObject.getString("text"));
					
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
	
//	public List<Tweet> getAllTweets(){
//		//
//	}
}
