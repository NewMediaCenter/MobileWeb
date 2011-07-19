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

<kme:page title="Announcements" id="announcements">
	<kme:content>
		<ul data-role="listview" data-theme="c" data-dividertheme="b" data-inset="false">
			<c:set var="lastCreatedDate" value=""/>
			<c:forEach items="${announcements}" var="item" varStatus="status">
				<c:if test="${item.createdDate != lastCreatedDate}">
				<li data-role="list-divider">${item.createdDate}</li>
				</c:if>
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${siteId}/announcements/${item.id}" data-direction="forward">
						<h3>${item.title}</h3>
					</a>
					<p>${item.createdOn}</p>
					<c:if test="${not empty item.attachments}">
						<c:set var="firstItem"><c:out value="false"/></c:set>
						<p>Attachments:
						<c:forEach items="${item.attachments}" var="attach" varStatus="status"><c:if test="${firstItem == 'true'}">, </c:if><c:out value="${attach.title}"/><c:set var="firstItem"><c:out value="true"/></c:set></c:forEach>
						</p>
					</c:if>
					<c:set var="lastCreatedDate" value="${item.createdDate}"/>
				</li>
			</c:forEach>
		</ul>
	</kme:content>
</kme:page>