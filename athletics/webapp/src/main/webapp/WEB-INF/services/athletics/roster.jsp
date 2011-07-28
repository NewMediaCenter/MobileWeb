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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="kme" uri="http://kuali.org/mobility"%>

<kme:page title="Roster" id="athletics-roster" backButton="true" homeButton="true" cssFilename="athletics" backButtonURL="${pageContext.request.contextPath}/athletics">
	<kme:content>
		<div class="subnav">
			<div class="subnav-container">
				<a href="${pageContext.request.contextPath}/athletics/viewSport?sportId=${rosterData.sport.sportId}" class="button-subnav left"><span>news</span> </a>
				<c:if test="${rosterData.sport.seasonId > 0}">
					<a href="${pageContext.request.contextPath}/athletics/viewRoster?sportId=${rosterData.sport.sportId}&seasonId=${rosterData.sport.seasonId}" class="button-subnav left"><span>roster</span> </a>
					<a href="${pageContext.request.contextPath}/athletics/viewSchedule?sportId=${rosterData.sport.sportId}&seasonId=${rosterData.sport.seasonId}" class="button-subnav left"><span>schedule</span> </a>
				</c:if>
			</div>
		</div>
		<div class="nonfocal">
			<c:out value="${rosterData.sport.name}" escapeXml="true"></c:out>
		</div>
		<ul class="nav">
			<c:forEach items="${rosterData.players}" var="player" varStatus="status">
				<li>
					<div>
						<a href="${pageContext.request.contextPath}/athletics/viewPlayer?playerId=${player.playerId}&seasonId=${rosterData.sport.seasonId}&sportId=${rosterData.sport.sportId}"> 
							<img class="rowicon-roster" src="<c:out value="${player.thumbnail}" escapeXml="true" />" /> 
								<em class="roster"> 
								<c:if test="${not empty player.number}">
									<c:out value="${player.number}" escapeXml="true" /> - </c:if> <c:if test="${not empty player.name}">
									<c:out value="${player.name}" escapeXml="true" />
								</c:if> </em> <br /> <em class="roster-smallprint"> <c:if test="${not empty player.position}">
									<c:out value="${player.position}" escapeXml="true" />,</c:if> <c:if test="${not empty player.height}">
									<c:out value="${player.height}" escapeXml="true" />
								</c:if> <c:if test="${not empty player.weight}">
									<c:out value="${player.weight}" escapeXml="true" />
								</c:if> </em> </a>
					</div>
					<div style="clear: both"></div></li>
			</c:forEach>
		</ul>
	</kme:content>
</kme:page>
