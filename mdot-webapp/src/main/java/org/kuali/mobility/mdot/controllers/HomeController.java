/**
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
 
package org.kuali.mobility.mdot.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.kuali.mobility.mdot.entity.HomeScreen;
import org.kuali.mobility.mdot.entity.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import flexjson.JSONSerializer;

@Controller 
@RequestMapping("/")
public class HomeController {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(HomeController.class);
	
    @RequestMapping(value = "home", method = RequestMethod.GET)
    public String getList(HttpServletRequest request, Model uiModel) {      
        uiModel.addAttribute("home", buildHomeScreen());    	
    	return "index";
    }

    @RequestMapping(value = "home.json", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public String getHomeScreenJson() {
    	return new JSONSerializer().exclude("*.class").include("tools").serialize(buildHomeScreen());
    }

    private HomeScreen buildHomeScreen() {
    	HomeScreen home = new HomeScreen();
    	home.setPrincipalId("1234");
    	home.setPrincipalName("natjohns");
    	
    	List<Tool> tools = new ArrayList<Tool>();

    	Tool tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("Class information; forums, grades, and more.");
    	tool.setIconUrl("images/service-icons/srvc-myclasses.png");
    	tool.setTitle("My Classes");
    	tool.setUrl("myclasses");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("Find furniture, books, an apartment, a job, and more.");
    	tool.setIconUrl("images/service-icons/srvc-classifieds.png");
    	tool.setTitle("Classifieds");
    	tool.setUrl("https://onestart.iu.edu/ccf2-prd/ClassifiedsMb.do");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("Never miss an IU Bloomington campus bus again.");
    	tool.setIconUrl("images/service-icons/srvc-bus.png");
    	tool.setTitle("Bus Schedules");
    	tool.setUrl("bus");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("Get from here to there. Search for buildings by name.");
    	tool.setIconUrl("images/service-icons/srvc-maps.png");
    	tool.setTitle("Maps");
    	tool.setUrl("maps");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("Live scores, rosters, news and schedules for your IU teams.");
    	tool.setIconUrl("images/service-icons/srvc-athletics.png");
    	tool.setTitle("Athletics");
    	tool.setUrl("athletics");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("Find contact information for IU students, faculty, and staff.");
    	tool.setIconUrl("images/service-icons/srvc-people.png");
    	tool.setTitle("People");
    	tool.setUrl("people");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("See which campus STC labs have an open computer.");
    	tool.setIconUrl("images/service-icons/srvc-stc.png");
    	tool.setTitle("Computer Labs");
    	tool.setUrl("computerlabs");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("Alerts and announcements affecting your technology.");
    	tool.setIconUrl("images/service-icons/srvc-itnotice.png");
    	tool.setTitle("IT Notices");
    	tool.setUrl("itnotices");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("The latest buzz on IU's exciting events and achievements.");
    	tool.setIconUrl("images/service-icons/srvc-news.png");
    	tool.setTitle("News");
    	tool.setUrl("news");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("See what's happening on campus today, this week, and year-round.");
    	tool.setIconUrl("images/service-icons/srvc-events.png");
    	tool.setTitle("Events");
    	tool.setUrl("events");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("Access and manage your personal OneStart calendar.");
    	tool.setIconUrl("images/service-icons/srvc-events.png");
    	tool.setTitle("Calendar");
    	tool.setUrl("calendar/month");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("Find answers to questions about IU information technology.");
    	tool.setIconUrl("images/service-icons/srvc-kb.png");
    	tool.setTitle("Knowledge Base");
    	tool.setUrl("knowledgebase");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("Take IU's popular question & answer service with you on the go.");
    	tool.setIconUrl("images/service-icons/srvc-askiu.png");
    	tool.setTitle("Ask IU");
    	tool.setUrl("askiu");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("Get up to date menus and prices for campus dining services.");
    	tool.setIconUrl("images/service-icons/srvc-dining.png");
    	tool.setTitle("Dining Services");
    	tool.setUrl("dining");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("Police and medical phone numbers.");
    	tool.setIconUrl("images/service-icons/srvc-emergency.png");
    	tool.setTitle("Emergency Contacts");
    	tool.setUrl("emergencycontacts");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("2");
    	tool.setDescription("See a list of active campus alert messages.");
    	tool.setIconUrl("images/service-icons/srvc-alerts-green.png");
    	tool.setTitle("Campus Alerts");
    	tool.setUrl("alerts");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("Submit questions and comments about IU Mobile.");
    	tool.setIconUrl("images/service-icons/srvc-feedback.png");
    	tool.setTitle("Feedback");
    	tool.setUrl("feedback");
    	tools.add(tool);

    	tool = new Tool();
    	tool.setBadgeCount("0");
    	tool.setDescription("IUPUI Athletics information.");
    	tool.setIconUrl("images/service-icons/srvc-jag.png");
    	tool.setTitle("Jaguar Athletics");
    	tool.setUrl("http://www.iupuijags.com");
    	tools.add(tool);
    	
    	home.setTools(tools);
    	
    	return home;
    }
    
}
