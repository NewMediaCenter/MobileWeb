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
<title>My Calendar</title>
<link href="${pageContext.request.contextPath}/css/jquery.mobile-1.0b1.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/custom.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0b1.min.js"></script>
</head>

<body>
<div data-role="page" id="Calendar-Events">
  <div data-role="header">
    <h1>Invite</h1>
  </div>
  <div data-role="content" >
      <ul data-role="listview" data-theme="g">
          <li>
                <h3><c:out value="${invite.title}"/></h3>
                <p><c:out value="${invite.start}"/> - <c:out value="${invite.end}"/></p>
          </li>
          <li>
                <h3>Host</h3>
                <p><c:out value="${invite.hostName}"/></p>
          </li>
          <li>
                <h3>Accepted</h3>
                <c:forEach var="accepted" items="${invite.acceptedAttendees}">
                    <p>
                        <c:out value="${accepted.name}"/>
                    </p>
                </c:forEach>
          </li>
          <li>
                <h3>Tentative</h3>
                <c:forEach var="tentative" items="${invite.tentativeAttendees}">
                    <p>
                        <c:out value="${tentative.name}"/>
                    </p>
                </c:forEach>
          </li>
          <li>
                <h3>Declined</h3>
                <c:forEach var="declined" items="${invite.declinedAttendees}">
                    <p>
                        <c:out value="${declined.name}"/>
                    </p>
                </c:forEach>
          </li>
          <li>
                <h3>No Reply</h3>
                <c:forEach var="noReply" items="${invite.noReplyAttendees}">
                    <p>
                        <c:out value="${noReply.name}"/>
                    </p>
                </c:forEach>
          </li>
      </ul>
      <br/>
       <a href="${pageContext.request.contextPath}/calendar/meetingAction?eventId=${invite.eventId}&type=A" class="ui-btn-right" data-ajax="false">accept</a> <a href="${pageContext.request.contextPath}/calendar/meetingAction?eventId=${invite.eventId}&type=T" class="ui-btn-right" data-ajax="false">tentative</a> <a href="${pageContext.request.contextPath}/calendar/meetingAction?eventId=${invite.eventId}&type=D" class="ui-btn-right" data-ajax="false">decline</a>
  </div>
  <div data-role="footer" data-id="events-footer" data-position="fixed" role="contentinfo" data-theme="b">
  </div>
</div>
<!-- /page -->

</body>
</html>