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

<kme:page title="Roster" id="roster" backButton="true" homeButton="true" backButtonURL="${pageContext.request.contextPath}/myclasses/${siteId}">
	<kme:content>
		<ul data-role="listview" data-inset="false">
			<c:choose>
				<c:when test="${not empty roster}">
					<c:forEach items="${roster}" var="item" varStatus="status">
						<li>
							<a href="${pageContext.request.contextPath}/myclasses/${siteId}/roster/${item.displayID}">
								<c:if test="${item.imageUrl != 'null'}">
									<img src="${item.imageUrl}" alt="image">
								</c:if>
								<h3>${item.displayName}</h3>
								<p>${item.roleTitle}</p>
							</a>
						</li>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<li>
						No roster listings
					</li>
				</c:otherwise>
			</c:choose>
		</ul>
	</kme:content>
</kme:page>