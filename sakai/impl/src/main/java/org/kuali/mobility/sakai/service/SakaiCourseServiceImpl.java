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

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONTokener;

import org.apache.commons.io.IOUtils;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.sakai.entity.SakaiAnnouncement;
import org.kuali.mobility.sakai.entity.SakaiAssignment;
import org.kuali.mobility.sakai.entity.SakaiHome;
import org.kuali.mobility.sakai.entity.SakaiRoster;
import org.kuali.mobility.sakai.entity.SakaiSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.iu.es.espd.oauth.OAuth2LegService;

@Service
public class SakaiCourseServiceImpl implements SakaiCourseService {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SakaiCourseServiceImpl.class);
	
	@Autowired
	private ConfigParamService configParamService;
	
	@Autowired
	private OAuth2LegService oncourseOAuthService;
	
	public SakaiHome findSakaiHome(String user) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "site.json";
			ResponseEntity<InputStream> is;
			is = oncourseOAuthService.oAuthGetRequest(user, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
			return parseCourses(json);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new SakaiHome();
		}
	}
	
	public SakaiSite findCourse(String siteId, String user) {
		SakaiSite course = new SakaiSite();
		String instructorName = "Not Defined";
		String instructorId = "";
		String courseDescription = "";
		String courseTitle = "";
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "site/" + siteId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(user, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
			courseDescription = jsonObj.getString("shortDescription");
			courseTitle = jsonObj.getString("title");
			
			url = configParamService.findValueByName("Sakai.Url.Base") + "participant.json?siteId=" + siteId;
			is = oncourseOAuthService.oAuthGetRequest(user, url, "text/html");
			json = IOUtils.toString(is.getBody(), "UTF-8");
			jsonObj = (JSONObject) JSONSerializer.toJSON(json);
			JSONArray itemArray = jsonObj.getJSONArray("participant_collection");
			for (int j = 0; j < itemArray.size(); j++) {
				String roleTitle = itemArray.getJSONObject(j).getString("roleTitle");
				if (roleTitle.equalsIgnoreCase("Instructor")) {
					instructorId = itemArray.getJSONObject(j).getString("displayID");
					instructorName = itemArray.getJSONObject(j).getString("displayName");
				}
			}
			
			//we might be able to get the available tools from this feed
//			url = configParamService.findValueByName("Sakai.Url.Base") + "site/" + siteId + "/pages.json";
//			is = oncourseOAuthService.oAuthGetRequest(user, url, "text/html");
//			json = IOUtils.toString(is.getBody(), "UTF-8");
//			jsonObj = (JSONObject) JSONSerializer.toJSON(json);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}	
		
		course.setId(siteId);
		course.setInstructorId(instructorId);
		course.setInstructorName(instructorName);
		course.setDescription(courseDescription);
		course.setTitle(courseTitle);
		
		return course;
	}
	
	@SuppressWarnings("unchecked")
	public SakaiHome parseCourses(String json) {
		SakaiHome home = new SakaiHome();
		Map<String,List<SakaiSite>> courses = home.getCourses();
		List<SakaiSite> projects = home.getProjects();
    	try {
			JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("site_collection");
            for (Iterator<JSONObject> iter = itemArray.iterator(); iter.hasNext();) {
            	JSONObject object = iter.next();
            	
            	String type = object.getString("type");
            	if (!"project".equals(type)) {
	                String courseDesc = object.getString("description");
	                courseDesc = courseDesc.replace("&nbsp;"," ");          
	                SakaiSite item = new SakaiSite();
	                item.setId(object.getString("entityId"));
	                item.setTitle(object.getString("title"));
	                
	                JSONObject props = (JSONObject) object.get("props");
	                String term = props.getString("term");
	                item.setTerm(term);
	                item.setDescription(courseDesc);
	                
	                List<SakaiSite> courseList = courses.get(term);
	                if (courseList == null) {
	                	courseList = new ArrayList<SakaiSite>();
	                	courses.put(term, courseList);
	                }
	                courseList.add(item);
            	} else {
            		SakaiSite project = new SakaiSite();
            		project.setId(object.getString("entityId"));
            		project.setDescription(object.getString("shortDescription"));
            		project.setTitle(object.getString("title"));
            		
            		projects.add(project);
            	}
            }
    	} catch (JSONException e) {
	    	e.printStackTrace();
	    } catch (Exception e) {
	    	e.printStackTrace();
	        //runOnUiThread(returnError);
	    }
		return home;
	}
	
	public List<SakaiAnnouncement> findAllCourseAnnouncements(String siteId, String user) {
		List<SakaiAnnouncement> anns = new ArrayList<SakaiAnnouncement>();

    	try {
    		String url = configParamService.findValueByName("Sakai.Url.Base") + "announcement/site/" + siteId + ".json";
    		ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(user, url, "text/html");
    		String json = IOUtils.toString(is.getBody(), "UTF-8");
    		
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json.toString());
            JSONArray itemArray = jsonObj.getJSONArray("announcement_collection");
            for (int i = 0; i < itemArray.size(); i++) {
                String id = itemArray.getJSONObject(i).getString("id");
                String title = itemArray.getJSONObject(i).getString("title");
                String attachments = itemArray.getJSONObject(i).getString("attachments");
                String body = itemArray.getJSONObject(i).getString("body");
                String createdByDisplayName = itemArray.getJSONObject(i).getString("createdByDisplayName");
                String createdOn = "";
                String createdDate = "";
                if(!itemArray.getJSONObject(i).getString("createdOn").equalsIgnoreCase("null")) {
	                JSONObject createdOnJSONObject = (JSONObject) new JSONTokener(itemArray.getJSONObject(i).getString("createdOn")).nextValue();
	                createdOn = createdOnJSONObject.getString("display");
	                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
	                Date date = sdf.parse(createdOn);
	                sdf = new SimpleDateFormat("MMM dd, yyyy");
	                createdDate = sdf.format(date);
                }
                String entityId = itemArray.getJSONObject(i).getString("entityId");
                String entityTitle = itemArray.getJSONObject(i).getString("entityTitle");
                String entityReference = itemArray.getJSONObject(i).getString("entityReference");
                String entityURL = itemArray.getJSONObject(i).getString("entityURL");
//                String siteId = itemArray.getJSONObject(i).getString("siteId");
//                String siteTitle = itemArray.getJSONObject(i).getString("siteTitle");
//                
                SakaiAnnouncement trs = new SakaiAnnouncement();
                trs.setId(id);
                trs.setTitle(title);
                trs.setAttachments(attachments);
                trs.setBody(body);
                trs.setCreatedByDisplayName(createdByDisplayName);
                trs.setCreatedOn(createdOn);
                trs.setCreatedDate(createdDate);
                trs.setEntityId(entityId);
                trs.setEntityTitle(entityTitle);
                trs.setEntityReference(entityReference);
                trs.setEntityURL(entityURL);
//                trs.setSiteId(siteId);
//                trs.setSiteTitle(siteTitle);
                anns.add(trs);
            }

		} catch (JSONException e) {
			LOG.error(e.getMessage(), e);
	    } catch (Exception e) {	    	
	    	LOG.error(e.getMessage(), e);
	    }
		return anns;
	}
	
	public List<SakaiAnnouncement> findAnnouncementDetails(String json) {
		List<SakaiAnnouncement> anns = new ArrayList<SakaiAnnouncement>();
    	try {
            JSONObject jsonObj = (JSONObject) new JSONTokener(json).nextValue();
            String body = jsonObj.getString("body");
            String title = jsonObj.getString("title");
            String createdOn = "";
            if(jsonObj.getString("createdOn").equalsIgnoreCase("null")) {
            JSONObject createdOnJSONObject = (JSONObject) new JSONTokener(jsonObj.getString("createdOn")).nextValue();
            createdOn = createdOnJSONObject.getString("display");
            }
            
            String createdByDisplayName = jsonObj.getString("createdByDisplayName");
            
            SakaiAnnouncement trs = new SakaiAnnouncement();
            trs.setTitle(title);
            trs.setBody(body);
            trs.setCreatedByDisplayName(createdByDisplayName);
            trs.setCreatedOn(createdOn);
            anns.add(trs);
    	} catch (JSONException e) {
    		LOG.error(e.getMessage(), e);
	    } catch (Exception e) {
	    	LOG.error(e.getMessage(), e);
	    }
		return anns;
	}
	
	public List<SakaiAssignment> findAllCourseAssignments(String siteId, String userId) {
		List<SakaiAssignment> anns = new ArrayList<SakaiAssignment>();
    	try {
    		String url = configParamService.findValueByName("Sakai.Url.Base") + "assignment.json?siteId=" + siteId + "&userId=" + userId;
    		ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
    		String json = IOUtils.toString(is.getBody(), "UTF-8");
    		
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json.toString());
            JSONArray itemArray = jsonObj.getJSONArray("assignment_collection");
            for (int i = 0; i < itemArray.size(); i++) {
                String id = itemArray.getJSONObject(i).getString("id");
                String title = itemArray.getJSONObject(i).getString("title");
                String dropByDate = itemArray.getJSONObject(i).getString("dropByDate");
                
                SakaiAssignment trs = new SakaiAssignment();
                trs.setId(id);
                trs.setTitle(title);
                trs.setDropByDate(dropByDate);
                anns.add(trs);
            }
		} catch (Exception e) {
	    	LOG.error(e.getMessage(), e);
	    }
		return anns;
	}
	
	public List<SakaiAssignment> findAssignmentDetails(String json) {
		List<SakaiAssignment> ass = new ArrayList<SakaiAssignment>();
    	try {
            JSONObject jsonObj = (JSONObject) new JSONTokener(json).nextValue();
            
            JSONArray itemArray = jsonObj.getJSONArray("assignment_collection");
            for (int i = 0; i < itemArray.size(); i++) {
	            String body = itemArray.getJSONObject(i).getString("body");
	            String maxGradePoints = itemArray.getJSONObject(i).getString("gradeScale");
	            String title = itemArray.getJSONObject(i).getString("title");
	            String submittedStatus = null;
	            String submittedText = null;
	            String submittedAttachments = null;
	            String graded = null;
	            String submissionGrade = null;
	            String submitted = itemArray.getJSONObject(i).getString("submittedStatus");
	            if (submitted.equals("true")) {
	            	submittedStatus = "Submitted";
	            	submittedText = itemArray.getJSONObject(i).getString("submittedText");
		            submittedAttachments = itemArray.getJSONObject(i).getString("submittedAttachments");
		            
		            graded = itemArray.getJSONObject(i).getString("submissionGraded");
		            submissionGrade = itemArray.getJSONObject(i).getString("submissionGrade");
	            }
	            else {
	            	submittedStatus = "Not Submitted";
	            }
	            
	            SakaiAssignment trs = new SakaiAssignment();
	            trs.setTitle(title);
	            trs.setBody(body);
	            trs.setGradeScale(maxGradePoints);
	            trs.setSubmittedStatus(submittedStatus);
	            trs.setSubmittedText(submittedText);
	            trs.setSubmittedAttachments(submittedAttachments);
	            trs.setSubmissionGraded(graded);
	            trs.setSubmissionGrade(submissionGrade);
	            ass.add(trs);
            }
    	} catch (Exception e) {
	    	LOG.error(e.getMessage(), e);
	    }
		return ass;
	}
	
	public String findCourseGrade(String json) {
		String courseGrade = null;
    	try {
		
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json.toString());
            JSONArray itemArray = jsonObj.getJSONArray("gradebook_collection");
            for (int i = 0; i < itemArray.size(); i++) {
                courseGrade = itemArray.getJSONObject(i).getString("courseGrade");
            }

		} catch (Exception e) {
	    	LOG.error(e.getMessage(), e);
	    }
		return courseGrade;
	}
	
	public List<SakaiRoster> findCourseRoster(String json) {
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
    		LOG.error(e.getMessage(), e);
        }
		return roster;
	}
	
	public List<SakaiRoster> findCourseParticipantDetails(String json, String displayId) {
		List<SakaiRoster> roster = new ArrayList<SakaiRoster>();
    	try {
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("participant_collection");
            for (int i = 0; i < itemArray.size(); i++) {
            	String displayID = itemArray.getJSONObject(i).getString("displayID");
            	if(!displayId.equalsIgnoreCase(displayID)){
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
    		LOG.error(e.getMessage(), e);
        }
		return roster;
	}

}
