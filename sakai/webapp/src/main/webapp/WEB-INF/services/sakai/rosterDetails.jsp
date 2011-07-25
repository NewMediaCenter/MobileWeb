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

<kme:page title="Details" id="roster_details" backButton="true" homeButton="true">
	<kme:content>
    	<ul data-role="listview" data-theme="c"  data-dividertheme="b"  data-inset="true">
			<li>
				<div class="container_12">
		        	<div class="grid_5"><img src="http://localhost:9090/direct/profile/${roster.displayID}/image/thumb?sakai.session=${sessionId}" alt="image"></div>
		        	<div class="grid_7">
		            	<h3>${roster.lastName}, ${roster.firstName}</h3>
		            	<c:if test="${not empty item.school && item.school != 'null'}">
			            	<p>${roster.school}</p>
			            </c:if>
			            <c:if test="${not empty item.roleTitle && item.roleTitle != 'null'}">
			            	<p>${roster.roleTitle}</p>
			            </c:if>
		          	</div>
		        </div>
		    </li>
	      	<c:if test="${not empty item.email && item.email != 'null'}">
		      	<li class="link-email"><a href="mailto:${roster.email}"> ${roster.email} </a> </li>
		    </c:if>
		    <c:if test="${not empty item.workPhone && item.workPhone != 'null'}">
		      	<li class="link-phone"><a href="tel:${roster.workPhone}"> ${roster.workPhone} </a> </li>
		    </c:if>
		</ul>
	</kme:content>
</kme:page>