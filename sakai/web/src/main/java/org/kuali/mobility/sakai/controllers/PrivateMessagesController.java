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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.kuali.mobility.sakai.entity.ForumTopic;
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

@Controller
@RequestMapping("myclasses/{siteId}/messages")
public class PrivateMessagesController {
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PrivateMessagesController.class);

	@Autowired
	private SakaiPrivateTopicService sakaiPrivateTopicService;

	@RequestMapping(method = RequestMethod.GET)
	public String getMessages(HttpServletRequest request, @PathVariable("siteId") String siteId, Model uiModel) {
		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
		List<ForumTopic> topics = sakaiPrivateTopicService.findPrivateTopics(siteId, user.getUserId());
		uiModel.addAttribute("privatetopics", topics);
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
	
	@RequestMapping(value = "/folder/{typeUuid}/{messageId}/markread/ajax", method = RequestMethod.GET)
	public ResponseEntity<String> markMessageRead(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("messageId") String messageId, Model uiModel) {
		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
		return sakaiPrivateTopicService.markMessageRead(siteId, messageId, user.getUserId());
	}
	
	@RequestMapping(value = "/folder/{typeUuid}/{messageId}/markread", method = RequestMethod.GET)
	public String markMessageRead(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("typeUuid") String typeUuid, @PathVariable("messageId") String messageId, @RequestParam("title") String title, Model uiModel) {
		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
		sakaiPrivateTopicService.markMessageRead(siteId, messageId, user.getUserId());
		return getFolder(request, siteId, typeUuid, title, uiModel);
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
			ResponseEntity<String> response = sakaiPrivateTopicService.postMessage(message, siteId, user.getUserId());
			
			if (response.getStatusCode().value() < 200 || response.getStatusCode().value() >= 300) {
				Errors errors = ((Errors) result);
		    	errors.rejectValue("body", "", "There was an error posting your message. Please try again later.");
		    	uiModel.addAttribute("siteId", siteId);
		    	return "sakai/forums/privatemessagecreate";  
			}
			
			return getMessages(request, siteId, uiModel);
		} else {
			return "sakai/forums/privatemessagecreate";  
		}
	}
	
	@RequestMapping(value = "/folder/{typeUuid}/{messageId}/reply", method = RequestMethod.GET)
	public String getReply(@PathVariable("siteId") String siteId, Model uiModel) {
		uiModel.addAttribute("siteId", siteId);
		return "sakai/forums/privatemessagereply";
	}
	
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
	
	public void setSakaiPrivateTopicService(SakaiPrivateTopicService sakaiPrivateTopicService) {
		this.sakaiPrivateTopicService = sakaiPrivateTopicService;
	}
}
