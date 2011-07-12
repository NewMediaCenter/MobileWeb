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
<link href="css/jquery.mobile-1.0a4.1.css" rel="stylesheet" type="text/css" />
<link href="css/custom.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="js/custom.js"></script>
<script type="text/javascript" src="js/jquery.mobile-1.0a4.1.js"></script>

</head>

<body>
<div data-role="page" id="">
	<div data-role="header" data-position="">
    <h1>Details</h1><a href="/mdot/index.jsp" data-icon="home" class="ui-btn-right">home</a>
  </div>
	<!-- /header -->

	<div data-role="content" style="padding-top:0px">
    <ul data-role="listview" data-theme="c"  data-dividertheme="b"  data-inset="true">
			<c:forEach items="${roster}" var="item" varStatus="status">
				<li>
		        <div class="ui-grid-a">
		          <div class="ui-block-a" style="width:40%"> <img src="http://localhost:9090/direct/profile/${item.displayID}/image/thumb?sakai.session=${sessionId}" width="70" height="70" alt="pic"></div>
		          <div class="ui-block-b" style="width:60%">
		            <h3 style="margin-top:0px">${item.lastName}, ${item.firstName}</h3>
		            <p>${item.school}</p>
		            <p>${item.roleTitle}</p>
		          </div>
		        </div>
		        <!-- /grid-a --> 
		        
		      </li>
		      <li><a href="mailto:${item.email}"> ${item.email} </a> </li>
		      <li><a href="tel:${item.workPhone}"> ${item.workPhone} </a> </li>
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