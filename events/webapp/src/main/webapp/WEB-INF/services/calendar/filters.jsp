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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>My Calendar</title>
<link href="${pageContext.request.contextPath}/css/jquery.mobile-1.0b1.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/jquery.mobile.datebox.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/custom.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.1.min.js"></script>
<script type="text/javascript">
    $( document ).bind( "mobileinit", function(){ $.mobile.page.prototype.options.degradeInputs.date = 'text'; });	
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0b1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile.datebox.min.js"></script>
</head>

<body>
<div data-role="page" id="Calendar-Events-edit">
  <div data-role="header">
    <h1>Event</h1><a href="${pageContext.request.contextPath}/calendar/options" class="ui-btn-right">options</a>
  </div>
  <div data-role="content" >
    <form:form action="${pageContext.request.contextPath}/calendar/selectFilter" commandName="filter" data-ajax="false">
        <fieldset>
            <label for="title">Filter by</label>
             <form:select path="filterId" cssClass="ui-widget-content ui-corner-all" data-native-menu="false">
              	<form:option value="">---</form:option>
             	<form:options items="${filters}" itemLabel="filterName" itemValue="filterId" />
             </form:select>
        </fieldset>
        <input name="select" type="image" value="Select" src="${pageContext.request.contextPath}/images/btn-select.gif" alt="select" />
    </form:form>
  </div>
  <div data-role="footer" data-id="events-footer" data-position="fixed" role="contentinfo" data-theme="b">
  </div>
</div>
<!-- /page -->

</body>
</html>