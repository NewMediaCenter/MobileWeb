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
		<h1>News</h1>
	</div>
	<!-- /header -->

	<div data-role="content" data-theme="a">
		<ul data-role="listview" data-theme="c" class="news-index">
			<li class="news-topstory">
		        <div class="bottom-fade"></div>
		        <a href="/mdot/news/${topArticleSourceId}?articleId=${topArticle.articleId}&referrer=home">
		        	<c:choose>
		        		<c:when test="${!empty topArticle.thumbnailImageUrl}">
					    	<img src="${topArticle.thumbnailImageUrl}" alt="topstory">
					    </c:when>
					    <c:otherwise>
					    	<img src="images/news-generic.png" alt="topstory">
					    </c:otherwise>
					</c:choose>
			        <div>
			          <p class="news-title">${topArticle.title}</p>
			          <p class="news-teaser">${topArticle.description}</p>
			        </div>
		        </a> 
	        </li>
		
			<c:forEach items="${newsStreams}" var="stream" varStatus="status">
			
				<li data-role="" class="" data-theme="b" data-icon="listview" >
					<a href="/mdot/news/${stream.sourceId}">${stream.title}</a>
				</li> 
				
				<c:forEach items="${stream.articles}" var="day" varStatus="status">
					<c:forEach items="${day.articles}" var="article" varStatus="status">
						<li>
							<a href="/mdot/news/${stream.sourceId}?articleId=${article.articleId}&referrer=home">
				        		<p class="news-title">${article.title}</p>
				        	</a>
				        </li>
					</c:forEach>
				</c:forEach>
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