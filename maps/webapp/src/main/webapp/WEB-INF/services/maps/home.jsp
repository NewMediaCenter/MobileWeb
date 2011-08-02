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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<kme:page title="Campus Maps" id="maps" backButton="true" homeButton="true" cssFilename="maps">
	<kme:content>
		<form:form action="${pageContext.request.contextPath}/maps/building/search" commandName="mapsearchform" data-ajax="false">
			<fieldset>
            <label for="searchText">Search</label>
			<form:input path="searchText" cssClass="text ui-widget-content ui-corner-all" />
			<form:errors path="searchText" />
			<label for="searchCampus">Campus</label>
			<form:select path="searchCampus" multiple="false">
				<option value="UA" label="">select:</option>			  
				<option value="BL" label="">IU Bloomington</option>
				<option value="CO" label="">IUPUC Columbus</option>
				<option value="EA" label="">IU East</option>
				<option value="FW" label="">IPFW Fort Wayne</option>
				<option value="IN" label="">IUPUI Indianapolis</option>
				<option value="KO" label="">IU Kokomo</option>
				<option value="NW" label="">IU Northwest</option>
				<option value="SB" label="">IU South Bend</option>
				<option value="SE" label="">IU Southeast</option>
			</form:select>
			</fieldset>
		</form:form>
	    <kme:definitionListView id="mapsearchresults">
			<div id="searchresults">
	        <c:forEach items="${container.results}" var="item" varStatus="status">
	            <kme:definitionListTerm>
					<a href="${pageContext.request.contextPath}/maps/building/${item.code}">${item.name}</a>
	            </kme:definitionListTerm>
	        </c:forEach>
	        </div>
	    </kme:definitionListView>
	    
<script type="text/javascript">
$('#maps').live("pagebeforeshow", function() {
	$('#searchText').keypress(function (event) {
		var searchText = $("#searchText").val();
		var groupCode = $("#searchCampus").val();
		console.log(searchText + " " + groupCode);
		mapSearch(searchText, groupCode);
		if (event.keyCode == 13) {
			return false;
		}
	});
	$("#searchCampus").change(function() {
		var searchText = $("#searchText").val();
		var groupCode = $("#searchCampus").val();
		console.log(searchText + " " + groupCode);
		mapSearch(searchText, groupCode);
	});
});

$('#maps').live("pageshow", function() {
	// If the browser keeps the search parameters, search on page load
	var searchText = $("#searchText").val();
	var groupCode = $("#searchCampus").val();
	console.log(searchText + " " + groupCode);
	mapSearch(searchText, groupCode);	
});

var mapsRemoteCallCount = 0;
var mapsCurrentDisplayNumber = 0;

function mapSearch(inputString, groupCode) {
	mapsRemoteCallCount++;
	var mapsRemoteCallCountAtStartOfRequest = mapsRemoteCallCount;
	if (inputString.length < 2 || groupCode == "UA") {
		// Hide the suggestion box.
		$('#searchresults').html('');
	} else {
		var requestUrlString = '${pageContext.request.contextPath}/maps/building/search?criteria=' + encodeURI(inputString) + '&groupCode=' + encodeURI(groupCode);
		$.get(requestUrlString, function(data) {
			//console.log("" + requestUrlString + " " + mapsRemoteCallCount + " " + mapsCurrentDisplayNumber);
			if (mapsRemoteCallCountAtStartOfRequest >= mapsCurrentDisplayNumber) {
				mapsCurrentDisplayNumber = mapsRemoteCallCount;
				// Show results
				var pagehtml = '<div id="resultdata"></div>'
				$('#searchresults').html(pagehtml);
				$("#resultdata").html(data).page();
			}
		});
	}
} // mapSearch
</script>

	</kme:content>
</kme:page>
