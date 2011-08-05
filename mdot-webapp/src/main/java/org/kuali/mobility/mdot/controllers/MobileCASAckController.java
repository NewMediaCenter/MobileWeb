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

import javax.servlet.http.HttpServletRequest;

import org.kuali.mobility.shared.Constants;
import org.kuali.mobility.user.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller 
@RequestMapping("/mobileCasAck")
public class MobileCASAckController {
        
    @RequestMapping(method = RequestMethod.GET)
    public String backdoor(HttpServletRequest request, Model uiModel) {
    	return "mobileCasAck";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(HttpServletRequest request, Model uiModel, @ModelAttribute("command") Object command, BindingResult result) {
    	User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
    	String service = user.getUserAttribute("service");
    	user.removeUserAttribute("service");
    	user.setUserAttribute("acked", "true");
    	return "redirect:" + service; 
    }

}
