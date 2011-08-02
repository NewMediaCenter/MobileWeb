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
				<c:url var="newsUrl" value="/athletics/viewSport">
					<c:param name="sportId" value="${rosterData.sport.sportId}" />
				</c:url>
				<a href="${newsUrl}" class="button-subnav left"><span>news</span> </a>
				<c:if test="${rosterData.sport.seasonId > 0}">
					<c:url var="rosterUrl" value="/athletics/viewRoster">
						<c:param name="sportId" value="${rosterData.sport.sportId}" />
						<c:param name="seasonId" value="${rosterData.sport.seasonId}" />
					</c:url>
					<c:url var="scheduleUrl" value="/athletics/viewSchedule">
						<c:param name="sportId" value="${rosterData.sport.sportId}" />
						<c:param name="seasonId" value="${rosterData.sport.seasonId}" />
					</c:url>

					<a href="${rosterUrl}" class="button-subnav left"><span>roster</span> </a>
					<a href="${scheduleUrl}" class="button-subnav left"><span>schedule</span> </a>
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
						<c:url var="playerUrl" value="/athletics/viewPlayer">
							<c:param name="sportId" value="${rosterData.sport.sportId}" />
							<c:param name="seasonId" value="${rosterData.sport.seasonId}" />
							<c:param name="playerId" value="${player.playerId}" />
						</c:url>
						<a href="${playerUrl}"> <img class="rowicon-roster" src="<c:out value="${player.thumbnail}" escapeXml="true" />" /> <em class="roster"> <c:if test="${not empty player.number}">
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
