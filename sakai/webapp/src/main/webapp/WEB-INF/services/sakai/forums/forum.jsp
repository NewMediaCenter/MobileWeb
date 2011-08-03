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
<%@ taglib prefix="kme" uri="http://kuali.org/mobility" %>
	
<kme:page title="${forum.title}" id="forum" cssFilename="sakai" backButton="true" homeButton="true" backButtonURL="${pageContext.request.contextPath}/myclasses/${siteId}/forums">
	<kme:content>
		<kme:listView>
			<c:choose>
				<c:when test="${not empty forum.topics}">
					<c:forEach items="${forum.topics}" var="topic" varStatus="status">
						<kme:listItem>
							<a href="${pageContext.request.contextPath}/myclasses/${siteId}/forums/${forum.forumId}/${topic.id}?title=${topic.title}">
								${topic.title}
								<span class="ui-li-count">${topic.unreadCount}</span>
							</a>
						</kme:listItem>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<kme:listItem>
						No topics
					</kme:listItem>
				</c:otherwise>
			</c:choose>
		</kme:listView>
	</kme:content>
</kme:page>