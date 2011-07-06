package org.kuali.mobility.news.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class NewsArticle implements Serializable, Comparable<NewsArticle> {

	private static final long serialVersionUID = -133725965130444787L;
	
	private String title;
	private String link;
	private String description;
	private String thumbnailImageUrl;
	private Timestamp publishDate;
	private String articleId;
	
	public NewsArticle copy() {
		NewsArticle copy = new NewsArticle();
		if (title != null) {
			copy.setTitle(new String(title));
		}
		if (link != null) {
			copy.setLink(new String(link));
		}
		if (description != null) {
			copy.setDescription(new String(description));
		}
		if (thumbnailImageUrl != null) {
			copy.setThumbnailImageUrl(new String(thumbnailImageUrl));
		}
		if (articleId != null) {
			copy.setArticleId(new String(articleId));
		}
		copy.setPublishDate(new Timestamp(publishDate.getTime()));
		
		return copy;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Timestamp pubDate) {
		this.publishDate = pubDate;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getThumbnailImageUrl() {
		return thumbnailImageUrl;
	}
	public void setThumbnailImageUrl(String thumbnailImageUrl) {
		this.thumbnailImageUrl = thumbnailImageUrl;
	}
	
	@Override
	public int compareTo(NewsArticle arg0) {
		return publishDate.compareTo(arg0.publishDate);
	}
}
