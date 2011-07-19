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
	
<kme:page title="${site.title}" id="class_details">
	<kme:content>
		<c:if test="${site.description != null && not empty site.description && site.description != 'null'}">
			<h3>${site.description}</h3>
		</c:if>
		<c:if test="${site.instructorId != null && not empty site.instructorId}">
			<div>
			  	<img src="http://localhost:9090/direct/profile/${site.instructorId}/image/thumb?sakai.session=${sessionId}" width="70" height="70" alt="image">
			</div>
			<div>
				<h3>Instructor</h3>
				<a href="${pageContext.request.contextPath}/myclasses/${site.id}/roster/${site.instructorId}">
					<p>${site.instructorName}</p>
				</a>
			</div>
		</c:if>
		<ul data-role="listview" data-theme="c" data-dividertheme="b" data-inset="false">
			<c:if test="${site.hasAnnouncementsTool}">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/announcements">
						<h3>Announcements</h3>
					</a>
				</li>
			</c:if>
			<c:if test="${site.hasAssignmentsTool && false}">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/assignments">
						<h3>Assignments</h3>
					</a>
				</li>
			</c:if>
			<c:if test="${site.hasGradesTool && false}">
				<li>					
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/grades">
						<h3>Gradebook</h3>
					</a>
				</li>
			</c:if>
			<c:if test="${site.hasRosterTool}">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/roster">
						<h3>Roster</h3>
					</a>
				</li>
			</c:if>
			<c:if test="${site.hasForumsTool}">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/forums">
						<h3>Forums</h3>
					</a>
				</li>
			</c:if>
			<c:if test="${site.hasResourcesTool}">
				<li>
					<a href="${pageContext.request.contextPath}/resources?siteId=${site.id}&siteTitle=${site.title}">
						<h3>Resources</h3>
					</a>
				</li>
			</c:if>
			<c:if test="${site.hasMessagesTool}">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/messages">
						<h3>Messages</h3>
					</a>
				</li>
			</c:if>
		</ul>
	</kme:content>
</kme:page>

</body>
</html>