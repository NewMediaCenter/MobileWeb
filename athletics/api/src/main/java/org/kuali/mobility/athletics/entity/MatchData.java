package org.kuali.mobility.athletics.entity;

import java.io.Serializable;
import java.util.List;

public class MatchData implements Serializable {

	private static final long serialVersionUID = 1758726180629674311L;

	private String category;

	private List<Match> matches;

	private String type;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
