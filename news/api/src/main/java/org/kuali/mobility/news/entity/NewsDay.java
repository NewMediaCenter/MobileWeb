package org.kuali.mobility.news.entity;

import java.util.List;

public class NewsDay {
	private String formattedDate;
	private List<NewsArticle> articles;
	
	public String getFormattedDate() {
		return formattedDate;
	}
	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}
	public List<NewsArticle> getArticles() {
		return articles;
	}
	public void setArticles(List<NewsArticle> articles) {
		this.articles = articles;
	}
}
