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

package org.kuali.mobility.askiu.controllers;

import org.kuali.mobility.askiu.entity.AskIU;
import org.kuali.mobility.askiu.service.AskIUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller 
@RequestMapping("/askiu")
public class AskIUController {
    
    @Autowired
    private AskIUService askiuService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String getList(Model uiModel) {
    	try {
    		uiModel.addAttribute("askiu", new AskIU());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return "askiu/form";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String submitFeedback(Model uiModel, @ModelAttribute("askiu") AskIU ask, BindingResult result) {
        if (isValidQuery(ask, result)) {
        	if (askiuService.postQuery(ask)) {
        		return "askiu/confirmation"; 
        	} else {
        		Errors errors = ((Errors) result);
        		errors.rejectValue("message", "", "There was an error sending your query.  Please try again later.");
        		return "askiu/form"; 
        	}
        } else {
        	return "askiu/form";    	
        }
    }
    
    private boolean isValidQuery(AskIU ask, BindingResult result) {
    	boolean hasErrors = false;
    	Errors errors = ((Errors) result);
    	if (ask.getMessage() == null || "".equals(ask.getMessage().trim())) {
    		errors.rejectValue("message", "", "Please type a message into the input box.");
    		hasErrors = true;
    	}
    	if (ask.getEmail() == null || "".equals(ask.getEmail().trim()) || !askiuService.isValidEmail(ask.getEmail())) {
    		errors.rejectValue("email", "", "Please enter a valid email address.");
    		hasErrors = true;
    	}
    	return !hasErrors;
    }
    
    public void setAskIUService(AskIUService askiuService) {
        this.askiuService = askiuService;
    }    
}
