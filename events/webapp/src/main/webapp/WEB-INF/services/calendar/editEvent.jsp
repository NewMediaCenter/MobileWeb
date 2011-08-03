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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>My Calendar</title>
<link href="${pageContext.request.contextPath}/css/jquery.mobile-1.0b1.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/jquery-mobile-fluid960.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/jquery.mobile.datebox.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/custom.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/events.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.1.min.js"></script>
<script type="text/javascript">
    $( document ).bind( "mobileinit", function(){ $.mobile.page.prototype.options.degradeInputs.date = 'text'; });
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0b1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile.datebox.min.js"></script>


	
		
	<script type="text/javascript">
 $(window).load(function() {
     $('.tabs-tab4').addClass('selected');
     $('.tabs-panel4').show();
 });
</script>

</head>

<body>
<div data-role="page" id="Calendar-Events-edit">
  <div data-role="header">
    <h1>Event</h1>
  </div>
  <div data-role="content" >
  
  
  	<div class="tabs-tabcontainer container_12">
    <div class="grid_3"><a class="tabs-tab1" name="tabs-tab1" href="${pageContext.request.contextPath}/calendar/month?date=${monthSelectedDate}">Month</a></div>
    <div class="grid_3"><a class="tabs-tab2" name="tabs-tab2" href="${pageContext.request.contextPath}/calendar/list?date=${selectedDate}">List</a></div>
      <div class="grid_3"><a class="tabs-tab3" name="tabs-tab3" href="${pageContext.request.contextPath}/calendar/options">Options</a></div>
      <div class="grid_3"><a class="tabs-tab4" name="tabs-tab4" href="${pageContext.request.contextPath}/calendar/createEvent" data-ajax="false">Add Event</a></div>
  </div>
  
    <div class="tabs-panel4" name="tabs-panel4">
  
    <form:form action="${pageContext.request.contextPath}/calendar/saveEvent" commandName="event" data-ajax="false">
        <form:hidden path="eventId"/>
        <form:hidden path="lockingNumber"/>
        <form:hidden path="seriesId"/>
        <form:hidden path="date"/>
        <form:hidden path="foursquareVenueId"/>
        <form:hidden path="locationId"/>
        
        <fieldset>
            <label for="title">Title</label>
            <form:input path="title" cssClass="text ui-widget-content ui-corner-all" />
            <form:errors path="title" />
                        <br/>
            <label for="location">Location</label>
            <form:input path="location"  cssClass="text ui-widget-content ui-corner-all" />
            <form:errors path="location" />
            <br/>
            <form:checkbox path="allDay" onclick='javascript:$("div#calendar-time").toggle();' />
            <label for="allDay">All Day</label>
            <br/>
            <label for="startDate">Start Date</label>
            <form:input path="startDate"  cssClass="genField textEntry date" data-role="datebox" data-options='{"useDialogForceFalse": true, "dateFormat": "MM/DD/YYYY"}'/>
            <form:errors path="startDate" />
            <br/>
            <c:choose>
                <c:when test="${event.allDay}">
                    <c:set var="displayCalendarTime" value="none" />
                </c:when>
                <c:otherwise>
                    <c:set var="displayCalendarTime" value="block" />
                </c:otherwise>
            </c:choose>
            <div id="calendar-time" style="display: ${displayCalendarTime}">
                <label for="startTime">Start Time</label>
                <form:input path="startTime"  cssClass="text ui-widget-content ui-corner-all" data-role="datebox" data-options='{"mode": "timebox", "timeFormat":12}' />
                <br/>
                <label for="endDate">End Date</label>
                <form:input path="endDate"  cssClass="text ui-widget-content ui-corner-all" data-role="datebox" data-options='{"useDialogForceFalse": true, "dateFormat": "MM/DD/YYYY"}'/>
                <form:errors path="endDate" />
                <br/>
                 <label for="endTime">End Time</label>
                <form:input path="endTime"  cssClass="text ui-widget-content ui-corner-all" data-role="datebox" data-options='{"mode": "timebox", "timeFormat":12}' />
                <br/>
            </div>
            <label for="categories">Category</label>
            
            <form:select path="categories" multiple="true"  data-native-menu="false">
            	<form:option value="" label="No Category" data-placeholder="true"/>
            	<form:options items="${event.defaultCategories}"/>
            </form:select>
            <form:errors path="categories" />
            <br/>
            <label for="showAs">Show As</label>
             <form:select path="showAs"  cssClass="ui-widget-content ui-corner-all" data-native-menu="false">
                <form:option value="BUSY" label="Busy" />
                <form:option value="FREE" label="Free" />
            </form:select><form:errors path="showAs" />
            <br/>
            <label for="sharing">Sharing</label>
            <form:select path="sharing"  cssClass="ui-widget-content ui-corner-all" data-native-menu="false">
                <form:option value="PUBLIC" label="Public" />
                <form:option value="PRIVATE" label="Private" />
                <form:option value="CONFIDENTIAL" label="Confidential" />
            </form:select><form:errors path="sharing" />
            <br/>
            <label for="description">Description</label>
            <form:textarea path="description" />
            <form:errors path="description" />
            <br/>
            
            <c:if test="${empty seriesId}">
                <form:hidden path="dailyRecurrenceType"/>
                <form:hidden path="recurrenceDay"/>
                <form:hidden path="recurrenceInterval"/>
                <form:hidden path="recurrenceMonth"/>
                <form:hidden path="recurrenceRelativeDay"/>
                <form:hidden path="recurrenceRelativePosition"/>
                <form:hidden path="recurrenceUntilCount"/>
                <form:hidden path="relativeInterval"/>
                <c:forEach var="weekDay" items="${event.weeklyRecurrenceWeekDays}" varStatus="status">
                    <form:hidden path="weeklyRecurrenceWeekDays[${status.index}]" />
                </c:forEach>
	            <fieldset data-role="controlgroup">
	                <hr/>
	    	       <label for="recurrenceType">Repeat:</label><br/>
	               <c:if test="${event.recurrenceType eq 'CUSTOM-DAILY' or event.recurrenceType eq 'CUSTOM-WEEKLY' or event.recurrenceType eq 'CUSTOM-MONTHLY' or event.recurrenceType eq 'CUSTOM-YEARLY'}">
	                    <form:radiobutton path="recurrenceType" value="${event.recurrenceType}" label="Custom: ${event.recurrenceMessage}"/>
	                </c:if>                
	                <form:radiobutton path="recurrenceType" value="NONE" label="None"/>
	                <form:radiobutton path="recurrenceType" value="DAILY" label="Every Day"/>
	                <form:radiobutton path="recurrenceType" value="WEEKLY" label="Every Week"/>
	    	        <form:radiobutton path="recurrenceType" value="WEEKLY2" label="Every 2 Weeks" />
	                <form:radiobutton path="recurrenceType" value="MONTHLY" label="Every Month" />
	                <form:radiobutton path="recurrenceType" value="YEARLY" label="Every Year"/>
	                <form:errors path="recurrenceType" />
                </fieldset>
                <br/>
                <label for="recurrenceUntilDate">Repeat Until Date</label>
            	<form:input path="recurrenceUntilDate"  cssClass="text ui-widget-content ui-corner-all" data-role="datebox" data-options='{"useDialogForceFalse": true, "dateFormat": "MM/DD/YYYY"}'/>
            	<form:errors path="recurrenceUntilDate" /><br/>
            </c:if>
            
            <br/>
            <label for="remindBy">Reminder Delivery</label>
            <form:select path="remindBy" cssClass="ui-widget-content ui-corner-all" data-native-menu="false">
            	<form:option value="NONE" label="Choose delivery" data-placeholder="true"/>
            	<form:option value="EMAIL" label="Email Address" />
            	<form:option value="SMS" label="SMS" />
            </form:select><form:errors property="remindBy" />
            <br/>
            <label for="reminder">Send Reminder</label>
            <form:select path="reminder"  cssClass="ui-widget-content ui-corner-all" data-native-menu="false">
            	<form:option value="0" label="Choose option" data-placeholder="true"/>
                <form:option value="300000" label="5 Minutes before event" />
                <form:option value="900000" label="15 Minutes before event" />
                <form:option value="1800000" label="30 Minutes before event" />
                <form:option value="3600000" label="1 Hour before event" />
                <form:option value="7200000" label="2 Hours before event" />
                <form:option value="36000000" label="10 Hours before event" />
                <form:option value="43200000" label="12 Hours before event" />
                <form:option value="86400000" label="1 Day before event" />
                <form:option value="172800000" label="2 Days before event" />
                <form:option value="604800000" label="1 Week before event" />
                <form:option value="1209600000" label="2 Weeks before event" />
            </form:select><form:errors property="reminder" />
            <br/>
            <label for="invitations">Invite</label>
            <form:textarea path="invitations" />
            <form:errors path="invitations" />
            <br/>
        </fieldset>
        <input name="save" type="image" value="Save" src="${pageContext.request.contextPath}/images/btn-save.gif" alt="save" />
    </form:form>
  </div>

</div>
<!-- /page -->

</body>
</html>