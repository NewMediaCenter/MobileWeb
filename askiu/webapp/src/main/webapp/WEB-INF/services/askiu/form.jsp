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

<kme:page title="Ask IU" id="ask_iu">
	<kme:content>
		<form:form action="${pageContext.request.contextPath}/askiu" commandName="askiu" data-ajax="false" method="post">
			<fieldset>
				<div data-role="fieldcontain">
					<label for="email">Your email address:</label>
			        <form:input path="email" class="required email" />
			        <form:errors path="email"/>
				</div>
				<div data-role="fieldcontain">
			        <label for="subject">Subject:</label>
			        <form:input path="subject"></form:input>
			        <form:errors path="subject"/>
				</div>
				<div data-role="fieldcontain">
			        <label for="message">Message:</label>
			        <form:textarea path="message" cols="40" rows="8" class="required" />
			        <form:errors path="message"/>
				</div>
		      </fieldset>
		      <div data-inline="true">
		        <div class="ui-grid-a">
		          <div class="ui-block-a"><a href="/mdot" data-role="button">Cancel</a></div>
		          <div class="ui-block-b"><input class="submit" type="submit" value="Submit" /></div>
		        </div>
		      </div>
	    </form:form>
	</kme:content>
</kme:page>
