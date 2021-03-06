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

package org.kuali.mobility.news.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsStream implements Serializable {

	private static final long serialVersionUID = 475441282491797666L;
	
	private String title;
	private String description;
	private String sourceId;
	private List<NewsDay> articles;
	
	public NewsStream copy() {
		NewsStream copy = new NewsStream();
		if (sourceId != null) {
			copy.setSourceId(new String(sourceId));
		}
		if (title != null) {
			copy.setTitle(new String(title));
		}
		if (description != null) {
			copy.setDescription(new String(description));
		}
		if (!articles.isEmpty()) {
			List<NewsDay> articlesCopy = new ArrayList<NewsDay>();
			for (NewsDay day : articles) {
				articlesCopy.add(day.copy());
			}
			copy.setArticles(articlesCopy);
		}
		return copy;
	}
    
	public NewsStream() {
		articles = new ArrayList<NewsDay>();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<NewsDay> getArticles() {
		return articles;
	}
	public void setArticles(List<NewsDay> articles) {
		this.articles = articles;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
}
