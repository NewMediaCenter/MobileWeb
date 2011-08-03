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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.PartialResultException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import edu.iu.uis.sit.util.directory.AdsConnectionFactory;
import edu.iu.uis.sit.util.directory.AdsPerson;
import edu.iu.uis.sit.util.directory.Constants;
import edu.iu.uis.sit.util.directory.IUEduCampusPersonAffiliation;
import edu.iu.uis.sit.util.directory.IUEduInstitution;
import edu.iu.uis.sit.util.directory.IUEduJob;

public class AddressBookAdsHelper {
	
	private org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AddressBookAdsHelper.class.getName());
	
//	public static final String AD_HOST_SHORT_NAME = "ads";
//	public static final String SAM_ACCOUNT_NAME = "sAMAccountName";
//	public static final String LEFT_PAREN = "(";
//	public static final String RIGHT_PAREN = ")";
//	public static final String EQUAL = "=";
	
	private String username = "";

	private String password = "";

	private int port = 0;

	public AddressBookAdsHelper(String username, String password) {
		setUsername(username);
		setPassword(password);
	}

	public AddressBookAdsHelper(String username, String password, int port) {
		setUsername(username);
		setPassword(password);
		setPort(port);
	}
	
	public List<AdsPerson> getAdsPersonsReturnedAttributes(Map<String, String> keyValues, String[] returnedAttributes, int resultLimit) throws AuthenticationException, NamingException {
		return getAdsPersons(keyValues, false, 0, returnedAttributes, resultLimit);
	}
	
	private List<AdsPerson> getAdsPersons(Map<String, String> keyValues, boolean retrieveGroups, int maximumGroupDepth, String[] returnedAttributes, int resultLimit) throws AuthenticationException, NamingException {
		DirContext ctx = AdsConnectionFactory.getDirContext(username, password, port);
		List<AdsPerson> adsPersons = new ArrayList<AdsPerson>();
		try {
			String[] attributesToGet = null;
			if (retrieveGroups) {
//				if (returnedAttributes != null && returnedAttributes.length > 0) {
//					attributesToGet = new String[returnedAttributes.length + 5];
//
//					for (int i = 0; i < returnedAttributes.length; i++) {
//						attributesToGet[i] = returnedAttributes[i];
//					}
//					attributesToGet[returnedAttributes.length] = "memberOf";
//					attributesToGet[returnedAttributes.length + 1] = "memberOf;range=0-999";
//					attributesToGet[returnedAttributes.length + 2] = "cn";
//					attributesToGet[returnedAttributes.length + 3] = "objectCategory";
//					attributesToGet[returnedAttributes.length + 4] = "distinguishedname";
//				} else {
//					attributesToGet = new String[23];
//					attributesToGet[0] = "mail";
//					attributesToGet[1] = "sn";
//					attributesToGet[2] = "givenName";
//					attributesToGet[3] = "displayName";
//					attributesToGet[4] = "department";
//					attributesToGet[5] = "eduPersonAffiliation";
//					attributesToGet[6] = "eduPersonNickname";
//					attributesToGet[7] = "iuEduPSEMPLID";
//					attributesToGet[8] = "iuEduCardID";
//					attributesToGet[9] = "iuEduUUID";
//					attributesToGet[10] = "ou";
//					attributesToGet[11] = "sAMAccountName";
//					attributesToGet[12] = "iuEduPersonAffiliation";
//					attributesToGet[13] = "iuEduPrimaryStudentAffiliation";
//					attributesToGet[14] = "iuEduJobs";
//					attributesToGet[15] = "iuEduInstitutions";
//					attributesToGet[16] = "iuEduPersonCampusAffiliation";
//					attributesToGet[17] = "iuEduFERPAMask";
//					attributesToGet[18] = "memberOf";
//					attributesToGet[19] = "memberOf;range=0-999";
//					attributesToGet[20] = "cn";
//					attributesToGet[21] = "objectCategory";
//					attributesToGet[22] = "distinguishedname";
//				}
			} else {
				if (returnedAttributes != null && returnedAttributes.length > 0) {
					attributesToGet = returnedAttributes;
				} else {
					attributesToGet = new String[18];
					attributesToGet[0] = "mail";
					attributesToGet[1] = "sn";
					attributesToGet[2] = "givenName";
					attributesToGet[3] = "displayName";
					attributesToGet[4] = "department";
					attributesToGet[5] = "eduPersonAffiliation";
					attributesToGet[6] = "eduPersonNickname";
					attributesToGet[7] = "iuEduPSEMPLID";
					attributesToGet[8] = "iuEduCardID";
					attributesToGet[9] = "iuEduUUID";
					attributesToGet[10] = "ou";
					attributesToGet[11] = "sAMAccountName";
					attributesToGet[12] = "iuEduPersonAffiliation";
					attributesToGet[13] = "iuEduPrimaryStudentAffiliation";
					attributesToGet[14] = "iuEduJobs";
					attributesToGet[15] = "iuEduInstitutions";
					attributesToGet[16] = "iuEduPersonCampusAffiliation";
					attributesToGet[17] = "iuEduFERPAMask";
				}
			}
			if (keyValues != null && keyValues.size() == 1 && keyValues.get(Constants.SAM_ACCOUNT_NAME) != null) {
				Attributes userAttributes = ctx.getAttributes("cn=" + keyValues.get(Constants.SAM_ACCOUNT_NAME) + ",ou=Accounts,dc=" + Constants.AD_HOST_SHORT_NAME + ",dc=iu,dc=edu", attributesToGet);
				if (userAttributes != null) {
					AdsPerson adsPerson = new AdsPerson();
					adsPerson.copyAllAttributes(userAttributes);
					populate(adsPerson);
					if (retrieveGroups) {
//						populateGroups(adsPerson, userAttributes, ctx, maximumGroupDepth);
					}
					adsPersons.add(adsPerson);
				}
			} else {
				SearchControls ctls = new SearchControls();
				ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
				ctls.setReturningAttributes(attributesToGet);
				String filter = "(&(objectClass=user)";
				for (Iterator<Map.Entry<String, String>> iterator = keyValues.entrySet().iterator(); iterator.hasNext();) {
					Map.Entry<String, String> entry = iterator.next();
					filter += Constants.LEFT_PAREN + entry.getKey() + Constants.EQUAL + entry.getValue() + Constants.RIGHT_PAREN;
				}
				filter += Constants.RIGHT_PAREN;
				int count = 0;
				try {
					NamingEnumeration<SearchResult> answer = ctx.search("ou=Accounts,dc=" + Constants.AD_HOST_SHORT_NAME + ",dc=iu,dc=edu", filter, ctls);
					if (answer != null && answer.hasMore()) {
						while (answer.hasMore() && count < resultLimit) {
							count++;
							AdsPerson adsPerson = null;
							SearchResult sr = answer.next();
							Attributes userAttributes = sr.getAttributes();
							if (userAttributes != null) {
								adsPerson = new AdsPerson();
								adsPerson.copyAllAttributes(userAttributes);
								populate(adsPerson);
								if (retrieveGroups) {
//									populateGroups(adsPerson, userAttributes, ctx, maximumGroupDepth);
								}
								adsPersons.add(adsPerson);
							}
						}
					}
				} catch (PartialResultException pre) {
					LOG.info("PartialResultException at count: " + count);
				} catch (NamingException e) {
					LOG.info("NamingException at count: " + count);
					throw e;
				}
			}
		} finally {
			ctx.close();
			
		}

		return adsPersons;
	}
	
	@SuppressWarnings("unchecked")
	private void populate(AdsPerson adsPerson) {
		adsPerson.setDisplayName(convertSingleValue(adsPerson.getAttribute("displayName")));
		adsPerson.setEduPersonAffiliation(convertMultiValue(adsPerson.getAttribute("eduPersonAffiliation")));
		adsPerson.setEduPersonNickname(convertSingleValue(adsPerson.getAttribute("eduPersonNickname")));
		adsPerson.setGivenName(convertSingleValue(adsPerson.getAttribute("givenName")));
		adsPerson.setIuEduPSEMPLID(convertSingleValue(adsPerson.getAttribute("iuEduPSEMPLID")));
		adsPerson.setIuEduCardID(convertMultiValue(adsPerson.getAttribute("iuEduCardID")));
		adsPerson.setIuEduUUID(convertSingleValue(adsPerson.getAttribute("iuEduUUID")));
		adsPerson.setMail(convertSingleValue(adsPerson.getAttribute("mail")));
		adsPerson.setOu(convertSingleValue(adsPerson.getAttribute("ou")));
		adsPerson.setSn(convertSingleValue(adsPerson.getAttribute("sn")));
		adsPerson.setUid(convertSingleValue(adsPerson.getAttribute("sAMAccountName")));
		adsPerson.setIuEduPersonAffiliation(convertMultiValue(adsPerson.getAttribute("iuEduPersonAffiliation")));
		adsPerson.setIuEduPrimaryStudentAffiliation(convertSingleValue(adsPerson.getAttribute("iuEduPrimaryStudentAffiliation")));

		List<IUEduCampusPersonAffiliation> iuEduCampusPersonAffiliations = new ArrayList<IUEduCampusPersonAffiliation>();
		if (adsPerson.getAttribute("iuEduPersonCampusAffiliation") != null) {
			for (Iterator<String> iterator2 = convertMultiValue(adsPerson.getAttribute("iuEduPersonCampusAffiliation")).iterator(); iterator2.hasNext();) {
				String campusPersonAffiliation = (String) iterator2.next();
				String[] campusPerson = campusPersonAffiliation.split(":");
				iuEduCampusPersonAffiliations.add(new IUEduCampusPersonAffiliation(campusPerson[0], campusPerson[1]));
			}
		}
		adsPerson.setIuEduCampusPersonAffiliations(iuEduCampusPersonAffiliations);

		List<IUEduInstitution> iuEduInstitutions = new ArrayList<IUEduInstitution>();
		if (adsPerson.getAttribute("iuEduInstitutions") != null) {
			for (Iterator<String> iterator2 = convertMultiValue(adsPerson.getAttribute("iuEduInstitutions")).iterator(); iterator2.hasNext();) {
				String institution = iterator2.next();
				iuEduInstitutions.add(new IUEduInstitution(institution));
			}
		}
		adsPerson.setIuEduInstitutions(iuEduInstitutions);

		List<IUEduJob> iuEduJobs = new ArrayList<IUEduJob>();
		if (adsPerson.getAttribute("iuEduJobs") != null) {
			for (Iterator<String> iterator2 = convertMultiValue(adsPerson.getAttribute("iuEduJobs")).iterator(); iterator2.hasNext();) {
				String eduJob = iterator2.next();
				IUEduJob iuEduJob = new IUEduJob(eduJob);
				if (iuEduJob.isPrimary()) {
					adsPerson.setIuEduEmployeePrimaryAppointmentType(iuEduJob.getAppointmentType());
					adsPerson.setIuEduEmployeePrimaryChart(iuEduJob.getChart());
					adsPerson.setIuEduEmployeePrimaryOrg(iuEduJob.getOrg());
				}
				iuEduJobs.add(iuEduJob);
			}
		}
		adsPerson.setIuEduJobs(iuEduJobs);

		Integer ferpa = convertSingleValueInt(adsPerson.getAttribute("iuEduFERPAMask"));
		if (ferpa != null) {
			adsPerson.setIuEduFERPAMask(ferpa.intValue());
		}
	}
	
	private static Integer convertSingleValueInt(Object attribute) {
		if (attribute != null && attribute instanceof List && ((List) attribute).size() == 1) {
			return new Integer((String) (((List) attribute).get(0)));
		}
		return null;
	}

	private static String convertSingleValue(Object attribute) {
		if (attribute != null && attribute instanceof List && ((List) attribute).size() == 1) {
			return (String) ((List) attribute).get(0);
		}
		return null;
	}

	private static List convertMultiValue(Object attribute) {
		if (attribute != null && attribute instanceof List) {
			return (List) attribute;
		}
		return null;
	}
	
	// standard attribute getters and setters

	private void setPassword(String password) {
		this.password = password;
	}

	private void setUsername(String username) {
		this.username = username;
	}

	private void setPort(int port) {
		this.port = port;
	}
	
}
