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

package org.kuali.mobility.itnotices.controllers;

import java.util.List;

import org.kuali.mobility.itnotices.entity.ITNotice;
import org.kuali.mobility.itnotices.service.ITNoticesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 
@RequestMapping("/itnotices")
public class ITNoticesController {

    @Autowired
    private ITNoticesService itNoticesService;
    public void setComputerLabsService(ITNoticesService itNoticesService) {
        this.itNoticesService = itNoticesService;
    }
	
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public String findAllComputerLabsByCampus(@RequestParam(value = "campus", required = true) String campus) {
    	String value = "";
    	try {
        	List<ITNotice> notices = itNoticesService.findAllITNotices();
        	value = itNoticesService.toJson(notices);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return value;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String getList(Model uiModel) {
    	try {
    		List<ITNotice> notices = itNoticesService.findAllITNotices();
    		uiModel.addAttribute("notices", notices);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return "itnotices/list";
    }
    
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public String getDetails(Model uiModel, @RequestParam(required = true) int id) {
        try {
            List<ITNotice> notices = itNoticesService.findAllITNotices();
            uiModel.addAttribute("notice", notices.get(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "itnotices/details";
    }
     
}
