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

package org.kuali.mobility.alerts.controllers;

import java.util.List;

import org.kuali.mobility.alerts.entity.Alert;
import org.kuali.mobility.alerts.service.AlertsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller 
@RequestMapping("/alerts")
public class AlertsController {
    
    @Autowired
    private AlertsService alertsService;
    public void setAlertsService(AlertsService alertsService) {
        this.alertsService = alertsService;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String getList(Model uiModel) {
    	try {
    		List<Alert> alerts = alertsService.findAllAlertsByCampus("BL");
    		uiModel.addAttribute("alerts", alerts);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return "alerts";
    }
    
}
