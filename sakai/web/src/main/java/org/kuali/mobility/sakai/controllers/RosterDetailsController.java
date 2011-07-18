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

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.sakai.entity.SakaiRoster;
import org.kuali.mobility.sakai.service.SakaiRosterService;
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
@RequestMapping("/sakaiparticipantdetails")
public class RosterDetailsController {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RosterDetailsController.class);
	
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
	private SakaiRosterService sakaiRosterService;

	public void setSakaiRosterService(SakaiRosterService sakaiRosterService) {
		this.sakaiRosterService = sakaiRosterService;
	}

	@Autowired
	private SakaiSessionService sakaiSessionService;

	public void setSakaiSessionService(SakaiSessionService sakaiSessionService) {
		this.sakaiSessionService = sakaiSessionService;
	}
	@RequestMapping(method = RequestMethod.GET)
	public String getList(HttpServletRequest request, @RequestParam("siteId") String siteId, @RequestParam("displayId") String displayId, Model uiModel) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "participant.json?siteId=" + siteId;
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");

			List<SakaiRoster> roster = sakaiRosterService.findCourseParticipantDetails(json, displayId);
			uiModel.addAttribute("roster", roster);
			
			url = configParamService.findValueByName("Sakai.Url.Base") + "session.json";
			ResponseEntity<InputStream> is1 = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String jsonSession = IOUtils.toString(is1.getBody(), "UTF-8");
			String sessionId = sakaiSessionService.findSakaiSessionId(jsonSession);
			uiModel.addAttribute("sessionId", sessionId);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakairoster/rosterDetails";
	}

}
