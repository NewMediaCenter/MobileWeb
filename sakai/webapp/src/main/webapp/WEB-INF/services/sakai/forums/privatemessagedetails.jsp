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

<script type="text/javascript">
	if (window.attachEvent) {window.attachEvent('onload', captureLinks);}
	else if (window.addEventListener) {window.addEventListener('load', captureLinks, false);}
	else {document.addEventListener('load', captureLinks, false);}
	
	function captureLinks() {
	   $('a.markread').click(function() {
		   markRead(this);
		   return false;
	   });
	}
	
	function markRead(anchor) {
		$.ajax({
			url: fixUrl(anchor.href),
			context: anchor,
			success: function() {
				$(this).hide();
			},
			error: function() {
				var span = $(this).children('span')[0];
				if (!span){
					$(this).append('<span class="error">There was an error marking the message as read.</span>');
				}
			}
		});
	}
	
	function fixUrl(url){
		var fields = url.split("/")
		var end = fields[fields.length-1].split("?")
		var results = "";
		for (i=0;i < fields.length-1;i++) {
			results += fields[i] + "/";
		}
		results += end[0] + "/ajax";
		return results;
	}
</script>
	
<kme:page title="Message Details" id="message" cssFilename="sakai">
	<kme:content>
		<h3>${message.title}</h3>
		<p>${message.createdBy} ${message.createdDate}</p>
		<p>${message.body}</p>
		<c:if test="${not item.isRead}">
			<a class="markread" href="${pageContext.request.contextPath}/myclasses/${siteId}/messages/folder/${messageFolder.typeUuid}/${item.id}/markread?title=${messageFolder.title}">
				Mark as read
			</a>
		</c:if>
		<%--
		<a href="${pageContext.request.contextPath}/myclasses/${siteId}/messages/folder/${typeUuid}/${message.id}/reply" data-role="button">Reply</a>
		<a href="${pageContext.request.contextPath}/myclasses/${siteId}/messages/folder/${typeUuid}/${message.id}/forward" data-role="button">Forward</a>
		 --%>
	</kme:content>
</kme:page>