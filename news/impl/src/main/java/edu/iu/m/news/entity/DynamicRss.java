package edu.iu.m.news.entity;


public class DynamicRss extends DynamicCache {

	private Rss rss;
	private String feedUrl;
	
	public DynamicRss(int id, Rss rss, String feedUrl) {
		super(id, rss.getUrl(), 15 * 60); // Default 15 minute update
		this.feedUrl = feedUrl;
		this.update(rss);
	}
	
	public void update(Rss rss) {
		super.update();
		this.rss = rss;
	}

	public Rss getRss() {
		return rss;
	}

	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}
}
