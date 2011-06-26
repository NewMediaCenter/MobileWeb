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

public class MyClassesHome implements Serializable {

	private static final long serialVersionUID = -5535399038213510511L;
	
	private String title;
	private String displayId;
	private int code;
    private boolean active;
    
 
    public MyClassesHome() {}
    
    public MyClassesHome(int code, String title) {
        this.setTitle(title);
        this.setCode(code);
        this.active = true;
    }
  
   
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}

	public String getDisplayId() {
		return displayId;
	}

	
	
}
