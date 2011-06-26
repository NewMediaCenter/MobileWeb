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

public class SakaiResource implements Serializable, Comparable<SakaiResource> {

private static final long serialVersionUID = -2272816569200642551L;
	
    private String id;
    private String title;
    private String extension;
    private String childResource;
    private String sessionId;
    private Boolean hasChild;
    
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
	
	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}

	public void setChildResource(String childResource) {
		this.childResource = childResource;
	}

	public String getChildResource() {
		return childResource;
	}

	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
	}

	public Boolean getHasChild() {
		return hasChild;
	}
	
    public int compareTo(SakaiResource that) {
        if (this == null || that == null || this.getId() == null || that.getId() == null) {
            return -1;
        }
        return this.getId().compareTo(that.getId());
    }

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

		
}
