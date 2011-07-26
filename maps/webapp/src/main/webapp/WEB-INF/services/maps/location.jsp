<%--
  Copyright 2011 The Kuali Foundation Licensed under the Educational Community
  License, Version 2.0 (the "License"); you may not use this file except in
  compliance with the License. You may obtain a copy of the License at
  http://www.osedu.org/licenses/ECL-2.0 Unless required by applicable law or
  agreed to in writing, software distributed under the License is distributed
  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  express or implied. See the License for the specific language governing
  permissions and limitations under the License.
--%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="kme" uri="http://kuali.org/mobility" %>

<kme:page title="Maps" id="mapslocation" backButton="true" homeButton="true">
	<kme:content>
	
<div id="map_canvas" style="height:300px;"></div>
	
<script type="text/javascript">
/* Maps */

var hasLoaded = 0;

var buildingCode;

var markersArray = [];
var userMarkersArray = [];

function showBuildingByCode(map, buildingCode) {
	if (buildingCode) {
		$.getJSON('${pageContext.request.contextPath}/maps/building/' + buildingCode, function(data) {
			var items = [];
			var latitude = data.latitude;
			var longitude = data.longitude;
			showLocationByCoordinates(map, markersArray, latitude, longitude);
		});
	}
}

$('#mapslocation').live("pageshow", function() {
/* 	$('#map_canvas').gmap({'center': getLatLng(), 'callback': function() {
		
	}); */
	//var buildingCode = $('#map_canvas').jqmData('code');
	var map = initialize("map_canvas", 39.17, -86.5);
	deleteOverlays(markersArray);
	var buildingCode = getParameterByName("id");
	if (buildingCode) {
		showBuildingByCode(map, buildingCode);	
	} else {
		var latitude = getParameterByName("latitude");
		var longitude = getParameterByName("longitude");
		if (latitude && longitude) {
			showLocationByCoordinates(map, markersArray, latitude, longitude);	
		}
	}
	drawUserLocation(map);
	
	//alert(buildingCode);

});

function drawUserLocation(map) {
	if(navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position){
			//initialize(position.coords.latitude,position.coords.longitude);
			var location = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
			deleteOverlays(userMarkersArray);
			addMarker(map, userMarkersArray, location, "http://www.google.com/intl/en_us/mapfiles/ms/micons/blue-dot.png");
			if (hasLoaded == 0) {
				hasLoaded = 1;
				centerOverAllLocations(map);
			}
		});
	}
}

function centerOverAllLocations(map) {
	var bounds = new google.maps.LatLngBounds();
	if (markersArray) {
		for (i in markersArray) {
			bounds.extend(markersArray[i].getPosition());
		}
	}
	if (userMarkersArray) {
		for (i in userMarkersArray) {
			bounds.extend(userMarkersArray[i].getPosition());
		}
	}
	map.fitBounds(bounds);
}
</script>
	</kme:content>
</kme:page>
