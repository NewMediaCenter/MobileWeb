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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

<title>Maps</title>

<jsp:include page="../resources.jsp" />

</head>

<body>
<div data-role="page" id="mapslocation" class="page-map">
	<div data-role="header">
		<h1>Maps</h1>
	</div>
	<!-- /header -->

<script type="text/javascript">
/* Maps */

var hasLoaded = 0;

var buildingCode;

var map;
var markersArray = [];
var userMarkersArray = [];
  
function addMarker(array, location) {
  marker = new google.maps.Marker({
    position: location,
    map: map
  });
  array.push(marker);
}

// Removes the overlays from the map, but keeps them in the array
function clearOverlays(array) {
  if (array) {
    for (i in array) {
    	array[i].setMap(null);
    }
  }
}

// Shows any overlays currently in the array
function showOverlays(array) {
  if (array) {
    for (i in array) {
    	array[i].setMap(map);
    }
  }
}

// Deletes all markers in the array by removing references to them
function deleteOverlays(array) {
  if (array) {
    for (i in array) {
    	array[i].setMap(null);
    }
    array.length = 0;
  }
}


$('[data-role=page][id=mapslocation2]').live('pageshow',function(e, ui){ 

	//var page_name = e.target.id;
	//if (page_name == 'mapslocation'){
		var buildingCode = $('#map_canvas').jqmData('code');
//		var buildingCode = getParameterByName("bc");
		if (buildingCode) {
			//alert(buildingCode);
			$.getJSON('/mdot/maps/' + buildingCode, function(data) {
				initialize(39.17, -86.5);
				var items = [];
				var latitude = data.latitude;
				var longitude = data.longitude;
				
				if (map) {
					google.maps.event.trigger(map, 'resize');
					var location = new google.maps.LatLng(latitude, longitude);
//					var bounds = new google.maps.LatLngBounds();
//					bounds.extend(location);
//					map.fitBounds(bounds);
					map.setZoom(17);
					map.setCenter(location);
					deleteOverlays(markersArray);
				    addMarker(markersArray, location);
				    google.maps.event.trigger(map, 'resize');
//				    alert("test");
				}
				
			});
		}
	//}
});

// When map page opens get location and display map
//$('.page-map').live("pagecreate", function() {
//	var buildingCode = getParameterByName("bc");
//	if (buildingCode) {
//		//alert(buildingCode);
//		$.getJSON('/mdot/maps/' + buildingCode, function(data) {
//			var items = [];
//			$.each(data.results, function(key, val) {
//				//items.push('key:'+key+' val:'+val.documentId+' '+val.title+'<br/><br/>');
//				//items.push('<a href="/mdot/knowledgebase/'+val.documentId+'"><li><h3>'+val.title+'</h3></li></a>')
//				
//			});
////			var pagehtml = '<div id="resultdata"></div>'
////			$('#searchresults').html(pagehtml);
////			$("#resultdata").html(items.join('')).page();
//			alert(data);
//		});
//	} else {
//		initialize(39.17, -86.5);	
//	}
//});

function initialize(lat,lng) {
	var latlng = new google.maps.LatLng(lat, lng);
	var myOptions = {
		zoom: 17,
		center: latlng,
		mapTypeId: google.maps.MapTypeId.ROADMAP
    };
	map = new google.maps.Map(document.getElementById("map_canvas"),myOptions);
    //map = new google.maps.Map(document.getElementById("map_canvas"),myOptions);
    google.maps.event.trigger(map, 'resize');
}

$('#mapslocation').live("pageshow", function() {
/* 	$('#map_canvas').gmap({'center': getLatLng(), 'callback': function() {
		
	}); */
	drawUserLocation();
	//var buildingCode = $('#map_canvas').jqmData('code');
	var buildingCode = getParameterByName("id");
	//alert(buildingCode);
		if (buildingCode) {
			$.getJSON('/mdot/maps/' + buildingCode, function(data) {
				initialize(39.17, -86.5);
				var items = [];
				var latitude = data.latitude;
				var longitude = data.longitude;
				
				if (map) {
					google.maps.event.trigger(map, 'resize');
					var location = new google.maps.LatLng(latitude, longitude);
					map.setZoom(17);
					map.setCenter(location);
					deleteOverlays(markersArray);
				    addMarker(markersArray, location);
				    google.maps.event.trigger(map, 'resize');
				}
				
			});
		}
});

function drawUserLocation() {
	if(navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position){
			//initialize(position.coords.latitude,position.coords.longitude);
			var location = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
			deleteOverlays(userMarkersArray);
			addMarker(userMarkersArray, location);
			if (hasLoaded == 0) {
				hasLoaded = 1;
				centerOverAllLocations();
			}
		});
	}
}

function centerOverAllLocations() {
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

	<div data-role="content" data-theme="a">
		<div id="map_canvas" style="height:300px;"></div>
	</div>
	<!-- /content -->

</div>
<!-- /maps --> 

<!-- /page -->

</body>
</html>
