package org.kuali.mobility.socialmedia.entity;

import java.io.Serializable;

public class TweetEntity implements Serializable, Comparable<TweetEntity>{

	private static final long serialVersionUID = -3777608200171736989L;
	
	private int startIndex;
	private int endIndex;
	private String displayText;
	private String linkText;
	
	@Override
	public int compareTo(TweetEntity arg0) {
		return startIndex - arg0.startIndex;
	}	
	
	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}
}
