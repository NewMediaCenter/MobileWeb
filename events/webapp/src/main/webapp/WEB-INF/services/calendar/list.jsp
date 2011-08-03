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
 $(window).load(function() {
     $('.tabs-tab2').addClass('selected');
     $('.tabs-panel2').show();
 });
</script>
	

	
	<div class="tabs-tabcontainer container_12">
    <div class="grid_3"><a class="tabs-tab1" name="tabs-tab1" href="${pageContext.request.contextPath}/calendar/month?date=${monthSelectedDate}">Month</a></div>
    <div class="grid_3"><a class="tabs-tab2" name="tabs-tab2" href="${pageContext.request.contextPath}/calendar/list?date=${selectedDate}">List</a></div>
      <div class="grid_3"><a class="tabs-tab3" name="tabs-tab3" href="${pageContext.request.contextPath}/calendar/options">Options</a></div>
      <div class="grid_3"><a class="tabs-tab4" name="tabs-tab4" href="${pageContext.request.contextPath}/calendar/createEvent" data-ajax="false">Add Event</a></div>
  </div>
	
	

<%--<div align="center"><b><c:out value="${viewData.title}"/></b><br/><br/></div>--%>

  <div class="tabs-panel2" name="tabs-panel2">
  
  <c:if test="${not empty filter}">Filtered by: ${filter.filterName}<br/><br/></c:if>
          <ul data-role="listview" data-theme="c"  data-dividertheme="d"  data-inset="false">
        
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
   
    </ul>



            
            
             <div class="container_12" style="padding-top:20px">
      <div class="grid_6"><a href="${pageContext.request.contextPath}/calendar/listEvents?date=${selectedDate}&beginDate=${previousDate}&endDate=${currentEndDate}" data-role="button" data-icon="arrow-l" data-theme="c"><c:out value="${days}"/> prev</a></div>
      <div class="grid_6"><a href="${pageContext.request.contextPath}/calendar/listEvents?date=${selectedDate}&beginDate=${beginDate}&endDate=${endDate}" data-role="button" data-icon="arrow-r" data-theme="c" data-iconpos="right"><c:out value="${days}"/> future</a></div>
    </div>
    

  </div>
 </kme:content></kme:page>
    