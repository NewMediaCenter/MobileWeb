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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<kme:page title="Maps" id="formtest">
	<kme:content>

<form:form action="${pageContext.request.contextPath}/maps/search" commandName="mapsearchform" data-ajax="false">
	<fieldset>
	<label for="searchText">Search</label>
	<form:input path="searchText" cssClass="text ui-widget-content ui-corner-all" />
	<label for="searchLatitude">Latitude</label>
	<form:input path="searchLatitude" cssClass="text ui-widget-content ui-corner-all" />
	<label for="searchLongitude">Longitude</label>
	<form:input path="searchLongitude" cssClass="text ui-widget-content ui-corner-all" />
	<%--
	<label for="searchBuilding">Building Code</label> 
	<form:input path="searchBuilding" cssClass="text ui-widget-content ui-corner-all" />
	--%>
	<label for="searchCampus">Campus</label>
	<form:select path="searchCampus" multiple="false">
		<form:option value="" label="" data-placeholder="true" />
		<form:option value="BL" label="Bloomington" data-placeholder="true"/>
		<form:option value="IN" label="Indianapolis" data-placeholder="true"/>
	</form:select>
	<label for="searchBuilding">Building Code</label>
	<form:select path="searchBuilding" multiple="false">
		<form:option value="" label="" data-placeholder="true"/>
	</form:select>
	<form:errors path="searchText" />
	</fieldset>
</form:form>

<div id="map_canvas" style="height:300px;"></div>
	
<script type="text/javascript">
/* Maps */

var map;
var markersArray = [];
var userMarkersArray = [];

$('#formtest').live("pagebeforeshow", function() {
		deleteOverlays(markersArray);
		map = initialize("map_canvas", 39.17, -86.5);
//		var buildingCode = getParameterByName("id");
//		showBuildingByCode(map, buildingCode);
//		drawUserLocation(map);
		$("#searchCampus").change(function() {
			var groupCode = $("#searchCampus").val();
			if (groupCode) {
				var buildingData = retrieveBuildingsForGroup(groupCode);
			} else {
				alert("No group code");
			}
		});
		$("#searchBuilding").change(function() {
			var buildingCode = $("#searchBuilding").val();
			//alert("Test");
			if (buildingCode) {
				retrieveBuildingByCode(buildingCode);	
			} else {
				alert("No building code");
			}
		});
	    google.maps.event.addListener(map, 'click', function(event) {
	    	//$('#searchCampus').val("");
	    	//$('#searchCampus').selectmenu('refresh', true);
	    	$('#searchBuilding').val("");
	    	$('#searchBuilding').selectmenu('refresh', true);
	    	$('#searchLatitude').val(event.latLng.lat());
	    	$('#searchLongitude').val(event.latLng.lng());
	    	deleteOverlays(markersArray);
	    	addMarker(map, markersArray, event.latLng);
	    	//alert(event.latLng);
	    });
});

function retrieveBuildingByCode(buildingCode) {
	clearOverlays(markersArray);
	$.getJSON('${pageContext.request.contextPath}/maps/building/' + buildingCode, function(data) {
		showLocationByCoordinates(map, markersArray, data.latitude, data.longitude);
	}).error(function() { 
		alert("Could not retrieve data at this time."); 
	});
}

function retrieveBuildingsForGroup(groupCode) {
	// http://localhost:9999/mdot/maps/group/BL
	$('#searchBuilding').empty();
	$('#searchBuilding').append($("<option></option>").attr("value", "").text(""));
	$.getJSON('${pageContext.request.contextPath}/maps/group/' + groupCode, function(data) {
		$.each(data.mapsLocations, function(key, value) {
			$('#searchBuilding').append($("<option></option>").attr("value", value.buildingCode).text(value.name));
		});
		$('#searchBuilding').selectmenu('refresh', true);
	}).error(function() { 
		alert("Could not retrieve data at this time."); 
	});
}

</script>
	</kme:content>
</kme:page>
