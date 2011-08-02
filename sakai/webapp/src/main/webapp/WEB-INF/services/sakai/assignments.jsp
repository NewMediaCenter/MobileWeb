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

<kme:page title="Assignments" id="assignments" backButton="true" homeButton="true" backButtonURL="${pageContext.request.contextPath}/myclasses/${siteId}" cssFilename="sakai">
	<kme:content>
		<ul data-role="listview">
			<c:set var="lastCreatedDate" value=""/>
			<li>
				<h3>Course Grade: ${courseGrade}</h3>
			</li>
			<c:forEach items="${sakaigrades}" var="item" varStatus="status">
				<li data-role="list-divider">${item.title} - ${item.submittedStatus}</li>
				<li>
						<h3>Grade Scale</h3> <p> ${item.gradeScale}</p>
						<c:set var="graded" value="${item.submissionGraded}" />
						<jsp:useBean id="graded" class="java.lang.String" />
						
						<c:if test='<%=graded.equalsIgnoreCase("true")%>'>
						<h3>Grade: </h3> <p> ${item.submissionGrade}</p>
						</c:if>
				</li>
			</c:forEach>
		</ul>
	</kme:content>
</kme:page>