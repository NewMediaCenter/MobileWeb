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
import java.util.List;

public class ComputerLab implements Serializable, Comparable<ComputerLab> {

	private static final long serialVersionUID = -5535399038213510311L;
	
	private String campus;
    private String lab;
    private String availability;
    private String buildingCode;
    
    private String labCode;
    private String buildingShortCode;
    
    private boolean active;
    
    private List<LabAvailability> avList;
    private String floor;
    private String labOnly;
    private String buildingNameOnly;

    public ComputerLab() {}
    
    public ComputerLab(String campus, String lab, String availability) {
        this.campus = campus;
        this.lab = lab;
        this.availability = availability;
        this.active = true;
    }
    
    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public int compareTo(ComputerLab that) {
        if (this == null || that == null || this.getLab() == null || that.getLab() == null) {
            return -1;
        }
        return this.getLab().compareTo(that.getLab());
    }

	public String getBuildingShortCode() {
		return buildingShortCode;
	}

	public void setBuildingShortCode(String buildingShortCode) {
		this.buildingShortCode = buildingShortCode;
	}

	public String getLabCode() {
		return labCode;
	}

	public void setLabCode(String labCode) {
		this.labCode = labCode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getBuildingCode() {
		return buildingCode;
	}

	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}

	public List<LabAvailability> getAvList() {
		return avList;
	}

	public void setAvList(List<LabAvailability> avList) {
		this.avList = avList;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getLabOnly() {
		return labOnly;
	}

	public void setLabOnly(String labOnly) {
		this.labOnly = labOnly;
	}

	public String getBuildingNameOnly() {
		return buildingNameOnly;
	}

	public void setBuildingNameOnly(String buildingNameOnly) {
		this.buildingNameOnly = buildingNameOnly;
	}
	
}
