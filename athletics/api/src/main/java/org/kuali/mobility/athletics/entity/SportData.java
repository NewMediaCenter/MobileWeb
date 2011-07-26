package org.kuali.mobility.athletics.entity;

import java.io.Serializable;
import java.util.List;

public class SportData implements Serializable {

	private static final long serialVersionUID = 1758726180629674311L;

	private String category;

	private List<Sport> sports;

	private String type;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Sport> getSports() {
		return sports;
	}

	public void setSports(List<Sport> sports) {
		this.sports = sports;
	}
}
