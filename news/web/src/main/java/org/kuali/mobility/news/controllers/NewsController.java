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

package org.kuali.mobility.news.controllers;

import java.util.ArrayList;
import java.util.List;

import org.kuali.mobility.news.entity.NewsArticle;
import org.kuali.mobility.news.entity.NewsDay;
import org.kuali.mobility.news.entity.NewsSource;
import org.kuali.mobility.news.entity.NewsStream;
import org.kuali.mobility.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller 
@RequestMapping("/news")
public class NewsController {
	
	private static String selectedCampus = "BL"; //TODO: this should be replaced by the yet-to-be-created general campus selection handler
    
    @Autowired
    private NewsService newsService;
    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String newsHome(Model uiModel) {	
    	List<NewsSource> sources = newsService.getAllNewsSourcesByLocation(selectedCampus);
    	List<NewsStream> newsStreams = new ArrayList<NewsStream>();
    	String defaultSourceId = newsService.getDefaultNewsSourceId();
    	NewsArticle topNewsArticle = null; 
    	for (NewsSource source : sources) {
    		NewsStream news = newsService.getNewsStream(source.getSourceId(), true);
    		if (news != null) {
    			newsStreams.add(news);
    			if (source.getSourceId().equals(defaultSourceId)) {
    				if (news.getArticles() != null && !news.getArticles().isEmpty()) {
    					NewsDay day = news.getArticles().get(0);
    					if (day.getArticles() != null && !day.getArticles().isEmpty()) {
        					topNewsArticle = day.getArticles().get(0);
        				}
    				}
    			}
    		}
    	}
    	uiModel.addAttribute("newsStreams", newsStreams);
    	uiModel.addAttribute("topArticle", topNewsArticle);
    	uiModel.addAttribute("topArticleSourceId", defaultSourceId);
    	
    	return "news/newsHome";
    }
    
    @RequestMapping(value = "/{sourceId}", method = RequestMethod.GET)
    public String getNewsArticle(@PathVariable("sourceId") String sourceId, @RequestParam(value = "articleId", required = false) String articleId, @RequestParam(value = "referrer", required = false) String referrer, Model uiModel) {
    	if (articleId != null && articleId != "") {
    		NewsArticle newsArticle = newsService.getNewsArticle(sourceId, articleId);
    		NewsSource news = newsService.getNewsSourceById(sourceId);
        	uiModel.addAttribute("newsArticle", newsArticle);
        	uiModel.addAttribute("sourceId", sourceId);
        	uiModel.addAttribute("sourceTitle", news.getSourceName());
        	uiModel.addAttribute("referrer", referrer);
        	return "news/newsArticle";
    	} else {
    		NewsStream news = newsService.getNewsStream(sourceId, false);
        	uiModel.addAttribute("newsStream", news);
        	uiModel.addAttribute("sourceId", sourceId);
        	
        	List<NewsSource> sources = newsService.getAllNewsSourcesByLocation(selectedCampus);
        	uiModel.addAttribute("newsSources", sources);
        	
        	return "news/newsStream";
    	}
    }
}
