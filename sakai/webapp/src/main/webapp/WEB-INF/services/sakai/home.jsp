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
    
    <style type="text/css">
    <!--
        div.ui-body-b { background: none; }
        div.ui-body-b { background-color: #E6E6E6 !important; }
        div#classesPanel, div#projectsPanel { margin-top: -20px; }   
    -->
    </style>
    
        <c:if test="${!empty home.courses && !empty home.projects}">
	        <script type="text/javascript">
			    $(window).load(function () {
			        $('#classesPanel').show();
			        $('#projectsPanel').hide();
			        
			        $('#classesTab').click(function() {
			            $('#classesTab').css('background-color', '#E6E6E6');
			            $('#classesTab').css('color', '#2F3E46');
			            $('#projectsTab').css('background-color', '#CCCCCC');
			            $('#projectsTab').css('color', '#999999');
			            $('#classesPanel').show();
			            $('#projectsPanel').hide();
			        });
			        
			        $('#projectsTab').click(function() {
			            $('#projectsTab').css('background-color', '#E6E6E6');
			            $('#projectsTab').css('color', '#2F3E46');
			            $('#classesTab').css('background-color', '#CCCCCC');
			            $('#classesTab').css('color', '#999999');
			            $('#classesPanel').hide();
			            $('#projectsPanel').show();
			        });
			    });
		    </script>
		    
            <a style="position:absolute; padding:1%; color:#2F3E46; box-shadow:0 -1px 1px #AAAAAA; border-radius:10px 10px 0 0; background-color:#E6E6E6; top:50px; left:0; width:48%; text-align:center; text-decoration:none;" id="classesTab" name="classesTab" href="#">Classes</a>
            <a style="position:absolute; padding:1%; color:#999999; box-shadow:0 -1px 1px #AAAAAA; border-radius:10px 10px 0 0; background-color:#CCCCCC; top:50px; left:50%; width:48%; text-align:center; text-decoration:none;" id="projectsTab" name="projectsTab" href="#">Projects</a>
            
            <style type="text/css">
			    <!--
			    div#classesPanel, div#projectsPanel { margin-top: 40px; }			    
			    -->
		    </style>
        </c:if>
        
        <c:if test="${!empty home.courses}">
        <div background-color:#E6E6E6;" id="classesPanel" name="classesPanel">
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
            <div background-color:#E6E6E6;" id="projectsPanel" name="projectsPanel">
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