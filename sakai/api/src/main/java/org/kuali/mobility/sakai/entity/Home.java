package org.kuali.mobility.sakai.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Home {
	private Map<String,List<Site>> courses;
	private List<Site> projects;
	
	public Home(){
		courses = new LinkedHashMap<String,List<Site>>();
		projects = new ArrayList<Site>();
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

}
