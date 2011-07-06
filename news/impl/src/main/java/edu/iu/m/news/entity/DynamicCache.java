package edu.iu.m.news.entity;

import java.util.Date;

public class DynamicCache {

	private int id;
	private String name;
	
	private Date lastUpdate;
	private boolean active;
	private int updateIntervalSeconds;
	
	public DynamicCache(int id, String name, int updateIntervalSeconds) {
		this.id = id;
		this.name = name;
		this.active = true;
		this.updateIntervalSeconds = updateIntervalSeconds;
	}
	
	protected void update() {
		this.lastUpdate = new Date();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getUpdateIntervalSeconds() {
		return updateIntervalSeconds;
	}

	public void setUpdateIntervalSeconds(int updateIntervalSeconds) {
		this.updateIntervalSeconds = updateIntervalSeconds;
	}

	public int getId() {
		return id;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}
	
}
