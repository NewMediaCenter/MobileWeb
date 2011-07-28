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

<kme:page title="Athletics" id="athletics" backButton="true" homeButton="true" cssFilename="athletics">
	<kme:content>
		<h3>
			<c:out value="${athletics.matchData.category}" />
		</h3>
		<ul class="nav">
			<c:forEach var="match" items="${athletics.matchData.matches}">
				<li>
					<div>

						<div class="livescore-container">
							<div class="livescore-sidebar1">
								<div class="scorebox">
									<c:if test="${not empty match.thumbnail}">
										<img src="${match.thumbnail}" class="team-icon" />
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
									<c:out value="${match.sportName}" escapeXml="true" />
									<br /> <span class="opponent"> <c:choose>
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
										<img src="${match.oppThumbnail}" class="team-icon" />
									</c:if>
									<div class="currentscore">
										<c:out value="${match.oppScore}" escapeXml="true" />
									</div>
								</div>
							</div>
						</div>
					</div>
				</li>
			</c:forEach>
		</ul>
		<h3>
			<c:out value="${athletics.newsData.category}" />
		</h3>
		<ul class="nav">
			<c:forEach var="news" items="${athletics.newsData.news}">
				<li>
					<div>
						<a href="${pageContext.request.contextPath}/athletics/viewStory?link=${news.url}"> 
							<c:choose>
								<c:when test="${not empty news.thumbnail}">
									<img src='<c:out value="${news.thumbnail}" escapeXml="true" />' class="rowicon-news" />
									<em class="news"><c:out value="${news.title}" /></em>
								</c:when> 
								<c:otherwise>
									<em class="news-noimage"><c:out value="${news.title}" /></em>
								</c:otherwise> 
							</c:choose>
						</a>
					</div>
				</li>
			</c:forEach>
		</ul>
		<h3>
			<c:out value="${athletics.sportData.category}" />
		</h3>
		<ul class="nav">
			<c:forEach var="sport" items="${athletics.sportData.sports}">
				<li>
					<div>
						<a href="${pageContext.request.contextPath}/athletics/viewSport?sportId=${sport.sportId}"> <c:if test="${not empty sport.thumbnail}">
								<img src="${sport.thumbnail}" class="rowicon-team" />
							</c:if><em class="team">${sport.name}</em> </a>
					</div>
				</li>
			</c:forEach>
		</ul>
	</kme:content>
</kme:page>
