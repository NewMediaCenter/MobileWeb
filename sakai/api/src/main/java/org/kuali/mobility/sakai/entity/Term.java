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
			return termEnumVal.compareTo(o.termEnumVal);
		} else {
			return year - o.year;
		}
	};
	
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
		String[] split = term.split(" ");
		termEnumVal = Terms.valueOf(split[0].toUpperCase());
		year = Integer.parseInt(split[1]);
	}

	public Collection<Site> getCourses() {
		return courses;
	}

	public void setCourses(Collection<Site> courses) {
		this.courses = courses;
	}

	
}
