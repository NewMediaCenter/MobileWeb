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
 
package org.kuali.mobility.sakai.service;

import java.util.List;

import org.kuali.mobility.sakai.entity.SakaiForum;
import org.kuali.mobility.sakai.entity.SakaiForumMessage;
import org.springframework.stereotype.Service;

@Service
public class SakaiForumServiceImpl implements SakaiForumService {

	public List<SakaiForum> findCourseForums(String json) {
		SakaiForumParser parser = new SakaiForumParser();
		List<SakaiForum> forums = parser.parseForums(json);
		return forums;
	}
	public List<SakaiForumMessage> findTopicMessages(String json, String topicTitle) {
		SakaiForumParser parser = new SakaiForumParser();
		List<SakaiForumMessage> messages = parser.parseForumMessages(json, topicTitle, false);
		return messages;
	}
	public List<SakaiForumMessage> findMessageDetails(String json, String messageId, String messageTitle) {
		SakaiForumParser parser = new SakaiForumParser();
		List<SakaiForumMessage> messages = parser.parseForumMessageDetails(json, messageId, messageTitle, true);
		return messages;
	}
}
