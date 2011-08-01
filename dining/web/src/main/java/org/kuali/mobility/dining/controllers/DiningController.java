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

package org.kuali.mobility.dining.controllers;

import java.util.List;

import org.kuali.mobility.dining.entity.Menu;
import org.kuali.mobility.dining.service.DiningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller 
@RequestMapping("/dining")
public class DiningController {
    
    @Autowired
    private DiningService diningService;
    public void setDiningService(DiningService diningService) {
        this.diningService = diningService;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String getList(Model uiModel) {
    	List<Menu> menus = diningService.getMenus("SE");
    	uiModel.addAttribute("menus", menus);
    	return "dining/list";
    }
}
