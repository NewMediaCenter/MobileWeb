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
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.sakai.entity.Announcement;
import org.kuali.mobility.sakai.entity.Assignment;
import org.kuali.mobility.sakai.entity.Forum;
import org.kuali.mobility.sakai.entity.ForumTopic;
import org.kuali.mobility.sakai.entity.Home;
import org.kuali.mobility.sakai.entity.Resource;
import org.kuali.mobility.sakai.entity.Roster;
import org.kuali.mobility.sakai.entity.Site;
import org.kuali.mobility.sakai.service.SakaiForumService;
import org.kuali.mobility.sakai.service.SakaiPrivateTopicService;
import org.kuali.mobility.sakai.service.SakaiSiteService;
import org.kuali.mobility.shared.Constants;
import org.kuali.mobility.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.iu.es.espd.oauth.OAuth2LegService;

@Controller
@RequestMapping("/myclasses")
public class SakaiController {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SakaiController.class);
	
	@Autowired
	private ConfigParamService configParamService;

	@Autowired
	private OAuth2LegService oncourseOAuthService;
	
	@Autowired
	private SakaiSiteService sakaiSiteService;
	
	@Autowired
	private SakaiForumService sakaiForumService;
	
	@Autowired
	private SakaiPrivateTopicService sakaiPrivateTopicService;

	@RequestMapping(method = RequestMethod.GET)
	public String getSites(HttpServletRequest request, @RequestParam(value="date", required=false) String date, Model uiModel) {
		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
		Home home = sakaiSiteService.findSakaiHome(user.getUserId(), date);
		uiModel.addAttribute("home", home);
		uiModel.addAttribute("tabCount", (home.getCourses() != null && home.getCourses().size() > 0 ? 1 : 0) + (home.getProjects() != null && home.getProjects().size() > 0 ? 1 : 0) + (home.getOther() != null && home.getOther().size() > 0 ? 1 : 0)  + (home.getTodaysCourses() != null && home.getTodaysCourses().size() > 0 ? 1 : 0));
		
		Calendar todayDate = Calendar.getInstance();
		if (date != null) {
			try {
				todayDate.setTime(SakaiSiteService.DATE_FORMAT.parse(date));
			} catch (Exception e) {}
		}
		todayDate.set(Calendar.HOUR, 0);
		todayDate.set(Calendar.MINUTE, 0);
		todayDate.set(Calendar.SECOND, 0);
		todayDate.set(Calendar.MILLISECOND, 0);
		todayDate.add(Calendar.DATE, -1);
		uiModel.addAttribute("yesterday", SakaiSiteService.DATE_FORMAT.format(todayDate.getTime()));
		todayDate.add(Calendar.DATE, 2);
		uiModel.addAttribute("tomorrow", SakaiSiteService.DATE_FORMAT.format(todayDate.getTime()));
		return "sakai/home";
	}
	
	@RequestMapping(value="/{siteId}", method = RequestMethod.GET)
	public String getSite(HttpServletRequest request, @PathVariable("siteId") String siteId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			Site site = sakaiSiteService.findSite(siteId, user.getUserId());
			uiModel.addAttribute("site", site);
			
			List<Forum> forums = sakaiForumService.findForums(siteId, user.getUserId());
			int forumCount = 0;
			for (Forum forum : forums) {
				forumCount += forum.getUnreadCount();
			}
			
			List<ForumTopic> topics = sakaiPrivateTopicService.findPrivateTopics(siteId, user.getUserId());
			int messageCount = 0;
			for (ForumTopic topic : topics) {
				messageCount += topic.getUnreadCount();
			}

			uiModel.addAttribute("siteId", siteId);
			uiModel.addAttribute("forumCount", forumCount);
			uiModel.addAttribute("messageCount", messageCount);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/site";
	}
	
	@RequestMapping(value="/{siteId}/announcements", method = RequestMethod.GET)
	public String getAnnouncements(HttpServletRequest request, @PathVariable("siteId") String siteId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			List<Announcement> announcements = sakaiSiteService.findAllAnnouncements(siteId, user.getUserId());
			uiModel.addAttribute("announcements", announcements);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		uiModel.addAttribute("siteId", siteId);
		return "sakai/announcements";
	}
	
	@RequestMapping(value="/{siteId}/announcements/{annId}", method = RequestMethod.GET)
	public String getAnnouncement(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("annId") String annId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			String url = configParamService.findValueByName("Sakai.Url.Base") + "announcement/message/" + siteId + "/" + annId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(user.getUserId(), url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");

			Announcement announcement = sakaiSiteService.findAnnouncementDetails(json);
			uiModel.addAttribute("announcement", announcement);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		uiModel.addAttribute("siteId", siteId);
		return "sakai/announcement";
	}
	
	@RequestMapping(value="/{siteId}/attachment", method = RequestMethod.GET)
	public String getAnnouncementAttachment(HttpServletRequest request, HttpServletResponse response, @PathVariable("siteId") String siteId, @RequestParam(value="attachmentId") String attachmentId, @RequestParam(value="type") String type, Model uiModel) {
		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
		
		byte [] fileData = sakaiSiteService.findAnnouncementAttachment(siteId, attachmentId, user.getUserId());
		
		try {
			if (type.equals(SakaiSiteService.URL_MIME_TYPE)) {
				String url = new String(fileData);
				response.sendRedirect(response.encodeRedirectURL(url));
			} else {
				response.setContentType(type);
				response.setContentLength(fileData.length);
				response.setHeader("Content-Disposition", "attachment; filename=\"" + getFileName(attachmentId) + "\"" );
				response.getOutputStream().write(fileData, 0, fileData.length);
				return null;
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@RequestMapping(value="/{siteId}/assignments", method = RequestMethod.GET)
	public String getAssignments(HttpServletRequest request, @PathVariable("siteId") String siteId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);

			List<Assignment> assignments = sakaiSiteService.findAllAssignments(siteId, user.getUserId());
			uiModel.addAttribute("sakaiassignments", assignments);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/assignments";
	}
	
	@RequestMapping(value="/{siteId}/assignments/{assId}", method = RequestMethod.GET)
	public String getAssignment(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("assId") String assId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			String url = configParamService.findValueByName("Sakai.Url.Base") + "assignment/submissions/" + user.getUserId() + "/" + assId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(user.getUserId(), url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");

			List<Assignment> assignments = sakaiSiteService.findAssignmentDetails(json);
			uiModel.addAttribute("sakaiassignments", assignments);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/assignment";
	}
		
	@RequestMapping(value="/{siteId}/grades", method = RequestMethod.GET)
	public String getGrades(HttpServletRequest request, @PathVariable("siteId") String siteId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			String url = configParamService.findValueByName("Sakai.Url.Base") + "assignment/grades/" + siteId + "/" + user.getUserId() + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(user.getUserId(), url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");

			List<Assignment> assignments = sakaiSiteService.findAssignmentDetails(json);
			
			url = configParamService.findValueByName("Sakai.Url.Base") + "gradebook/courseGrade/" + siteId + "/" + user.getUserId() + ".json";
			ResponseEntity<InputStream> is1 = oncourseOAuthService.oAuthGetRequest(user.getUserId(), url, "text/html");
			String gradeJson = IOUtils.toString(is1.getBody(), "UTF-8");
			String courseGrade = sakaiSiteService.findCourseGrade(gradeJson);
			
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
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			List<Roster> roster = sakaiSiteService.findRoster(siteId, user.getUserId());
			uiModel.addAttribute("roster", roster);
			uiModel.addAttribute("siteId", siteId);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return "sakai/roster";
	}
	
	@RequestMapping(value="/{siteId}/roster/{displayId}", method = RequestMethod.GET)
	public String getRosterDetails(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("displayId") String displayId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			String url = configParamService.findValueByName("Sakai.Url.Base") + "participant.json?siteId=" + siteId;
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(user.getUserId(), url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");

			Roster roster = sakaiSiteService.findParticipantDetails(json, displayId);
			uiModel.addAttribute("roster", roster);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		uiModel.addAttribute("siteId", siteId);
		return "sakai/rosterDetails";
	}
	
	@RequestMapping(value="/{siteId}/resources", method = RequestMethod.GET)
	public String getResources(HttpServletRequest request, HttpServletResponse response, @PathVariable("siteId") String siteId, @RequestParam(value="resId", required=false) String resId, @RequestParam(value="type", required=false) String type, Model uiModel) {
	    try {
	        //resId = URLEncoder.encode(resId, "UTF-8");
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			if (resId == null || resId.isEmpty()) {
				List<Resource> resources = sakaiSiteService.findSiteResources(siteId, user.getUserId(), null);
				uiModel.addAttribute("resources", resources);
			} else {
				char lastChar = resId.charAt(resId.length()-1);
    			if(lastChar == '/'){
    				//Go into a sub-folder
    				List<Resource> resources = sakaiSiteService.findSiteResources(siteId, user.getUserId(), resId);
    				uiModel.addAttribute("resources", resources);
    			} else {
    				//download the file
					byte [] fileData = sakaiSiteService.getResource(resId, user.getUserId());
					if (fileData!= null) {
						String mimeType = type;
						
						if (mimeType.equals(SakaiSiteService.URL_MIME_TYPE)) {
							String url = new String(fileData);
							response.sendRedirect(response.encodeRedirectURL(url));
						} else {
							response.setContentType(mimeType);
							response.setContentLength(fileData.length);
							response.setHeader("Content-Disposition", "attachment; filename=\"" + getFileName(resId) + "\"" );
							response.getOutputStream().write(fileData, 0, fileData.length);
							return null;
						}
	    			} else {
	    				return null;
	    			}
    			}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		uiModel.addAttribute("siteId", siteId);
		return "sakai/resources";
	}
	
	private String getFileName(String resourceId) {
		int index = resourceId.lastIndexOf("/");
		if (index == -1) {
			return resourceId;
		} else {
			return resourceId.substring(index +1 );
		}
	}
	
	public void setSakaiSiteService(SakaiSiteService sakaiSiteService) {
		this.sakaiSiteService = sakaiSiteService;
	}
	
	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
	
	public void setOncourseOAuthService(OAuth2LegService oncourseOAuthService) {
		this.oncourseOAuthService = oncourseOAuthService;
	}

	public SakaiForumService getSakaiForumService() {
		return sakaiForumService;
	}

	public void setSakaiForumService(SakaiForumService sakaiForumService) {
		this.sakaiForumService = sakaiForumService;
	}

	public SakaiPrivateTopicService getSakaiPrivateTopicService() {
		return sakaiPrivateTopicService;
	}

	public void setSakaiPrivateTopicService(SakaiPrivateTopicService sakaiPrivateTopicService) {
		this.sakaiPrivateTopicService = sakaiPrivateTopicService;
	}
}
