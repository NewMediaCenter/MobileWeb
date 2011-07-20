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
import org.kuali.mobility.sakai.entity.Forum;
import org.kuali.mobility.sakai.entity.Message;
import org.kuali.mobility.sakai.entity.MessageFolder;
import org.kuali.mobility.sakai.service.SakaiPrivateTopicService;
import org.kuali.mobility.shared.Constants;
import org.kuali.mobility.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.iu.es.espd.oauth.OAuth2LegService;

@Controller
@RequestMapping("myclasses/{siteId}/messages")
public class PrivateMessagesController {
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PrivateMessagesController.class);
	
	@Autowired
	private ConfigParamService configParamService;

	@Autowired
	private OAuth2LegService oncourseOAuthService;

	@Autowired
	private SakaiPrivateTopicService sakaiPrivateTopicService;

	@RequestMapping(method = RequestMethod.GET)
	public String getMessages(HttpServletRequest request, @PathVariable("siteId") String siteId, Model uiModel) {
		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
		List<Forum> topics = sakaiPrivateTopicService.findPrivateTopics(siteId, user.getUserId());
		uiModel.addAttribute("sakaiprivatetopics", topics);
		uiModel.addAttribute("siteId", siteId);
		return "sakai/forums/privatetopics";
	}

	@RequestMapping(value = "/folder/{typeUuid}", method = RequestMethod.GET)
	public String getFolder(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("typeUuid") String typeUuid, @RequestParam("title") String title, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			MessageFolder messages = sakaiPrivateTopicService.findPrivateMessages(siteId, typeUuid, user.getUserId());
			messages.setTitle(title);
			uiModel.addAttribute("messageFolder", messages);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		uiModel.addAttribute("siteId", siteId);
		return "sakai/forums/privatemessages";
	}

	@RequestMapping(value = "/folder/{typeUuid}/{messageId}", method = RequestMethod.GET)
	public String getMessage(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("typeUuid") String typeUuid, @PathVariable("messageId") String messageId, Model uiModel) {
		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
		Message message = sakaiPrivateTopicService.findPrivateMessageDetails(user.getUserId(), siteId, typeUuid, messageId);
		uiModel.addAttribute("message", message);
		uiModel.addAttribute("siteId", siteId);
		uiModel.addAttribute("typeUuid", typeUuid);
		return "sakai/forums/privatemessagedetails";
	}
	
	@RequestMapping(value = "/compose", method = RequestMethod.GET)
	public String getCompose(@PathVariable("siteId") String siteId, Model uiModel) {
		uiModel.addAttribute("siteId", siteId);
		uiModel.addAttribute("message", new Message());
		return "sakai/forums/privatemessagecreate";
	}
	
	@RequestMapping(value = "/compose", method = RequestMethod.POST)
	public String postMesssage(HttpServletRequest request, Model uiModel, @ModelAttribute("message") Message message, BindingResult result, @PathVariable("siteId") String siteId) {
		if (isValidMessage(message, result)) {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			submitMessage(message, siteId, user.getUserId());
			return "feedback/thanks";                	
		} else {
			return "feedback/form";    	
		}
	}
	
	@RequestMapping(value = "/folder/{typeUuid}/{messageId}/reply", method = RequestMethod.GET)
	public String getReply(@PathVariable("siteId") String siteId, Model uiModel) {
		uiModel.addAttribute("siteId", siteId);
		return "sakai/forums/privatemessagereply";
	}
	
//	@RequestMapping(method = RequestMethod.POST)
//	public ResponseEntity<String> post(HttpServletRequest request, @RequestParam("to") String to, @RequestParam("title") String title, @RequestParam("body") String body, @RequestParam("siteId") String siteId) {
//		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
//		submitData(to, title, body, siteId, user.getUserId());
//		return new ResponseEntity<String>(HttpStatus.CREATED);
//	}
	
	private boolean isValidMessage(Message m, BindingResult result) {
    	boolean hasErrors = false;
    	Errors errors = ((Errors) result);
    	if (m.getTitle() == null || "".equals(m.getTitle().trim())) {
    		errors.rejectValue("title", "", "Please enter a message subject.");
    		hasErrors = true;
    	}
    	if (m.getRecipients() == null || "".equals(m.getRecipients().trim())) {
     		errors.rejectValue("recipients", "", "Please enter a recipient of the message.");
     		hasErrors = true;    		
     	}
    	if (m.getBody() == null || "".equals(m.getBody().trim())) {
     		errors.rejectValue("body", "", "Please enter a message to send.");
     		hasErrors = true;    		
     	}
    	return !hasErrors;
    }

	private String submitMessage(Message message,String siteId, String userId) {
		String response = "";
		try {
			String jsonString = "{";
			jsonString += "\"attachments\": [],";
			jsonString += "\"authoredBy\":\"" + userId + "\", ";
			jsonString += "\"body\":\"" + message.getBody() + "\", ";
			jsonString += "\"label\":\"" + "" + "\", ";
			jsonString += "\"recipients\":\"" + message.getRecipients() + "\", ";
			jsonString += "\"replies\":" + "null" + ", ";
			jsonString += "\"replyTo\":" + "null" + ", ";
			jsonString += "\"title\":\"" + message.getTitle() + "\", ";
//			jsonString += "\"topicId\":\"" + null + "\", ";
//			jsonString += "\"typeUuid\":\""+ "4d9593ed-aaee-4826-b74a-b3c3432b384c" + "\", ";
			jsonString += "\"siteId\":\""+ siteId + "\", ";
			jsonString += "\"read\":" + "false" + ", ";
//			jsonString += "\"entityReference\":\"" + "\\/forum_message" + "\", ";
			//jsonString += "\"entityURL\":\"" + "http:\\/\\/localhost:8080\\/direct\\/forum_message" + "\", ";
			//jsonString += "\"entityTitle\":\"" + subject + "\"";

			jsonString += "}";

			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_message/new.json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthPostRequest(userId, url, "text/html", jsonString);
			response = IOUtils.toString(is.getBody(), "UTF-8");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return response;
	}
	
	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
	
	public void setOncourseOAuthService(OAuth2LegService oncourseOAuthService) {
		this.oncourseOAuthService = oncourseOAuthService;
	}
	
	public void setSakaiPrivateTopicService(SakaiPrivateTopicService sakaiPrivateTopicService) {
		this.sakaiPrivateTopicService = sakaiPrivateTopicService;
	}
}
