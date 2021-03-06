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

<kme:page title="IU Mobile" id="home" backButton="false" homeButton="false">
	<kme:content>
	    <kme:definitionListView id="homeserviceslist" filter="false">
	        <c:forEach items="${home.tools}" var="tool" varStatus="status">	            
	            <kme:definitionListTerm>
	            	<a href="${tool.url}" style="background-image: url('${tool.iconUrl}');">
			      	${tool.title}
			      	<c:if test="${not empty tool.badgeCount}"> 
			      		<span class="dl-dt-count ui-btn-up-c ui-btn-corner-all">${tool.badgeCount}</span>
			      	</c:if>
			      	</a>
	            </kme:definitionListTerm>
	            <kme:definitionListDefinition>
	                <a href="${tool.url}">${tool.description}</a>
	            </kme:definitionListDefinition>
			</c:forEach>
	    </kme:definitionListView>
	</kme:content>
</kme:page>


