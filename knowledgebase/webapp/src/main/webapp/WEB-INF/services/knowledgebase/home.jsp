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

<kme:page title="Knowledge Base" id="kb">
	<kme:content>
		<kme:form action="/test" method="post" id="kbsearchform">
			<kme:input id="kbsearch" title="Search:" name="search" type="search">
			</kme:input>
		</kme:form>
	    <kme:definitionListView id="kbsearchresults">
			<div id="searchresults">
	        <c:forEach items="${container.results}" var="item" varStatus="status">
	            <kme:definitionListTerm>
					<span>Term</span>
	            </kme:definitionListTerm>
	            <kme:definitionListDefinition>
					<a href="knowledgebase/${item.documentId}">${item.title}</a>
	            </kme:definitionListDefinition>
	        </c:forEach>
	        </div>
	    </kme:definitionListView>
	</kme:content>
</kme:page>
