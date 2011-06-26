/**
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.mobility.computerlabs.entity;

import java.io.Serializable;

public class LabAvailability implements Serializable, Comparable<LabAvailability> {
	
	private static final long serialVersionUID = -2975825577394559905L;
	
	private String name;
	private String description;
	private String availability;
	private int order;
	
	public LabAvailability(String name, String desc, String availability, int order) {
		this.name = name;
		this.description = desc;
		this.availability = availability;
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int compareTo(LabAvailability labAv) {
		if (this == labAv) {
			return 0;
		}
		if (this.order > labAv.getOrder()) {
			return 1;
		} else if (this.order < labAv.getOrder()) {
			return -1;
		} 
		return this.name.compareTo(labAv.getName());
	}
	
}
