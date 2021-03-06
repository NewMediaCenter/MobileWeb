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

<kme:page title="Reply" id="reply" cssFilename="sakai">
    <kme:content>
    	<form:form action="${pageContext.request.contextPath}/myclasses/${siteId}/forums/reply" commandName="message" data-ajax="false" method="post">
	        <fieldset>
	        	<form:hidden path="inReplyTo"/>
	        	<form:hidden path="title"/>
	        	<form:hidden path="forumId"/>
	        	<form:hidden path="topicId"/>
	        	<form:hidden path="threadId"/>
	        	
		        <div class="forum-reply">
	        		<label for="body">Message:</label>
	        		<form:textarea path="body" cols="40" rows="8" class="required" />
                	<form:errors path="body"/>
	      		</div>
				<div data-inline="true">
					<div class="ui-grid-a">
				    	<div class="ui-block-a"><a href="${pageContext.request.contextPath}/myclasses/${siteId}/forums/${message.forumId}/${message.topicId}/${message.threadId}" data-role="button" data-theme="c" data-direction="reverse">Cancel</a></div>
				    	<div class="ui-block-b"><input class="submit" type="submit" value="Post" data-theme="a" /></div>
					</div>
				</div>
	        
	        	<%--
	        	<label for="recipients">To:</label>
                <form:input path="recipients" type="text" value="" class="required"  />
                <form:errors path="recipients"/>
                 --%>
	        
	     	</fieldset>
		</form:form>
	</kme:content>
</kme:page>