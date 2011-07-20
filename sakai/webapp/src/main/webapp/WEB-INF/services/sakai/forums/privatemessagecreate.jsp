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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<kme:page title="Compose Message" id="compose_message">
    <kme:content>
    	<form:form action="${pageContext.request.contextPath}/myclasses/${siteId}/messages/compose" commandName="message" data-ajax="false" method="post">
	        <fieldset>
	        	<form:hidden path="inReplyTo"/>
	        
	        	<label for="recipients">To:</label>
                <form:input path="recipients" type="text" value="" class="required"  />
                <form:errors path="recipients"/>
	        
		        <label for="title">Subject:</label>
		        <form:input path="title" type="text" value="" class="required"  />
		        <form:errors path="title"/>
		        
		        <label for="body">Message:</label>
                <form:textarea path="body" cols="40" rows="8" class="required" />
                <form:errors path="body"/>
		     	
		     	<div data-inline="true">
	                <div class="ui-grid-a">
	                    <div class="ui-block-a"><a href="${pageContext.request.contextPath}/myclasses/${siteId}/messages" data-role="button">Cancel</a></div>
	                    <div class="ui-block-b">
	                        <input class="submit" type="submit" value="Submit" />
	                    </div>
	                </div>
	            </div>
	     	</fieldset>
		</form:form>
	</kme:content>
</kme:page>