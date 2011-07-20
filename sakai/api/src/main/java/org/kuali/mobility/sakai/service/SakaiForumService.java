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

import org.kuali.mobility.sakai.entity.Forum;
import org.kuali.mobility.sakai.entity.ForumThread;
import org.kuali.mobility.sakai.entity.ForumTopic;
import org.kuali.mobility.sakai.entity.Message;
import org.springframework.http.ResponseEntity;

public interface SakaiForumService {
	public List<Forum> findForums(String siteId, String userId);
	public Forum findForum(String forumId, String userId);
	public ForumTopic findTopic(String topicId, String userId, String topicTitle);
	public ForumThread findThread(String topicId, String threadId, String userId);
	public Message findMessage(String messageId, String topicId, String userId);
	public ResponseEntity<String> postMessage(Message message, String userId);
}
