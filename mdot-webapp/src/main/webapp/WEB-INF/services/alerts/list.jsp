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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>Alerts</title>

<jsp:include page="../resources.jsp" />

</head>

<body>
<div data-role="page" id="alertshome">
	<div data-role="header">
		<h1>Alerts</h1>
	</div>
	<!-- /header -->

	<div data-role="content" data-theme="a">
		<ul data-role="listview" data-theme="c" data-dividertheme="b" data-filter="true" data-inset="false" id="alertlist">

			<c:forEach items="${alerts}" var="alert" varStatus="status">
			<li data-role="list-divider">${alert.campus} - ${alert.title}</li>
		
			<li>
					<p>${alert.mobileText}</p>
			</li>
				
			</c:forEach>
 
		</ul>
	</div>
	<!-- /content -->

 
</div>

</body>
</html>