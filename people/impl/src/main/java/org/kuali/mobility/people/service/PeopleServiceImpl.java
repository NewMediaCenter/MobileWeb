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
 
package org.kuali.mobility.people.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.kuali.mobility.people.entity.Person;
import org.kuali.mobility.people.entity.Search;
import org.kuali.mobility.shared.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.iu.uis.sit.util.directory.AdsPerson;
import edu.iu.uis.sit.util.directory.IUEduJob;

@Service
public class PeopleServiceImpl implements PeopleService {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PeopleServiceImpl.class);
	
	@Autowired
	private AdsService adsService;
	
	@Override
	public List<Person> performSearch(Search search) {
		List<Person> persons = null;
		int resultLimit = adsService.getResultLimit();
		try {
			List<AdsPerson> adsPersons = this.getAdsService().getAdsPersons(search.getLastName(), search.getFirstName(), search.getStatus(), search.getLocation(), search.isExactLastName(), resultLimit);
//			int initialResultSize = adsPersons.size();
			// Filtering will remove users that should not be displayed
			this.filterAdsPersons(adsPersons);
			persons = this.convertAdsPersons(adsPersons);
//			if (initialResultSize >= resultLimit) {
////				search.setErrorMessage("Too many results, showing first " + persons.size() + ":");
////				search.setErrorMessage("Search Results - Too many results");
//				search.setErrorMessage("Too many results. Showing first group:");
//			}
////			if (search.getErrorMessage() != null) {
////				LOG.debug("Results filtered to: " + persons.size());
////			}
		} catch (Exception e) {
			LOG.error("Could not find users: " + search.getLastName() + " " + search.getFirstName() + " " + search.getStatus() + " " + search.getLocation() + " " + search.isExactLastName(), e);
		}
		Collections.sort(persons, new PersonSort());
		if (persons.size() > resultLimit) {
			return persons.subList(0, resultLimit - 1);
		}
		return persons;
	}
	
	@Override
	public Person getUserDetails(String userName) {
		AdsPerson adsPerson;
		try {
			adsPerson = adsService.getAdsPerson(userName);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
		Person p = new Person();
		if (convertAdsPerson(adsPerson, p)) {
			return p;
		} else {
			return null;
		}
	}
	
	private void filterAdsPersons(List<AdsPerson> adsPersons) {
		AdsPerson adsPerson = null;
		boolean remove;
		for (Iterator<AdsPerson> i = adsPersons.iterator(); i.hasNext(); ) {
			remove = false;
			adsPerson = i.next();
			int ferpa = adsPerson.getIuEduFERPAMask();
			if (ferpa > 0 && ((ferpa & 31) != 0 || (ferpa & 121) != 0)) {
				remove = true;
//				LOG.info("Skipping entry for " + this.getStringAttribute(adsPerson, "cn") + " due to FERPA restriction " + ferpa);
			}
			if (remove) {
				i.remove();
			}
		}
	}
	
	private List<Person> convertAdsPersons(List<AdsPerson> adsPersons) {
		List<Person> persons = new ArrayList<Person>();
		Person person = null;
		for (AdsPerson adsPerson : adsPersons) {
			person = new Person();
			if (this.convertAdsPerson(adsPerson, person)) {
				persons.add(person);
			}
		}
		return persons;
	}
	
	@SuppressWarnings("unchecked")
	private boolean convertAdsPerson(AdsPerson adsPerson, Person person) {
		person.setFirstName(adsPerson.getGivenName());
		person.setLastName(adsPerson.getSn());
		person.setEmail(adsPerson.getMail());
		person.setPhone(this.getStringAttribute(adsPerson, "telephoneNumber"));
		person.setAddress(this.getStringAttribute(adsPerson, "physicalDeliveryOfficeName"));
		person.setUserName(this.getStringAttribute(adsPerson, "cn"));
		person.setDisplayName(this.getStringAttribute(adsPerson, "displayName"));
		if (adsPerson.getAttribute("ou") != null && adsPerson.getAttribute("ou") instanceof ArrayList) {
			for (String ou : (ArrayList<String>) adsPerson.getAttribute("ou")) {
				person.getLocations().add(this.convertCampusCode(ou));
			}
		}
		if (adsPerson.getIuEduJobs() != null) {
			for (IUEduJob job : (List<IUEduJob>) adsPerson.getIuEduJobs()) {
				person.getDepartments().add(this.toProperCase(job.getDepartment()));
			}			
		}
		if (adsPerson.getIuEduPersonAffiliation() != null) {
//			for (String affiliation : (List<String>) adsPerson.getIuEduPersonAffiliation()) {
//				person.getAffiliations().add(affiliation);
//			}
			StringBuffer b = new StringBuffer();
			for (String affiliation : (List<String>) adsPerson.getIuEduPersonAffiliation()) {
				b.append(affiliation + "-");
			}
			String s = b.toString().toLowerCase();
			if (s.indexOf("hourly") > -1 || s.indexOf("staff") > -1) {
				person.getAffiliations().add("Employee");
			} 
			if (s.indexOf("faculty") > -1) {
				person.getAffiliations().add("Faculty");
			}
			if (s.indexOf("affiliate") > -1) {
				person.getAffiliations().add("Affiliate");
			}
			if ((s.indexOf("graduate") > -1 || s.indexOf("professional") > -1) && 
			"Enrolled".equals(this.getStringAttribute(adsPerson, "iuEduPrimaryStudentAffiliation")) && 
			"Y".equals(this.getStringAttribute(adsPerson, "iuEduCurrentlyEnrolled"))) {
				person.getAffiliations().add("Student");
			}
		}
		if (person.getAffiliations().size() < 1) {
//			LOG.info("Skipping entry for " + this.getStringAttribute(adsPerson, "cn") + " due to no remaining affiliations. " + this.getStringAttribute(adsPerson, "displayName"));
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private String getStringAttribute(AdsPerson adsPerson, String attributeName) {
		if (adsPerson.getAttribute(attributeName) != null && adsPerson.getAttribute(attributeName) instanceof ArrayList) {
			if (((ArrayList<String>) adsPerson.getAttribute(attributeName)).size() > 0) {
				return ((ArrayList<String>) adsPerson.getAttribute(attributeName)).get(0);	
			}
		}
		return null;
	}
	
	private String convertCampusCode(String code) {
		String name = null;
		if (code != null) {
			if (Constants.CAMPUS_NAMES.containsKey(code.trim())) {
				name = Constants.CAMPUS_NAMES.get(code.trim());
			}
		}
		if (name == null) {
			name = new String(code);
		}
		return name;
	}
	
	private String toProperCase(String name) {
		if (name == null || name.trim().length() == 0) {
			return name;
		}
		
		name = name.toLowerCase();
        String[] split = name.split(" ");
        name = "";
        for (String s : split) {
        	s = s.substring(0, 1).toUpperCase() + s.substring(1);
        	name = name + s + " ";
        }
        return name.trim();
	}

	public AdsService getAdsService() {
		return adsService;
	}

	public void setAdsService(AdsService adsService) {
		this.adsService = adsService;
	}
}
