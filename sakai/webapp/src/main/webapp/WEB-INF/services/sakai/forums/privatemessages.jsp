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
		
<kme:page title="${messageFolder.title}" id="messages" cssFilename="sakai" backButton="true" homeButton="true" backButtonURL="${pageContext.request.contextPath}/myclasses/${siteId}/messages">
	<kme:content>
		<ul data-role="listview">
			<c:choose>
				<c:when test="${not empty messageFolder.messages}">
					<c:forEach items="${messageFolder.messages}" var="item" varStatus="status">
						<li>
							<a href="${pageContext.request.contextPath}/myclasses/${siteId}/messages/folder/${messageFolder.typeUuid}/${item.id}">
								<c:if test="${not item.isRead}">
									<h3>${item.title}</h3>
								</c:if>
								<c:if test="${item.isRead}">
									<h3 class="read">${item.title}</h3>
								</c:if>
								<p>${item.createdDate}</p>
								<p>${item.createdBy}</p>
							</a>
						</li>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<li>
						No messages
					</li>
				</c:otherwise>
			</c:choose>
		</ul>
	</kme:content>
</kme:page>