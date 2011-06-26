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

import org.kuali.mobility.sakai.entity.SakaiCourse;

public class SakaiCourseParser {

	public List<SakaiCourse> parseCourses(String campus, String json) {
		List<SakaiCourse> courses = new ArrayList<SakaiCourse>();
		courses = parse(campus, false, json);
		return courses;
	}
	
	
    private List<SakaiCourse> parse(String campus, boolean convert, String json) {
    	List<SakaiCourse> courses = new ArrayList<SakaiCourse>();
    	try {
//          
			JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("site_collection");
            for (int i = 0; i < itemArray.size(); i++) {
                String courseDescTemp = itemArray.getJSONObject(i).getString("description");
                String courseDesc = courseDescTemp.replace("&nbsp;"," ");
                String courseTitle = itemArray.getJSONObject(i).getString("title");
                String courseId = itemArray.getJSONObject(i).getString("entityId");                
                SakaiCourse item = new SakaiCourse();
                item.setCourseId(courseId);
                item.setCourseTitle(courseTitle);
                item.setCourseDesc(courseDesc);
                courses.add(item);
            }
    	} catch (JSONException e) {
	    	e.printStackTrace();
	    } catch (Exception e) {
	    	e.printStackTrace();
	        //runOnUiThread(returnError);
	    }
		return courses;
    }
}
