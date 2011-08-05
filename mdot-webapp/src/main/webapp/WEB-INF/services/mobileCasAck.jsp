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

<kme:page title="Mobile CAS Acknowledgement" id="mobCasAck" backButton="true" homeButton="true" backButtonURL="home">
	<kme:content>
		<ol>
			<li>Mobile CAS allows a 24 hour session.</li>
			<li>A PIN should be used to secure your phone.</li>
			<li>If you lose your phone you should change your passphrase to invalidate your Mobile CAS Session.</li>
		</ol>		    
		<form:form action="${pageContext.request.contextPath}/mobileCasAck" commandName="command" data-ajax="false" method="post">
	      <div data-inline="true">
	        <div class="ui-grid-a">
	          <div class="ui-block-a"><a href="${pageContext.request.contextPath}/home" data-role="button">Cancel</a></div>
	          <div class="ui-block-b"><input class="submit" type="submit" value="I understand" /></div>
	        </div>
	      </div>
	    </form:form>
	</kme:content>
</kme:page>


