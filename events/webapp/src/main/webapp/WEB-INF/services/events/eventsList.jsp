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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="kme" uri="http://kuali.org/mobility"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<kme:page title="Events" id="events" backButton="true" homeButton="true" backButtonURL="${pageContext.request.contextPath}/events/">
	<kme:content>
		<h3>
			<c:out value="${category.title}" />
		</h3>
		<kme:listView id="eventslist" dataTheme="c" dataDividerTheme="b" filter="false">
			<c:forEach items="${category.days}" var="day" varStatus="statusEventDay">
				<kme:listItem dataTheme="b" dataRole="list-divider">
					<fmt:formatDate value="${day.date}" pattern="EEEE" />
					<fmt:formatDate value="${day.date}" pattern="MMMM d, yyyy" />
				</kme:listItem>
				<c:forEach items="${day.events}" var="event" varStatus="status">
					<kme:listItem>
						<c:url var="url" value="/events/viewEvent">
							<c:param name="categoryId" value="${category.categoryId}"></c:param>
							<c:param name="campus" value="${campus}"></c:param>
							<c:param name="eventId" value="${event.eventId}"></c:param>
						</c:url>
						<a href="${url}">
							<h3>
								<c:out value="${event.title}" />
							</h3>
							<p>
								<c:out value="${event.displayStartDate}" />
								<c:out value="${event.displayStartTime}" />
							</p> </a>
					</kme:listItem>
				</c:forEach>
			</c:forEach>
		</kme:listView>
	</kme:content>
</kme:page>
