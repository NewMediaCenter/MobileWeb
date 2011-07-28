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

import org.kuali.mobility.sakai.entity.Forum;
import org.kuali.mobility.sakai.entity.ForumThread;
import org.kuali.mobility.sakai.entity.ForumTopic;
import org.kuali.mobility.sakai.entity.Message;
import org.kuali.mobility.sakai.service.SakaiForumService;
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
@RequestMapping("/myclasses/{siteId}/forums")
public class ForumsController {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ForumsController.class);

	@Autowired
	private SakaiForumService sakaiForumService;

	public void setSakaiForumService(SakaiForumService sakaiForumService) {
		this.sakaiForumService = sakaiForumService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getForums(HttpServletRequest request, @PathVariable("siteId") String siteId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);

			List<Forum> forums = sakaiForumService.findForums(siteId, user.getUserId());
			uiModel.addAttribute("forums", forums);
			uiModel.addAttribute("siteId", siteId);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/forums/forums";
	}
	
	@RequestMapping(value = "/{forumId}", method = RequestMethod.GET)
	public String getForum(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("forumId") String forumId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);

			Forum forum = sakaiForumService.findForum(forumId, user.getUserId());
			uiModel.addAttribute("forum", forum);
			uiModel.addAttribute("siteId", siteId);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return "sakai/forums/forum";
	}

	@RequestMapping(value = "/{forumId}/{topicId}", method = RequestMethod.GET)
	public String getForumTopic(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("topicId") String topicId, @RequestParam("title") String topicTitle, @PathVariable("forumId") String forumId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			ForumTopic topic = sakaiForumService.findTopic(topicId, user.getUserId(), topicTitle);
			topic.setForumId(forumId);
			uiModel.addAttribute("topic", topic);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		uiModel.addAttribute("siteId", siteId);
		return "sakai/forums/forumtopic";
	}
	
	@RequestMapping(value = "/{forumId}/{topicId}/{threadId}", method = RequestMethod.GET)
	public String getForumTopicThread(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("topicId") String topicId, @RequestParam("topicTitle") String topicTitle, @PathVariable("forumId") String forumId, @PathVariable("threadId") String threadId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			ForumThread thread = sakaiForumService.findThread(topicId, threadId, user.getUserId());
			thread.setForumId(forumId);
			thread.setTopicTitle(topicTitle);
			uiModel.addAttribute("thread", thread);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		uiModel.addAttribute("siteId", siteId);
		return "sakai/forums/forumthread";
	}
	
	@RequestMapping(value = "/{forumId}/{topicId}/{threadId}/{messageId}/markread/ajax", method = RequestMethod.GET)
	public ResponseEntity<String> markMessageRead(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("messageId") String messageId, Model uiModel) {
		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
		return sakaiForumService.markMessageRead(siteId, messageId, user.getUserId());
	}
	
	@RequestMapping(value = "/{forumId}/{topicId}/{threadId}/{messageId}/markread", method = RequestMethod.GET)
	public String markMessageRead(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("topicId") String topicId, @RequestParam("topicTitle") String topicTitle, @PathVariable("forumId") String forumId, @PathVariable("threadId") String threadId, @PathVariable("messageId") String messageId, Model uiModel) {
		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
		sakaiForumService.markMessageRead(siteId, messageId, user.getUserId());
		return getForumTopicThread(request, siteId, topicId, topicTitle, forumId, threadId, uiModel);
	}
	
	@RequestMapping(value = "/{forumId}/{topicId}/{threadId}/{messageId}/reply", method = RequestMethod.GET)
	public String reply(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("forumId") String forumId, @PathVariable("topicId") String topicId, @RequestParam("topicTitle") String topicTitle, @PathVariable("threadId") String threadId, @PathVariable("messageId") String messageId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			Message message = sakaiForumService.findMessage(messageId, topicId, user.getUserId());
			message.setTopicId(topicId);
			message.setThreadId(threadId);
			message.setTopicTitle(topicTitle);
			message.setForumId(forumId);
			message.setTitle("Re: " + message.getTitle());
			message.setInReplyTo(messageId);
			message.setBody(null);
			
			uiModel.addAttribute("message", message);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		uiModel.addAttribute("siteId", siteId);
		return "sakai/forums/forumsmessagereply";
	}
	
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(HttpServletRequest request, @PathVariable("siteId") String siteId, @ModelAttribute("message") Message message, BindingResult result, Model uiModel) {
		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
		ResponseEntity<String> response = sakaiForumService.postMessage(message, user.getUserId());
		
		if (response.getStatusCode().value() < 200 || response.getStatusCode().value() >= 300) {
			Errors errors = ((Errors) result);
	    	errors.rejectValue("body", "", "There was an error posting your reply. Please try again later.");
	    	uiModel.addAttribute("siteId", siteId);
	    	return "sakai/forums/forumsmessagereply";
		}
		
		return getForumTopicThread(request, siteId, message.getTopicId(), message.getTopicTitle(), message.getForumId(), message.getThreadId(), uiModel);
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String getCreateJsp(Model uiModel) {
		return "sakai/forums/forumsthreadcreate";
	}
}
