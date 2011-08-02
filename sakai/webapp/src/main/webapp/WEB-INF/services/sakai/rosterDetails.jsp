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

<kme:page title="Details" id="roster_details" backButton="true" homeButton="true" cssFilename="sakai">
	<kme:content>
    	<ul data-role="listview" data-inset="true">
			<li>
				<div class="container_12">
					<c:if test="${not empty roster.imageUrl && roster.imageUrl != 'null'}">
			        	<div class="grid_5">
			        			<img src="${roster.imageUrl}" width="80px" alt="image">
			        	</div>
			        	<div class="grid_7">
		        	</c:if>
		        	<c:if test="${empty roster.imageUrl || roster.imageUrl == 'null'}">
			        	<div class="grid_12">
		        	</c:if>
		            	<c:choose>
			            	<c:when test="${not empty roster.lastName && roster.lastName != 'null' && not empty roster.firstName && roster.firstName != 'null'}">
				            	<h3>${roster.lastName}, ${roster.firstName}</h3>
				            </c:when>
				            <c:otherwise>
				            	<h3>${roster.displayName}</h3>
				            </c:otherwise>
			            </c:choose>
		            	<c:if test="${not empty roster.school && roster.school != 'null'}">
			            	<p>${roster.school}</p>
			            </c:if>
			            <c:if test="${not empty roster.department && roster.department != 'null'}">
			            	<p>${roster.department}</p>
			            </c:if>
			            <c:if test="${not empty roster.position && roster.position != 'null'}">
			            	<p>${roster.position}</p>
			            </c:if>
			            <c:if test="${not empty roster.room && roster.room != 'null'}">
			            	<p>${roster.room}</p>
			            </c:if>
			            <%--
			            <c:if test="${not empty roster.otherInformation && roster.otherInformation != 'null'}">
			            	<p>${roster.otherInformation}</p>
			            </c:if>
			            --%>
		          	</div>
		        </div>
		    </li>
	      	<c:if test="${not empty roster.email && roster.email != 'null'}">
		      	<li class="link-email"><a href="mailto:${roster.email}"> ${roster.email} </a> </li>
		    </c:if>
		    <c:if test="${not empty roster.workPhone && roster.workPhone != 'null'}">
		      	<li class="link-phone"><a href="tel:${roster.workPhone}">${roster.workPhone} (Work)</a> </li>
		    </c:if>
		    <c:if test="${not empty roster.homePhone && roster.homePhone != 'null'}">
		      	<li class="link-phone"><a href="tel:${roster.homePhone}">${roster.homePhone} (Home)</a> </li>
		    </c:if>
		    <c:if test="${not empty roster.homePage && roster.homePage != 'null'}">
		      	<li><a href="${roster.homePage}">Home Page</a></li>
		    </c:if>
		</ul>
	</kme:content>
</kme:page>