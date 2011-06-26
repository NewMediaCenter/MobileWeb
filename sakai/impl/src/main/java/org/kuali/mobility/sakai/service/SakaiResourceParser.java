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
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.kuali.mobility.sakai.entity.SakaiResource;

public class SakaiResourceParser {

	List<SakaiResource> resources = new ArrayList<SakaiResource>();
	public List<SakaiResource> parseResource(String json) {
		List<SakaiResource> resources = new ArrayList<SakaiResource>();
		resources = parse(false, json);
		return resources;
	}
	
	public List<SakaiResource> parseChildResources(String json, String resId) {
		try {
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
                
        		if((alreadyExists(id, strArr[0], strArr.length==1?null:strArr[1])) || (strArr[0].equals(""))) {
        			continue;
        		}
        		
        		SakaiResource item = new SakaiResource();
        		item.setHasChild(false);
        		if(strArr.length > 1) {
                	item.setHasChild(true);
                	item.setChildResource(strArr[1]);
                }
        		item.setId(originalId);
                item.setTitle(strArr[0]);
                if (!item.getHasChild()){
                	String resExt [] = strArr[strArr.length-1].split("\\.");
                	if(resExt!=null && resExt.length!=0) {
                		item.setExtension(resExt[resExt.length-1]);
                		if((resExt[resExt.length-1]).equalsIgnoreCase("URL")){
                			item.setTitle(strArr[0].replace("\\.URL", ""));
                		}
                	}
                	else {
                		item.setExtension("None");
                	}
                }
                
                resources.add(item);
                
            }

        } catch (JSONException e) {
           
        } catch (Exception e) {
            
        }
		return resources;
	}
	
	private List<SakaiResource> parse(boolean convert, String json) {
    	
	     try {
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("resources_collection");

            for (int i = 0; i < itemArray.size(); i++) {
                String id = itemArray.getJSONObject(i).getString("resourceID");
                
                String strArr [] = id.split("/");
                String resArr [] = new String[strArr.length-3];
        		for(int j=0; j<(strArr.length-3); j++) {
        			resArr[j] = strArr[j+3];
        		}
        		
        		if(alreadyExists(id, resArr[0], resArr.length==1?null:resArr[1])) {
        			continue;
        		}
        		
        		SakaiResource item = new SakaiResource();
        		item.setHasChild(false);
        		if(resArr.length > 1) {
                	item.setHasChild(true);
                	item.setChildResource(resArr[1]);
                }
        		item.setId(id);
                item.setTitle(resArr[0]);
                if (!item.getHasChild()){
                	String resExt [] = resArr[resArr.length-1].split("\\.");
                	if(resExt!=null && resExt.length!=0) {
                		item.setExtension(resExt[resExt.length-1]);
                	}
                	else {
                		item.setExtension("None");
                	}
                }
                
                resources.add(item);
                
            }

        } catch (JSONException e) {
           
        } catch (Exception e) {
            
        }
		return resources;
    }
	
	private Boolean alreadyExists(String id, String title, String childRes) {
		Iterator<SakaiResource> iterator = resources.iterator();
		Boolean titleExists = false;
		int i = 0;
		while (iterator.hasNext()) {
			SakaiResource res = iterator.next();
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

}
