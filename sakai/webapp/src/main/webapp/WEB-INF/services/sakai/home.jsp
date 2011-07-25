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

<kme:page title="My Classes" id="myclasses" homeButton="true" cssFilename="sakai">
    <kme:content>
        <c:if test="${!empty home.courses}">
            <h2>Classes</h2>
            <c:forEach items="${home.courses}" var="termItem" varStatus="status">
                <h3>${termItem.key}</h3>
                <kme:definitionListView id="classlist" filter="false">
                    <c:forEach items="${termItem.value}" var="item" varStatus="status">
                        <kme:definitionListTerm>
                            <a href="${pageContext.request.contextPath}/myclasses/${item.id}">${item.title}</a>
                        </kme:definitionListTerm>
                        <c:if test="${not empty item.description && item.description != 'null'}">
                            <kme:definitionListDefinition>
                                <a href="${pageContext.request.contextPath}/myclasses/${item.id}">${item.description}</a>
                            </kme:definitionListDefinition>
                        </c:if>
                    </c:forEach>
                </kme:definitionListView>
            </c:forEach>
        </c:if>
        <c:if test="${!empty home.projects}">
            <h2>Projects</h2>
            <kme:definitionListView id="classlist" filter="false">
                <c:forEach items="${home.projects}" var="item" varStatus="status">
                    <kme:definitionListTerm>
                        <a href="${pageContext.request.contextPath}/myclasses/${item.id}">${item.title}</a>
                    </kme:definitionListTerm>
                    <c:if test="${not empty item.description && item.description != 'null'}">
                        <kme:definitionListDefinition>
                            <a href="${pageContext.request.contextPath}/myclasses/${item.id}">${item.description}</a>
                        </kme:definitionListDefinition>
                    </c:if>
                </c:forEach>
            </kme:definitionListView>
        </c:if>
    </kme:content>
</kme:page>