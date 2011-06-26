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
<title>My Calendar</title>
<link href="${pageContext.request.contextPath}/css/jquery.mobile-1.0a4.1.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/custom.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0a4.1.js"></script>
</head>

<body>
<div data-role="page" id="Calendar-Events">
  <div data-role="header">
    <h1>Events</h1><a href="${pageContext.request.contextPath}/calendar/createEvent" data-ajax="false">Add Event</a> <a href="${pageContext.request.contextPath}/calendar/options" class="ui-btn-right">options</a>
  </div>
  <!-- /header -->
  
  <div data-role="content" ><div align="center"><b><c:out value="${viewData.title}"/></b><br/><br/></div>
    <ul data-role="listview" data-theme="g">
        <li><a href="${pageContext.request.contextPath}/calendar/listEvents?date=${selectedDate}&beginDate=${previousDate}&endDate=${currentEndDate}" >
            <h3>Display Previous <c:out value="${days}"/> days.</h3>
            </a> </li>
    <c:forEach var="listData" items="${events}">
      <li data-role="list-divider"><c:out value="${listData.weekday}"/> <c:out value="${listData.displayDate}"/></li>
      <c:forEach var="event" items="${listData.events}">      
          <li><a href="${pageContext.request.contextPath}/calendar/event?eventId=${event.eventId}&date=${event.date}">
            <h3><c:out value="${event.title}"/></h3>
                <p><c:out value="${event.location}"/></p>
                <p><c:out value="${event.time}"/></p>
            </a> </li>
      </c:forEach>
    </c:forEach>
    <li><a href="${pageContext.request.contextPath}/calendar/listEvents?date=${selectedDate}&beginDate=${beginDate}&endDate=${endDate}" >
            <h3>Display Next <c:out value="${days}"/> days</h3>
            </a> </li>
    </ul>
</div>
    
  <div data-role="footer" data-id="events-footer" data-position="fixed" role="contentinfo" data-theme="b">
    <div data-role="navbar" role="navigation">
      <ul class="ui-grid-a">
        <li class="ui-block-1"><a href="${pageContext.request.contextPath}/calendar/month?date=${selectedDate}">Month View</a></li>
        <li class="ui-block-2"><a href="${pageContext.request.contextPath}/calendar/list?date=${selectedDate}">List View</a></li>
      </ul>
    </div>
  </div>
  
</div>
<!-- /stc --> 

<!-- /page -->

</body>
</html>