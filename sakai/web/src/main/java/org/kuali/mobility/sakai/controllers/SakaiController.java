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
import org.kuali.mobility.sakai.entity.SakaiAnnouncement;
import org.kuali.mobility.sakai.entity.SakaiAssignment;
import org.kuali.mobility.sakai.entity.SakaiHome;
import org.kuali.mobility.sakai.entity.SakaiRoster;
import org.kuali.mobility.sakai.entity.SakaiSite;
import org.kuali.mobility.sakai.service.SakaiCourseService;
import org.kuali.mobility.sakai.service.SakaiSessionService;
import org.kuali.mobility.shared.Constants;
import org.kuali.mobility.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.iu.es.espd.oauth.OAuth2LegService;
import edu.iu.uis.cas.filter.CASFilter;

@Controller
@RequestMapping("/myclasses")
public class SakaiController {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SakaiController.class);
	
	@Autowired
	private ConfigParamService configParamService;

	@Autowired
	private SakaiSessionService sakaiSessionService;

	@Autowired
	private OAuth2LegService oncourseOAuthService;
	
	@Autowired
	private SakaiCourseService sakaiCourseService;

	@RequestMapping(method = RequestMethod.GET)
	public String getClasses(HttpServletRequest request, Model uiModel) {
		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
		SakaiHome home = sakaiCourseService.findSakaiHome(user.getUserId());
		uiModel.addAttribute("home", home);
		return "sakai/home";
	}
	
	@RequestMapping(value="/{siteId}", method = RequestMethod.GET)
	public String getClass(HttpServletRequest request, @PathVariable("siteId") String siteId, Model uiModel) {
		try {
			String remoteUser = CASFilter.getRemoteUser(request);
			SakaiSite course = sakaiCourseService.findCourse(siteId, remoteUser);
			uiModel.addAttribute("course", course);
			
			String url = configParamService.findValueByName("Sakai.Url.Base") + "session.json";
			ResponseEntity<InputStream> is1 = oncourseOAuthService.oAuthGetRequest(remoteUser, url, "text/html");
			String jsonSession = IOUtils.toString(is1.getBody(), "UTF-8");
			String sessionId = sakaiSessionService.findSakaiSessionId(jsonSession);
			
			uiModel.addAttribute("sessionId", sessionId);
			uiModel.addAttribute("siteId", siteId);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/site";
	}
	
	@RequestMapping(value="/{siteId}/announcements", method = RequestMethod.GET)
	public String getClassAnnouncements(HttpServletRequest request, @PathVariable("siteId") String siteId, Model uiModel) {
		try {
			String remoteUser = CASFilter.getRemoteUser(request);
			List<SakaiAnnouncement> announcements = sakaiCourseService.findAllCourseAnnouncements(siteId, remoteUser);
			uiModel.addAttribute("sakaiannouncements", announcements);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/announcements";
	}
	
	@RequestMapping(value="/{siteId}/announcements/{annId}", method = RequestMethod.GET)
	public String getAnnouncementDetails(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("annId") String annId, Model uiModel) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "announcement/message/" + siteId + "/" + annId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");

			List<SakaiAnnouncement> announcements = sakaiCourseService.findAnnouncementDetails(json);
			uiModel.addAttribute("sakaiannouncements", announcements);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/announcement";
	}

	@RequestMapping(value="/{siteId}/assignments", method = RequestMethod.GET)
	public String getAssignments(HttpServletRequest request, @PathVariable("siteId") String siteId, Model uiModel) {
		try {
			String userId = CASFilter.getRemoteUser(request);

			List<SakaiAssignment> assignments = sakaiCourseService.findAllCourseAssignments(siteId, userId);
			uiModel.addAttribute("sakaiassignments", assignments);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/assignments";
	}
	
	@RequestMapping(value="/{siteId}/assignments/{assId}", method = RequestMethod.GET)
	public String getAssignment(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("assId") String assId, Model uiModel) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "assignment/submissions/" + CASFilter.getRemoteUser(request) + "/" + assId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");

			List<SakaiAssignment> assignments = sakaiCourseService.findAssignmentDetails(json);
			uiModel.addAttribute("sakaiassignments", assignments);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/assignment";
	}
		
	@RequestMapping(value="/{siteId}/grades", method = RequestMethod.GET)
	public String getGrades(HttpServletRequest request, @PathVariable("siteId") String siteId, Model uiModel) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "assignment/grades/" + siteId + "/" + CASFilter.getRemoteUser(request) + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");

			List<SakaiAssignment> assignments = sakaiCourseService.findAssignmentDetails(json);
			
			url = configParamService.findValueByName("Sakai.Url.Base") + "gradebook/courseGrade/" + siteId + "/" + CASFilter.getRemoteUser(request) + ".json";
			ResponseEntity<InputStream> is1 = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String gradeJson = IOUtils.toString(is1.getBody(), "UTF-8");
			String courseGrade = sakaiCourseService.findCourseGrade(gradeJson);
			
			uiModel.addAttribute("sakaigrades", assignments);
			uiModel.addAttribute("courseGrade", courseGrade);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/grades";
	}
	
	@RequestMapping(value="/{siteId}/roster", method = RequestMethod.GET)
	public String getRoster(HttpServletRequest request, @PathVariable("siteId") String siteId, Model uiModel) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "participant.json?siteId=" + siteId;
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");

			List<SakaiRoster> roster = sakaiCourseService.findCourseRoster(json);
			uiModel.addAttribute("roster", roster);
			
			url = configParamService.findValueByName("Sakai.Url.Base") + "session.json";
			ResponseEntity<InputStream> is1 = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String jsonSession = IOUtils.toString(is1.getBody(), "UTF-8");
			String sessionId = sakaiSessionService.findSakaiSessionId(jsonSession);
			uiModel.addAttribute("sessionId", sessionId);
			uiModel.addAttribute("siteId", siteId);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/roster";
	}
	
	@RequestMapping(value="/{siteId}/roster/{displayId}", method = RequestMethod.GET)
	public String getRosterDetails(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("displayId") String displayId, Model uiModel) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "participant.json?siteId=" + siteId;
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");

			List<SakaiRoster> roster = sakaiCourseService.findCourseParticipantDetails(json, displayId);
			uiModel.addAttribute("roster", roster);
			
			url = configParamService.findValueByName("Sakai.Url.Base") + "session.json";
			ResponseEntity<InputStream> is1 = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String jsonSession = IOUtils.toString(is1.getBody(), "UTF-8");
			String sessionId = sakaiSessionService.findSakaiSessionId(jsonSession);
			uiModel.addAttribute("sessionId", sessionId);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/rosterDetails";
	}

	
	public void setSakaiCourseService(SakaiCourseService sakaiCourseService) {
		this.sakaiCourseService = sakaiCourseService;
	}
	
	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
	
	public void setOncourseOAuthService(OAuth2LegService oncourseOAuthService) {
		this.oncourseOAuthService = oncourseOAuthService;
	}
}
