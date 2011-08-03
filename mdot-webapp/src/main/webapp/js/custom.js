/*
  Copyright 2011 The Kuali Foundation Licensed under the Educational Community
  License, Version 2.0 (the "License"); you may not use this file except in
  compliance with the License. You may obtain a copy of the License at
  http://www.osedu.org/licenses/ECL-2.0 Unless required by applicable law or
  agreed to in writing, software distributed under the License is distributed
  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  express or implied. See the License for the specific language governing
  permissions and limitations under the License.
*/

// Remove the back button from all pages
//$.mobile.page.prototype.options.addBackBtn=false;

//  turns default transitions off


// $(document).bind("mobileinit", function(){
//         $.mobile.defaultTransition = 'none';
// });

/* Test */

$(document).bind("mobileinit", function(){
  $.mobile.ajaxEnabled = false;
});

function getParameterByName( name )
{
  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  var regexS = "[\\?&]"+name+"=([^&#]*)";
  var regex = new RegExp( regexS );
  var results = regex.exec( window.location.href );
  if( results == null )
    return "";
  else
    return decodeURIComponent(results[1].replace(/\+/g, " "));
}



/* Computer Labs */

$('[data-role=page][id=computerlabshome]').live('pagebeforeshow',function(event, ui){
	//alert('test44');
	$('#clListTemplate').template('clListTemplate');
	//refreshComputerLabs();
});

function refreshComputerLabs() {
	$.mobile.pageLoading();
	$('#cllist').text('');
	var dynamicDataResp = $.ajax({
		url: "computerlabs?campus=BL",
		dataType: 'json',
		async: false,
		cache: false           
	});
	if(dynamicDataResp.status == 200){
		var dynamicDataObj = jQuery.parseJSON(dynamicDataResp.responseText);
		$.tmpl('clListTemplate', dynamicDataObj).appendTo('#cllist');
		$('#cllist').listview('refresh');
	}
}

//$('[data-role=page][id=kb]').live("pagebeforeshow", function(event) {
////	alert("test");
//	$('#searchText').keyup(function() {
//		lookup($('#searchText').val());
//	});	
//});

/*
 * Maps
 */

function initialize(id, lat, lng) {
	var latlng = new google.maps.LatLng(lat, lng);
	var myOptions = {
		zoom: 17,
		center: latlng,
		mapTypeId: google.maps.MapTypeId.ROADMAP
    };
	var newmap = new google.maps.Map(document.getElementById(id), myOptions);
    //map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
    google.maps.event.trigger(newmap, 'resize');
    return newmap;
}

function addMarker(map, array, location, icon) {
	if (icon) {
		var myOptions = {
			position: location,
			map: map,
			icon: icon 
		};
		
	} else {
		var myOptions = {
			position: location,
			map: map
		};
	}
	marker = new google.maps.Marker(myOptions);
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
function showOverlays(map, array) {
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

function showLocationByCoordinates(map, markersArray, latitude, longitude) {
	if (map) {
		google.maps.event.trigger(map, 'resize');
		var location = new google.maps.LatLng(latitude, longitude);
		map.setZoom(17);
		map.setCenter(location);
		addMarker(map, markersArray, location);
		google.maps.event.trigger(map, 'resize');
	}
}



/* Begin Calendar js*/

var calendarSelectedDate = null;
var calendarSelectedMonthYear = null;

$("div#Calendar-Events").live('pagecreate',function(e, ui){ 
    if (calendarSelectedDate == null){
    	var d = new Date();
    	var curr_date = ""+d.getDate();
    	if(curr_date.length < 2){
    		curr_date = "0"+curr_date;
    	}
    	var curr_month = (d.getMonth()+1)+"";
    	if(curr_month.length < 2){
    		curr_month = "0"+curr_month;
    	}
    	var curr_year = d.getFullYear();
    	calendarSelectedDate = "" + curr_year + curr_month + curr_date;
    	calendarSelectedMonthYear = "" + curr_year + curr_month;
//    	alert("selected: "+calendarSelectedDate + " year: "+calendarSelectedMonthYear);
    }
	hideCalendarDay(calendarSelectedMonthYear);
	showCalendarDay(calendarSelectedMonthYear, calendarSelectedDate);
 });

function hideCalendarDay(selectedMonthYear){
	$("div.Calendar-Day-"+selectedMonthYear).hide();
	
	var events = $("div.event-true-selected"+selectedMonthYear);
	events.removeClass("event-true-selected event-true-selected"+selectedMonthYear);
	events.addClass("event-true event-true"+selectedMonthYear);

	var eventsFalse = $("div.event-false-selected"+selectedMonthYear);
	eventsFalse.removeClass("event-false-selected event-false-selected"+selectedMonthYear);
	eventsFalse.addClass("event-false event-false"+selectedMonthYear);
}

function showCalendarDay(selectedMonthYear, selectedDate){
	$("div.Calendar-Day-"+selectedMonthYear+"-"+selectedDate).show()

    var currentEvent = $("div.event-true"+selectedMonthYear+selectedDate);
    currentEvent.removeClass("event-true event-true-"+selectedMonthYear);
    currentEvent.addClass("event-true-selected event-true-selected"+selectedMonthYear);

    var currentEventFalse = $("div.event-false"+selectedMonthYear+selectedDate);
    currentEventFalse.removeClass("event-false event-false-"+selectedMonthYear);
    currentEventFalse.addClass("event-false-selected event-false-selected"+selectedMonthYear);
}

/* End Calendar js*/

/* 	function fill(thisValue) {
	$('#inputString').val(thisValue);
	setTimeout("$('#suggestions').hide();", 200);
} */


/* Begin Horizontal Tab Navigation*/

 $(window).load(function() {
     $('.tabs-panel2').hide();
     $('.tabs-panel3').hide();
     $('.tabs-panel4').hide();
     $('.tabs-panel1').hide();

     $('.tabs-tab2').click(function() {
         $('.tabs-tab2').addClass('selected');
         $('.tabs-tab3').removeClass('selected');
         $('.tabs-tab4').removeClass('selected');
         $('.tabs-tab1').removeClass('selected');
         $('.tabs-panel2').show();
         $('.tabs-panel3').hide();
         $('.tabs-panel4').hide();
         $('.tabs-panel1').hide();
     });

     $('.tabs-tab3').click(function() {
         $('.tabs-tab3').addClass('selected');
         $('.tabs-tab2').removeClass('selected');
         $('.tabs-tab4').removeClass('selected');
         $('.tabs-tab1').removeClass('selected');
         $('.tabs-panel2').hide();
         $('.tabs-panel3').show();
         $('.tabs-panel4').hide();
         $('.tabs-panel1').hide();
     });

     $('.tabs-tab4').click(function() {
         $('.tabs-tab3').removeClass('selected');
         $('.tabs-tab2').removeClass('selected');
         $('.tabs-tab4').addClass('selected');
         $('.tabs-tab1').removeClass('selected');
         $('.tabs-panel2').hide();
         $('.tabs-panel3').hide();
         $('.tabs-panel4').show();
         $('.tabs-panel1').hide();
     });

     $('.tabs-tab1').click(function() {
         $('.tabs-tab3').removeClass('selected');
         $('.tabs-tab2').removeClass('selected');
         $('.tabs-tab4').removeClass('selected');
         $('.tabs-tab1').addClass('selected');
         $('.tabs-panel2').hide();
         $('.tabs-panel3').hide();
         $('.tabs-panel4').hide();
         $('.tabs-panel1').show();
     });

 });


/* End Horizontal Tab Navigation*/