package org.kuali.mobility.people.entity;

public class Search {
	private String firstName;
	private String lastName;
	private String userName;
	
	private String exactness = "starts";
	private String status;
	private String location;
	
	public boolean isExactLastName() {
		return "exact".equals(exactness);
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getExactness() {
		return exactness;
	}
	public void setExactness(String exactness) {
		this.exactness = exactness;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
