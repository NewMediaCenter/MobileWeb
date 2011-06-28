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

/* Maps */

var buildingCode;

var map;
var markersArray = [];
  
function addMarker(location) {
  marker = new google.maps.Marker({
    position: location,
    map: map
  });
  markersArray.push(marker);
}

// Removes the overlays from the map, but keeps them in the array
function clearOverlays() {
  if (markersArray) {
    for (i in markersArray) {
      markersArray[i].setMap(null);
    }
  }
}

// Shows any overlays currently in the array
function showOverlays() {
  if (markersArray) {
    for (i in markersArray) {
      markersArray[i].setMap(map);
    }
  }
}

// Deletes all markers in the array by removing references to them
function deleteOverlays() {
  if (markersArray) {
    for (i in markersArray) {
      markersArray[i].setMap(null);
    }
    markersArray.length = 0;
  }
}


$('[data-role=page]').live('pageshow',function(e, ui){ 
	var page_name = e.target.id;
	if (page_name == 'mapslocation'){
		
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
					deleteOverlays();
				    addMarker(location);
				    google.maps.event.trigger(map, 'resize');
//				    alert("test");
				}
				
			});
		}
	}
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

/* Computer Labs */

$('[data-role=page][id=computerlabshome]').live('pagebeforeshow',function(event, ui){
	//alert('test44');
	$('#clListTemplate').template('clListTemplate');
	refreshComputerLabs();
});

function refreshComputerLabs() {
	  $.mobile.pageLoading();
	  $('#cllist').text('');
	  var dynamicDataResp = $.ajax({
	        url: "/mdot/computerlabs?campus=BL",
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

$('[data-role=page]').live('pagecreate', function (event) {
	var activekb = 0;
	var activekbdoc = 0;
	var activemapslocation = 0;
	var activecomputerlabshome = 0;
	if (this.id == "kb" && activekb == 0) {
		activekb = 1;
		//alert('pagecreate');
		
		$('#kbsearch').keyup(function() {
			lookup($('#kbsearch').val());
		});	
	} else if (this.id == "kbdoc" && activekbdoc == 0) {
		activekbdoc = 1;
//	    $('a.native-anchor').bind('click', function(ev) {
//	          var target = $( $(this).attr('href') ).get(0).offsetTop;
//	          $.mobile.silentScroll(target);
//	          return false;
//	    });
		//$('a[href^="\\$"]').click(function(e){
		$('a[href*="knowledgebase"][href*="#"]').click(function(e){
			e.preventDefault();
			var name = $(this).attr('href').substr(24);
//			var pos = $('a[name='+name+']').offset();
//			$('html,body').animate({ scrollTop: pos.top });
//			alert(name);
			var target = $('a[name='+name+']').get(0).offsetTop;
			//alert(target);
			$.mobile.silentScroll(target);
			//alert(pos.top);
			//alert(e.isDefaultPrevented());
			return false;
		});
	} else if (this.id == "mapslocation" && activemapslocation == 0) {
		activemapslocation = 1;
//		initialize(39.17, -86.5);
	} else if (this.id == "computerlabshome" && activecomputerlabshome == 0) {
		$('#cllist').delegate('li', 'click', function() {
			var detailId = $(this).get(0).getAttribute('detailId');
			buildingCode = detailId;
			//alert(detailId);
			$.mobile.changePage({
				url: "/mdot/maps", 
				type: "get"
			});
		});
	}
});

function lookup(inputString) {
	if(inputString.length < 2) {
		// Hide the suggestion box.
		// $('#suggestions').hide();
		$('#searchresults').html('');
	} else {
/* 			$.post("rpc.php", {queryString: ""+inputString+""}, function(data){
			if(data.length >0) {
				$('#suggestions').show();
				$('#autoSuggestionsList').html(data);
			}
		});
*/
		$.getJSON('/mdot/knowledgebase/search?criteria=' + inputString, function(data) {
			var items = [];
			$.each(data.results, function(key, val) {
				//items.push('key:'+key+' val:'+val.documentId+' '+val.title+'<br/><br/>');
				items.push('<a href="/mdot/knowledgebase/'+val.documentId+'"><li><h3>'+val.title+'</h3></li></a>')
			});
			var pagehtml = '<div id="resultdata"></div>'
			$('#searchresults').html(pagehtml);
			$("#resultdata").html(items.join('')).page();
			//$('#searchresults').html(pagehtml).page();
			//alert(items.join(''));
/* 				  $.each(data, function(key, val) {
			    items.push('<li id="' + key + '">' + val + '</li>');
			  });
*/
/* 				  $('<ul/>', {
			    'class': 'my-new-list',
			    html: items.join('')
			  }).appendTo('body'); */
			});
		}
} // lookup

/* Begin Calendar js*/

var calendarSelectedDate = null;
var calendarSelectedMonthYear = null;

$("div#Calendar-Events").live('pagecreate',function(e, ui){ 
    if (calendarSelectedDate == null){
    	var d = new Date();
    	var curr_date = ""+d.getDate();
    	if(curr_date.length < 2){
    		curr_date = "0"+curr_Date;
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
	hideCalendarDay();
	showCalendarDay();
 });

function setCalendarSelectedDate(selectedDate, monthYear){
	calendarSelectedDate = selectedDate;
	calendarSelectedMonthYear = monthYear;
}

function hideCalendarDay(){
	$("div.Calendar-Day-"+calendarSelectedMonthYear).hide();
	var days = $("div.datebox-true-current"+calendarSelectedMonthYear);
	days.removeClass("datebox-true-current datebox-true-current"+calendarSelectedMonthYear);
	days.addClass("datebox-true datebox-true"+calendarSelectedMonthYear);
	
	var events = $("div.event-true-current"+calendarSelectedMonthYear);
	events.removeClass("event-true-current event-true-current"+calendarSelectedMonthYear);
	events.addClass("event-true event-true"+calendarSelectedMonthYear);

	var eventsFalse = $("div.event-false-current"+calendarSelectedMonthYear);
	eventsFalse.removeClass("event-false-current event-false-current"+calendarSelectedMonthYear);
	eventsFalse.addClass("event-false event-false"+calendarSelectedMonthYear);
}

function showCalendarDay(){
	$("div.Calendar-Day-"+calendarSelectedMonthYear+"-"+calendarSelectedDate).show()

    var currentDay = $("div.datebox-true"+calendarSelectedDate);
    currentDay.removeClass("datebox-true datebox-true-"+calendarSelectedMonthYear);
    currentDay.addClass("datebox-true-current datebox-true-current"+calendarSelectedMonthYear);

    var currentEvent = $("div.event-true"+calendarSelectedMonthYear+calendarSelectedDate);
    currentEvent.removeClass("event-true event-true-"+calendarSelectedMonthYear);
    currentEvent.addClass("event-true-current event-true-current"+calendarSelectedMonthYear);

    var currentEventFalse = $("div.event-false"+calendarSelectedDate);
    currentEventFalse.removeClass("event-false event-false-"+calendarSelectedMonthYear);
    currentEventFalse.addClass("event-false-current event-false-current"+calendarSelectedMonthYear);
}

/* End Calendar js*/

/* 	function fill(thisValue) {
	$('#inputString').val(thisValue);
	setTimeout("$('#suggestions').hide();", 200);
} */