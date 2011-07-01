package org.kuali.mobility.news.entity;

import java.io.Serializable;

public class RssCategory implements Serializable {

	private static final long serialVersionUID = 8941441326817592157L;

	private String domain;
	private String value;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
