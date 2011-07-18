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

package org.kuali.mobility.computerlabs.controllers;

import java.util.List;

import org.kuali.mobility.computerlabs.entity.LabLocation;
import org.kuali.mobility.computerlabs.service.ComputerLabsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 
@RequestMapping("/computerlabs")
public class ComputerLabsController {

    @Autowired
    private ComputerLabsService computerLabsService;
    public void setComputerLabsService(ComputerLabsService computerLabsService) {
        this.computerLabsService = computerLabsService;
    }
    
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public String findAllComputerLabsByCampus(@RequestParam(value = "campus", required = true) String campus) {
    	List<LabLocation> labLocations = computerLabsService.findAllLabLocationsByCampus("BL");
    	return computerLabsService.toJsonLabLocation(labLocations);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String getList(Model uiModel) {
   		List<LabLocation> labLocations = computerLabsService.findAllLabLocationsByCampus("BL");
   		uiModel.addAttribute("lablocations", labLocations);
    	return "computerlabs/list";
    }

}
