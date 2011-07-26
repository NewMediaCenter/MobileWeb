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

import org.kuali.mobility.mdot.entity.Backdoor;
import org.kuali.mobility.shared.Constants;
import org.kuali.mobility.user.entity.User;
import org.kuali.mobility.user.entity.UserImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller 
@RequestMapping("/backdoor")
public class BackdoorController {
        
    @RequestMapping(method = RequestMethod.GET)
    public String backdoor(HttpServletRequest request, Model uiModel) {
    	Backdoor backdoor = (Backdoor) request.getSession().getAttribute(Constants.KME_BACKDOOR_USER_KEY);
    	if (backdoor != null) {
       		uiModel.addAttribute("backdoor", backdoor);	
    	} else {
    		uiModel.addAttribute("backdoor", new Backdoor());
    	}
    	return "backdoor";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String removeBackdoor(HttpServletRequest request, Model uiModel) {
    	Backdoor backdoor = (Backdoor) request.getSession().getAttribute(Constants.KME_BACKDOOR_USER_KEY);
    	if (backdoor != null) {
    		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
    		if (user != null && backdoor.getActualUser() != null) {
        		request.getSession().setAttribute(Constants.KME_USER_KEY, backdoor.getActualUser());
    		}
    		request.getSession().setAttribute(Constants.KME_BACKDOOR_USER_KEY, null);
    	}
    	return "redirect:/home";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(HttpServletRequest request, Model uiModel, @ModelAttribute("backdoor") Backdoor backdoor, BindingResult result) {
    	if (isValidQuery(backdoor, result)) {
        	User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			backdoor.setActualUser(user);
   			user = new UserImpl();
       		user.setUserId(backdoor.getUserId());
    		request.getSession().setAttribute(Constants.KME_BACKDOOR_USER_KEY, backdoor);
    		request.getSession().setAttribute(Constants.KME_USER_KEY, user);
        	return "redirect:/home"; 
        } else {
        	return "backdoor";    	
        }
    }
    
    private boolean isValidQuery(Backdoor backdoor, BindingResult result) {
    	boolean hasErrors = false;
    	Errors errors = ((Errors) result);
    	if (backdoor.getUserId() == null || "".equals(backdoor.getUserId().trim())) {
    		errors.rejectValue("userId", "", "Please enter a username");
    		hasErrors = true;
    	}
    	return !hasErrors;
    }
    
}
