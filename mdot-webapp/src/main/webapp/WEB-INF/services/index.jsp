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

<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="kme" uri="http://kuali.org/mobility" %>

<kme:page title="IU Mobile" id="home">
	<kme:content>
	    <kme:unorderedList>
	        <c:forEach items="${home.tools}" var="tool" varStatus="status">	            
	            <kme:listItem>
	            	<a href="${tool.url}"><img src="images/service-icons/${tool.iconUrl}" alt="${tool.title}" class="ui-li-icon">
			      	${tool.title}
			      	<c:if test="${tool.badgeCount gt 0}"> 
			      		<span class="ui-li-count ui-btn-up-c ui-btn-corner-all">${tool.badgeCount}</span>
			      	</c:if>
			      	</a>
	            </kme:listItem>
			</c:forEach>
	    </kme:unorderedList>
	</kme:content>
</kme:page>
