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
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.kuali.mobility.sakai.entity.FileType;
import org.kuali.mobility.sakai.entity.Home;
import org.kuali.mobility.sakai.entity.Resource;
import org.kuali.mobility.sakai.entity.Roster;
import org.kuali.mobility.sakai.entity.Site;
import org.kuali.mobility.sakai.entity.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.iu.es.espd.ccl.oauth.CalendarEventOAuthService;
import edu.iu.es.espd.ccl.oauth.CalendarViewEvent;
import edu.iu.es.espd.ccl.oauth.ListData;
import edu.iu.es.espd.ccl.oauth.ListViewEvents;
import edu.iu.es.espd.oauth.OAuth2LegService;
import edu.iu.es.espd.oauth.OAuthException;

@Service
public class SakaiSiteServiceImpl implements SakaiSiteService {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SakaiSiteServiceImpl.class);
	
	@Autowired
	private ConfigParamService configParamService;
	
	@Autowired
	private OAuth2LegService oncourseOAuthService;
	
	@Autowired
	private CalendarEventOAuthService calendarEventOAuthService;
	
	private static final Map<String, FileType> fileTypes;
	private static final String FOLDER_EXTENSION = "fldr";
	private static final String URL_EXTENSION = "url";
	
	static {
		fileTypes = new HashMap<String, FileType>();
		fileTypes.put("txt", FileType.TEXT);
		fileTypes.put("rtf", FileType.TEXT);
		fileTypes.put("doc", FileType.TEXT);
		fileTypes.put("docx", FileType.TEXT);
		fileTypes.put("odt", FileType.TEXT);
		fileTypes.put("wpd", FileType.TEXT);
		fileTypes.put("jpg", FileType.IMAGE);
		fileTypes.put("jpeg", FileType.IMAGE);
		fileTypes.put("png", FileType.IMAGE);
		fileTypes.put("gif", FileType.IMAGE);
		fileTypes.put("bmp", FileType.IMAGE);
		fileTypes.put("psd", FileType.IMAGE);
		fileTypes.put("tiff", FileType.IMAGE);
		fileTypes.put("wav", FileType.AUDIO);
		fileTypes.put("wma", FileType.AUDIO);
		fileTypes.put("mpa", FileType.AUDIO);
		fileTypes.put("mp3", FileType.AUDIO);
		fileTypes.put("mid", FileType.AUDIO);
		fileTypes.put("midi", FileType.AUDIO);
		fileTypes.put("m4a", FileType.AUDIO);
		fileTypes.put("m3u", FileType.AUDIO);
		fileTypes.put("aif", FileType.AUDIO);
		fileTypes.put("avi", FileType.VIDEO);
		fileTypes.put("flv", FileType.VIDEO);
		fileTypes.put("mov", FileType.VIDEO);
		fileTypes.put("mp4", FileType.VIDEO);
		fileTypes.put("mpg", FileType.VIDEO);
		fileTypes.put("swf", FileType.VIDEO);
		fileTypes.put("vob", FileType.VIDEO);
		fileTypes.put("wmv", FileType.VIDEO);
		fileTypes.put("wks", FileType.SPREADSHEET);
		fileTypes.put("xls", FileType.SPREADSHEET);
		fileTypes.put("xlsx", FileType.SPREADSHEET);
		fileTypes.put("ods", FileType.SPREADSHEET);
		fileTypes.put("ppt", FileType.PRESENTATION);
		fileTypes.put("pptx", FileType.PRESENTATION);
		fileTypes.put("odp", FileType.PRESENTATION);
		fileTypes.put("pdf", FileType.PDF);
		fileTypes.put(URL_EXTENSION, FileType.LINK);
		fileTypes.put(FOLDER_EXTENSION, FileType.FOLDER);
	}
	
	@SuppressWarnings("unchecked")
	public Home findSakaiHome(String user) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "user_prefs.json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(user, url, "text/html");
			String prefsJson = IOUtils.toString(is.getBody(), "UTF-8");
			Set<String> visibleSiteIds = new HashSet<String>();
			try {
				JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(prefsJson);
				JSONArray itemArray = jsonObj.getJSONArray("user_prefs_collection");
				if (itemArray != null) {
					for (Iterator<JSONObject> iter = itemArray.iterator(); iter.hasNext();) {
		            	JSONObject object = iter.next();
		            	visibleSiteIds.add(object.getString("siteId"));
					}
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			
			Set<String> calendarCourseIds = new HashSet<String>();
			try {
				Calendar todayDate = Calendar.getInstance();
				Calendar tomorrowDate = Calendar.getInstance();
				//todayDate.set(Calendar.MONTH, Calendar.AUGUST);
				//todayDate.set(Calendar.YEAR, 2011);
				//todayDate.set(Calendar.DAY_OF_MONTH, 29);
				todayDate.set(Calendar.HOUR, 0);
				todayDate.set(Calendar.MINUTE, 0);
				todayDate.set(Calendar.SECOND, 0);
				todayDate.set(Calendar.MILLISECOND, 0);
				tomorrowDate.setTime(todayDate.getTime());
				tomorrowDate.add(Calendar.DATE, 1);
				ListViewEvents listViewEvents = calendarEventOAuthService.retrieveViewEventsList(user, todayDate.getTime(), todayDate.getTime(), todayDate.getTime(), null);
				List<ListData> events = listViewEvents.getEvents();
				if (events.size() > 0) {
					ListData list = events.get(0);
					List<CalendarViewEvent> viewEvents = list.getEvents();
					for (CalendarViewEvent event : viewEvents) {
						if (event.getOncourseSiteId() != null) {
							calendarCourseIds.add(event.getOncourseSiteId().toLowerCase());
						}
					}
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			
			url = configParamService.findValueByName("Sakai.Url.Base") + "site.json";
			is = oncourseOAuthService.oAuthGetRequest(user, url, "text/html");
			String siteJson = IOUtils.toString(is.getBody(), "UTF-8");
	
			Home home = new Home();
			List<Term> courses = home.getCourses();
			List<Site> projects = home.getProjects();
			List<Site> other = home.getOther();
			List<Site> today = home.getTodaysCourses();
			Map<String, Term> courseMap = new HashMap<String, Term>();
			JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(siteJson);
            JSONArray itemArray = jsonObj.getJSONArray("site_collection");
            for (Iterator<JSONObject> iter = itemArray.iterator(); iter.hasNext();) {
            	JSONObject object = iter.next();
            	
            	String id = object.getString("id");
            	if (!visibleSiteIds.contains(id)) {
            		continue;
            	}
            	
            	Site item = new Site();
            	item.setId(id);
            	item.setTitle(object.getString("title"));
            	item.setDescription(object.getString("shortDescription").replace("&nbsp;"," "));
            	
            	String type = object.getString("type");
            	if ("course".equals(type)) {
	                Object jsonProps = object.get("props");
	                String term = "No Term";
	                if (jsonProps instanceof JSONObject) {
		                JSONObject props = (JSONObject) jsonProps;
		                term = props.getString("term");
		                String[] split = term.split(" ");
		                term = "";
		                for (String s : split) {
		                	s = s.toLowerCase();
		                	s = s.substring(0, 1).toUpperCase() + s.substring(1);
		                	term = term + s + " ";
		                }
		                term.trim();
	                }
	                item.setTerm(term);  
	                
	                Term termObj = courseMap.get(term);
	                if (termObj == null) {
	                	termObj = new Term();
	                	termObj.setTerm(term);
	                	courseMap.put(term, termObj);
	                }
	                termObj.getCourses().add(item);
	                
	                if (calendarCourseIds.contains(item.getId().toLowerCase())) {
	                	today.add(item);
	                }
            	} else if ("project".equals(type)) {
        			projects.add(item);
        		} else {
        			other.add(item);
        		}
            }
            
            for (Map.Entry<String, Term> entry : courseMap.entrySet()) {
            	courses.add(entry.getValue());
            }
            Collections.sort(courses);
            Collections.reverse(courses);
			return home;
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
	                
	                long time = createdOnJSONObject.getLong("time");
	                Calendar now = Calendar.getInstance();
	                if (now.getTimeInMillis() < time) {
	                	continue; //this is a time-restricted announcement for the future
	                }
	                
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
                		JSONObject attachObj = attachments.getJSONObject(j);
                		attachment.setTitle(attachObj.getString("name"));
                		attachment.setMimeType(attachObj.getString("type"));
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
            		JSONObject attach = attachmentArray.getJSONObject(i);
            		Attachment attachment = new Attachment();
            		attachment.setUrl(attach.getString("id"));
            		attachment.setTitle(attach.getString("name"));
            		attachment.setMimeType(attach.getString("type"));
            		attachment.setFileType(determineAttachmentFileType(attachment.getUrl(), attachment.getMimeType()));
            		attachments.add(attachment);
            	}
            	anns.setAttachments(attachments);
            }
            
    	} catch (Exception e) {
	    	LOG.error(e.getMessage(), e);
	    }
		return anns;
	}
	
	public byte[] findAnnouncementAttachment(String siteId, String attachmentId, String userId) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "announcement" + attachmentId;
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "application/octet-stream");
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
	
	public List<Roster> findRoster(String siteId, String userId) {
		List<Roster> roster = new ArrayList<Roster>();
    	try {
    		String url = configParamService.findValueByName("Sakai.Url.Base") + "participant.json?siteId=" + siteId;
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("participant_collection");
            for (int i = 0; i < itemArray.size(); i++) {
            	Roster trs = new Roster();
            	JSONObject rosterObject = itemArray.getJSONObject(i);
            	trs.setDisplayID(rosterObject.getString("displayID"));
            	trs.setDisplayName(rosterObject.getString("displayName"));
            	trs.setFirstName(rosterObject.getString("firstName"));
            	trs.setLastName(rosterObject.getString("lastName"));
            	trs.setNickName(rosterObject.getString("nickName"));
            	trs.setImageUrl(rosterObject.getString("imageUrl"));
            	trs.setDepartment(rosterObject.getString("department"));
            	trs.setEmail(rosterObject.getString("email"));
            	trs.setHomePage(rosterObject.getString("homePage"));
            	trs.setHomePhone(rosterObject.getString("homePhone"));
            	trs.setWorkPhone(rosterObject.getString("workPhone"));
            	trs.setPosition(rosterObject.getString("position"));
            	trs.setRoleTitle(rosterObject.getString("roleTitle"));
            	trs.setRoom(rosterObject.getString("room"));
            	trs.setSchool(rosterObject.getString("school"));
            	trs.setOtherInformation(rosterObject.getString("otherInformation"));
            	trs.setEntityReference(rosterObject.getString("entityReference"));
            	trs.setEntityURL(rosterObject.getString("entityURL"));
            	trs.setSortName();
                roster.add(trs);
            }
    	} catch (Exception e) {
    		LOG.error(e.getMessage(), e);
        }
    	Collections.sort(roster);
		return roster;
	}
	
	public Roster findParticipantDetails(String json, String displayId) {
    	try {
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("participant_collection");
            for (int i = 0; i < itemArray.size(); i++) {
            	JSONObject participant = itemArray.getJSONObject(i);
            	String displayID = participant.getString("displayID");
            	if(!displayId.equalsIgnoreCase(displayID)){
            		continue;
            	}
                Roster trs = new Roster();
                trs.setDisplayID(participant.getString("displayID"));
            	trs.setDisplayName(participant.getString("displayName"));
            	trs.setFirstName(participant.getString("firstName"));
            	trs.setLastName(participant.getString("lastName"));
            	trs.setNickName(participant.getString("nickName"));
            	trs.setImageUrl(participant.getString("imageUrl"));
            	trs.setDepartment(participant.getString("department"));
            	trs.setEmail(participant.getString("email"));
            	trs.setHomePage(participant.getString("homePage"));
            	trs.setHomePhone(participant.getString("homePhone"));
            	trs.setWorkPhone(participant.getString("workPhone"));
            	trs.setPosition(participant.getString("position"));
            	trs.setRoleTitle(participant.getString("roleTitle"));
            	trs.setRoom(participant.getString("room"));
            	trs.setSchool(participant.getString("school"));
            	trs.setOtherInformation(participant.getString("otherInformation"));
            	trs.setEntityReference(participant.getString("entityReference"));
            	trs.setEntityURL(participant.getString("entityURL"));
                return trs;
            }
    	} catch (JSONException e) {
    		LOG.error(e.getMessage(), e);
        }
		return null;
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
            	JSONObject resourceObject = itemArray.getJSONObject(i);
            	String id = resourceObject.getString("resourceID");
            	
            	if (resId != null && !resId.isEmpty() && (!id.startsWith(resId) || id.equals(resId))) {
            		continue;
            	}
                
            	String strArr[];
            	if (resId == null) {
            		String strippedId = id.substring(1); //remove the leading slash
                	strArr = strippedId.split("/");
            		if (strArr.length > 3) {
            			continue;
            		}
            	} else {
            		String strippedId = id.substring(resId.length());
                	strArr = strippedId.split("/");
            		if (strArr.length > 1) {
            			continue;
            		}
            	}
            	
            	Resource item = new Resource();

            	item.setId(id);
            	item.setEncodedId(URLEncoder.encode(id, "UTF-8"));
            	item.setTitle(resourceObject.getString("resourceName"));
            	item.setMimeType(resourceObject.getString("resourceType"));

            	char lastChar = id.charAt(id.length()-1);
    			if(lastChar == '/'){
    				item.setExtension(FOLDER_EXTENSION);
    			} else {
                	String resExt [] = strArr[strArr.length-1].split("\\.");
                	if(resExt!=null && resExt.length!=0) {
                		item.setExtension(resExt[resExt.length-1].toLowerCase());
                	}
                	else {
                		item.setExtension(null);
                	}
    			}
                item.setFileType(determineFileType(item.getExtension()));
                resources.add(item);
            }
            Collections.sort(resources);
        } catch (Exception e) {
        	LOG.error(e.getMessage(), e);
        }
		return resources;
	}
	
	private FileType determineFileType(String fileExtension) {
		FileType type = fileTypes.get(fileExtension.toLowerCase());
		if (type != null) {
			return type;
		} else {
			return FileType.GENERIC;
		}
	}
	
	private FileType determineAttachmentFileType(String attachmentUrl, String mimeType) {
		if (URL_MIME_TYPE.equals(mimeType)) {
			return FileType.LINK;
		}
		
		String strArr[] = attachmentUrl.split("/");
		String resExt[] = strArr[strArr.length-1].split("\\.");
		String extension = null;
    	if(resExt!=null && resExt.length!=0) {
    		extension = resExt[resExt.length-1].toLowerCase();
    	}
		
		FileType type = fileTypes.get(extension);
		if (type != null) {
			return type;
		} else {
			return FileType.GENERIC;
		}
	}

	@Override
	public byte[] getResource(String resId, String userId) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "resources/getresource" + resId;
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "application/octet-stream");
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
	
	public void setCalendarEventOAuthService(CalendarEventOAuthService calendarEventOAuthService) {
		this.calendarEventOAuthService = calendarEventOAuthService;
	}
}
