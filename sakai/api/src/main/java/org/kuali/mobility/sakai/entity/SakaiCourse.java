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

public class SakaiCourse implements Serializable, Comparable<SakaiCourse> {

	private static final long serialVersionUID = -5535399038213510311L;
	
	private String campus;
    private String courseId;
    private String courseTitle;
    private String courseDesc;
    private String courseTerm;
    private Timestamp createdTime;
    private Timestamp modifiedTime;
    private boolean active;
    private String instructorName;
    private String instructorId;
    
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

	public SakaiCourse() {}
    
    public SakaiCourse(String campus, String courseId, String courseTitle, String courseDesc) {
        this.campus = campus;
        this.setCourseId(courseId);
        this.setCourseTitle(courseTitle);
        this.setCourseDesc(courseDesc);
        this.active = true;
    }
    
    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    

    public int compareTo(SakaiCourse that) {
        if (this == null || that == null || this.getCourseId() == null || that.getCourseId() == null) {
            return -1;
        }
        return this.getCourseId().compareTo(that.getCourseId());
    }

	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseDesc(String courseDesc) {
		this.courseDesc = courseDesc;
	}

	public String getCourseDesc() {
		return courseDesc;
	}

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public String getCourseTerm() {
		return courseTerm;
	}

	public void setCourseTerm(String courseTerm) {
		this.courseTerm = courseTerm;
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
}
