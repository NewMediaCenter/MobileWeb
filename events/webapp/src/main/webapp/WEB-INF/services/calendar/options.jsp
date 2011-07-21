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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>My Calendar</title>
<link href="${pageContext.request.contextPath}/css/jquery.mobile-1.0b1.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/custom.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0b1.min.js"></script>
</head>

<body>
	<div data-role="page" id="Calendar-Events">
		<div data-role="header">
			<h1>Options</h1>
			<a href="${pageContext.request.contextPath}/calendar/month" class="ui-btn-left" data-direction="reverse" data-transition="slide" data-ajax="false">view calendar</a> <a href="${pageContext.request.contextPath}/calendar/createEvent" class="ui-btn-right" data-ajax="false">Add Event</a>
		</div>
		<div data-role="content">
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
		</div>
		<div data-role="footer" data-id="events-footer" data-position="fixed" role="contentinfo" data-theme="b"></div>
	</div>
	<!-- /page -->

</body>
</html>