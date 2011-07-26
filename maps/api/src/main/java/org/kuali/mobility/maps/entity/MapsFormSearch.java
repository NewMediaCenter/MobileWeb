package org.kuali.mobility.maps.entity;

public class MapsFormSearch {

	private String searchText;
	private String searchLatitude;
	private String searchLongitude;
	private String searchBuilding;
	private String searchCampus;

	public String getSearchCampus() {
		return searchCampus;
	}

	public void setSearchCampus(String searchCampus) {
		this.searchCampus = searchCampus;
	}

	private MapsGroup mapsGroup;
	
	public MapsGroup getMapsGroup() {
		return mapsGroup;
	}

	public void setMapsGroup(MapsGroup mapsGroup) {
		this.mapsGroup = mapsGroup;
	}

	public String getSearchLatitude() {
		return searchLatitude;
	}

	public void setSearchLatitude(String searchLatitude) {
		this.searchLatitude = searchLatitude;
	}

	public String getSearchLongitude() {
		return searchLongitude;
	}

	public void setSearchLongitude(String searchLongitude) {
		this.searchLongitude = searchLongitude;
	}

	public String getSearchBuilding() {
		return searchBuilding;
	}

	public void setSearchBuilding(String searchBuilding) {
		this.searchBuilding = searchBuilding;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
}
