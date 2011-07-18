package org.kuali.mobility.sakai.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SakaiHome {
	private Map<String,List<SakaiSite>> courses;
	private List<SakaiSite> projects;
	
	public SakaiHome(){
		courses = new LinkedHashMap<String,List<SakaiSite>>();
		projects = new ArrayList<SakaiSite>();
	}

	public Map<String, List<SakaiSite>> getCourses() {
		return courses;
	}

	public void setCourses(Map<String, List<SakaiSite>> courses) {
		this.courses = courses;
	}

	public List<SakaiSite> getProjects() {
		return projects;
	}

	public void setProjects(List<SakaiSite> projects) {
		this.projects = projects;
	}

}
