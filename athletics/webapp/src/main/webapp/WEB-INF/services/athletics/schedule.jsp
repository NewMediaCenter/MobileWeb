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

<kme:page title="Schedule" id="athletics-schedule" backButton="true" homeButton="true" cssFilename="athletics" backButtonURL="${pageContext.request.contextPath}/athletics">
	<kme:content>
		<div class="subnav">
			<div class="subnav-container">
				<c:url var="newsUrl" value="/athletics/viewSport">
					<c:param name="sportId" value="${matchData.sport.sportId}" />
				</c:url>
				<a href="${newsUrl}" class="button-subnav left"><span>news</span> </a>
				<c:if test="${matchData.sport.seasonId > 0}">
					<c:url var="rosterUrl" value="/athletics/viewRoster">
						<c:param name="sportId" value="${matchData.sport.sportId}" />
						<c:param name="seasonId" value="${matchData.sport.seasonId}" />
					</c:url>
					<c:url var="scheduleUrl" value="/athletics/viewSchedule">
						<c:param name="sportId" value="${matchData.sport.sportId}" />
						<c:param name="seasonId" value="${matchData.sport.seasonId}" />
					</c:url>
					<a href="${rosterUrl}" class="button-subnav left"><span>roster</span> </a>
					<a href="${scheduleUrl}" class="button-subnav left"><span>schedule</span> </a>
				</c:if>
			</div>
		</div>
		<div class="nonfocal">
			<c:out value="${matchData.sport.name}" escapeXml="true"></c:out>
		</div>
		<ul class="nav">
			<c:forEach items="${matchData.matches}" var="match" varStatus="status">
				<li>
					<div>
						<div class="livescore-container">
							<div class="livescore-sidebar1">
								<div class="scorebox">
									<c:if test="${not empty match.thumbnail}">
										<img src='<c:out value="${match.thumbnail}"/>' class="team-icon" />
									</c:if>
									<div class="currentscore">
										<c:out value="${match.score}" escapeXml="true" />
									</div>
								</div>
								<!-- end .sidebar1 -->
							</div>
							<div class="livescore-content">
								<div class="sport-name">
									<c:if test="${not empty match.winLoss}">
										<span class="gamestatus"><c:out value="${match.winLoss}" /> </span>
										<br />
									</c:if>
									<span class="opponent"> <c:choose>
											<c:when test="${not empty match.opponent}">
												<c:out value="${match.home}" />&nbsp;<c:out value="${match.opponent}" escapeXml="true" />
											</c:when>
											<c:when test="${not empty match.name}">
												<c:out value="${match.name}" />
											</c:when>
											<c:otherwise>TBD</c:otherwise>
										</c:choose> </span>
								</div>
								<div class="gametimeloc">
									<c:choose>
										<c:when test="${match.winLoss eq 'in progress' and not empty match.timeRemaining}">
											<c:out value="${match.timeRemaining}" escapeXml="true" />
											<br />
										</c:when>
										<c:when test="${not empty match.dateTime}">
											<c:out value="${match.dateTime}" escapeXml="true" />
											<br />
										</c:when>
									</c:choose>
									<c:if test="${not empty match.location}">
										<c:out value="${match.location}" escapeXml="true" />
									</c:if>
								</div>
							</div>
							<div class="livescore-sidebar2">
								<div class="scorebox">
									<c:if test="${not empty match.oppThumbnail}">
										<img src='<c:out value="${match.oppThumbnail}"/>' class="team-icon" />
									</c:if>
									<div class="currentscore">
										<c:out value="${match.oppScore}" escapeXml="true" />
									</div>
								</div>
								<!-- end .sidebar2 -->
							</div>
						</div>
					</div>
				</li>
			</c:forEach>
		</ul>
	</kme:content>
</kme:page>