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
<link href="${pageContext.request.contextPath}/css/jquery.mobile-1.0b1.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/custom.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0b1.js"></script>
</head>

<body>
<div data-role="page" id="">
	<div data-role="header">
		<h1>My Classes</h1><a href="${pageContext.request.contextPath}/index.jsp" data-icon="home" class="ui-btn-right">home</a>
	</div>
	<!-- /header -->

	<div data-role="content" data-theme="" style="padding-top:0px">
		<h3>Fall 2011 [static]</h3>
		<ul data-role="listview" data-theme="c" data-inset="true">
			<c:forEach items="${myclasses}" var="item" varStatus="status">
				<li>
					<a href="${pageContext.request.contextPath}/myclasseshome?siteId=${item.courseId}&siteTitle=${item.courseTitle}">
						<h3>${item.courseTitle}</h3>
						<p>${item.courseDesc}</p>
					</a>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div data-role="footer" data-id="mcl-footer" data-position="fixed"  role="contentinfo"  data-theme="b">
    	<div data-role="navbar" role="navigation">
      	<ul class="ui-grid-a">
        <li class="ui-block-a"><a href="${pageContext.request.contextPath}/myclasses">My Classes</a></li>
        <li class="ui-block-b"><a href="mcl-prog.html">My Projects </a></li>
      	</ul>
    </div>
  </div>
	<!-- /content -->

	<!-- /header --> 
</div>
<!-- /stc --> 

<!-- /page -->

</body>
</html>