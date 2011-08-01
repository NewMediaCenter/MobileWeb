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

<kme:page title="Knowledge Base" id="kbdoc" backButton="true" homeButton="true">
	<kme:content>
		${kbdoc}
		
<script type="text/javascript">
$('[data-role=page][id=kbdoc]').live("pagebeforeshow", function(event) {
	//    $('a.native-anchor').bind('click', function(ev) {
	//    var target = $( $(this).attr('href') ).get(0).offsetTop;
	//    $.mobile.silentScroll(target);
	//    return false;
	//});
	//$('a[href^="\\$"]').click(function(e){
	$('a[href*="knowledgebase"][href*="#"]').click(function(e){
		e.preventDefault();
		var name = $(this).attr('href').substr(25);
	//	var pos = $('a[name='+name+']').offset();
	//	$('html,body').animate({ scrollTop: pos.top });
	//	alert(name);
		var target = $('a[name='+name+']').get(0).offsetTop;
		//alert(target);
		$.mobile.silentScroll(target);
		//alert(pos.top);
		//alert(e.isDefaultPrevented());
		return false;
	});
});
</script>


	</kme:content>
</kme:page>