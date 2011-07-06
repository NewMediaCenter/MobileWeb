package org.kuali.mobility.news.entity;

import java.util.ArrayList;
import java.util.List;

public class NewsDay {
	private String formattedDate;
	private List<NewsArticle> articles;
	
	public NewsDay() {
		articles = new ArrayList<NewsArticle>();
	}
	
	public NewsDay copy() {
		NewsDay copy = new NewsDay();
		if (formattedDate != null) {
			copy.setFormattedDate(new String(formattedDate));
		}
		if (!articles.isEmpty()) {
			List<NewsArticle> articlesCopy = new ArrayList<NewsArticle>();
			for (NewsArticle article : articles) {
				articlesCopy.add(article.copy());
			}
			copy.setArticles(articlesCopy);
		}
		return copy;
	}
	
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
