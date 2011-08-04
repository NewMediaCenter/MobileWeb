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
<%@ taglib prefix="kme" uri="http://kuali.org/mobility"%>


<kme:page title="Events" id="Calendar-Events" backButton="true" homeButton="true" cssFilename="events" backButtonURL="${pageContext.request.contextPath}/home">


	<kme:content>



<script type="text/javascript">
	calendarSelectedDate = ${selectedDate};
	calendarSelectedMonthYear = ${monthYear};
</script>

<script type="text/javascript">
 $(window).load(function() {
     $('.tabs-tab1').addClass('selected');
     $('.tabs-panel1').show();
 });
</script>
  
  
  <div class="tabs-tabcontainer container_12">
      <div class="grid_3"><a class="tabs-tab1 selected" name="tabs-tab1" href="${pageContext.request.contextPath}/calendar/month?date=${monthYear}">Month</a></div>
      <div class="grid_3"><a class="tabs-tab2" name="tabs-tab2" href="${pageContext.request.contextPath}/calendar/list?date=${selectedDate}">List</a></div>
      <div class="grid_3"><a class="tabs-tab3" name="tabs-tab3" href="${pageContext.request.contextPath}/calendar/options">Options</a></div>
      <div class="grid_3"><a class="tabs-tab4" name="tabs-tab4" href="${pageContext.request.contextPath}/calendar/createEvent" data-ajax="false">Add Event</a></div>
    </div>
    <div class="tabs-panel1" name="tabs-panel1" style="margin-top:25px">
    
      <div class="container_16 monthgrid" style="background-color:; margin-top:px">
        <div class="grid_4">
          <div class="cal-arrow-left">  <a href="${pageContext.request.contextPath}/calendar/month?date=${previousMonth}" onclick="javascript:setCalendarSelectedDate(${previousMonth}01, ${previousMonth}); return true;" data-direction="reverse">
                    <img src="${pageContext.request.contextPath}/images/arrow-left.png" width="16" height="16" alt="back">
                </a>  </div>
        </div>
        <div class="grid_8">
          <div class="month-year"><c:out value="${viewData.title}"/></div>
        </div>
        <div class="grid_4">
          <div class="cal-arrow-right">  <a href="${pageContext.request.contextPath}/calendar/month?date=${nextMonth}" onclick="javascript:setCalendarSelectedDate(${nextMonth}01, ${nextMonth});return true;">
                    <img src="${pageContext.request.contextPath}/images/arrow-right.png" width="16" height="16" alt="forward">
                </a>  </div>
        </div>
        
  <div class="month-grid">  
        <div class=" daysofweek grid_cal"> Sun</div>
        <div class=" daysofweek grid_cal"> Mon</div>
        <div class=" daysofweek grid_cal"> Tue</div>
        <div class=" daysofweek grid_cal"> Wed</div>
        <div class=" daysofweek grid_cal"> Thu</div>
        <div class=" daysofweek grid_cal"> Fri</div>
        <div class=" daysofweek grid_cal"> Sat</div>
   

      <c:forEach var="day" items="${events}">
      
              <c:choose>
                <c:when test="${day.value.currentMonth}">
                	<c:choose>
               			<c:when test="${selectedDate eq day.key}">
               				<c:set var="current" value="-selected"/>
               			</c:when>
               			<c:otherwise>
               				<c:set var="current" value=""/>
               			</c:otherwise>
               		</c:choose>
               		<c:choose>
               			<c:when test="${today eq day.key}">
               				<c:set var="todaySelected" value="event-${day.value.hasEvents}-today"/>
               			</c:when>
               			<c:otherwise>
               				<c:set var="todaySelected" value=""/>
               			</c:otherwise>
               		</c:choose>
                    <div class="datebox-${day.value.currentMonth} grid_cal" onclick='javascript:hideCalendarDay(${monthYear});showCalendarDay(${monthYear}, ${day.key});return false;'>
                        <div class="event-${day.value.hasEvents}${current} event-${day.value.hasEvents}${monthYear}${day.key} event-${day.value.hasEvents}${current}${monthYear} ${todaySelected}">${day.value.day}</div>
                    </div>
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
                        <div class="datebox-${day.value.currentMonth} grid_cal">
                            <div class="event-${day.value.hasEvents} event-${day.value.hasEvents}-${day.key} event-${day.value.hasEvents}${monthYear}">${day.value.day}</div>
                        </div>
                 </c:otherwise>
              </c:choose>
          
      </c:forEach>
  
      </div></div>
     
       
  <c:if test="${not empty filter}">Filtered by: ${filter.filterName}</c:if>

    <c:forEach var="day" items="${events}">
    <c:choose>
       <c:when test="${selectedDate eq day.key}">
           <c:set var="display" value="block"/>
       </c:when>
       <c:otherwise>
           <c:set var="display" value="none"/>
       </c:otherwise>
	</c:choose>
    
      <div class="Calendar-Day-${monthYear} Calendar-Day-${monthYear}-${day.key}" style="display: ${display};">
     <ul data-role="listview" data-theme="c" data-inset="true">
        <c:forEach var="event" items="${day.value.events}">
           <li>
              <a href="${pageContext.request.contextPath}/calendar/event?eventId=${event.eventId}&date=${event.date}">
                <h3 style="white-space:normal"><c:out value="${event.title}"/></h3>
                <p style="white-space:normal"><c:out value="${event.location}"/></p>
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
    
    </kme:content></kme:page>
    
   