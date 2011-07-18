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
import java.util.LinkedHashMap;
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
import org.kuali.mobility.sakai.entity.SakaiCourse;
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
	
	public Map<String,List<SakaiCourse>> findAllCourses(String campus, String user) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "site.json";
			ResponseEntity<InputStream> is;
			is = oncourseOAuthService.oAuthGetRequest(user, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
			Map<String,List<SakaiCourse>> courses = parseCourses(campus, json);
			return courses;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new LinkedHashMap<String,List<SakaiCourse>>();
		}
	}
	
	public SakaiCourse findCourse(String campus, String siteId, String user) {
		SakaiCourse course = new SakaiCourse();
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

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}	
		
		course.setCourseId(siteId);
		course.setInstructorId(instructorId);
		course.setInstructorName(instructorName);
		course.setCourseDesc(courseDescription);
		course.setCourseTitle(courseTitle);
		
		return course;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,List<SakaiCourse>> parseCourses(String campus, String json) {
		Map<String,List<SakaiCourse>> courses = new LinkedHashMap<String,List<SakaiCourse>>();
    	try {
			JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("site_collection");
            for (Iterator<JSONObject> iter = itemArray.iterator(); iter.hasNext();) {
            	JSONObject object = iter.next();
                String courseDesc = object.getString("description");
                courseDesc = courseDesc.replace("&nbsp;"," ");          
                SakaiCourse item = new SakaiCourse();
                item.setCourseId(object.getString("entityId"));
                item.setCourseTitle(object.getString("title"));
                
                JSONObject props = (JSONObject) object.get("props");
                String term = props.getString("term");
                item.setCourseTerm(term);
                item.setCourseDesc(courseDesc);
                
                List<SakaiCourse> courseList = courses.get(term);
                if (courseList == null) {
                	courseList = new ArrayList<SakaiCourse>();
                	courses.put(term, courseList);
                }
                courseList.add(item);
            }
    	} catch (JSONException e) {
	    	e.printStackTrace();
	    } catch (Exception e) {
	    	e.printStackTrace();
	        //runOnUiThread(returnError);
	    }
		return courses;
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
	
}
