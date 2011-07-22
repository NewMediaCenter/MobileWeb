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
	
<kme:page title="Resources" id="class_details" backButton="true" homeButton="true">
	<kme:content>
		<ul data-role="listview">
			<c:forEach items="${resources}" var="item" varStatus="status">
				<li>
					<a href="${pageContext.request.contextPath}/myclasses/${siteId}/resources?resId=${item.id}" >
						<div class="ui-block-a" style="width:10%">
							<c:choose>
								<c:when test="${item.extension == 'fldr'}">
									<img src="${pageContext.request.contextPath}/images/service-icons/folder.png" width="32" height="32" alt="image" />
								</c:when>
								<c:when test="${item.extension == 'pdf'}">
									<img src="${pageContext.request.contextPath}/images/service-icons/pdf.png" width="32" height="32" alt="image" />
								</c:when>
								<c:when test="${item.extension == 'rtf' || item.extension == 'txt' || item.extension == 'doc' || item.extension == 'docx'}">
									<img src="${pageContext.request.contextPath}/images/service-icons/ms_office_word.png" width="32" height="32" alt="image" />
								</c:when>
								<c:when test="${item.extension == 'ppt' || item.extension == 'pptx'}">
									<img src="${pageContext.request.contextPath}/images/service-icons/ms_office_ppt.png" width="32" height="32" alt="image" />
								</c:when>
								<c:when test="${item.extension == 'url'}">
									<img src="${pageContext.request.contextPath}/images/service-icons/link.png" width="32" height="32" alt="image" />
								</c:when>
								<c:otherwise>
									<img src="${pageContext.request.contextPath}/images/service-icons/file.png" width="32" height="32" alt="image" />
								</c:otherwise>
							</c:choose>
						</div>
						<div class="ui-block-b" style="width:90%">
							${item.title}
						</div>
				  	</a>
				</li>
			</c:forEach>
		</ul>
	</kme:content>
</kme:page>