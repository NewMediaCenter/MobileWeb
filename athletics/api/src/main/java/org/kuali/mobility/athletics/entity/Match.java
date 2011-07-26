package org.kuali.mobility.athletics.entity;

import java.io.Serializable;

public class Match implements Serializable {

	private static final long serialVersionUID = 4770941782330330623L;

	private String home;
	private String winLoss;
	private String dateTime;
	private String location;
	private String score;
	private String date;
	private String opponent;
	private String time;
	private String name;
	private String timeRemaining;
	private String sportName;
	private boolean hasSummary;
	private String oppScore;
	private Long matchId;
	private String thumbnail;
	private String thumbnailSmall;
	private String thumbnailMedium;
	private String thumbnailLarge;
	private String oppThumbnail;
	private String oppThumbnailSmall;
	private String oppThumbnailMedium;
	private String oppThumbnailLarge;
	private String sportThumbnail;
	private String sportThumbnailSmall;
	private String sportThumbnailMedium;
	private String sportThumbnailLarge;

	public String getSportThumbnailSmall() {
		return sportThumbnailSmall;
	}

	public void setSportThumbnailSmall(String sportThumbnailSmall) {
		this.sportThumbnailSmall = sportThumbnailSmall;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getWinLoss() {
		return winLoss;
	}

	public void setWinLoss(String winLoss) {
		this.winLoss = winLoss;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOppThumbnailMedium() {
		return oppThumbnailMedium;
	}

	public void setOppThumbnailMedium(String oppThumbnailMedium) {
		this.oppThumbnailMedium = oppThumbnailMedium;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	public String getOppThumbnail() {
		return oppThumbnail;
	}

	public void setOppThumbnail(String oppThumbnail) {
		this.oppThumbnail = oppThumbnail;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOppThumbnailLarge() {
		return oppThumbnailLarge;
	}

	public void setOppThumbnailLarge(String oppThumbnailLarge) {
		this.oppThumbnailLarge = oppThumbnailLarge;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimeRemaining() {
		return timeRemaining;
	}

	public void setTimeRemaining(String timeRemaining) {
		this.timeRemaining = timeRemaining;
	}

	public String getSportThumbnailMedium() {
		return sportThumbnailMedium;
	}

	public void setSportThumbnailMedium(String sportThumbnailMedium) {
		this.sportThumbnailMedium = sportThumbnailMedium;
	}

	public String getSportName() {
		return sportName;
	}

	public void setSportName(String sportName) {
		this.sportName = sportName;
	}

	public String getOppThumbnailSmall() {
		return oppThumbnailSmall;
	}

	public void setOppThumbnailSmall(String oppThumbnailSmall) {
		this.oppThumbnailSmall = oppThumbnailSmall;
	}

	public boolean isHasSummary() {
		return hasSummary;
	}

	public void setHasSummary(boolean hasSummary) {
		this.hasSummary = hasSummary;
	}

	public String getOppScore() {
		return oppScore;
	}

	public void setOppScore(String oppScore) {
		this.oppScore = oppScore;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getSportThumbnailLarge() {
		return sportThumbnailLarge;
	}

	public void setSportThumbnailLarge(String sportThumbnailLarge) {
		this.sportThumbnailLarge = sportThumbnailLarge;
	}

	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}

	public String getThumbnailLarge() {
		return thumbnailLarge;
	}

	public void setThumbnailLarge(String thumbnailLarge) {
		this.thumbnailLarge = thumbnailLarge;
	}

	public String getSportThumbnail() {
		return sportThumbnail;
	}

	public void setSportThumbnail(String sportThumbnail) {
		this.sportThumbnail = sportThumbnail;
	}
}
