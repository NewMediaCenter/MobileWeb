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

public class Roster implements Serializable, Comparable<Roster> {

	private static final long serialVersionUID = -5535399238213510311L;
	
	private String displayID,displayName,firstName,lastName,nickName, imageUrl;
	private String department,email,homePage,homePhone,workPhone;
	private String position,roleTitle,room,school,otherInformation;
	private String entityReference,entityURL;
	private String sortName;
	
	public void setSortName() {
		if (lastName != null && !"null".equals(lastName) && firstName != null && !"null".equals(firstName)) {
			sortName = lastName + " " + firstName;
		} else {
			String[] split = displayName.split(" ");
			sortName = split[split.length-1];
		}
	}
	
	public String getSortName() {
		return sortName;
	}
	public String getDisplayID() {
		return displayID;
	}
	public void setDisplayID(String displayID) {
		this.displayID = displayID;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHomePage() {
		return homePage;
	}
	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getRoleTitle() {
		return roleTitle;
	}
	public void setRoleTitle(String roleTitle) {
		this.roleTitle = roleTitle;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getOtherInformation() {
		return otherInformation;
	}
	public void setOtherInformation(String otherInformation) {
		this.otherInformation = otherInformation;
	}
	public String getEntityReference() {
		return entityReference;
	}
	public void setEntityReference(String entityReference) {
		this.entityReference = entityReference;
	}
	public String getEntityURL() {
		return entityURL;
	}
	public void setEntityURL(String entityURL) {
		this.entityURL = entityURL;
	}	

    public int compareTo(Roster that) {
        return this.getSortName().compareToIgnoreCase(that.getSortName());
    }
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

		
}
