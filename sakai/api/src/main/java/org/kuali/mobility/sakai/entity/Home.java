package org.kuali.mobility.sakai.entity;

import java.util.ArrayList;
import java.util.List;

public class Home {
	private List<Term> courses;
	private List<Site> projects;
	private List<Site> other;
	private List<Site> todaysCourses;
	
	public Home(){
		courses = new ArrayList<Term>();
		projects = new ArrayList<Site>();
		other = new ArrayList<Site>();
		todaysCourses = new ArrayList<Site>();
	}

	public List<Term> getCourses() {
		return courses;
	}

	public void setCourses(List<Term> courses) {
		this.courses = courses;
	}

	public List<Site> getProjects() {
		return projects;
	}

	public void setProjects(List<Site> projects) {
		this.projects = projects;
	}

	public List<Site> getOther() {
		return other;
	}

	public void setOther(List<Site> other) {
		this.other = other;
	}

	public List<Site> getTodaysCourses() {
		return todaysCourses;
	}

	public void setTodaysCourses(List<Site> todaysCourses) {
		this.todaysCourses = todaysCourses;
	}
}
