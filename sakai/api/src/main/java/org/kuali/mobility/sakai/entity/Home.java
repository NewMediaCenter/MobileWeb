package org.kuali.mobility.sakai.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Home {
	private Map<String,List<Site>> courses;
	private List<Site> projects;
	private List<Site> other;
	private List<Site> todaysCourses;
	
	public Home(){
		courses = new LinkedHashMap<String,List<Site>>();
		projects = new ArrayList<Site>();
		other = new ArrayList<Site>();
	}

	public Map<String, List<Site>> getCourses() {
		return courses;
	}

	public void setCourses(Map<String, List<Site>> courses) {
		this.courses = courses;
	}

	public List<Site> getProjects() {
		return projects;
	}

	public void setProjects(List<Site> projects) {
		this.projects = projects;
	}

	public List<Site> getTodaysCourses() {
		return todaysCourses;
	}

	public void setTodaysCourses(List<Site> todaysCourses) {
		this.todaysCourses = todaysCourses;
	}

	public List<Site> getOther() {
		return other;
	}

	public void setOther(List<Site> other) {
		this.other = other;
	}

}
