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

package org.kuali.mobility.people.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.mobility.people.entity.Person;
import org.kuali.mobility.people.entity.Search;
import org.kuali.mobility.people.service.PeopleService;
import org.kuali.mobility.shared.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller 
@RequestMapping("/people")
public class PeopleController {
    
    @Autowired
    private PeopleService peopleService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String getSearchForm(Model uiModel) {
    	uiModel.addAttribute("search", new Search());
    	uiModel.addAttribute("locations", Constants.CAMPUS_NAMES);
    	
    	Map<String, String> statusTypes = new HashMap<String, String>();
    	statusTypes.put("Employee", "Employee");
    	statusTypes.put("Faculty", "Faculty");
    	statusTypes.put("Student", "Student");
    	
    	uiModel.addAttribute("statusTypes", statusTypes);
    	
    	return "people/form";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String postSearchForm(Model uiModel, @ModelAttribute("search") Search search, BindingResult result) {
    	if (validateSearch(search, result)) {
    		List<Person> people = peopleService.performSearch(search);
    		uiModel.addAttribute("people", people);
    		uiModel.addAttribute("search", search);
    		return "people/list";
    	} else {
    		return "people/form";
    	}
    }
    
    @RequestMapping(value="/{userName}", method = RequestMethod.GET)
    public String getUserDetails(Model uiModel, @PathVariable("userName") String userName, @RequestParam("lName") String searchLastName,
    		@RequestParam("fName") String searchFirstName, @RequestParam("uName") String searchUserName,
    		@RequestParam("exact") String searchExactness, @RequestParam("status") String searchStatus,
    		@RequestParam("location") String searchLocation) {
    	Search search = new Search();
    	search.setLastName(searchLastName);
    	search.setFirstName(searchFirstName);
    	search.setUserName(searchUserName);
    	search.setExactness(searchExactness);
    	search.setLocation(searchLocation);
    	search.setStatus(searchStatus);
    	
    	Person p = peopleService.getUserDetails(userName);
    	uiModel.addAttribute("person", p);
		uiModel.addAttribute("search", search);
    	return "people/details";
    }
    
    private boolean validateSearch(Search search, BindingResult result) {
    	boolean hasErrors = false;
    	Errors errors = ((Errors) result);
    	if ((search.getLastName() == null || search.getLastName().trim().isEmpty()) &&
    			(search.getFirstName() == null || search.getFirstName().trim().isEmpty()) &&
    			(search.getUserName() == null || search.getUserName().trim().isEmpty())) {
    		errors.rejectValue("lastName", "", "You must provide at least one letter of the name or the entire username of the person you are searching for.");
    		hasErrors = true;
    	}
    	return !hasErrors;
    }
    
    
    public void setPeopleService(PeopleService peopleService) {
        this.peopleService = peopleService;
    }
}
