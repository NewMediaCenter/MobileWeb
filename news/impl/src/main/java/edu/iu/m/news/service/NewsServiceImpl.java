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

package edu.iu.m.news.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.news.dao.NewsDao;
import org.kuali.mobility.news.entity.NewsArticle;
import org.kuali.mobility.news.entity.NewsDay;
import org.kuali.mobility.news.entity.NewsSource;
import org.kuali.mobility.news.entity.NewsStream;
import org.kuali.mobility.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.iu.m.news.entity.LinkFeed;
import edu.iu.m.news.entity.MaintRss;
import edu.iu.m.news.entity.Rss;
import edu.iu.m.news.entity.RssItem;

@Service
@Transactional 
public class NewsServiceImpl implements NewsService {
	
	@Autowired
    private RssService rssService;
	
	@Autowired
	private NewsDao newsDao;
	
	@Autowired
    private RssCacheService rssCacheService;
	
	@Autowired
	private DynamicRssCacheService dynamicRssCacheService;
	
	@Autowired
	private ConfigParamService configParamService;
	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
	
	private static final String NEWS_DEFAULT_SOURCE_ID = "NEWS_DEFAULT_SOURCE_ID";
	private static final int SAMPLE_COUNT_DEFAULT = 2;
	private static final String SAMPLE_COUNT = "NEWS_SAMPLE_COUNT";
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(NewsServiceImpl.class);
	
	@Override
	public List<NewsSource> getAllNewsSourcesByLocation(String campusCode){
		List<NewsSource> sources = newsDao.getAllActiveNewsSourcesByLocationCode(campusCode);
		return sources;
	}
	
	@Override
	public NewsSource getNewsSourceById(String sourceId) {
		return newsDao.getNewsSourceById(sourceId);
	}

	@Override
	public NewsStream getNewsStream(String rssShortCode, boolean sample) {
		MaintRss maintRss = this.getRssCacheService().getMaintRssByCampusAndShortCode("BL", rssShortCode);
        if (maintRss != null) {
			try {
	        	Rss rssFeed = this.getRssCacheService().getRssByMaintRssId(maintRss.getRssId());
	        	if (rssFeed != null) {
	        		NewsStream stream = convertRssToNewsStream(rssFeed, sample);
	        		stream.setSourceId(rssShortCode);
	        		stream.setTitle(maintRss.getDisplayName());
	        		return stream;
	        	}
	        } catch (Exception e) {
	            LOG.error(e.getMessage(), e);
	        }
        }
        return null;
	}

	@Override
	public NewsArticle getNewsArticle(String sourceId, String articleId) {
		MaintRss maintRss = this.getRssCacheService().getMaintRssByCampusAndShortCode("BL", sourceId);
        if (maintRss != null) {
            Rss rss = this.getRssCacheService().getRssByMaintRssId(maintRss.getRssId());
            LinkFeed lf = this.getDynamicRssCacheService().getLinkFeed(articleId, rss);
            if (lf != null) {
            	return convertLinkFeedToArticle(lf);
            }        	
        }
		return null;
	}

	private NewsArticle convertLinkFeedToArticle(LinkFeed lf) {
		NewsArticle article = new NewsArticle();
		article.setArticleId(lf.getFeedUrl());
		//article.setDescription(lf.getRichBodyText());
		article.setDescription(lf.getBodyText());
		article.setTitle(lf.getTitle());
		return article;
	}

	@Override
	public String getDefaultNewsSourceId() {
		return configParamService.findValueByName(NEWS_DEFAULT_SOURCE_ID);
	}
	
	private NewsStream convertRssToNewsStream(Rss rss, boolean sample) {
		int sampleCount = SAMPLE_COUNT_DEFAULT;
		if (sample) {
			try {
				sampleCount = Integer.parseInt(configParamService.findValueByName(SAMPLE_COUNT));
			} catch (Exception e) {}
		}
		
		NewsStream newsStream = new NewsStream();
		
		newsStream.setTitle(rss.getTitle());
		newsStream.setDescription(rss.getFormDescription());
		
		List<NewsArticle> articles = new ArrayList<NewsArticle>();
		int count = 0;
		for (RssItem item : rss.getRssItems()) {
			NewsArticle article = new NewsArticle();
			article.setTitle(item.getTitle());
			article.setPublishDate(item.getPublishDate());
			//article.setArticleId(item.getRssItemId().toString());
			article.setLink(item.getLink());
			article.setThumbnailImageUrl(item.getEnclosureUrl());
			article.setDescription(item.getDescription());
			article.setArticleId(item.getLinkUrlEncoded());
			articles.add(article);
			count++;
			if (sample && count == sampleCount) break;
		}
		
		Collections.sort(articles); //sort the articles in ascending order
		Collections.reverse(articles); //reverse the list so the newest appears first
		
		List<NewsDay> dayArticles = new ArrayList<NewsDay>();
		
		Calendar date = Calendar.getInstance();
		Calendar previousDate = Calendar.getInstance();
		SimpleDateFormat longDate = new SimpleDateFormat("EEEEE, MMMMM dd");
		String groupDateString;
		String currentDateString = null;
		List<NewsArticle> articleListForDate = null;
		for (NewsArticle article : articles) {
			date.setTime(article.getPublishDate());
			groupDateString = longDate.format(date.getTime());
			if (!groupDateString.equals(currentDateString)) {
				if (currentDateString != null && currentDateString != "") {
					NewsDay newsDay = new NewsDay();
					newsDay.setArticles(articleListForDate);
					newsDay.setFormattedDate(currentDateString);
					dayArticles.add(newsDay);
				}
				articleListForDate = new ArrayList<NewsArticle>();
				currentDateString = groupDateString;
				previousDate.setTime(article.getPublishDate());
			}
			articleListForDate.add(article);
		}
		NewsDay newsDay = new NewsDay();
		newsDay.setArticles(articleListForDate);
		newsDay.setFormattedDate(currentDateString);
		dayArticles.add(newsDay);
		
		newsStream.setArticles(dayArticles);
		return newsStream;
	}

	
	public RssService getRssService() {
		return rssService;
	}

	public void setRssService(RssService rssService) {
		this.rssService = rssService;
	}

	public NewsDao getNewsDao() {
		return newsDao;
	}

	public void setNewsDao(NewsDao newsDao) {
		this.newsDao = newsDao;
	}

	public RssCacheService getRssCacheService() {
		return rssCacheService;
	}

	public void setRssCacheService(RssCacheService rssCacheService) {
		this.rssCacheService = rssCacheService;
	}

	public DynamicRssCacheService getDynamicRssCacheService() {
		return dynamicRssCacheService;
	}

	public void setDynamicRssCacheService(DynamicRssCacheService dynamicRssCacheService) {
		this.dynamicRssCacheService = dynamicRssCacheService;
	}
}
