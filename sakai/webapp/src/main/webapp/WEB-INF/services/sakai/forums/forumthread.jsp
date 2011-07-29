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


<kme:page title="${thread.title}" id="forum" cssFilename="sakai" backButton="true" homeButton="true" backButtonURL="${pageContext.request.contextPath}/myclasses/${siteId}/forums/${thread.forumId}/${thread.topicId}?title=${thread.topicTitle}">
	<kme:content cssClass="forum">
		<ul>
			<c:forEach var="message" items="${thread.messages}">
				<li>
					<div style="margin-left:${message.indentIndex*10}px;">
						<c:if test="${not message.isRead}">
							<h3>${message.createdBy}</h3>
						</c:if>
						<c:if test="${message.isRead}">
							<h3 class="read">${message.createdBy}</h3>
						</c:if>
						<span class="date">${message.createdDate}</span>
						<c:if test="${not message.isRead}">
							<a class="markread ui-li-static" style="display:inline;" href="${pageContext.request.contextPath}/myclasses/${siteId}/forums/${thread.forumId}/${thread.topicId}/${thread.id}/${message.id}/markread?topicTitle=${thread.topicTitle}">
								Mark as read
							</a>
						</c:if>
						<p class="forum-thread">${message.body}</p>
					</div>
				</li>
			</c:forEach>
		</ul>
	</kme:content>
</kme:page>

<%--
<kme:page title="${thread.title}" id="forum" cssFilename="sakai" backButton="true" homeButton="true" backButtonURL="${pageContext.request.contextPath}/myclasses/${siteId}/forums/${thread.forumId}/${thread.topicId}?title=${thread.topicTitle}">
	<kme:content>
		<ul data-role="listview" data-theme="c"  data-dividertheme="b">
			<c:forEach items="${thread.messages}" var="message" varStatus="status">
				<li data-role="list-divider">${message.createdBy}<span class="ui-li-rightcont ui-btn-up-c ui-btn-corner-all">${message.createdDate}</span> </li>
      			<li class="ui-li-static" data-icon="false">
      				<c:if test="${not message.isRead}">
						<a class="markread ui-li-static" style="display:inline;" href="${pageContext.request.contextPath}/myclasses/${siteId}/forums/${thread.forumId}/${thread.topicId}/${thread.id}/${message.id}/markread?topicTitle=${thread.topicTitle}">
							Mark as read
						</a>
					</c:if>
        				<p class="forum-thread">${message.body}</p>
        		</li>
			</c:forEach>
		</ul>
	</kme:content>
</kme:page>
--%>