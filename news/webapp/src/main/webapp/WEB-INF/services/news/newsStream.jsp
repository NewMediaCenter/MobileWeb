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
		<h1>${newsStream.title}</h1>
	</div>
	<!-- /header -->

	<div data-role="content" data-theme="a">
		<ul data-role="listview" data-theme="c" class="news-index">
			<c:forEach items="${newsStream.articles}" var="day" varStatus="status">
				</li><li data-role="list-divider" class="divider">${day.formattedDate}</li>
				<c:forEach items="${day.articles}" var="article" varStatus="status">
					<li>
						<a href="/mdot/news/${sourceId}?articleId=${article.articleId}">
							<p class="news-title">${article.title}</p>
							<div class="container_12">
					          <c:if test="${!empty article.thumbnailImageUrl}">
					          	<div class="grid_2"> <img src="${article.thumbnailImageUrl}" alt="news"></div>
					          </c:if>
					          <div class="grid_10">
					            <p class="news-teaser">${article.description}</p>
					            <div class="bottom-fade"> </div>
					          </div>
					        </div>
						</a>
					</li>
				</c:forEach>
			</c:forEach>
		</ul>
		<ul data-role="listview" data-inset="true" data-theme="c">
			<c:forEach items="${newsSources}" var="item" varStatus="status">
				<li>
					<a href="/mdot/news/${item.sourceId}">
						${item.sourceName}
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