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

<kme:page title="My Classes" id="myclasses">
	<kme:content>
		<c:forEach items="${myclasses}" var="termItem" varStatus="status">
			<h3>${termItem.key}</h3>
			<ul data-role="listview" data-theme="c" data-inset="true">
				<c:forEach items="${termItem.value}" var="item" varStatus="status">
					<li>
						<a href="${pageContext.request.contextPath}/myclasses/${item.courseId}">
							<h3>${item.courseTitle}</h3>
							<p>${item.courseDesc}</p>
						</a>
					</li>
				</c:forEach>
			</ul>
		</c:forEach>
		<div data-role="footer" data-id="mcl-footer" data-position="fixed"  role="contentinfo"  data-theme="b">
	    	<div data-role="navbar" role="navigation">
		      	<ul class="ui-grid-a">
			        <li class="ui-block-a"><a href="${pageContext.request.contextPath}/myclasses">My Classes</a></li>
			        <li class="ui-block-b"><a href="mcl-prog.html">My Projects </a></li>
		      	</ul>
		    </div>
		</div>
	</kme:content>
</kme:page>