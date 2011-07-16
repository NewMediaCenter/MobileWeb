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
//		$('#cllist').delegate('li', 'vclick', function() {
//			var clid = $(this).jqmData('id');
//			$('#map_canvas').jqmData('code', clid);
//			$.mobile.changePage({
//				url: "/mdot/maps", 
//				type: "get",
//				data: clid
//			});
//		});
		/*
		$('#cllist').delegate('li', 'click', function() {
			var detailId = $(this).get(0).getAttribute('detailId');
			buildingCode = detailId;
			//alert(detailId);
			$.mobile.changePage({
				url: "/mdot/maps", 
				type: "get"
			});
		});
		*/
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
		$.getJSON('knowledgebase/search?criteria=' + inputString, function(data) {
			var items = [];
			$.each(data.results, function(key, val) {
				//items.push('key:'+key+' val:'+val.documentId+' '+val.title+'<br/><br/>');
				items.push('<a href="knowledgebase/'+val.documentId+'"><li><h3>'+val.title+'</h3></li></a>')
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