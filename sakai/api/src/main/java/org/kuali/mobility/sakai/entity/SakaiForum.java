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

public class SakaiForum implements Serializable, Comparable<SakaiForum> {

	private static final long serialVersionUID = -5535393238213510311L;
	
	private String id;
    private String title;
    private String description;
    private Boolean isForumHeader;
    private String typeUuid;
    private String forumId;
    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public void setIsForumHeader(Boolean isForumHeader) {
		this.isForumHeader = isForumHeader;
	}

	public Boolean getIsForumHeader() {
		return isForumHeader;
	}

	
	public void setTypeUuid(String typeUuid) {
		this.typeUuid = typeUuid;
	}

	public String getTypeUuid() {
		return typeUuid;
	}

	public void setForumId(String forumId) {
		this.forumId = forumId;
	}

	public String getForumId() {
		return forumId;
	}

    public int compareTo(SakaiForum that) {
        if (this == null || that == null || this.getId() == null || that.getId() == null) {
            return -1;
        }
        return this.getId().compareTo(that.getId());
    }

		
}
