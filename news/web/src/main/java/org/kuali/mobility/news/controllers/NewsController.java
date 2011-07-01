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

import java.util.List;

import org.kuali.mobility.news.entity.NewsArticle;
import org.kuali.mobility.news.entity.NewsSource;
import org.kuali.mobility.news.entity.NewsStream;
import org.kuali.mobility.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String getNewsStream(@RequestParam(value = "source", required = false) String source, Model uiModel) {
    	if (source == null || "".equals(source)) {
    		source = newsService.getDefaultNewsSourceCode();
    	}
    	NewsStream news = newsService.getNewsStream(source);
    	uiModel.addAttribute("newsStream", news);
    	
    	List<NewsSource> sources = newsService.getAllNewsSourcesByLocation(selectedCampus);
    	uiModel.addAttribute("newsSources", sources);
    	
    	return "news/newsStream";
    }
    
    @RequestMapping(value = "/article", method = RequestMethod.GET)
    public String getNewsArticle(@RequestParam(value = "id", required = true) String articleId, Model uiModel) {
    	NewsArticle newsArticle = newsService.getNewsArticle(articleId);
    	uiModel.addAttribute("newsArticle", newsArticle);
    	uiModel.addAttribute("category", "");
    	return "news/newsArticle";
    }
      
}
