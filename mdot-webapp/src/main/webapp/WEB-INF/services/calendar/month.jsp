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

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.5.2.min.js">
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0a4.1.js"></script>
</head>

<body>
<div data-role="page" id="Calendar-Events">
<script type="text/javascript">
calendarSelectedDate = ${selectedDate};
calendarUniquePage = ${uniquePage};
</script>
  <div data-role="header">
    <h1>Events</h1><a href="${pageContext.request.contextPath}/calendar/createEvent" data-ajax="false">Add Event</a> <a href="${pageContext.request.contextPath}/calendar/options" class="ui-btn-right">options</a>
  </div>
  <!-- /header -->
  <div data-role="content" >
      <c:set var="uniqueId" value="${sessionScope['calendarUniqueId'].id}" />
      <c:set var="nextUniqueId" value="${sessionScope['calendarUniqueId'].nextId}" />
      <div class="cal-header" >
      <div class="ui-grid-20-60-20">
        <div class="ui-block-a">
          <div class="cal-arrow-left">
                <a href="${pageContext.request.contextPath}/calendar/month?date=${previousMonth}" data-direction="reverse">
                    <img src="${pageContext.request.contextPath}/images/arrow-left.png" width="16" height="16" alt="back">
                </a> 
          </div>
        </div>
        <div class="ui-block-b">
          <div class="month-year"><c:out value="${viewData.title}"/></div>
        </div>
        <div class="ui-block-c">
          <div class="cal-arrow-right">
                <a href="${pageContext.request.contextPath}/calendar/month?date=${nextMonth}" >
                    <img src="${pageContext.request.contextPath}/images/arrow-right.png" width="16" height="16" alt="forward">
                </a> 
           </div>
        </div>
      </div>
      <div class="ui-grid-evt-month"> 
        <!--Days of Week-->
        <div class="ui-block-1">
          <div class="daysofweek"> Sun </div>
        </div>
        <div class="ui-block-2">
          <div class="daysofweek"> Mon </div>
        </div>
        <div class="ui-block-3">
          <div class="daysofweek"> Tue </div>
        </div>
        <div class="ui-block-4">
          <div class="daysofweek"> Wed </div>
        </div>
        <div class="ui-block-5">
          <div class="daysofweek"> Thu </div>
        </div>
        <div class="ui-block-6">
          <div class="daysofweek"> Fri </div>
        </div>
        <div class="ui-block-7">
          <div class="daysofweek"> Sat </div>
        </div>
      </div>
    </div>
    <div class="ui-grid-evt-month"> 
      <c:forEach var="day" items="${events}">
           <div class="ui-block-${day.value.dayOfWeek}"> 
            <c:choose>
                <c:when test="${day.key eq selectedDate}">
                    <c:set var="current" value="-current"/>
                </c:when>
                <c:otherwise>
                    <c:set var="current" value=""/>
                </c:otherwise>
             </c:choose>
              <c:choose>
                <c:when test="${day.value.currentMonth}">
             	      <a href="#" onclick='javascript:hideCalendarDay(${uniquePage},${day.value.monthYear});showCalendarDay(${uniquePage},${day.value.monthYear},${day.key});return false;'>
                        <div id="datebox-${uniquePage}-${day.key}" class="datebox-${day.value.currentMonth}${current} datebox-${day.value.currentMonth}${day.key} datebox-${day.value.currentMonth}${current}${day.value.monthYear}">
                            <div id="event-${uniquePage}-${day.key}" class="event-${day.value.hasEvents}${current} event-${day.value.hasEvents}${day.key} event-${day.value.hasEvents}${current}${day.value.monthYear}">${day.value.day}</div>
                        </div>
                      </a> 
                 </c:when>
                 <c:otherwise>
                    <c:choose>
                        <c:when test="${day.value.beforeCurrentMonth}">
                            <c:set var="dataDirection" value="reverse"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="dataDirection" value=""/>
                        </c:otherwise>
                     </c:choose>
                    <a href="${pageContext.request.contextPath}/calendar/month?date=${day.key}" data-direction="${dataDirection}" onclick='javascript:hideCalendarDay(${uniquePage},${day.value.monthYear});showCalendarDay(${uniquePage},${day.value.monthYear},${day.key});return true;'>
                        <div id="datebox-${uniquePage}-${day.key}" class="datebox-${day.value.currentMonth} datebox-${day.value.currentMonth}${day.key} datebox-${day.value.currentMonth}${day.value.monthYear}">
                            <div id="event-${uniquePage}-${day.key}" class="event-${day.value.hasEvents} event-${day.value.hasEvents}${day.key} event-${day.value.monthYear}">${day.value.day}</div>
                        </div>
                      </a> 
                 </c:otherwise>
              </c:choose>
          </div>
      </c:forEach>
    </div>
  <br/>
   <c:forEach var="day" items="${events}">
      <div id="Calendar-Day-${uniquePage}-${day.key}" class="Calendar-Day-${day.value.monthYear} Calendar-Day-${uniquePage}">
      <ul data-role="listview" data-theme="g">
        <c:forEach var="event" items="${day.value.events}">
           <li>
              <a href="${pageContext.request.contextPath}/calendar/event?eventId=${event.eventId}&date=${event.date}">
                <h3><c:out value="${event.title}"/></h3>
                <p><c:out value="${event.location}"/></p>
                <p><c:out value="${event.time}"/></p>
             </a> 
          </li>
        </c:forEach>
        <c:if test="${empty day.value.events}">
           <li>
                <h3>There are no events on this day.</h3>
          </li>     
        </c:if>
      </ul>
     </div>
   </c:forEach>
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