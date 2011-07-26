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
    
    <script type="text/javascript">
    $(window).load(function () {
    	$('#classesTab').css('top','50px');
    	$('#classesTab').css('left','25%');
    	$('#projectsTab').css('top','50px');
    	$('#projectsTab').css('left','55%');
    	
    	$('#classesPanel').show(); //css('left', '15px');
        $('#projectsPanel').hide(); //css('left', '-9999px');
        //$('#classesPanel').css('top', '100px');
        //$('#projectsPanel').css('top', '100px');
        
        $('#classesTab').click(function() {
        	$('#classesPanel').show(); //css('left', '15px');
            $('#projectsPanel').hide(); //css('left', '-9999px');
        });
        
        $('#projectsTab').click(function() {
        	$('#classesPanel').hide(); //css('left', '-9999px');
            $('#projectsPanel').show(); //css('left', '15px');
        });
    });
    </script>
    
        <a style="position:absolute; text-align:center; text-decoration:none;" id="classesTab" name="classesTab" href="#">Classes</a>
        <a style="position:absolute; text-align:center; text-decoration:none;" id="projectsTab" name="projectsTab" href="#">Projects</a>
        
        <c:if test="${!empty home.courses}">
        
        <div id="classesPanel" name="classesPanel">
        <h2>Classes</h2>
            <c:forEach items="${home.courses}" var="termItem" varStatus="status">
                <h3>${termItem.key}</h3>
                <kme:listView dataTheme="c">
                    <c:forEach items="${termItem.value}" var="item" varStatus="status">
                        <kme:listItem>
                            <a href="${pageContext.request.contextPath}/myclasses/${item.id}">
                                <h3>${item.title}</h3>
                                <c:if test="${not empty item.description && item.description != 'null'}">
                                    <p>${item.description}</p>
                                </c:if>
                            </a>
                        </kme:listItem>
                    </c:forEach>
                </kme:listView>
            </c:forEach>
        </div>
        </c:if>
        
        <c:if test="${!empty home.projects}">
            
            <div id="projectsPanel" name="projectsPanel">
            <h2>Projects</h2>
            <kme:listView dataTheme="c">
                <c:forEach items="${home.projects}" var="item" varStatus="status">
                    <kme:listItem>
                        <a href="${pageContext.request.contextPath}/myclasses/${item.id}">
                            <h3>${item.title}</h3>
                            <c:if test="${not empty item.description && item.description != 'null'}">
                                <p>${item.description}</p>
                            </c:if>
                        </a>
                    </kme:listItem>
                </c:forEach>
            </kme:listView>
            </div>
        </c:if>
    </kme:content>
</kme:page>