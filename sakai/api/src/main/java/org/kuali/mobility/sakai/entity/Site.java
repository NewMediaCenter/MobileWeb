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
 
package org.kuali.mobility.sakai.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import flexjson.JSONSerializer;

public class Site implements Serializable, Comparable<Site> {

	private static final long serialVersionUID = -5535399038213510311L;
	
	private String campus;
    private String id;
    private String title;
    private String description;
    private String term;
    private Timestamp createdTime;
    private Timestamp modifiedTime;
    private boolean active;
    private String instructorName;
    private String instructorId;
    private boolean hasAnnouncementsTool;
    private boolean hasAssignmentsTool;
    private boolean hasForumsTool;
    private boolean hasGradesTool;
    private boolean hasRosterTool;
    private boolean hasResourcesTool;
    private boolean hasMessagesTool;
    
    public String getInstructorName() {
		return instructorName;
	}

	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}

	public String getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(String instructorId) {
		this.instructorId = instructorId;
	}

	public Site() {}
    
    public Site(String campus, String courseId, String courseTitle, String courseDesc) {
        this.campus = campus;
        this.setId(courseId);
        this.setTitle(courseTitle);
        this.setDescription(courseDesc);
        this.active = true;
    }
    
    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public int compareTo(Site that) {
        if (this == null || that == null || this.getId() == null || that.getId() == null) {
            return -1;
        }
        return this.getId().compareTo(that.getId());
    }

	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
   
	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Timestamp modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public boolean getHasAnnouncementsTool() {
		return hasAnnouncementsTool;
	}

	public void setHasAnnouncementsTool(boolean hasAnnouncementsTool) {
		this.hasAnnouncementsTool = hasAnnouncementsTool;
	}

	public boolean getHasAssignmentsTool() {
		return hasAssignmentsTool;
	}

	public void setHasAssignmentsTool(boolean hasAssignmentsTool) {
		this.hasAssignmentsTool = hasAssignmentsTool;
	}

	public boolean getHasForumsTool() {
		return hasForumsTool;
	}

	public void setHasForumsTool(boolean hasForumsTool) {
		this.hasForumsTool = hasForumsTool;
	}

	public boolean getHasGradesTool() {
		return hasGradesTool;
	}

	public void setHasGradesTool(boolean hasGradesTool) {
		this.hasGradesTool = hasGradesTool;
	}

	public boolean getHasRosterTool() {
		return hasRosterTool;
	}

	public void setHasRosterTool(boolean hasRosterTool) {
		this.hasRosterTool = hasRosterTool;
	}

	public boolean getHasResourcesTool() {
		return hasResourcesTool;
	}

	public void setHasResourcesTool(boolean hasResourcesTool) {
		this.hasResourcesTool = hasResourcesTool;
	}

	public boolean getHasMessagesTool() {
		return hasMessagesTool;
	}

	public void setHasMessagesTool(boolean hasMessagesTool) {
		this.hasMessagesTool = hasMessagesTool;
	}
}
