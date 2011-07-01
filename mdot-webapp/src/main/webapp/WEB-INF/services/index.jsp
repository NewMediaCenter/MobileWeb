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
<!DOCTYPE html>
<html>
<head>
<title>IU Mobile</title>

<jsp:include page="resources.jsp" />

</head>

<body>

<!------------------------------ Start of home page -------------------------------->
<div data-role="page" id="home">
  <div  data-theme="a" data-role="header" data-backbtn="false"> <a href="login.html" class="ui-btn-left" data-transition="pop" data-theme="a" >Login</a>
    <h1>IU Mobile</h1>
    <a href="campus-select.html" class="ui-btn-right" data-transition="flip" data-theme="a" > Campus</a> </div>
  <!-- /header -->
  
  <div data-role="content" data-theme="a">
    <ul data-role="listview" data-theme="c" data-inset="true">
      <li><a href="/mdot/myclasses"><img src="images/service-icons/srvc-myclasses.png" alt="My Classes" class="ui-li-icon">My Classes </a></li>
      <li><a href="#cls"><img src="images/service-icons/srvc-classifieds.png" alt="Classifieds" class="ui-li-icon">Classifieds</a></li>
      <li><a href="#bus"><img src="images/service-icons/srvc-bus.png" alt="Bus Schedules" class="ui-li-icon">Bus Schedules </a></li>
      <li><a href="#map"><img src="images/service-icons/srvc-maps.png" alt="Campus Maps" class="ui-li-icon">Campus Maps </a></li>
      <li><a href="svc-ath.html"><img src="images/service-icons/srvc-athletics.png" alt="Athletics" class="ui-li-icon">Athletics</a></li>
      <li><a href="svc-ppl.html"><img src="images/service-icons/srvc-people.png" alt="People" class="ui-li-icon">People</a></li>
      <li><a href="/mdot/computerlabs"><img src="images/service-icons/srvc-stc.png" alt="Computer Labs" class="ui-li-icon">Computer Labs </a></li>
      <li><a href="/mdot/itnotices"><img src="images/service-icons/srvc-itnotice.png" alt="IT Notices" class="ui-li-icon">IT Notices </a></li>
      <li><a href="/mdot/news"><img src="images/service-icons/srvc-news.png" alt="News" class="ui-li-icon">News</a></li>
      <li><a href="/mdot/calendar/month"><img src="images/service-icons/srvc-events.png" alt="Events" class="ui-li-icon">Events</a></li>
      <li><a href="/mdot/knowledgebase"><img src="images/service-icons/srvc-kb.png" alt="Knowledge Base" class="ui-li-icon">Knowledge Base </a></li>
      <li><a href="#ask"><img src="images/service-icons/srvc-askiu.png" alt="AskIU" class="ui-li-icon">AskIU </a></li>
      <li><a href="#din"><img src="images/service-icons/srvc-dining.png" alt="Dining Services" class="ui-li-icon">Dining Services</a></li>
      <li><a href="/mdot/emergencycontacts"><img src="images/service-icons/srvc-emergency.png" alt="Emergency Contacts" class="ui-li-icon">Emergency Contacts </a></li>
      
      <li>
      	<a href="/mdot/alerts"><img src="images/service-icons/srvc-alerts-green.png" alt="Campus Alerts" class="ui-li-icon">
      	Campus Alerts
      	<c:if test="${alertCount gt 0}"> 
      		<span class="ui-li-count ui-btn-up-c ui-btn-corner-all">${alertCount}</span>
      	</c:if>
      	</a>
      </li>
      
      <li><a href="#fdb"><img src="images/service-icons/srvc-feedback.png" alt="Feedback" class="ui-li-icon">Feedback</a></li>
    </ul>
  </div>
  <!-- /content -->
  
  <div data-role="footer" data-theme="a">
    <h4>&nbsp; </h4>
  </div>
  <!-- /header --> 
</div>
<!-- /page --> 

<!------------------------------ Start of Classifieds -------------------------------->
<div data-role="page" id="cls">
  <div data-role="header">
    <h1>Classifieds</h1>
  </div>
  <!-- /header -->
  
  <div data-role="content">
    <p>I'm first in the source order so I'm shown as the page.</p>
    <p><a href="#home">Back to foo</a></p>
  </div>
  <!-- /content -->
  
  <div data-role="footer">
    <h4>Page Footer</h4>
  </div>
  <!-- /header --> 
</div>
<!-- /Classifieds --> 

<!------------------------------ Start of Bus -------------------------------->
<div data-role="page" id="bus">
  <div data-role="header">
    <h1>Bus Schedules</h1>
  </div>
  <!-- /header -->
  
  <div data-role="content">
    <p>I'm first in the source order so I'm shown as the page.</p>
    <p><a href="#home">Back to foo</a></p>
  </div>
  <!-- /content -->
  
  <div data-role="footer">
    <h4>Page Footer</h4>
  </div>
  <!-- /header --> 
</div>
<!-- /Bus --> 

<!------------------------------ Start of map -------------------------------->
<div data-role="page" id="map">
  <div data-role="header">
    <h1>Campus Maps</h1>
  </div>
  <!-- /header -->
  
  <div data-role="content">
    <p>I'm first in the source order so I'm shown as the page.</p>
    <p><a href="#home">Back to foo</a></p>
  </div>
  <!-- /content -->
  
  <div data-role="footer">
    <h4>Page Footer</h4>
  </div>
  <!-- /header --> 
</div>
<!-- /map --> 

<!------------------------------ Start of ath -------------------------------->
<div data-role="page" id="ath">
  <div data-role="header">
    <h1>Athletics</h1>
  </div>
  <!-- /header -->
  
  <div data-role="content">
    <p>I'm first in the source order so I'm shown as the page.</p>
    <p><a href="#home">Back to foo</a></p>
  </div>
  <!-- /content -->
  
  <div data-role="footer">
    <h4>Page Footer</h4>
  </div>
  <!-- /header --> 
</div>
<!-- /ath --> 

<!------------------------------ Start of ppl -------------------------------->
<div data-role="page" id="ppl">
  <div data-role="header">
    <h1>People</h1>
  </div>
  <!-- /header -->
  
  <div data-role="content">
    <p>I'm first in the source order so I'm shown as the page.</p>
    <p><a href="#home">Back to foo</a></p>
  </div>
  <!-- /content -->
  
  <div data-role="footer">
    <h4>Page Footer</h4>
  </div>
  <!-- /header --> 
</div>
<!-- /ppl --> 

<!------------------------------ Start of nws -------------------------------->
<div data-role="page" id="nws">
  <div data-role="header">
    <h1>News</h1>
  </div>
  <!-- /header -->
  
  <div data-role="content">
    <p>I'm first in the source order so I'm shown as the page.</p>
    <p><a href="#home">Back to foo</a></p>
  </div>
  <!-- /content -->
  
  <div data-role="footer">
    <h4>Page Footer</h4>
  </div>
  <!-- /header --> 
</div>
<!-- /nws --> 

<!------------------------------ Start of evt -------------------------------->
<div data-role="page" id="evt">
  <div data-role="header">
    <h1>Events</h1>
  </div>
  <!-- /header -->
  
  <div data-role="content">
    <p>I'm first in the source order so I'm shown as the page.</p>
    <p><a href="#home">Back to foo</a></p>
  </div>
  <!-- /content -->
  
  <div data-role="footer">
    <h4>Page Footer</h4>
  </div>
  <!-- /header --> 
</div>
<!-- /evt --> 

<!------------------------------ Start of ask -------------------------------->
<div data-role="page" id="ask">
  <div data-role="header">
    <h1>AskIU</h1>
  </div>
  <!-- /header -->
  
  <div data-role="content">
    <p>I'm first in the source order so I'm shown as the page.</p>
    <p><a href="#home">Back to foo</a></p>
  </div>
  <!-- /content -->
  
  <div data-role="footer">
    <h4>Page Footer</h4>
  </div>
  <!-- /header --> 
</div>
<!-- /ask --> 

<!------------------------------ Start of din -------------------------------->
<div data-role="page" id="din">
  <div data-role="header">
    <h1>Dining Services</h1>
  </div>
  <!-- /header -->
  
  <div data-role="content">
    <p>I'm first in the source order so I'm shown as the page.</p>
    <p><a href="#home">Back to foo</a></p>
  </div>
  <!-- /content -->
  
  <div data-role="footer">
    <h4>Page Footer</h4>
  </div>
  <!-- /header --> 
</div>
<!-- /din --> 

<!------------------------------ Start of fdb -------------------------------->
<div data-role="page" id="fdb">
  <div data-role="header">
    <h1>Feedback</h1>
  </div>
  <!-- /header -->
  
  <div data-role="content">
    <p>I'm first in the source order so I'm shown as the page.</p>
    <p><a href="#home">Back to foo</a></p>
  </div>
  <!-- /content -->
  
  <div data-role="footer">
    <h4>Page Footer</h4>
  </div>
  <!-- /header --> 
</div>
<!-- /fdb -->


</body>
</html>