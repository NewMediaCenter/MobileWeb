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

import flexjson.JSONSerializer;

public class SakaiCourse implements Serializable, Comparable<SakaiCourse> {

	private static final long serialVersionUID = -5535399038213510311L;
	
	private String campus;
    private String courseId;
    private String courseTitle;
    private String courseDesc;
    private CreatedTime createdTime;
    private ModifiedTime modifiedTime;
    private SiteOwner siteOwner;
    private boolean active;
    
 
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
	
	public void setCreatedTime(CreatedTime createdTime) {
		this.createdTime = createdTime;
	}

	public CreatedTime getCreatedTime() {
		return createdTime;
	}

	public void setModifiedTime(ModifiedTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public ModifiedTime getModifiedTime() {
		return modifiedTime;
	}

	public void setSiteOwner(SiteOwner siteOwner) {
		this.siteOwner = siteOwner;
	}

	public SiteOwner getSiteOwner() {
		return siteOwner;
	}

	private class CreatedTime {
		private String display;
		private String time;
		public void setDisplay(String display) {
			this.display = display;
		}
		public String getDisplay() {
			return display;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getTime() {
			return time;
		}
		
	}
	
	private class ModifiedTime {
		private String display;
		private String time;
		public void setDisplay(String display) {
			this.display = display;
		}
		public String getDisplay() {
			return display;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getTime() {
			return time;
		}
		
	}
   
	private class SiteOwner {
		private String userDisplayName;
		private String userEntityURL;
		private String userId;
		public void setUserDisplayName(String userDisplayName) {
			this.userDisplayName = userDisplayName;
		}
		public String getUserDisplayName() {
			return userDisplayName;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserEntityURL(String userEntityURL) {
			this.userEntityURL = userEntityURL;
		}
		public String getUserEntityURL() {
			return userEntityURL;
		}
		
		
	}
}
