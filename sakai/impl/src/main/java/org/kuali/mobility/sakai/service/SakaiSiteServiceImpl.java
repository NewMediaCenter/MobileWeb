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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.kuali.mobility.sakai.entity.Announcement;
import org.kuali.mobility.sakai.entity.Assignment;
import org.kuali.mobility.sakai.entity.Attachment;
import org.kuali.mobility.sakai.entity.Home;
import org.kuali.mobility.sakai.entity.Resource;
import org.kuali.mobility.sakai.entity.Roster;
import org.kuali.mobility.sakai.entity.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.iu.es.espd.oauth.OAuth2LegService;
import edu.iu.es.espd.oauth.OAuthException;

@Service
public class SakaiSiteServiceImpl implements SakaiSiteService {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SakaiSiteServiceImpl.class);
	
	@Autowired
	private ConfigParamService configParamService;
	
	@Autowired
	private OAuth2LegService oncourseOAuthService;
	
	public Home findSakaiHome(String user) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "site.json";
			ResponseEntity<InputStream> is;
			is = oncourseOAuthService.oAuthGetRequest(user, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
			return parseCourses(json);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new Home();
		}
	}
	
	public Site findSite(String siteId, String user) {
		Site site = new Site();
		String instructorName = null;
		String instructorId = null;
		String courseDescription = null;
		String courseTitle = null;
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
			
			//check for available tools
			url = configParamService.findValueByName("Sakai.Url.Base") + "site/" + siteId + "/pages.json";
			is = oncourseOAuthService.oAuthGetRequest(user, url, "text/html");
			json = IOUtils.toString(is.getBody(), "UTF-8");
			itemArray = (JSONArray) JSONSerializer.toJSON(json);
			List<String> availableTools = new ArrayList<String>();
			for (int j = 0; j < itemArray.size(); j++) {
				String title = itemArray.getJSONObject(j).getString("title");
				if (title != null && !title.isEmpty()) {
					availableTools.add(title);
				}
			}
			
			if (availableTools.contains("Announcements")) site.setHasAnnouncementsTool(true);
			if (availableTools.contains("Assignments")) site.setHasAssignmentsTool(true);
			if (availableTools.contains("Forums")) site.setHasForumsTool(true);
			if (availableTools.contains("Grades")) site.setHasGradesTool(true);
			if (availableTools.contains("Messages")) site.setHasMessagesTool(true);
			if (availableTools.contains("Resources")) site.setHasResourcesTool(true);
			if (availableTools.contains("Roster")) site.setHasRosterTool(true);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}	
		
		site.setId(siteId);
		site.setInstructorId(instructorId);
		site.setInstructorName(instructorName);
		site.setDescription(courseDescription);
		site.setTitle(courseTitle);
		
		return site;
	}
	
	@SuppressWarnings("unchecked")
	public Home parseCourses(String json) {
		Home home = new Home();
		Map<String,List<Site>> courses = home.getCourses();
		List<Site> projects = home.getProjects();
    	try {
			JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("site_collection");
            for (Iterator<JSONObject> iter = itemArray.iterator(); iter.hasNext();) {
            	JSONObject object = iter.next();
            	
            	String type = object.getString("type");
            	if (!"project".equals(type)) {
	                String courseDesc = object.getString("description");
	                courseDesc = courseDesc.replace("&nbsp;"," ");          
	                Site item = new Site();
	                item.setId(object.getString("entityId"));
	                item.setTitle(object.getString("title"));
	                
	                JSONObject props = (JSONObject) object.get("props");
	                String term = props.getString("term");
	                item.setTerm(term);
	                item.setDescription(courseDesc);
	                
	                List<Site> courseList = courses.get(term);
	                if (courseList == null) {
	                	courseList = new ArrayList<Site>();
	                	courses.put(term, courseList);
	                }
	                courseList.add(item);
            	} else {
            		Site project = new Site();
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
	
	public List<Announcement> findAllAnnouncements(String siteId, String user) {
		List<Announcement> anns = new ArrayList<Announcement>();

    	try {
    		String url = configParamService.findValueByName("Sakai.Url.Base") + "announcement/site/" + siteId + ".json";
    		ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(user, url, "text/html");
    		String json = IOUtils.toString(is.getBody(), "UTF-8");
    		
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json.toString());
            JSONArray itemArray = jsonObj.getJSONArray("announcement_collection");
            for (int i = 0; i < itemArray.size(); i++) {
            	JSONObject announcment = itemArray.getJSONObject(i);
                String id = announcment.getString("id");
                String title = announcment.getString("title");
                String body = announcment.getString("body");
                String createdByDisplayName = announcment.getString("createdByDisplayName");
                String createdOn = "";
                String createdDate = "";
                if(!announcment.getString("createdOn").equalsIgnoreCase("null")) {
	                JSONObject createdOnJSONObject = (JSONObject) new JSONTokener(announcment.getString("createdOn")).nextValue();
	                createdOn = createdOnJSONObject.getString("display");
	                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
	                Date date = sdf.parse(createdOn);
	                sdf = new SimpleDateFormat("MMM dd, yyyy");
	                createdDate = sdf.format(date);
                }
                String entityId = announcment.getString("entityId");
                String entityTitle = announcment.getString("entityTitle");
                String entityReference = announcment.getString("entityReference");
                String entityURL = announcment.getString("entityURL");
//                String siteId = itemArray.getJSONObject(i).getString("siteId");
//                String siteTitle = itemArray.getJSONObject(i).getString("siteTitle");
//                
                JSONArray attachments = itemArray.getJSONObject(i).getJSONArray("attachments");
                List<Attachment> attach = new ArrayList<Attachment>();
                if (attachments != null && !attachments.isEmpty()) {
                	for (int j = 0; j < attachments.size(); j++) {
                		Attachment attachment = new Attachment();
                		attachment.setTitle(attachments.getString(j));
                		attach.add(attachment);
                	}
                }
                
                Announcement trs = new Announcement();
                trs.setId(id);
                trs.setTitle(title);
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
                trs.setAttachments(attach);
                anns.add(trs);
            }

		} catch (JSONException e) {
			LOG.error(e.getMessage(), e);
	    } catch (Exception e) {	    	
	    	LOG.error(e.getMessage(), e);
	    }
		return anns;
	}
	
	public Announcement findAnnouncementDetails(String json) {
		Announcement anns = new Announcement();
    	try {
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONObject created = (JSONObject) jsonObj.get("createdOn");
            if (jsonObj.get("createdOn") != null) {
            	anns.setCreatedOn(created.getString("display"));
            }
            
            anns.setTitle(jsonObj.getString("title"));
            anns.setBody(jsonObj.getString("body"));
            anns.setCreatedByDisplayName(jsonObj.getString("createdByDisplayName"));
            
            JSONArray attachmentArray = jsonObj.getJSONArray("attachments");
            if (attachmentArray != null && !attachmentArray.isEmpty()) {
            	List<Attachment> attachments = new ArrayList<Attachment>();
            	for (int i = 0; i < attachmentArray.size(); i++) {
            		String attach = attachmentArray.getString(i);
            		Attachment attachment = new Attachment();
            		attachment.setUrl(attach);
            		
            		attach = attach.substring(attach.lastIndexOf("/") + 1);
            		attachment.setTitle(attach);
            		attachments.add(attachment);
            	}
            	anns.setAttachments(attachments);
            }
            
    	} catch (Exception e) {
	    	LOG.error(e.getMessage(), e);
	    }
		return anns;
	}
	
	public List<Assignment> findAllAssignments(String siteId, String userId) {
		List<Assignment> anns = new ArrayList<Assignment>();
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
                
                Assignment trs = new Assignment();
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
	
	public List<Assignment> findAssignmentDetails(String json) {
		List<Assignment> ass = new ArrayList<Assignment>();
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
	            
	            Assignment trs = new Assignment();
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
	
	public List<Roster> findRoster(String json) {
		List<Roster> roster = new ArrayList<Roster>();
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
                
                Roster trs = new Roster();
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
	
	public List<Roster> findParticipantDetails(String json, String displayId) {
		List<Roster> roster = new ArrayList<Roster>();
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
                
                Roster trs = new Roster();
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
	
	public List<Resource> findSiteResources(String siteId, String userId, String resId) {
		List<Resource> resources = new ArrayList<Resource>();
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "resources.json?siteId=" + siteId;
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("resources_collection");

            for (int i = 0; i < itemArray.size(); i++) {
                String id = itemArray.getJSONObject(i).getString("resourceID");
                
                String originalId = new String(id);
                String resArr[];
                if (resId != null) {
                	if (!id.contains(resId) || id.equals(resId)){
                		continue;
                	}
	                id = id.replace(resId, "");
	                resArr = id.split("/");
                } else {
                	String strArr[] = id.split("/");
                    resArr = new String[strArr.length-3];
                    for(int j=0; j<(strArr.length-3); j++) {
            			resArr[j] = strArr[j+3];
            		}
                }

        		if(resourceAlreadyExists(id, resArr[0], resArr.length==1?null:resArr[1], resources)) {
        			continue;
        		}
        		
        		Resource item = new Resource();
        		item.setHasChild(false);
        		if(resArr.length > 1) {
                	item.setHasChild(true);
                	item.setChildResource(resArr[1]);
                }
        		item.setId(originalId);
        		
        		String title = resArr[0];
        		title = title.replace("http:__", "");
        		int dotIndex = title.lastIndexOf(".");
        		if (dotIndex > 0) {
        			item.setTitle(title.substring(0, dotIndex));
        		} else {
        			item.setTitle(title);
        		}
                
                if (!item.getHasChild()){
                	char lastChar = id.charAt(id.length()-1);
        			if(lastChar == '/'){
        				item.setExtension("fldr");
        			} else {
	                	String resExt [] = resArr[resArr.length-1].split("\\.");
	                	if(resExt!=null && resExt.length!=0) {
	                		item.setExtension(resExt[resExt.length-1].toLowerCase());
	                	}
	                	else {
	                		item.setExtension(null);
	                	}
        			}
                } else {
                	item.setExtension("fldr");
                }
                
                resources.add(item);
                
            }

        } catch (Exception e) {
        	LOG.error(e.getMessage(), e);
        }
		return resources;
	}

	public List<Resource> findChildResources(String siteId, String resId, String userId) {
		List<Resource> resources = new ArrayList<Resource>();
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "resources.json?siteId=" + siteId;
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("resources_collection");

            for (int i = 0; i < itemArray.size(); i++) {
                String id = itemArray.getJSONObject(i).getString("resourceID");
                
                if(!id.contains(resId)){
                	continue;
                }
                String originalId = id;
                id = id.replace(resId, "");
                String strArr [] = id.split("/");
                
        		if(resourceAlreadyExists(id, strArr[0], strArr.length==1?null:strArr[1], resources) || strArr[0].equals("")) {
        			continue;
        		}
        		
        		Resource item = new Resource();
        		item.setHasChild(false);
        		if(strArr.length > 1) {
                	item.setHasChild(true);
                	item.setChildResource(strArr[1]);
                }
        		item.setId(originalId);
        		
        		String title = strArr[0];
        		title = title.replace("http:__", "");
        		int dotIndex = title.lastIndexOf(".");
        		if (dotIndex > 0) {
        			item.setTitle(title.substring(0, dotIndex));
        		} else {
        			item.setTitle(title);
        		}
        		
                if (!item.getHasChild()){
                	char lastChar = id.charAt(id.length()-1);
        			if(lastChar == '/'){
        				item.setExtension("fldr");
        			} else {
	                	String resExt [] = strArr[strArr.length-1].split("\\.");
	                	if(resExt!=null && resExt.length!=0) {
	                		item.setExtension(resExt[resExt.length-1]);
	                		if((resExt[resExt.length-1]).equalsIgnoreCase("URL")){
	                			item.setTitle(strArr[0].replace("\\.URL", ""));
	                		}
	                	}
	                	else {
	                		item.setExtension(null);
	                	}
	                }
                } else {
                	item.setExtension("fldr");
                }
                
                resources.add(item);
                
            }

        } catch (Exception e) {
        	LOG.error(e.getMessage(), e);
        }
		return resources;
	}

	private Boolean resourceAlreadyExists(String id, String title, String childRes, List<Resource> resources) {
		Iterator<Resource> iterator = resources.iterator();
		Boolean titleExists = false;
		int i = 0;
		while (iterator.hasNext()) {
			Resource res = iterator.next();
			if(title.equals(res.getTitle())) {
				titleExists = true;
				if(childRes != null){
					res.setHasChild(true);
					String itemChild = res.getChildResource();
					res.setChildResource(itemChild + "," + childRes);
				}
				res.setId(id);
				resources.set(i, res);
				
			}
			i++;
		}
		return titleExists;
	}

	@Override
	public byte[] getResource(String resId, String userId) {
		try {
			resId = resId.replaceAll(" ", "%20");
			String url = configParamService.findValueByName("Sakai.Url.Base") + "resources/getresource" + resId;
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "application/pdf");
			return IOUtils.toByteArray(is.getBody());
		} catch (OAuthException e) {
			BufferedReader br = new BufferedReader(new InputStreamReader(e.getResponseBody()));
			String body = "";
			try {
				body = br.readLine();
			} catch (IOException e1) {
			}
			LOG.error(e.getResponseCode() + ", " + body, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
}
