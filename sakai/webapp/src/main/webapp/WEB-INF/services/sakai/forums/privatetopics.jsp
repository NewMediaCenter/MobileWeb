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
	
<kme:page title="Messages" id="messages" cssFilename="sakai" backButton="true" homeButton="true" backButtonURL="${pageContext.request.contextPath}/myclasses/${siteId}">
	<kme:content>
		<ul data-role="listview">
			<c:forEach items="${privatetopics}" var="item" varStatus="status">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${siteId}/messages/folder/${item.typeUuid}?title=${item.title}">
						<span style="text-transform:capitalize">${item.title}</span>
						<span class="ui-li-count">${item.unreadCount}</span>
					</a>
				</li>
			</c:forEach>
		</ul>
		<%--<a href="${pageContext.request.contextPath}/myclasses/${siteId}/messages/compose" data-role="button" data-icon="plus">Compose Message</a>--%>
	</kme:content>
</kme:page>