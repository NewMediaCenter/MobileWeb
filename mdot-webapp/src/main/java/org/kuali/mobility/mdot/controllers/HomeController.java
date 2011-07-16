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
import org.kuali.mobility.util.HttpUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Controller 
@RequestMapping("/")
public class HomeController {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(HomeController.class);
	
    @RequestMapping(method = RequestMethod.GET)
    public String getList(HttpServletRequest request, Model uiModel) {      
    	HomeScreen home = new HomeScreen();

    	String json = HttpUtil.stringFromUrl(request.getScheme() + "://" + request.getRemoteHost() + ":" + request.getServerPort() + request.getContextPath() + "/testdata/home.json");           
        if (json != null && !"".equals(json.trim())) {
            home = new JSONDeserializer<HomeScreen>().use(null, HomeScreen.class).deserialize(json);
        }
        
        uiModel.addAttribute("home", home);
    	
    	return "index";
    }

    @RequestMapping(value = "home.json", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public String getHomeScreenJson() {
    	HomeScreen home = new HomeScreen();
    	home.setPrincipalId("1234");
    	home.setPrincipalName("natjohns");
    	List<Tool> tools = new ArrayList<Tool>();
    	Tool itnotices = new Tool();
    	itnotices.setBadgeCount("0");
    	itnotices.setDescription("Information about campus IT services.");
    	itnotices.setIconUrl("srvc-itnotice.png");
    	itnotices.setTitle("IT Notices");
    	itnotices.setUrl("/itnotices");
    	tools.add(itnotices);
    	Tool myclasses = new Tool();
    	myclasses.setBadgeCount("2");
    	myclasses.setDescription("Class information; forums, grades, etc.");
    	myclasses.setIconUrl("srvc-myclasses.png");
    	myclasses.setTitle("My Classes");
    	myclasses.setUrl("/myclasses");
    	tools.add(myclasses);    	
    	home.setTools(tools);
    	return new JSONSerializer().exclude("*.class").include("tools").serialize(home);
    }
    
}
