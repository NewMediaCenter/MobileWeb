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

<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="kme" uri="http://kuali.org/mobility" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<kme:page title="Feedback" id="feedback_page" backButton="true" homeButton="true" cssFilename="feedback">
    <kme:content>
        <form:form action="${pageContext.request.contextPath}/feedback" commandName="feedback" data-ajax="false" method="post"> Do you have suggestions for other mobile services at Indiana University or ideas on how we can improve the current services? Let us know!
            <%--hidden fields: <form:hidden path="eventId"/>--%>
            <fieldset>
                <div data-role="fieldcontain">
                    <label for="service" class="select">Subject:</label>
                    <form:select path="service" data-native-menu="false">
                        <%--<option value="N/A" selected="selected"> Select type:</option> --%>
                        <form:option value="General Feedback" selected="selected">General Feedback</form:option>
                        <form:option value="Bus Schedules">Bus Schedules</form:option>
                        <form:option value="Campus Alerts">Campus Alerts</form:option>
                        <form:option value="Campus Maps">Campus Maps</form:option>
                        <form:option value="Emergency Contacts">Emergency Contacts</form:option>
                        <form:option value="Events">Events</form:option>
                        <form:option value="IT Notices">IT Notices</form:option>
                        <form:option value="News">News</form:option>
                        <form:option value="Oncourse">Oncourse</form:option>
                        <form:option value="People">People</form:option>
                        <form:option value="Search IU">Search IU</form:option>
                        <form:option value="Computer Labs">Computer Labs</form:option>
                    </form:select>
                </div>
                        
                <div data-role="fieldcontain">
                    <label for="deviceType" class="select">Device Type:</label>
                    <form:select path="deviceType" multiple="false" items="${deviceTypes}" data-native-menu="false" class="required"/>
                    <form:errors path="deviceType"/>
                </div> 
                
                <div data-role="fieldcontain">
                    <label for="noteText">Message:</label>
                    <form:textarea path="noteText" cols="40" rows="8" class="required" />
                    <form:errors path="noteText"/>
                </div>
                <div data-role="fieldcontain">
                    <label for="email">Your Email:</label>
                    <form:input path="email" type="text" value="" class="email"  />
                </div>
            </fieldset>
            
            <div data-inline="true">
                <div class="ui-grid-a">
                    <div class="ui-block-a"><a href="${pageContext.request.contextPath}" data-role="button">Cancel</a></div>
                    <div class="ui-block-b">
                        <input class="submit" type="submit" value="Submit" />
                    </div>
                </div>
            </div>
        </form:form>
    </kme:content>
</kme:page>
