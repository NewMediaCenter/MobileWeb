package org.kuali.mobility.news.entity;

public class NewsSource {

	private String locationCode;
	private String sourceUrl;
	private String sourceName;
	private String sourceId;
	private NewsStream newsStream;
	
	public NewsSource copy() {
		NewsSource copy = new NewsSource();
		if (locationCode!=null) {
			copy.setLocationCode(new String(locationCode));
		}
		if (sourceUrl!=null) {
			copy.setSourceUrl(new String(sourceUrl));
		}
		if (sourceName!=null) {
			copy.setSourceName(new String(sourceName));
		}
		if (sourceId!=null) {
			copy.setSourceId(new String(sourceId));
		}
		if (newsStream != null) {
			copy.setNewsStream(newsStream.copy());
		}
		return copy;
	}
	
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public NewsStream getNewsStream() {
		return newsStream;
	}
	public void setNewsStream(NewsStream newsStream) {
		this.newsStream = newsStream;
	}
}
