package org.kuali.mobility.athletics.entity;

import java.io.Serializable;

public class News implements Serializable {

	private static final long serialVersionUID = 1050022382561621206L;
	private String thumbnail;
	private String summary;
	private String title;
	private String urlEsc;
	private String url;
	private Long newsId;

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrlEsc() {
		return urlEsc;
	}

	public void setUrlEsc(String urlEsc) {
		this.urlEsc = urlEsc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

}
