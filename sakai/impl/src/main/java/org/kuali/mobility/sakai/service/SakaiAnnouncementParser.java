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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONTokener;

import org.kuali.mobility.sakai.entity.SakaiAnnouncement;

public class SakaiAnnouncementParser {

	public List<SakaiAnnouncement> parseAnnouncements(String json) {
		List<SakaiAnnouncement> anns = new ArrayList<SakaiAnnouncement>();
		anns = parse(false, json);
		return anns;
	}
	
		
	public List<SakaiAnnouncement> parseAnnouncementDetails(String json) {
		
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
	    	e.printStackTrace();
	    } catch (Exception e) {
	    	e.printStackTrace();
	        //runOnUiThread(returnError);
	    }
		return anns;
		
	}

    private List<SakaiAnnouncement> parse(boolean convert, String json) {
    	List<SakaiAnnouncement> anns = new ArrayList<SakaiAnnouncement>();
    	try {
//          
//			StringBuilder json = new StringBuilder();
////            json.append(HttpUtility.getStringFromUrl(url));
//            //Log.e(TAG, "json = " + json.toString());
//			
//			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(new URI(url, false), SecurityContextHolder.getContext().getAuthentication().getName(), "text/html");     
//            json.append(IOUtils.toString(is.getBody(), "UTF-8"));
//			
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
	    	e.printStackTrace();
	    } catch (Exception e) {
	    	e.printStackTrace();
	        //runOnUiThread(returnError);
	    }
		return anns;
    }
}
