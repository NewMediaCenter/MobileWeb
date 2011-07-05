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
<link href="${pageContext.request.contextPath}/css/jquery.mobile.datebox.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/custom.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.1.min.js"></script>
<script type="text/javascript">
    $( document ).bind( "mobileinit", function(){ $.mobile.page.prototype.options.degradeInputs.date = 'text'; });	
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0b1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile.datebox.min.js"></script>
</head>

<body>
<div data-role="page" id="Calendar-Events-edit">
  <div data-role="header">
    <h1>Event</h1><a href="${pageContext.request.contextPath}/calendar/options" class="ui-btn-right">options</a>
  </div>
  <div data-role="content" >
    <form:form action="${pageContext.request.contextPath}/calendar/saveEvent" commandName="event" data-ajax="false">
        <form:hidden path="eventId"/>
        <form:hidden path="lockingNumber"/>
        <form:hidden path="seriesId"/>
        <form:hidden path="date"/>
        
        <fieldset>Event:<br/><br/>
            <label for="title">Title</label>
            <form:input path="title"  cssClass="text ui-widget-content ui-corner-all" />
            <br/><form:errors path="title" />
           
            <br/><br/>
            <label for="allDay">All Day</label>
            <form:checkbox path="allDay"  cssClass="text ui-widget-content ui-corner-all" onclick='javascript:$("div#calendar-time").toggle();' />
            <br/><br/>
            <label for="startDate">Start Date</label>
            <form:input path="startDate"  cssClass="text ui-widget-content ui-corner-all" data-role="datebox" data-options='{"useDialogForceFalse": true, "dateFormat": "MM/DD/YYYY"}'/>
            <br/><form:errors path="startDate" />
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
                <br/><br/>
            
                <label for="endDate">End Date</label>
                <form:input path="endDate"  cssClass="text ui-widget-content ui-corner-all" data-role="datebox" data-options='{"useDialogForceFalse": true, "dateFormat": "MM/DD/YYYY"}'/>
                <br/><form:errors path="endDate" />
                <br/>
                
                 <label for="endTime">End Time</label>
                <form:input path="endTime"  cssClass="text ui-widget-content ui-corner-all" data-role="datebox" data-options='{"mode": "timebox", "timeFormat":12}' />
                <br/><br/>
            </div>
            <label for="location">Location</label>
            <form:input path="location"  cssClass="text ui-widget-content ui-corner-all" />
            <br/><form:errors path="location" />
            <br/><br/>

            <label for="categories">Category</label>
            
            <form:select path="categories" multiple="true" items="${event.defaultCategories}" data-native-menu="false"/>
            <br/><form:errors path="categories" />
            <br/><br/>
            <label for="showAs">Show As</label>
             <form:select path="showAs"  cssClass="ui-widget-content ui-corner-all" >
                    <form:option value="BUSY" label="Busy" />
                    <form:option value="FREE" label="Free" />
            </form:select><br/><form:errors path="showAs" />
            <br/><br/>
            <label for="sharing">Sharing</label>
            <form:select path="sharing"  cssClass="ui-widget-content ui-corner-all" >
                    <form:option value="PUBLIC" label="Public" />
                    <form:option value="PRIVATE" label="Private" />
                    <form:option value="CONFIDENTIAL" label="Confidential" />
            </form:select><br/><form:errors path="sharing" />
            
            <br/><br/>
            <label for="description">Description</label>
            <form:textarea path="description" />
            <br/><form:errors path="description" />
            <br/><br/>
            
            <c:if test="${empty seriesId}">
                <hr/>
                 <br/><br/>
    	       <label for="recurrenceType">Repeat:</label><br/>
               
               <c:if test="${event.recurrenceType eq 'CUSTOM-DAILY' or event.recurrenceType eq 'CUSTOM-WEEKLY' or event.recurrenceType eq 'CUSTOM-MONTHLY' or event.recurrenceType eq 'CUSTOM-YEARLY'}">
                    <form:radiobutton path="recurrenceType" value="${event.recurrenceType}" label="${event.recurrenceMessage}"/><br/>
                </c:if> 
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
                
                <form:radiobutton path="recurrenceType" value="NONE" label="None"/><br/>
                <form:radiobutton path="recurrenceType" value="DAILY" label="Every Day"/><br/>
                <form:radiobutton path="recurrenceType" value="WEEKLY" label="Every Week"/><br/>
    	        <form:radiobutton path="recurrenceType" value="WEEKLY2" label="Every 2 Weeks" /><br/>
                <form:radiobutton path="recurrenceType" value="MONTHLY" label="Every Month" /><br/>
                <form:radiobutton path="recurrenceType" value="YEARLY" label="Every Year"/><br/>
                <form:errors path="recurrenceType" />
                <br/><br/>
               <label for="recurrenceUntilDate">Repeat Until Date</label>
            	<form:input path="recurrenceUntilDate"  cssClass="text ui-widget-content ui-corner-all" data-role="datebox" data-options='{"useDialogForceFalse": true, "dateFormat": "MM/DD/YYYY"}'/>
            	<br/><form:errors path="recurrenceUntilDate" />
            </c:if>
        </fieldset>
        <input name="save" type="image" value="Save" src="${pageContext.request.contextPath}/images/btn-save.gif" alt="save" />
    </form:form>
  </div>
  <div data-role="footer" data-id="events-footer" data-position="fixed" role="contentinfo" data-theme="b">
  </div>
</div>
<!-- /page -->

</body>
</html>