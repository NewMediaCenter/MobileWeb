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

<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="kme"  uri="http://kuali.org/mobility" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<kme:page title="Find People" id="people" backButton="true" homeButton="true" cssFilename="people">
	<kme:content>
		<form:form action="${pageContext.request.contextPath}/people" commandName="search" data-ajax="false" method="post">
			<div data-role="fieldcontain">
				<label for="lastName">Last Name:</label>
                <form:input path="lastName" type="text" value="" />
                <form:errors path="lastName"/>
                <fieldset data-role="controlgroup" data-type="horizontal">
					<form:radiobutton path="exactness" value="starts" />
			        <label for="exactness1">starts with</label>
			
			        <form:radiobutton path="exactness" value="exact"  />
			        <label for="exactness2">is exactly</label>
                </fieldset>
			</div>
               
               
			<div data-role="fieldcontain">
				<label for="firstName">First Name:</label>
                <form:input path="firstName" type="text" value=""  />
			</div>
               
			<div data-role="fieldcontain">
                <label for="userName">User Name:</label>
                <form:input path="userName" type="text" value=""  />
			</div>
               
			<div data-role="fieldcontain">
                <label for="status" class="select">Status:</label>
                <form:select path="status" multiple="false" items="${statusTypes}" data-native-menu="false"/>
			</div>
                       
			<div data-role="fieldcontain">
                <label for="location" class="select">Location:</label>
                <form:select path="location" multiple="false" items="${locations}" data-native-menu="false"/>
                <form:errors path="location"/>
			</div> 
            
			<div data-inline="true">
                <div class="ui-grid-a">
                    <div class="ui-block-a"><a href="${pageContext.request.contextPath}" data-role="button">Cancel</a></div>
                    <div class="ui-block-b">
                        <input class="submit" type="submit" value="Submit" />
                    </div>
                </div>
            </div>
        </form:form>
	</kme:content>
</kme:page>
