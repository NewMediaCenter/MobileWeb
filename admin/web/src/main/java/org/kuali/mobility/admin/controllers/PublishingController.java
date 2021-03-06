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

package org.kuali.mobility.admin.controllers;

import org.kuali.mobility.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller 
@RequestMapping("/publishing")
public class PublishingController {
    
    @Autowired
    private AdminService adminService;
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }
    
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(Model uiModel) {

    	return "publishing/index";
    }

    @RequestMapping(value = "tool", method = RequestMethod.GET)
    public String tool(Model uiModel) {

    	return "publishing/tool";
    }

    @RequestMapping(value = "layout", method = RequestMethod.GET)
    public String layout(Model uiModel) {

    	return "publishing/layout";
    }

}
