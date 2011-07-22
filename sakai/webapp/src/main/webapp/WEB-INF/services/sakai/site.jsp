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
	
<kme:page title="${site.title}" id="class_details" backButton="true" homeButton="true" backButtonURL="${pageContext.request.contextPath}/myclasses">
	<kme:content>
		<ul data-role="listview" data-theme="c" data-dividertheme="b" data-inset="false">
			<c:if test="${site.instructorName != null && site.instructorName != 'null'}">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/roster/${site.instructorId}">
						Instructor<br />
						<span style=" font-weight:normal; font-size:.8em">${site.instructorName}</span>
					</a>
				</li>
			</c:if>
			<c:if test="${site.hasAnnouncementsTool}">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/announcements">
						Announcements
					</a>
				</li>
			</c:if>
			<c:if test="${site.hasAssignmentsTool && false}">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/assignments">
						Assignments
					</a>
				</li>
			</c:if>
			<c:if test="${site.hasGradesTool && false}">
				<li>					
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/grades">
						Gradebook
					</a>
				</li>
			</c:if>
			<c:if test="${site.hasRosterTool}">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/roster">
						Roster
					</a>
				</li>
			</c:if>
			<c:if test="${site.hasForumsTool}">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/forums">
						Forums
					</a>
				</li>
			</c:if>
			<c:if test="${site.hasResourcesTool}">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${siteId}/resources">
						Resources
					</a>
				</li>
			</c:if>
			<c:if test="${site.hasMessagesTool}">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/messages">
						Messages
					</a>
				</li>
			</c:if>
		</ul>
	</kme:content>
</kme:page>

</body>
</html>