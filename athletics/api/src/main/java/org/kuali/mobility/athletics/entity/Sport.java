package org.kuali.mobility.athletics.entity;

import java.io.Serializable;

public class Sport implements Serializable {

	private static final long serialVersionUID = -5661487023809222108L;
	private String thumbnail;
	private String thumbnailSmall;
	private String thumbnailMedium;
	private String thumbnailLarge;
	private String name;
	private Long seasonId;
	private String link;
	private String linkEsc;
	private Long sportId;

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getThumbnailSmall() {
		return thumbnailSmall;
	}

	public void setThumbnailSmall(String thumbnailSmall) {
		this.thumbnailSmall = thumbnailSmall;
	}

	public String getThumbnailMedium() {
		return thumbnailMedium;
	}

	public void setThumbnailMedium(String thumbnailMedium) {
		this.thumbnailMedium = thumbnailMedium;
	}

	public String getThumbnailLarge() {
		return thumbnailLarge;
	}

	public void setThumbnailLarge(String thumbnailLarge) {
		this.thumbnailLarge = thumbnailLarge;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Long seasonId) {
		this.seasonId = seasonId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLinkEsc() {
		return linkEsc;
	}

	public void setLinkEsc(String linkEsc) {
		this.linkEsc = linkEsc;
	}

	public Long getSportId() {
		return sportId;
	}

	public void setSportId(Long sportId) {
		this.sportId = sportId;
	}


}
