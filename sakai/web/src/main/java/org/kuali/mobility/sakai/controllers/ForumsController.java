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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String getForumTopicThread(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("topicId") String topicId, @PathVariable("forumId") String forumId, @PathVariable("threadId") String threadId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			ForumThread thread = sakaiForumService.findThread(topicId, threadId, user.getUserId());
			thread.setForumId(forumId);
			uiModel.addAttribute("thread", thread);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		uiModel.addAttribute("siteId", siteId);
		return "sakai/forums/forumthread";
	}
	
	@RequestMapping(value = "/{forumId}/{topicId}/{threadId}/{messageId}/reply", method = RequestMethod.GET)
	public String reply(HttpServletRequest request, @PathVariable("siteId") String siteId, @PathVariable("forumId") String forumId, @PathVariable("topicId") String topicId, @PathVariable("threadId") String threadId, @PathVariable("messageId") String messageId, Model uiModel) {
		try {
			User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
			Message message = sakaiForumService.findMessage(messageId, topicId, user.getUserId());
			message.setTopicId(topicId);
			message.setThreadId(threadId);
			message.setTitle("Re: " + message.getTitle());
			message.setInReplyTo(messageId);
			message.setBody(null);
			
			uiModel.addAttribute("message", message);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		uiModel.addAttribute("forumId", forumId);
		uiModel.addAttribute("siteId", siteId);
		return "sakai/forums/forumsmessagereply";
	}

//	@RequestMapping(value = "/{forumId}/{topicId}", method = RequestMethod.POST)
//	public ResponseEntity<String> post(HttpServletRequest request, @RequestParam("title") String title, @RequestParam("body") String body, @PathVariable("topicId") String topicId, @PathVariable("forumId") String forumId) {
//		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
//		try {
//			String jsonString = "{";
//			jsonString += "\"attachments\": [],";
//			jsonString += "\"authoredBy\":\"" + user.getUserId() + "\", ";
//			jsonString += "\"body\":\"" + body + "\", ";
//			jsonString += "\"label\":\"" + "" + "\", ";
//			jsonString += "\"recipients\":\"" + "" + "\", ";
//			jsonString += "\"replies\":" + "null" + ", ";
//			jsonString += "\"replyTo\":" + "null" + ", ";
//			jsonString += "\"title\":\"" + title + "\", ";
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
//			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthPostRequest(user.getUserId(), url, "text/html", jsonString);
//			return new ResponseEntity<String>(is.getStatusCode());
//		} catch (Exception e) {
//			LOG.error(e.getMessage(), e);
//			return new ResponseEntity<String>(HttpStatus.METHOD_FAILURE);
//		}
//	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String getCreateJsp(Model uiModel) {
		return "sakai/forums/forumsthreadcreate";
	}
}
