package org.kuali.mobility.news.entity;

import java.util.ArrayList;
import java.util.List;

public class RssModel implements MobileModel {
	
	private static final long serialVersionUID = -8287650114673832090L;

	private Rss rss;
	private List<MaintRss> feeds;
	private String storyHtml;
	private String code;
	private String viewCampus;
	private String eventHtml;
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public RssModel() {
	    feeds = new ArrayList<MaintRss>();
	}
	
	public Rss getRss() {
		return rss;
	}

	public void setRss(Rss rss) {
		this.rss = rss;
	}

    public List<MaintRss> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<MaintRss> feeds) {
        this.feeds = feeds;
    }

    public String getStoryHtml() {
        return storyHtml;
    }

    public void setStoryHtml(String storyHtml) {
        this.storyHtml = storyHtml;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEventHtml() {
        return eventHtml;
    }

    public void setEventHtml(String eventHtml) {
        this.eventHtml = eventHtml;
    }

	public String getViewCampus() {
		return viewCampus;
	}

	public void setViewCampus(String viewCampus) {
		this.viewCampus = viewCampus;
	}
  
}
