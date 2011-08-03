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

<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="kme"  uri="http://kuali.org/mobility" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<kme:page title="Search Results" id="people" backButton="true" homeButton="true">
	<kme:content>
		<c:choose>
			<c:when test="${person != null}">
				<h3><c:out value="${person.lastName}" />, <c:out value="${person.firstName}" /></h3>
				<c:if test="${not empty person.email}" ><p>Email: <c:out value="${person.email}" /></p></c:if>
				<c:if test="${not empty person.phone}" ><p>Phone: <c:out value="${person.phone}" /></p></c:if>
				
				<c:if test="${not empty person.departments}" >
					<p>Department:
				      		<c:forEach items="${person.departments}" var="department" varStatus="status">
				      			<c:out value="${department}" /><c:if test="${not status.last}">, </c:if>
				      		</c:forEach>
					</p>
				</c:if>
				<c:if test="${not empty person.locations}" >
					<p>Location:
				      		<c:forEach items="${person.locations}" var="location" varStatus="status">
				      			<c:out value="${location}" /><c:if test="${not status.last}">, </c:if>
				      		</c:forEach>
					</p>
				</c:if>
				<c:if test="${not empty person.affiliations}" >
					<p>Affiliation:
				      		<c:forEach items="${person.affiliations}" var="affiliation" varStatus="status">
				      			<c:out value="${affiliation}" /><c:if test="${not status.last}">, </c:if>
				      		</c:forEach>
					</p>
				</c:if>
				<c:if test="${not empty person.address}" ><p>Address: <c:out value="${person.address}" /></p></c:if>
			</c:when>
			<c:otherwise>
				The person was not found.
			</c:otherwise>
		</c:choose>
	</kme:content>
</kme:page>
