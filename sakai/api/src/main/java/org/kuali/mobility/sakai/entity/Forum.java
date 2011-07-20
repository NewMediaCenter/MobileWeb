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
 
package org.kuali.mobility.sakai.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Forum implements Serializable, Comparable<Forum> {

	private static final long serialVersionUID = -5535393238213510311L;
	
    private String title;
    private String typeUuid;
    private String forumId;
    private int unreadCount;
    private int messageCount;
    private List<ForumTopic> topics;
    
    public Forum() {
    	topics = new ArrayList<ForumTopic>();
    }
    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setTypeUuid(String typeUuid) {
		this.typeUuid = typeUuid;
	}

	public String getTypeUuid() {
		return typeUuid;
	}

	public void setForumId(String forumId) {
		this.forumId = forumId;
	}

	public String getForumId() {
		return forumId;
	}

    public int compareTo(Forum that) {
        if (this == null || that == null || this.getForumId() == null || that.getForumId() == null) {
            return -1;
        }
        return this.getForumId().compareTo(that.getForumId());
    }

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public List<ForumTopic> getTopics() {
		return topics;
	}

	public void setTopics(List<ForumTopic> topics) {
		this.topics = topics;
	}

		
}
