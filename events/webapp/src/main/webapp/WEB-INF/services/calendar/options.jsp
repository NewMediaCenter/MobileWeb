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
     $('.tabs-tab3').addClass('selected');
     $('.tabs-panel3').show();
 });
</script>
	

	
	<div class="tabs-tabcontainer container_12">
    <div class="grid_3"><a class="tabs-tab1" name="tabs-tab1" href="${pageContext.request.contextPath}/calendar/month?date=${monthSelectedDate}">Month</a></div>
    <div class="grid_3"><a class="tabs-tab2" name="tabs-tab2" href="${pageContext.request.contextPath}/calendar/list?date=${selectedDate}">List</a></div>
      <div class="grid_3"><a class="tabs-tab3" name="tabs-tab3" href="${pageContext.request.contextPath}/calendar/options">Options</a></div>
      <div class="grid_3"><a class="tabs-tab4" name="tabs-tab4" href="${pageContext.request.contextPath}/calendar/createEvent" data-ajax="false">Add Event</a></div>
  </div>
	
	
  <div class="tabs-panel3" name="tabs-panel3">
			<ul data-role="listview" data-theme="g">
				<li><a data-ajax="false" href="${pageContext.request.contextPath}/calendar/filters">
						<h3>Filters</h3>
						<p>Filter your events.</p> </a></li>
				<c:if test="${not empty filter}">
					<li><a data-ajax="false" href="${pageContext.request.contextPath}/calendar/removeFilter">
							<h3>Remove Filter ${filter.filterName}</h3>
							<p>Remove your filter.</p> </a></li>
				</c:if>
				<li><a data-ajax="false" href="${pageContext.request.contextPath}/calendar/refresh">
						<h3>Refresh My Calendar</h3>
						<p>This will refresh your calendar.</p> </a></li>
				<li><a data-ajax="false" href="${pageContext.request.contextPath}/calendar/pendingMeetings">
						<h3>Pending Meetings</h3>
						<p>New, updated, or cancelled meetings.</p> </a></li>
			</ul>
		
		

	<!-- /page -->
</div>
 </kme:content></kme:page>