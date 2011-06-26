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
<title><%= request.getParameter("siteTitle") %></title>
<link href="css/jquery.mobile-1.0a4.1.css" rel="stylesheet" type="text/css" />
<link href="css/custom.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="js/custom.js"></script>
<script type="text/javascript" src="js/jquery.mobile-1.0a4.1.js"></script>

<script type="text/javascript">
$("a[href*=.pdf]").click(function(){
	window.open(this.href);
	return false;
});

$("a[href*=.rtf]").click(function(){
	window.open(this.href);
	return false;
});

$("a[href*=.doc]").click(function(){
	window.open(this.href);
	return false;
});
</script>
</head>

<body>
<div data-role="page" id="">
	<div data-role="header">
		<h1>Resources</h1><a href="/mdot/index.jsp" data-icon="home" class="ui-btn-right">home</a>
	</div>
	<!-- /header -->

	<div data-role="content" data-theme="a">
		<ul data-role="listview" data-theme="c" data-dividertheme="b"
			data-filter="true" data-inset="false">
			<c:forEach items="${resources}" var="item" varStatus="status">
				
				<li>
				  <div class="ui-grid-a">
					  <c:if test="${item.hasChild}">
					  	<div class="ui-block-a" style="width:10%">
					  	<img src="/mdot/images/service-icons/folder.png" width="70" height="70" alt="pic">
					  	</div>
						<div class="ui-block-b" style="width:90%">
							<a href="/mdot/sakairesourcedetails?siteId=
							<%= request.getParameter("siteId") %>&sessionId=${sessionId}&resId=
							${item.id}">
							<h3>${item.title}</h3>
							</a>
					  	</div>
					  </c:if>
				  	<c:set var="extension" value="${item.extension}" />
				  	<jsp:useBean id="extension" class="java.lang.String" />
				  	<c:if test='<%=extension.equalsIgnoreCase("pdf")%>'>
				  		<div class="ui-block-a" style="width:10%">
				  		<img src="/mdot/images/service-icons/pdf.png" width="70" height="70" alt="pic">
				  		</div>
				  		<div class="ui-block-b" style="width:90%">
							<a href="http://localhost:9090/direct/resources/getresource${item.id}?sakai.session=${sessionId}">
							<h3>${item.title}</h3>
							</a>
					  	</div>
				  	</c:if>
				  	<c:if test='<%=extension.equalsIgnoreCase("rtf") || extension.equalsIgnoreCase("doc")%>'>
				  		<div class="ui-block-a" style="width:10%">
				  		<img src="/mdot/images/service-icons/ms_office_word.png" width="70" height="70" alt="pic">
				  		</div>
				  		<div class="ui-block-b" style="width:90%">
							<a href="http://localhost:9090/direct/resources/getresource${item.id}?sakai.session=${sessionId}">
							<h3>${item.title}</h3>
							</a>
					  	</div>
				  	</c:if>
				  	<c:if test='<%=extension.equalsIgnoreCase("ppt") || extension.equalsIgnoreCase("pptx")%>'>
				  		<div class="ui-block-a" style="width:10%">
				  		<img src="/mdot/images/service-icons/ms_office_ppt.png" width="70" height="70" alt="pic">
				  		</div>
				  		<div class="ui-block-b" style="width:90%">
							<a href="http://localhost:9090/direct/resources/getresource${item.id}?sakai.session=${sessionId}">
							<h3>${item.title}</h3>
							</a>
					  	</div>
				  	</c:if>
				  	<c:set var="title" value="${item.title}" />
				  	<jsp:useBean id="title" class="java.lang.String" />
				  	<c:if test='<%=extension.equalsIgnoreCase("URL")%>'>
				  		<div class="ui-block-a" style="width:10%">
				  		<img src="/mdot/images/service-icons/link.png" width="70" height="70" alt="pic">
				  		</div>
				  		<div class="ui-block-b" style="width:90%">
							<a href="${item.title}">
							<h3>${item.title}</h3>
							</a>
					  	</div>
				  	</c:if>
				  </div>	
				</li>
			</c:forEach>
		</ul>
	</div>
	<!-- /content -->

	<!-- /header --> 
</div>
<!-- /stc --> 

<!-- /page -->

</body>
</html>