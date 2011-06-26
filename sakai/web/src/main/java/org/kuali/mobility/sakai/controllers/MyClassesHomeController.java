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
 
package org.kuali.mobility.sakai.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.sakai.entity.MyClassesHome;
import org.kuali.mobility.sakai.service.MyClassesHomeService;
import org.kuali.mobility.sakai.service.SakaiSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.iu.es.espd.oauth.OAuth2LegService;
import edu.iu.uis.cas.filter.CASFilter;

@Controller
@RequestMapping("/myclasseshome")
public class MyClassesHomeController {

	@Autowired
	private ConfigParamService configParamService;

	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}

	@Autowired
	private OAuth2LegService oncourseOAuthService;

	public void setOncourseOAuthService(OAuth2LegService oncourseOAuthService) {
		this.oncourseOAuthService = oncourseOAuthService;
	}

	@Autowired
	private MyClassesHomeService myClassesHomeService;

	public void setMyClassesHomeService(MyClassesHomeService myClassesHomeService) {
		this.myClassesHomeService = myClassesHomeService;
	}
	@Autowired
	private SakaiSessionService sakaiSessionService;

	public void setSakaiSessionService(SakaiSessionService sakaiSessionService) {
		this.sakaiSessionService = sakaiSessionService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getList(HttpServletRequest request, @RequestParam("siteId") String siteId, Model uiModel) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "participant.json?siteId=" + siteId;
			String instructorName = "Not Defined";
			String instructorId = "";
			try {
				ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
				String json = IOUtils.toString(is.getBody(), "UTF-8");
				JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json.toString());
				JSONArray itemArray = jsonObj.getJSONArray("participant_collection");
				for (int j = 0; j < itemArray.size(); j++) {
					String roleTitle = itemArray.getJSONObject(j).getString("roleTitle");
					if (roleTitle.equalsIgnoreCase("Instructor")) {
						instructorId = itemArray.getJSONObject(j).getString("displayID");
						instructorName = itemArray.getJSONObject(j).getString("displayName");
					}
				}

			} catch (IOException e) {
				// runOnUiThread(returnErrorNetwork);
			} catch (JSONException e) {}

			List<MyClassesHome> classHome = myClassesHomeService.findMyClassHomeByCampus("BL", instructorId, instructorName);
			uiModel.addAttribute("myclasseshome", classHome);
			
			url = configParamService.findValueByName("Sakai.Url.Base") + "session.json";
			ResponseEntity<InputStream> is1 = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String jsonSession = IOUtils.toString(is1.getBody(), "UTF-8");
			String sessionId = sakaiSessionService.findSakaiSessionId(jsonSession);
			uiModel.addAttribute("sessionId", sessionId);
			
			uiModel.addAttribute("userId", CASFilter.getRemoteUser(request));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "myclasseshome/list";
	}

}
