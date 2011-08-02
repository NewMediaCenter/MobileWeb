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
	
<kme:page title="${site.title}" id="class_details" backButton="true" homeButton="true" backButtonURL="${pageContext.request.contextPath}/myclasses" cssFilename="sakai">
	<kme:content>
		<kme:listView dataTheme="c" dataDividerTheme="b">
			<c:if test="${site.instructorName != null && site.instructorName != 'null'}">
				<kme:listItem>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/roster/${site.instructorId}" class="icon-INSTRUCTOR">
						Instructor<br />
						<span style=" font-weight:normal; font-size:.8em">${site.instructorName}</span>
					</a>
				</kme:listItem>>
			</c:if>
			<c:if test="${site.hasAnnouncementsTool}">
				<kme:listItem>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/announcements" class="icon-ANNOUNCEMENTS">
						Announcements
					</a>
				</kme:listItem>
			</c:if>
			<c:if test="${site.hasAssignmentsTool && false}">
				<kme:listItem>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/assignments" class="icon-ASSIGNMENTS">
						Assignments
					</a>
				</kme:listItem>
			</c:if>
			<c:if test="${site.hasGradesTool && false}">
				<kme:listItem>					
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/grades" class="icon-GRADES">
						Gradebook
					</a>
				</kme:listItem>
			</c:if>
			<c:if test="${site.hasRosterTool}">
				<kme:listItem>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/roster" class="icon-ROSTER">
						Roster
					</a>
				</kme:listItem>
			</c:if>
			<c:if test="${site.hasForumsTool}">
				<kme:listItem>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/forums" class="icon-FORUMS">
						Forums
						<span class="ui-li-count">${forumCount}</span>
					</a>
				</kme:listItem>
			</c:if>
			<c:if test="${site.hasResourcesTool}">
				<kme:listItem>
					<a href="${pageContext.request.contextPath}/myclasses/${siteId}/resources" class="icon-RESOURCES">
						Resources
					</a>
				</kme:listItem>
			</c:if>
			<c:if test="${site.hasMessagesTool}">
				<kme:listItem>
					<a href="${pageContext.request.contextPath}/myclasses/${site.id}/messages" class="icon-MESSAGES">
						Messages
						<span class="ui-li-count">${messageCount}</span>
					</a>
				</kme:listItem>
			</c:if>
		</kme:listView>
	</kme:content>
</kme:page>

</body>
</html>