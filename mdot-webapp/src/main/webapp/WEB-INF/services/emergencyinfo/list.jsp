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
<title>Emergency Info</title>
<link href="css/jquery.mobile-1.0a4.1.css" rel="stylesheet" type="text/css" />
<link href="css/custom.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="js/custom.js"></script>
<script type="text/javascript" src="js/jquery.mobile-1.0a4.1.js"></script>

</head>

<body>
<div data-role="page" id="">
	<div data-role="header">
		<h1>Emergency Info</h1>
	</div>
	<!-- /header -->

	<div data-role="content" data-theme="a">
		<ul data-role="listview" data-theme="c" data-dividertheme="b"
			data-filter="false" data-inset="false">
			<c:forEach items="${emergencyinfos}" var="item" varStatus="status">
				<li>
					<a href="tel:${item.link}">
						<h3>${item.title}</h3>
						<p>${item.link}</p>
					</a>
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