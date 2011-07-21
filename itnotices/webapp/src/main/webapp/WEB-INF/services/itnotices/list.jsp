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

<kme:page title="IT Notices" id="itnotices" homeButton="true" backButton="true">
	<kme:content>
	    <kme:definitionListView id="itnoticeslist">
	        <c:forEach items="${notices}" var="notice" varStatus="status">
	            <kme:definitionListTerm>
	                <c:if test="${empty notice.title && empty notice.noticeType ? 'false' : 'true'}">
						<a href="itnotices/details?id=${status.index}">
						    <c:if test="${empty notice.noticeType ? 'false' : 'true'}">
								<span class="itNoticeIcon-${notice.noticeType}">${notice.noticeType}: </span>
		                    </c:if>
		                    <c:if test="${empty notice.title ? 'false' : 'true'}">
		                        ${notice.title}
		                    </c:if>
						</a>
					</c:if>
	            </kme:definitionListTerm>
	            <kme:definitionListDefinition>
	                <a href="itnotices/details?id=${status.index}">
	                    <p>${notice.service}</p>
	                </a>
	                <a href="itnotices/details?id=${status.index}">
	                    <p>Last Updated: ${notice.lastUpdated}</p>
	                </a>
	            </kme:definitionListDefinition>
	        </c:forEach>
	    </kme:definitionListView>
	</kme:content>
</kme:page>