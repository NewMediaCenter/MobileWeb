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
	
<kme:page title="Resources" id="class_details" backButton="true" homeButton="true" cssFilename="sakai">
	<kme:content>
	<style type="text/css">
    <!--
	div.ui-li { border:none !important; }
	-->
	</style>
		<ul data-role="listview">
			<c:choose>
				<c:when test="${not empty resources}">
					<c:forEach items="${resources}" var="item" varStatus="status">
						<c:choose>
							<c:when test="${item.fileType == 'FOLDER'}">
								<li>
							</c:when>
							<c:otherwise>
								<li class="link-view">
							</c:otherwise>
						</c:choose>
							<a href="${pageContext.request.contextPath}/myclasses/${siteId}/resources?resId=${item.encodedId}&type=${item.mimeType}" class="icon-${item.fileType}" >
								${item.title}
						  	</a>
						</li>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<li>No resources</li>
				</c:otherwise>
			</c:choose>
		</ul>
	</kme:content>
</kme:page>