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
<title><%= request.getParameter("siteTitle") %></title>
<link href="${pageContext.request.contextPath}/css/jquery.mobile-1.0b1.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/custom.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0b1.js"></script>
</head>

<body>
<div data-role="page" id="">
	<div data-role="header">
		<h1>Roster</h1><a href="${pageContext.request.contextPath}/index.jsp" data-icon="home" class="ui-btn-right">home</a>
	</div>
	<!-- /header -->

	<div data-role="content" data-theme="a">
		<ul data-role="listview" data-theme="c" data-dividertheme="b" data-inset="false">
			<c:forEach items="${roster}" var="item" varStatus="status">
				
				<li>
					<div class="ui-block-a" style="width:10%">
					  	<img src="http://localhost:9090/direct/profile/${item.displayID}/image/thumb?sakai.session=${sessionId}" width="70" height="70" alt="pic">
					</div>
					<div class="ui-block-b" style="width:90%">
					<a href="${pageContext.request.contextPath}/sakaiparticipantdetails?siteId=
					<%= request.getParameter("siteId") %>&siteTitle=
					<%= request.getParameter("siteTitle") %>&displayId=
					${item.displayID}">
						<h3>${item.displayName}</h3>
						<p>${item.roleTitle}</p>
					</a>
					</div>
				</li>
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