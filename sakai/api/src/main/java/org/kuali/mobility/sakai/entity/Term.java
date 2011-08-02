package org.kuali.mobility.sakai.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Term implements Serializable, Comparable<Term>{
	
	private static final long serialVersionUID = 4671279471060244186L;
	
	private String term;
	private int year;
	private Collection<Site> courses;
	private Terms termEnumVal;
	
	enum Terms {SPRING, SUMMER, FALL};
	
	public Term() {
		courses = new ArrayList<Site>();
	}

	@Override
	public int compareTo(Term o) {
		if (year == o.year) {
			if (termEnumVal != null && o.termEnumVal != null) {
				return termEnumVal.compareTo(o.termEnumVal);
			} else {
				return 0;
			}
		} else {
			return year - o.year;
		}
	};
	
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
		
		if (term != null) {
			String[] split = term.split(" ");
			
			try {
				termEnumVal = Terms.valueOf(split[0].toUpperCase());
			} catch (Exception e) {}
			
			try {
				year = Integer.parseInt(split[1]);
			} catch (Exception e) {}
		}
	}

	public Collection<Site> getCourses() {
		return courses;
	}

	public void setCourses(Collection<Site> courses) {
		this.courses = courses;
	}

	
}
