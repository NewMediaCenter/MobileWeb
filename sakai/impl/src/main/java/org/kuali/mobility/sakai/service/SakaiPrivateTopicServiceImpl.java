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
public class SakaiPrivateTopicServiceImpl implements SakaiPrivateTopicService {

	public List<SakaiForum> findPrivateTopics(String json) {
		SakaiPrivateTopicParser parser = new SakaiPrivateTopicParser();
		List<SakaiForum> forums = parser.parsePrivateTopics(json);
		return forums;
	}
	
	public List<SakaiForumMessage> findPrivateMessages(String json) {
		SakaiPrivateTopicParser parser = new SakaiPrivateTopicParser();
		List<SakaiForumMessage> forums = parser.parsePrivateMessages(json);
		return forums;
	}
	
	public List<SakaiForumMessage> findPrivateMessageDetails(String json, String messageId, String messageTitle) {
		SakaiPrivateTopicParser parser = new SakaiPrivateTopicParser();
		List<SakaiForumMessage> forums = parser.parsePrivateMessageDetails(json, messageId, messageTitle);
		return forums;
	}

}
