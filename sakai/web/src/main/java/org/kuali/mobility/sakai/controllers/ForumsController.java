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
import org.kuali.mobility.sakai.entity.SakaiForum;
import org.kuali.mobility.sakai.entity.SakaiForumMessage;
import org.kuali.mobility.sakai.service.SakaiForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.iu.es.espd.oauth.OAuth2LegService;
import edu.iu.uis.cas.filter.CASFilter;

@Controller
@RequestMapping("/myclasses/{siteId}/forums")
public class ForumsController {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ForumsController.class);
	
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
	private SakaiForumService sakaiForumService;

	public void setSakaiForumService(SakaiForumService sakaiForumService) {
		this.sakaiForumService = sakaiForumService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getForums(HttpServletRequest request, @PathVariable("siteId") String siteId, Model uiModel) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_topic/site/" + siteId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");

			List<SakaiForum> forums = sakaiForumService.findCourseForums(json);
			uiModel.addAttribute("sakaiforums", forums);
			uiModel.addAttribute("siteId", siteId);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/forums/forums";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String getCreateJsp(Model uiModel) {
		return "sakai/forums/forumsthreadcreate";
	}

	@RequestMapping(value = "/{forumId}/{topicId}", method = RequestMethod.GET)
	public String getForumMessages(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("topicId") String topicId, @RequestParam("topicTitle") String topicTitle, @PathVariable("forumId") String forumId, Model uiModel) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_message/topic/" + topicId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");

			List<SakaiForumMessage> messages = sakaiForumService.findTopicMessages(json, topicTitle);
			uiModel.addAttribute("sakaiforumsmessages", messages);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/forums/forumsmessages";
	}

	@RequestMapping(value = "/{forumId}/{topicId}", method = RequestMethod.POST)
	public ResponseEntity<String> post(HttpServletRequest request, @RequestParam("title") String title, @RequestParam("body") String body, @PathVariable("topicId") String topicId, @PathVariable("forumId") String forumId) {
		String userId = CASFilter.getRemoteUser(request);
		try {
			String jsonString = "{";
			jsonString += "\"attachments\": [],";
			jsonString += "\"authoredBy\":\"" + userId + "\", ";
			jsonString += "\"body\":\"" + body + "\", ";
			jsonString += "\"label\":\"" + "" + "\", ";
			jsonString += "\"recipients\":\"" + "" + "\", ";
			jsonString += "\"replies\":" + "null" + ", ";
			jsonString += "\"replyTo\":" + "null" + ", ";
			jsonString += "\"title\":\"" + title + "\", ";
			jsonString += "\"topicId\":\"" + topicId + "\", ";
			jsonString += "\"forumId\":\"" + forumId + "\", ";
			jsonString += "\"read\":" + "false" + ", ";
			jsonString += "\"entityReference\":\"" + "\\/forum_message" + "\", ";
			//jsonString += "\"entityURL\":\"" + "http:\\/\\/localhost:8080\\/direct\\/forum_message" + "\", ";
			//jsonString += "\"entityTitle\":\"" + subject + "\"";

			jsonString += "}";

			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_message/new.json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthPostRequest(userId, url, "text/html", jsonString);
			return new ResponseEntity<String>(is.getStatusCode());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.METHOD_FAILURE);
		}
	}
}
