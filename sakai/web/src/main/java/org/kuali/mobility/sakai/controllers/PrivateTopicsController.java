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
import org.kuali.mobility.sakai.service.SakaiPrivateTopicService;
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
@RequestMapping("/sakaiprivatetopics")
public class PrivateTopicsController {
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
	private SakaiPrivateTopicService sakaiPrivateTopicService;

	public void setSakaiPrivateTopicService(SakaiPrivateTopicService sakaiPrivateTopicService) {
		this.sakaiPrivateTopicService = sakaiPrivateTopicService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getList(HttpServletRequest request, @RequestParam("siteId") String siteId, Model uiModel) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_topic/private/" + siteId + ".json";
			String user = CASFilter.getRemoteUser(request);
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");

			List<SakaiForum> topics = sakaiPrivateTopicService.findPrivateTopics(json);
			uiModel.addAttribute("sakaiprivatetopics", topics);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "sakaiforums/privatetopics";
	}

//	@RequestMapping(method = RequestMethod.POST)
//	public ResponseEntity<String> post(HttpServletRequest request, @RequestParam("title") String title, @RequestParam("body") String body, @RequestParam("topicId") String topicId, @RequestParam("forumId") String forumId) {
//		submitData(title, body, topicId, forumId, CASFilter.getRemoteUser(request));
//		return new ResponseEntity<String>(HttpStatus.CREATED);
//	}
//
//	private String submitData(String subject, String comment, String topicId, String forumId, String userId) {
//		String response = "";
//		try {
//			String jsonString = "{";
//			jsonString += "\"attachments\": [],";
//			jsonString += "\"authoredBy\":\"" + userId + "\", ";
//			jsonString += "\"body\":\"" + comment + "\", ";
//			jsonString += "\"label\":\"" + "" + "\", ";
//			jsonString += "\"recipients\":\"" + "" + "\", ";
//			jsonString += "\"replies\":" + "null" + ", ";
//			jsonString += "\"replyTo\":" + "null" + ", ";
//			jsonString += "\"title\":\"" + subject + "\", ";
//			jsonString += "\"topicId\":\"" + topicId + "\", ";
//			jsonString += "\"forumId\":\"" + forumId + "\", ";
//			jsonString += "\"read\":" + "false" + ", ";
//			jsonString += "\"entityReference\":\"" + "\\/forum_message" + "\", ";
//			//jsonString += "\"entityURL\":\"" + "http:\\/\\/localhost:8080\\/direct\\/forum_message" + "\", ";
//			//jsonString += "\"entityTitle\":\"" + subject + "\"";
//
//			jsonString += "}";
//
//			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_message/new.json";
//			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthPostRequest(userId, url, "text/html", jsonString);
//			response = IOUtils.toString(is.getBody(), "UTF-8");
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (NullPointerException e) {
//			e.printStackTrace();
//		} catch (OAuthException e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
}
