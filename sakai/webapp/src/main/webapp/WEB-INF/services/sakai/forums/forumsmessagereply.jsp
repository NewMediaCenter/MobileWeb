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
<!DOCTYPE html>
<html>
<head>
<title>Page Title</title>
<link href="${pageContext.request.contextPath}/css/jquery.mobile-1.0b1.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/custom.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0b1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>

<script type="text/javascript">

$(function(){
	$("#jQForumMessageReplyForm").submit(function(){
		$.blockUI({ message: '<h1>Submitting post...</h1>' });
		var $form = $( this ),
        title = $form.find( 'input[name="title_r"]' ).val(),
        body = $form.find( 'textArea[name="body_r"]' ).val();
		messageId = $form.find( 'input[name="messageId"]' ).val();
		topicId = $form.find( 'input[name="topicId"]' ).val();
		forumId = $form.find( 'input[name="forumId"]' ).val();
		var referrer = document.referrer;
		$.post("${pageContext.request.contextPath}/sakaiforumsmessagedetails", { 'title': title, 'body': body, 'messageId': messageId, 'topicId': topicId, 'forumId': forumId },
				   function(data) {
				     //alert("Posted " + data);
				     $.unblockUI();
				     $.mobile.changePage(referrer, "slide", false, true);
				   });
		  return false;
	});
});
</script>
</head>

<body>
<div data-role="page" id="">
  <div data-role="header" data-position="">
    <h1>Reply to <%= request.getParameter("messageTitle") %></h1>
  </div>
  <!-- /header -->
 
  <div data-role="content" style="padding-top:0px" >
    <form id="jQForumMessageReplyForm" method="post">
        <fieldset>
        <div data-role="fieldcontain" >
        <label for="title">Title:</label>
        <input type="text" name="title_r" value="Re: <%= request.getParameter("messageTitle") %>" id="title" />
     
        <label for="body">Message:</label>
        <textarea cols="40" rows="8" name="body_r" id="body"></textarea>
     </div>
     	<input type="hidden" name="messageId" value=<%= request.getParameter("messageId") %>>
     	<input type="hidden" name="topicId" value=<%= request.getParameter("topicId") %>>
     	<input type="hidden" name="forumId" value=<%= request.getParameter("forumId") %>>
     	<div data-inline="true">
        <div class="ui-grid-a">
          <div class="ui-block-b"><button type="submit" data-theme="a" name="submit" value="Create" class="ui-btn-hidden" aria-disabled="false">Create</button></div>
        </div>
     	 </div>
     	</fieldset>
     </form>
  </div>
  <!-- /content --> 
  
  <!-- /header --> 
</div>
<!-- /stc --> 

<!-- /page -->

</body>
</html>