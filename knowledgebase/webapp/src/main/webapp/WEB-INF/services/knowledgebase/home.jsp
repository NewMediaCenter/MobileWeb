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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<kme:page title="Knowledge Base" id="kb" backButton="true" homeButton="true">
	<kme:content>
		<form:form action="${pageContext.request.contextPath}/knowledgebase/search" commandName="kbsearchform" data-ajax="false">
			<fieldset>
            <label for="searchText">Search</label>
			<form:input path="searchText" cssClass="text ui-widget-content ui-corner-all" />
			<form:errors path="searchText" />
			</fieldset>
		</form:form>
		<%--
		<kme:form action="/search" method="post" id="kbsearchform">
			<kme:input id="kbsearch" title="Search:" name="search" type="search">
			</kme:input>
		</kme:form>
 		--%>
	    <kme:definitionListView id="kbsearchresults">
			<div id="searchresults">
	        <c:forEach items="${container.results}" var="item" varStatus="status">
	            <kme:definitionListTerm>
					<a href="${pageContext.request.contextPath}/knowledgebase/${item.documentId}">${item.title}</a>
	            </kme:definitionListTerm>
	        </c:forEach>
	        </div>
	    </kme:definitionListView>
	</kme:content>
</kme:page>
