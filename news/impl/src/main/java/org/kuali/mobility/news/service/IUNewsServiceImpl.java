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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kuali.mobility.news.dao.NewsDaoImpl;
import org.kuali.mobility.news.entity.NewsArticle;
import org.kuali.mobility.news.entity.NewsDay;
import org.kuali.mobility.news.entity.NewsSource;
import org.kuali.mobility.news.entity.NewsStream;
import org.kuali.mobility.news.entity.Rss;
import org.kuali.mobility.news.entity.RssCategory;
import org.kuali.mobility.news.entity.RssItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IUNewsServiceImpl implements NewsService {
  
	@Autowired
    private RssService rssService;
	
	@Autowired
	private NewsDaoImpl newsDao;
	
	@Override
	public List<NewsSource> getAllNewsSourcesByLocation(String campusCode){
		List<NewsSource> sources = newsDao.getAllActiveNewsSourcesByLocationCode(campusCode);
		return sources;
	}

	@Override
	public NewsStream getNewsStream(String rssShortCode) {
		// TODO Auto-generated method stub
		NewsSource newsSource = newsDao.getNewsSourceByCode(rssShortCode);
		Rss rss;
		try {
			rss = rssService.fetch(newsSource.getSourceUrl());
			return convertRssToNewsStream(rss);
		} catch (Exception e) {
			NewsStream testNewsStream = new NewsStream();
			final Writer result = new StringWriter();
		    final PrintWriter printWriter = new PrintWriter(result);
		    e.printStackTrace(printWriter);

		    NewsDay newsDay = new NewsDay();
		    List<NewsArticle> articles = new ArrayList<NewsArticle>();
		    newsDay.setArticles(articles);
		    newsDay.setFormattedDate("The Date");
			NewsArticle a = new NewsArticle();
			a.setDescription(e.getMessage() + "<br/><br />" + result.toString());
			a.setTitle("Error");
			articles.add(a);
			testNewsStream.getArticles().add(newsDay);
			return testNewsStream;
		}
	}

	@Override
	public NewsArticle getNewsArticle(String articleId) {
		// TODO Auto-generated method stub
		return null;
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
			Collection<String> categories = new ArrayList<String>();
			for (RssCategory category : item.getCategories()) {
				categories.add(category.getValue());
			}
			article.setCategories(categories);
			article.setThumbnailImageUrl(item.getEnclosureUrl());
			article.setDescription(item.getDescription());
			articles.add(article);
		}
		
		Collections.sort(articles);
		Collections.reverse(articles);
		
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
}
