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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kuali.mobility.people.entity.Person;
import org.kuali.mobility.people.entity.Search;
import org.kuali.mobility.people.service.PeopleService;
import org.kuali.mobility.shared.Constants;
import org.kuali.mobility.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller 
@RequestMapping("/people")
public class PeopleController {
    
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PeopleController.class);
	
    @Autowired
    private PeopleService peopleService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String getSearchForm(Model uiModel) {
    	Search s = new Search();
    	s.setStatus("Any");
    	uiModel.addAttribute("search", s);
    	uiModel.addAttribute("locations", Constants.CAMPUS_NAMES);
    	
    	Map<String, String> statusTypes = new HashMap<String, String>();
    	statusTypes.put("Employee", "Employee");
    	statusTypes.put("Faculty", "Faculty");
    	statusTypes.put("Student", "Student");
    	statusTypes.put("Any", "Any Status");
    	
    	uiModel.addAttribute("statusTypes", statusTypes);
    	
    	return "people/form";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String postSearchForm(Model uiModel, @ModelAttribute("search") Search search, BindingResult result, HttpServletRequest request) {
    	if (validateSearch(search, result)) {	
    		List<Person> people = peopleService.performSearch(search);
    		uiModel.addAttribute("people", people);
    		uiModel.addAttribute("search", search);
    		
    		Map<String, String> userNameHashes = new HashMap<String, String>();
    		for (Person p : people) {
    			userNameHashes.put(p.getHashedUserName(), p.getUserName());
    		}
    		request.getSession().setAttribute("People.UserNames.Hashes", userNameHashes);
    		
    		return "people/list";
    	} else {
    		return "people/form";
    	}
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value="/{userNameHash}", method = RequestMethod.GET)
    public String getUserDetails(Model uiModel, HttpServletRequest request, @PathVariable("userNameHash") String userNameHash/*,
    		@RequestParam("lName") String searchLastName, @RequestParam("fName") String searchFirstName, @RequestParam("uName") String searchUserName,
    		@RequestParam("exact") String searchExactness, @RequestParam("status") String searchStatus, @RequestParam("location") String searchLocation*/) {
    	
    	User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
    	boolean isLoggedIn = false;
    	if (user != null) {
    		isLoggedIn = true;
    	}
    	
    	/*
    	Search search = new Search();
    	search.setLastName(searchLastName);
    	search.setFirstName(searchFirstName);
    	search.setUserName(searchUserName);
    	search.setExactness(searchExactness);
    	search.setLocation(searchLocation);
    	search.setStatus(searchStatus);
    	*/
    	
    	Map<String, String> userNameHashes = (Map<String, String>) request.getSession().getAttribute("People.UserNames.Hashes");
    	String userName = userNameHashes.get(userNameHash);
    	Person p = peopleService.getUserDetails(userName);
    	uiModel.addAttribute("person", p);
//		uiModel.addAttribute("search", search);
		uiModel.addAttribute("loggedIn", isLoggedIn);
		
		if (!isLoggedIn && p.getEmail() != null && !"".equals(p.getEmail())) {
			BufferedImage bufferedImage = peopleService.generateObfuscatedImage(p.getEmail());
			ByteArrayOutputStream os = new ByteArrayOutputStream();
		    try {
	    		ImageIO.write(bufferedImage, "png", os);
	    		String key = "People.Image.Email.";
	    		// Add an identifier to the key so heavily cached browsers do not reuse the same image for different people
	    		String hash = null;
	    		if (p.getUserName() != null) {
	    			hash = "" + Math.abs(p.getUserName().hashCode());
	    		} else {
	    			Date now = new Date();
	    			hash = "" + now.getTime();
	    		}
	    		key = key + hash;
	    		request.getSession().setAttribute(key, os.toByteArray());
	    		uiModel.addAttribute("imageKey", hash);
		    } catch (Exception ioException) {
		    	LOG.error("Error generating email image: ", ioException);
		    }
		}
    	return "people/details";
    }
    
    @RequestMapping(value="/image/{hash}", method = RequestMethod.GET)
    public void getImage(@PathVariable("hash") String imageKeyHash, Model uiModel, HttpServletRequest request, HttpServletResponse response) throws Exception {
		byte[] byteArray = (byte[]) request.getSession().getAttribute("People.Image.Email." + imageKeyHash);
		if (byteArray != null) {
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream(byteArray.length);
			baos.write(byteArray);
			if (baos != null) {
				ServletOutputStream sos = null;
				try {
					response.setContentLength(baos.size());
					sos = response.getOutputStream();
					baos.writeTo(sos);
					sos.flush();
				} catch (Exception e) {
					LOG.error("error creating image file", e);
				} finally {
					try {
						baos.close();
						sos.close();
					} catch (Exception e1) {
						LOG.error("error closing output stream", e1);
					}
				}
			}
		}
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
