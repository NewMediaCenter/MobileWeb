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

package org.kuali.mobility.news.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.kuali.mobility.news.dao.NewsDaoImpl;
import org.kuali.mobility.news.entity.LinkFeed;
import org.kuali.mobility.news.entity.MaintRss;
import org.kuali.mobility.news.entity.NewsArticle;
import org.kuali.mobility.news.entity.NewsDay;
import org.kuali.mobility.news.entity.NewsSource;
import org.kuali.mobility.news.entity.NewsStream;
import org.kuali.mobility.news.entity.Rss;
import org.kuali.mobility.news.entity.RssItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl implements NewsService {
	
	@Autowired
    private RssService rssService;
	
	@Autowired
	private NewsDaoImpl newsDao;
	
	@Autowired
    private RssCacheService rssCacheService;
	
	@Autowired
	private DynamicRssCacheService dynamicRssCacheService;
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(NewsServiceImpl.class);
	
	@Override
	public List<NewsSource> getAllNewsSourcesByLocation(String campusCode){
		List<NewsSource> sources = newsDao.getAllActiveNewsSourcesByLocationCode(campusCode);
		
		return sources;
	}

	@Override
	public NewsStream getNewsStream(String rssShortCode) {
		// TODO Auto-generated method stub
		MaintRss maintRss = this.getRssCacheService().getMaintRssByCampusAndShortCode("BL", rssShortCode);
        if (maintRss != null) {
			try {
	        	Rss rssFeed = this.getRssCacheService().getRssByMaintRssId(maintRss.getRssId());
	        	if (rssFeed != null) {
	        		return convertRssToNewsStream(rssFeed);
	        	}
	        } catch (Exception e) {
	            LOG.error(e.getMessage(), e);
	        }
        }
        return null;
		
//		NewsSource newsSource = rssService.getNewsSource(rssShortCode);
//		if (newsSource == null) {
//			newsSource = newsDao.getNewsSourceByCode(rssShortCode);
//			rssCacheService.putNewsSource(newsSource.getSourceCode(), newsSource);
//		}
//		NewsStream newsStream = newsSource.getNewsStream();
//		if (newsStream == null) {
//			Rss rss;
//			try {
//				rss = rssService.fetch(newsSource.getSourceUrl());
//				newsStream =  convertRssToNewsStream(rss);
//				newsSource.setNewsStream(newsStream);
//				return newsStream;
//			} catch (Exception e) {
//				return null;
//			}
//		} else {
//			return newsStream;
//		}
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
		article.setDescription(lf.getBodyText());
		article.setTitle(lf.getTitle());
		article.setPublishDate(new Timestamp(lf.getLastUpdate().getTime()));
		return article;
	}

	@Override
	public String getDefaultNewsSourceCode() {
		// TODO Auto-generated method stub
		return "top";
	}
	
	private NewsStream convertRssToNewsStream(Rss rss) {
		NewsStream newsStream = new NewsStream();
		
		newsStream.setTitle(rss.getTitle());
		newsStream.setDescription(rss.getFormDescription());
		
		List<NewsArticle> articles = new ArrayList<NewsArticle>();
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

	public NewsDaoImpl getNewsDao() {
		return newsDao;
	}

	public void setNewsDao(NewsDaoImpl newsDao) {
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
