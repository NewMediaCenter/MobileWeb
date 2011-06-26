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
 
package org.kuali.mobility.sakai.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.kuali.mobility.sakai.entity.SakaiRoster;

public class SakaiRosterParser {

	public List<SakaiRoster> parseRoster(String json) {
		List<SakaiRoster> roster = new ArrayList<SakaiRoster>();
		roster = parse(false, json);
		return roster;
	}
	
	private List<SakaiRoster> parse(boolean convert, String json) {
    	List<SakaiRoster> roster = new ArrayList<SakaiRoster>();
    	try {
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("participant_collection");
            for (int i = 0; i < itemArray.size(); i++) {
            	String displayID = itemArray.getJSONObject(i).getString("displayID");
                String displayName = itemArray.getJSONObject(i).getString("displayName");
                String firstName = itemArray.getJSONObject(i).getString("firstName");
                String lastName = itemArray.getJSONObject(i).getString("lastName");
                String nickName = itemArray.getJSONObject(i).getString("nickName");
                String department = itemArray.getJSONObject(i).getString("department");
                String email = itemArray.getJSONObject(i).getString("email");
                String homePage = itemArray.getJSONObject(i).getString("homePage");
                String homePhone = itemArray.getJSONObject(i).getString("homePhone");
                String workPhone = itemArray.getJSONObject(i).getString("workPhone");
                String position = itemArray.getJSONObject(i).getString("position");
                String roleTitle = itemArray.getJSONObject(i).getString("roleTitle");
                String room = itemArray.getJSONObject(i).getString("room");
                String school = itemArray.getJSONObject(i).getString("school");
                String otherInformation = itemArray.getJSONObject(i).getString("otherInformation");
                String entityReference = itemArray.getJSONObject(i).getString("entityReference");
                String entityURL = itemArray.getJSONObject(i).getString("entityURL");
                
                SakaiRoster trs = new SakaiRoster();
                trs.setDisplayID(displayID);
                trs.setDisplayName(displayName);
                trs.setFirstName(firstName);
                trs.setLastName(lastName);
                trs.setNickName(nickName);
                trs.setDepartment(department);
                trs.setEmail(email);
                trs.setHomePage(homePage);
                trs.setHomePhone(homePhone);
                trs.setWorkPhone(workPhone);
                trs.setPosition(position);
                trs.setRoleTitle(roleTitle);
                trs.setRoom(room);
                trs.setSchool(school);
                trs.setOtherInformation(otherInformation);
                trs.setEntityReference(entityReference);
                trs.setEntityURL(entityURL);
                
                roster.add(trs);
            }
    	} catch (JSONException e) {
        }
		return roster;
    }
	
	public List<SakaiRoster> parseParticipantDetails(String json, String participantDisplayId) {
    	List<SakaiRoster> roster = new ArrayList<SakaiRoster>();
    	try {
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("participant_collection");
            for (int i = 0; i < itemArray.size(); i++) {
            	String displayID = itemArray.getJSONObject(i).getString("displayID");
            	if(!participantDisplayId.equalsIgnoreCase(displayID)){
            		continue;
            	}
                String displayName = itemArray.getJSONObject(i).getString("displayName");
                String firstName = itemArray.getJSONObject(i).getString("firstName");
                String lastName = itemArray.getJSONObject(i).getString("lastName");
                String nickName = itemArray.getJSONObject(i).getString("nickName");
                String department = itemArray.getJSONObject(i).getString("department");
                String email = itemArray.getJSONObject(i).getString("email");
                String homePage = itemArray.getJSONObject(i).getString("homePage");
                String homePhone = itemArray.getJSONObject(i).getString("homePhone");
                String workPhone = itemArray.getJSONObject(i).getString("workPhone");
                String position = itemArray.getJSONObject(i).getString("position");
                String roleTitle = itemArray.getJSONObject(i).getString("roleTitle");
                String room = itemArray.getJSONObject(i).getString("room");
                String school = itemArray.getJSONObject(i).getString("school");
                String otherInformation = itemArray.getJSONObject(i).getString("otherInformation");
                String entityReference = itemArray.getJSONObject(i).getString("entityReference");
                String entityURL = itemArray.getJSONObject(i).getString("entityURL");
                
                SakaiRoster trs = new SakaiRoster();
                trs.setDisplayID(displayID);
                trs.setDisplayName(displayName);
                trs.setFirstName(firstName);
                trs.setLastName(lastName);
                trs.setNickName(nickName);
                trs.setDepartment(department);
                trs.setEmail(email);
                trs.setHomePage(homePage);
                trs.setHomePhone(homePhone);
                trs.setWorkPhone(workPhone);
                trs.setPosition(position);
                trs.setRoleTitle(roleTitle);
                trs.setRoom(room);
                trs.setSchool(school);
                trs.setOtherInformation(otherInformation);
                trs.setEntityReference(entityReference);
                trs.setEntityURL(entityURL);
                
                roster.add(trs);
            }
    	} catch (JSONException e) {
        }
		return roster;
    }
}
