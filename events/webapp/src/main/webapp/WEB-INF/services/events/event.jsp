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

<c:url var="back" value="/events/viewEvents">
	<c:param name="categoryId" value="${categoryId}" />
	<c:param name="campus" value="${campus}" />
</c:url>

<kme:page title="Event Detail" id="events" backButton="true" homeButton="true" backButtonURL="${back}">
	<kme:content>
		<kme:listView id="event" dataTheme="g">
			<kme:listItem>
				<h3>
					<c:out value="${event.title}" />
				</h3>
				<c:if test="${not empty event.location}">
					<p>
						<c:out value="${event.location}" />
					</p>
				</c:if>
				<p>
					<c:out value="${event.displayStartDate}" />
					<c:out value="${event.displayStartTime}" />
					<c:if test="${not empty event.displayEndDate}">- <c:if test="${event.displayEndDate ne event.displayStartDate}">
							<c:out value="${event.displayEndDate}" />
						</c:if>
						<c:out value="${event.displayEndTime}" />
					</c:if>
				</p>
				<c:if test="${not empty event.description}">
					<br />
					<h3>Description</h3>
					<c:forEach var="description" items="${event.description}" varStatus="status">
						<p style="white-space: normal">
							<c:out value="${description}" />
						</p>
						<c:if test="${not status.last}">
							<br/>
						</c:if>
					</c:forEach>
				</c:if>
				<c:if test="${not empty event.category}">
					<br />
					<h3>Category</h3>
					<p style="white-space: normal">
						<c:out value="${event.category}" />
					</p>
				</c:if>
				<c:if test="${not empty event.cost}">
					<br />
					<h3>Cost</h3>
					<p style="white-space: normal">
						<c:out value="${event.cost}" />
					</p>
				</c:if>
				<c:if test="${not empty event.contact}">
					<br />
					<h3>Contact</h3>
					<p style="white-space: normal">
						<c:choose>
							<c:when test="${not empty event.contactEmail}">
								<c:url var="email" value="mailto:${event.contactEmail}" />
								<a href="${email}"><c:out value="${event.contact}" /> </a>
							</c:when>
							<c:otherwise>
								<c:out value="${event.contact}" />
							</c:otherwise>
						</c:choose>
					</p>
				</c:if>
				<c:if test="${not empty event.otherInfo}">
					<br />
					<h3>Other Info</h3>
					<c:forEach var="otherInfo" items="${event.otherInfo}">
						<c:if test="${not empty otherInfo}">
							<c:forEach var="info" items="${otherInfo}">
								<p style="white-space: normal">
									<c:out value="${info}" />
								</p>
							</c:forEach>
							<br />
						</c:if>
					</c:forEach>
				</c:if>
			</kme:listItem>
		</kme:listView>
	</kme:content>
</kme:page>
