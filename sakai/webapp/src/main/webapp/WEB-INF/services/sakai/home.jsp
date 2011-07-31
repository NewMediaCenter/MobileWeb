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
	        div.ui-body-b { background: none; }
	        div.ui-body-b { background-color: #E6E6E6 !important; }
	        div#classesPanel, div#projectsPanel, div#otherPanel, div#todayPanel { margin-top: -20px; }   
	    </style>
    
        <c:if test="${tabCount gt 1}">
	        <script type="text/javascript">
			    $(window).load(function () {
			        $('#classesPanel').hide();
			        $('#projectsPanel').hide();
			        $('#otherPanel').hide();
			        $('#todayPanel').hide();
			        
			        $('#classesTab').click(function() {
			            $('#classesTab').css('background-color', '#E6E6E6');
			            $('#classesTab').css('color', '#2F3E46');
			            $('#projectsTab').css('background-color', '#CCCCCC');
			            $('#projectsTab').css('color', '#999999');
			            $('#otherTab').css('background-color', '#CCCCCC');
                        $('#otherTab').css('color', '#999999');
                        $('#todayTab').css('background-color', '#CCCCCC');
                        $('#todayTab').css('color', '#999999');
			            $('#classesPanel').show();
			            $('#projectsPanel').hide();
			            $('#otherPanel').hide();
			            $('#todayPanel').hide();
			        });
			        
			        $('#projectsTab').click(function() {
			            $('#projectsTab').css('background-color', '#E6E6E6');
			            $('#projectsTab').css('color', '#2F3E46');
			            $('#classesTab').css('background-color', '#CCCCCC');
			            $('#classesTab').css('color', '#999999');
			            $('#otherTab').css('background-color', '#CCCCCC');
                        $('#otherTab').css('color', '#999999');
                        $('#todayTab').css('background-color', '#CCCCCC');
                        $('#todayTab').css('color', '#999999');
                        $('#classesPanel').hide();
			            $('#projectsPanel').show();
			            $('#otherPanel').hide();
			            $('#todayPanel').hide();
			        });
			        
			        $('#otherTab').click(function() {
                        $('#projectsTab').css('background-color', '#CCCCCC');
                        $('#projectsTab').css('color', '#999999');
                        $('#classesTab').css('background-color', '#CCCCCC');
                        $('#classesTab').css('color', '#999999');
                        $('#otherTab').css('background-color', '#E6E6E6');
                        $('#otherTab').css('color', '#2F3E46');
                        $('#todayTab').css('background-color', '#CCCCCC');
                        $('#todayTab').css('color', '#999999');
                        $('#classesPanel').hide();
                        $('#projectsPanel').hide();
                        $('#otherPanel').show();
                        $('#todayPanel').hide();
                    });
			        
			        $('#todayTab').click(function() {
                        $('#projectsTab').css('background-color', '#CCCCCC');
                        $('#projectsTab').css('color', '#999999');
                        $('#classesTab').css('background-color', '#CCCCCC');
                        $('#classesTab').css('color', '#999999');
                        $('#otherTab').css('background-color', '#CCCCCC');
                        $('#otherTab').css('color', '#999999');
                        $('#todayTab').css('background-color', '#E6E6E6');
                        $('#todayTab').css('color', '#2F3E46');
                        $('#classesPanel').hide();
                        $('#projectsPanel').hide();
                        $('#otherPanel').hide();
                        $('#todayPanel').show();
                    });
			    });
		    </script>
		    
		    <div style="margin:10px 0 0 -15px; position:absolute; top:40px; width:100%;">
			    <c:if test="${!empty home.courses}">
	                <a style="float:left; padding:9px 1%; color:#999999; -moz-box-shadow:0 -1px 1px #AAAAAA; -webkit-box-shadow:0 -1px 1px #AAAAAA; box-shadow:0 -1px 1px #AAAAAA; border-radius:10px 10px 0 0; background-color:#CCCCCC; top:50px; width:${(100/tabCount)-2}%; text-align:center; text-decoration:none;" id="classesTab" name="classesTab" href="#">Classes</a>
	            </c:if>
	            <c:if test="${!empty home.projects}">
	                <a style="float:left; padding:9px 1%; color:#999999; -moz-box-shadow:0 -1px 1px #AAAAAA; -webkit-box-shadow:0 -1px 1px #AAAAAA; box-shadow:0 -1px 1px #AAAAAA; border-radius:10px 10px 0 0; background-color:#CCCCCC; top:50px; width:${(100/tabCount)-2}%; text-align:center; text-decoration:none;" id="projectsTab" name="projectsTab" href="#">Projects</a>
	            </c:if>
	            <c:if test="${!empty home.other}">
	                <a style="float:left; padding:9px 1%; color:#999999; -moz-box-shadow:0 -1px 1px #AAAAAA; -webkit-box-shadow:0 -1px 1px #AAAAAA; box-shadow:0 -1px 1px #AAAAAA; border-radius:10px 10px 0 0; background-color:#CCCCCC; top:50px; width:${(100/tabCount)-2}%; text-align:center; text-decoration:none;" id="otherTab" name="otherTab" href="#">Other</a>
	            </c:if>
	            <c:if test="${!empty home.todaysCourses}">
                    <a style="float:left; padding:9px 1%; color:#999999; -moz-box-shadow:0 -1px 1px #AAAAAA; -webkit-box-shadow:0 -1px 1px #AAAAAA; box-shadow:0 -1px 1px #AAAAAA; border-radius:10px 10px 0 0; background-color:#CCCCCC; top:50px; width:${(100/tabCount)-2}%; text-align:center; text-decoration:none;" id="todayTab" name="todayTab" href="#">Today</a>
                </c:if>
            </div>
            
            <c:choose>
                <c:when test="${!empty home.courses}">
                    <script type="text/javascript">
                    $(window).load(function () {
                        $('#classesTab').css('background-color', '#E6E6E6');
                        $('#classesTab').css('color', '#2F3E46');
                        $('#classesPanel').show();
                    });
                    </script>
                </c:when>
                <c:when test="${!empty home.projects}">
                    <script type="text/javascript">
                    $(window).load(function () {
                        $('#projectsTab').css('background-color', '#E6E6E6');
                        $('#projectsTab').css('color', '#2F3E46');
                        $('#projectsPanel').show();
                    });
                    </script>
                </c:when>
                <c:when test="${!empty home.todaysCourses}">
                    <script type="text/javascript">
                    $(window).load(function () {
                        $('#todayTab').css('background-color', '#E6E6E6');
                        $('#todayTab').css('color', '#2F3E46');
                        $('#todayPanel').show();
                    });
                    </script>
                </c:when>
                <c:otherwise>
                    <script type="text/javascript">
                    $(window).load(function () {
                        $('#otherTab').css('background-color', '#E6E6E6');
                        $('#otherTab').css('color', '#2F3E46');
                        $('#otherPanel').show();
                    });
                    </script>
                </c:otherwise>
                
            </c:choose>
            
            <style type="text/css">
			    <!--
			    div#classesPanel, div#projectsPanel, div#otherPanel, div#todayPanel  { margin-top: 40px; }			    
			    -->
		    </style>
        </c:if>
        
        <c:if test="${!empty home.courses}">
	        <div background-color:#E6E6E6;" id="classesPanel" name="classesPanel">
	        	<c:if test="${tabCount eq 1}"><h2>Classes</h2></c:if>
	            <c:forEach items="${home.courses}" var="termItem" varStatus="status">
	                <h3>${termItem.term}</h3>
	                <kme:listView dataTheme="c">
	                    <c:forEach items="${termItem.courses}" var="item" varStatus="status">
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
            <c:if test="${tabCount eq 1}"><h2>Projects</h2></c:if>
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
        
             
        <c:if test="${!empty home.other}">
            <div background-color:#E6E6E6;" id="otherPanel" name="otherPanel">
	            <c:if test="${tabCount eq 1}"><h2>Other</h2></c:if>
	            <kme:listView dataTheme="c">
	                <c:forEach items="${home.other}" var="item" varStatus="status">
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
     
        <c:if test="${!empty home.todaysCourses}">
            <div background-color:#E6E6E6;" id="todayPanel" name="todayPanel">
	            <c:if test="${tabCount eq 1}"><h2>Today</h2></c:if>
	            <kme:listView dataTheme="c">
	                <c:forEach items="${home.todaysCourses}" var="item" varStatus="status">
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