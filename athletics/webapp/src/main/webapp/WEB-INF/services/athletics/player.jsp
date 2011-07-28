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

<kme:page title="Player" id="athletics-roster" backButton="true" homeButton="true" cssFilename="athletics">
	<kme:content>
		<ul class="nav">
			<li>
				<div>
					<img class="rowicon-player" src="<c:out value="${player.thumbnailMedium}" escapeXml="true" />" />
					<em class="roster"> 
						<c:if test="${not empty player.number}">
							<c:out value="${player.number}" escapeXml="true" />
						</c:if> <c:if test="${not empty player.name}">
							<c:out value="${player.name}" escapeXml="true" />
						</c:if> </em><br /> <em class="roster-smallprint"> <c:if test="${not empty player.position}">
							<span class="roster-label">position: </span>
							<c:out value="${player.position}" escapeXml="true" />
							<br />
						</c:if> <c:if test="${not empty player.classStanding}">
							<span class="roster-label">year: </span>
							<c:out value="${player.classStanding}" escapeXml="true" />
							<br />
						</c:if> <c:if test="${not empty player.height}">
							<span class="roster-label">ht: </span>
							<c:out value="${player.height}" escapeXml="true" />
							<br />
						</c:if> <c:if test="${not empty player.weight}">
							<span class="roster-label">wt: </span>
							<c:out value="${player.weight}" escapeXml="true" />
							<br />
						</c:if> <c:if test="${not empty player.homeCity}">
							<span class="roster-label">city: </span>
							<c:out value="${player.homeCity}" escapeXml="true" />
							<br />
						</c:if>
						<c:if test="${not empty player.homeState}">
							<span class="roster-label">state: </span>
							<c:out value="${player.homeState}" escapeXml="true" />
							<br />
						</c:if>
						<c:if test="${not empty player.highSchool}">
							<span class="roster-label">high school: </span>
							<c:out value="${player.highSchool}" escapeXml="true" />
							<br />
						</c:if> </em>
				</div>
				<div style="clear: both"></div>
			</li>
		</ul>
	</kme:content>
</kme:page>
