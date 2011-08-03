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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.mobility.configparams.service.ConfigParamService;
import org.springframework.beans.factory.annotation.Autowired;

import edu.iu.uis.sit.util.directory.AdsPerson;

public class AdsServiceImpl implements AdsService {
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AdsServiceImpl.class);
	
	@Autowired
	private ConfigParamService configParamService;
	
	private String adsUsername;
	private String adsPassword;
	private static AddressBookAdsHelper adsHelper;
	private int defaultResultLimit;
	
	public AdsServiceImpl() {
		this.defaultResultLimit = 75;
	}
	
	private AddressBookAdsHelper getAdsHelper() {
		if (adsHelper == null) {
			try {
				AddressBookAdsHelper addressBookAdsHelper = new AddressBookAdsHelper(this.adsUsername, this.adsPassword);
				adsHelper = addressBookAdsHelper;
			} catch (Exception e) {
				LOG.error("error creating adsHelper: ", e);
			}
		}
		return adsHelper;
	}
	
	private int getCachedResultLimit() {
		Integer limit = new Integer(this.defaultResultLimit);
		try {
			String configParam = configParamService.findValueByName("PEOPLE_RESULT_LIMIT_ADS");
			if (configParam != null && !"".equals(configParam)) {
				limit = new Integer(configParam);
			}
		} catch (Exception e) {
			LOG.error("Configuration Parameter: PEOPLE_RESULT_LIMIT_ADS must exist and be a number. Using: " + limit, e);
		}
		return limit;
	}
	
	public AdsPerson getAdsPerson(String username) throws Exception {
		/*
		 * Any filtering done in getAdsPersons needs to be done here as well
		 */
		AddressBookAdsHelper helper = getAdsHelper();
		
		String[] returnedAttributes = { "cn", "givenName", "sn", "telephoneNumber", "iuEduPersonAffiliation", "mail", "ou", "physicalDeliveryOfficeName", "iuEduJobs", "iuEduCurrentlyEnrolled", "iuEduPrimaryStudentAffiliation" };
		Map<String, String> keyValues = new HashMap<String, String>();
		keyValues.put("cn", username);
		keyValues.put("msExchHideFromAddressLists", "FALSE");
		List<AdsPerson> adsPersons = helper.getAdsPersonsReturnedAttributes(keyValues, returnedAttributes, 1);
		AdsPerson adsPerson = null;
		if (adsPersons.size() > 0) {
			adsPerson = adsPersons.get(0);
		}
		
		return adsPerson;
	}

	public List<AdsPerson> getAdsPersons(String last, String first, String status, String campus, boolean isExactLastName, int resultLimit) throws Exception {
		AddressBookAdsHelper helper = getAdsHelper();
		String[] returnedAttributes = { "cn", "iuEduPersonAffiliation", "displayName", "ou", "iuEduPSEMPLID", "iuEduCurrentlyEnrolled", "iuEduPrimaryStudentAffiliation", "iuEduFERPAMask", "givenName", "sn" };
		Map<String, String> keyValues = new HashMap<String, String>();
		if (last != null && last.length() > 0) {
			if (isExactLastName) {
				keyValues.put("sn", last);
			} else {
				keyValues.put("sn", last + "*");
			}
		}
		if (first != null && first.length() > 0) {
			keyValues.put("givenName", first + "*");
		}
		if (campus != null && !campus.equals("Any")) {
			keyValues.put("ou", campus);
		}
		if (status != null && !status.equals("Any")) {
			if (status.equals("Faculty")) {
				keyValues.put("iuEduPersonAffiliation", "faculty");
			} else if (status.equals("Student")) {
				keyValues.put("|(iuEduPersonAffiliation=undergraduate)(iuEduPersonAffiliation=graduate)(iuEduPersonAffiliation","professional)");
			} else if (status.equals("Employee")) {
				keyValues.put("|(iuEduPersonAffiliation=regular hourly)(iuEduPersonAffiliation=student hourly)(iuEduPersonAffiliation=retired staff)(iuEduPersonAffiliation", "staff)");
			}
		}
		keyValues.put("iuEduPSEMPLID", "*");
		keyValues.put("msExchHideFromAddressLists", "FALSE");
				
		return helper.getAdsPersonsReturnedAttributes(keyValues, returnedAttributes, resultLimit);
	}

	public void setAdsUsername(String adsUsername) {
		this.adsUsername = adsUsername;
	}

	public void setAdsPassword(String adsPassword) {
		this.adsPassword = adsPassword;
	}

	public int getResultLimit() {
		return this.getCachedResultLimit();
	}

	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
}
