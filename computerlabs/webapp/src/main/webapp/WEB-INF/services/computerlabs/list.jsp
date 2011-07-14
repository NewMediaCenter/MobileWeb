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

<kme:page title="Computer Labs" id="computerlabs">
    <kme:content>
        <c:forEach items="${lablocations}" var="location" varStatus="status">
            <h3>${location.name}</h3>
	        <kme:definitionListView id="computerlablist" filter="false">
	            <c:forEach items="${location.computerLabs}" var="computerlab" varStatus="status">
	                <kme:definitionListTerm>
	                    <a href="/mdot/maps?id=${computerlab.buildingCode}">
                            ${computerlab.labCode}
                        </a>
	                </kme:definitionListTerm>
	                <kme:definitionListDefinition>
		                <a href="/mdot/maps?id=${computerlab.buildingCode}">
	                        ${computerlab.availability} seats available
	                    </a>
	                </kme:definitionListDefinition>
	            </c:forEach>     
	        </kme:definitionListView>
        </c:forEach>
    </kme:content>
</kme:page>
