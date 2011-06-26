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
<title>My Classes</title>
<link href="css/jquery.mobile-1.0a4.1.css" rel="stylesheet" type="text/css" />
<link href="css/custom.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="js/custom.js"></script>
<script type="text/javascript" src="js/jquery.mobile-1.0a4.1.js"></script>

</head>

<body>
<div data-role="page" id="">
	<div data-role="header">
		<h1><%= request.getParameter("siteTitle") %></h1><a href="/mdot/index.jsp" data-icon="home" class="ui-btn-right">home</a>
	</div>
	<!-- /header -->

	<div data-role="content" data-theme="a">
		<ul data-role="listview" data-theme="c" data-dividertheme="b" data-inset="false">
			<c:forEach items="${myclasseshome}" var="item" varStatus="status">
					<c:if test="${item.code=='-1'}">
				<li>
					<div class="ui-block-a" style="width:10%">
					  	<img src="http://localhost:9090/direct/profile/${item.displayId}/image/thumb?sakai.session=${sessionId}" width="70" height="70" alt="pic">
					</div>
					<div class="ui-block-b" style="width:90%">
					<a href="/mdot/sakaiparticipantdetails?siteId=
					<%= request.getParameter("siteId") %>&siteTitle=
					<%= request.getParameter("siteTitle") %>&displayId=
					${item.displayId}">
						<h3>Instructor</h3>
						<p>${item.title}</p>
					</a>
					</div>
				</li>
					</c:if>
					<c:if test="${item.code=='0'}">
				<li>
					<a href="/mdot/sakaiannouncements?siteId=<%= request.getParameter("siteId") %>&siteTitle=<%= request.getParameter("siteTitle") %>">
						<h3>${item.title}</h3>
					</a>
				</li>
					</c:if>
					<c:if test="${item.code=='1'}">
				<li>
					<a href="/mdot/sakaiassignments?siteId=<%= request.getParameter("siteId") %>&siteTitle=<%= request.getParameter("siteTitle") %>&userId=${userId}">
						<h3>${item.title}</h3>
					</a>
				</li>
					</c:if>
					<%-- 
					
					<c:if test="${item.code=='2'}">
					<a href="/mdot/gradebook?siteId=<%= request.getParameter("siteId") %>&siteTitle=<%= request.getParameter("siteTitle") %>">
						<h3>${item.title}</h3>
					</a>
					</c:if>
					--%>	
					<c:if test="${item.code=='3'}">
				<li>
					<a href="/mdot/roster?siteId=<%= request.getParameter("siteId") %>&siteTitle=<%= request.getParameter("siteTitle") %>">
						<h3>${item.title}</h3>
					</a>
				</li>
					</c:if>
					<c:if test="${item.code=='4'}">
				<li>
					<a href="/mdot/forums?siteId=<%= request.getParameter("siteId") %>&siteTitle=<%= request.getParameter("siteTitle") %>">
						<h3>${item.title}</h3>
					</a>
				</li>
					</c:if>
					<c:if test="${item.code=='5'}">
				<li>
					<a href="/mdot/resources?siteId=<%= request.getParameter("siteId") %>&siteTitle=<%= request.getParameter("siteTitle") %>">
						<h3>${item.title}</h3>
					</a>
				</li>
					</c:if>
					<c:if test="${item.code=='6'}">
				<li>
					<a href="/mdot/sakaiprivatetopics?siteId=<%= request.getParameter("siteId") %>&siteTitle=<%= request.getParameter("siteTitle") %>">
						<h3>${item.title}</h3>
					</a>
				</li>
					</c:if>
			</c:forEach>
		</ul>
	</div>
	<!-- /content -->

	<!-- /header --> 
</div>
<!-- /stc --> 

<!-- /page -->

</body>
</html>