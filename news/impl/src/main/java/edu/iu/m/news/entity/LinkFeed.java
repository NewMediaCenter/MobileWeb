package edu.iu.m.news.entity;


public class LinkFeed extends DynamicCache {

	private String title;
	
	private String bodyText;
	private String richBodyText;

	private String feedUrl;
	private String originalUrl;

	public LinkFeed(int id, String feedUrl) {
		super(id, feedUrl, 15 * 60); // Default 15 minute update
		// For the cache service
		this.feedUrl = feedUrl;
	}
	
	public void updateBodyText(String bodyText) {
		super.update();
//		this.lastUpdate = new Date();
		this.bodyText = bodyText;
	}
	
	public void updateRichBodyText(String richBodyText) {
		super.update();
//		this.lastUpdate = new Date();
		this.richBodyText = richBodyText;
	}
	
	public String getBodyText() {
		return bodyText;
	}

    public String getRichBodyText() {
        return richBodyText;
    }

	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	
}
