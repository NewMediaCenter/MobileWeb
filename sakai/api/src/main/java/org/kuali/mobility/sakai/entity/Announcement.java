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
import java.util.List;

public class Announcement implements Serializable, Comparable<Announcement> {

	private static final long serialVersionUID = -5535399038218510311L;
	
	private List<Attachment> attachments;
	private String body;
	private String createdByDisplayName;
	private String createdOn;
	private String createdDate;
	private String id;
	private String siteId;
	private String siteTitle;
	private String title;
	private String entityReference;
	private String entityURL;
	private String entityId;
	private String entityTitle;
	
    public Announcement() {} 

    public int compareTo(Announcement that) {
        if (this == null || that == null || this.getId() == null || that.getId() == null) {
            return -1;
        }
        return this.getId().compareTo(that.getId());
    }
	
	public String getBody() {
		return body;
	}
	
	public String getCreatedByDisplayName() {
		return createdByDisplayName;
	}
	
	public String getCreatedOn() {
		return createdOn;
	}
	
	public String getId() {
		return id;
	}
	
	public String getSiteId() {
		return siteId;
	}
	
	public String getSiteTitle() {
		return siteTitle;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getEntityReference() {
		return entityReference;
	}
	
	public String getEntityURL() {
		return entityURL;
	}
	
	public String getEntityId() {
		return entityId;
	}
	
	public String getEntityTitle() {
		return entityTitle;
	}
	
	public void setBody(String body) {
		this.body = body;
	}

	public void setCreatedByDisplayName(String createdByDisplayName) {
		this.createdByDisplayName = createdByDisplayName;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public void setSiteTitle(String siteTitle) {
		this.siteTitle = siteTitle;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setEntityReference(String entityReference) {
		this.entityReference = entityReference;
	}

	public void setEntityURL(String entityURL) {
		this.entityURL = entityURL;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public void setEntityTitle(String entityTitle) {
		this.entityTitle = entityTitle;
	}



	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}



	public String getCreatedDate() {
		return createdDate;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	
	
}
