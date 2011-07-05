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
<title>News</title>
<jsp:include page="../resources.jsp" />
</head>

<body>
<div data-role="page" id="">
	<div data-role="header">
		<a href="/mdot/news?category=${category}" data-icon="arrow-l">Back</a>
		<h1>${newsArticle.title}</h1>
	</div>
	<!-- /header -->

	<div data-role="content" data-theme="a">
		<h3>${newsArticle.title}</h3>
		<h4>${newsArticle.author}</h4>
		<h4>${newsArticle.pubDate}</h4>
		<p>${newsArticle.description}</p>
	</div>
	<!-- /content -->

	<!-- /header --> 
</div>
<!-- /stc --> 

<!-- /page -->

</body>
</html>