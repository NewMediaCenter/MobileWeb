package org.kuali.mobility.athletics.entity;

import java.io.Serializable;
import java.util.List;

public class NewsData implements Serializable {

	private static final long serialVersionUID = -7616201572220929588L;

	private String category;

	private List<News> news;

	private String type;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<News> getNews() {
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}

}
