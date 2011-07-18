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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.kuali.mobility.sakai.entity.SakaiForum;
import org.kuali.mobility.sakai.entity.SakaiForumMessage;
import org.springframework.stereotype.Service;

@Service
public class SakaiForumServiceImpl implements SakaiForumService {
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SakaiForumServiceImpl.class);

	public List<SakaiForum> findCourseForums(String json) {
		List<SakaiForum> forums = new ArrayList<SakaiForum>();
    	try {
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_topic_collection");

            for (int i = 0; i < itemArray.size(); i++) {
                String id = itemArray.getJSONObject(i).getString("forumId");
                String title = itemArray.getJSONObject(i).getString("forumTitle");
                SakaiForum item = new SakaiForum();
                item.setId(id);
                item.setTitle(title);
                item.setIsForumHeader(true);
                forums.add(item);
//                JSONObject topicsObj = new JSONObject(itemArray.getJSONObject(i).getJSONArray("topics"));
                JSONArray topicsArray = itemArray.getJSONObject(i).getJSONArray("topics");
//                List<ForumTopics> ftList = new ArrayList<ForumTopics>();
                for (int j = 0; j < topicsArray.size(); j++) {
                	String topicId = topicsArray.getJSONObject(j).getString("topicId");
                    String topicTitle = topicsArray.getJSONObject(j).getString("topicTitle");
                    String topicDescription = topicsArray.getJSONObject(j).
                    	getString("messagesCount") + " messages, " 
                    	+ topicsArray.getJSONObject(j).getString("unreadMessagesCount")
                    	+ " unread";
                    SakaiForum fTopic = new SakaiForum();
                    fTopic.setId(topicId);
                    fTopic.setTitle(topicTitle);
                    fTopic.setDescription(topicDescription);
                    fTopic.setForumId(id);
                    fTopic.setIsForumHeader(false);
                    forums.add(fTopic);
//                    ftList.add(fTopic);
                }
//                item.setTopics(ftList);
//                item.setDescription(description);
                
            }

    	} catch (JSONException e) {
    		LOG.error(e.getMessage(), e);
        }
		return forums;
	}
	
	public List<SakaiForumMessage> findTopicMessages(String json, String topicTitle) {
		List<SakaiForumMessage> messages = new ArrayList<SakaiForumMessage>();
    	try {
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_message_collection");
            
            SakaiForumMessage headerItem = new SakaiForumMessage();
            headerItem.setTitle(topicTitle);
            headerItem.setMessageHeader(true);
            messages.add(headerItem);
            
            for (int i = 0; i < itemArray.size(); i++) {
            	String inReplyTo = itemArray.getJSONObject(i).getString("replyTo");
            	if(!inReplyTo.equals("null")) {
            		continue;
            	}
            	String messageId = itemArray.getJSONObject(i).getString("messageId");
                String messageTitle = itemArray.getJSONObject(i).getString("title");
                String messageBody = itemArray.getJSONObject(i).getString("body");
                Boolean isRead = itemArray.getJSONObject(i).getBoolean("read");
                
//                String [] messageAuthor = new String[3];
                String messageAuthorName = itemArray.getJSONObject(i).getString("authoredBy");
//                String messageAuthorName = messageAuthor[0] + " " + messageAuthor[1];
//                String messageAuthorRole = messageAuthor[2];
                Date cDate = new Date(Long.parseLong(itemArray.getJSONObject(i).getString("createdOn")));
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String createdDate = df.format(cDate);
                
                
                SakaiForumMessage item = new SakaiForumMessage();
                item.setId(messageId);
                item.setTitle(messageTitle);
                item.setBody(messageBody);
                item.setCreatedBy(messageAuthorName);
//                item.setRole(messageAuthorRole);
                item.setCreatedDate(createdDate);
                item.setMessageHeader(false);
                item.setIsRead(isRead);
                item.setTopicTitle(topicTitle);
                messages.add(item);
                }
        } catch (JSONException e) {
        	LOG.error(e.getMessage(), e);
        }
    	return messages;
	}
	
	public List<SakaiForumMessage> findMessageDetails(String json, String messageId, String messageTitle) {
		List<SakaiForumMessage> messages = new ArrayList<SakaiForumMessage>();
    	try {
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_message_collection");
            
            for (int i = 0; i < itemArray.size(); i++) {
            	String inReplyTo = itemArray.getJSONObject(i).getString("replyTo");
            	String jMessageId = itemArray.getJSONObject(i).getString("messageId");
            	if(jMessageId.equals(messageId) || inReplyTo.equals(messageId)) {
            	
	                String messageBody = itemArray.getJSONObject(i).getString("body");
	                Boolean isRead = itemArray.getJSONObject(i).getBoolean("read");
	                
	//                String [] messageAuthor = new String[3];
	                String messageAuthorName = itemArray.getJSONObject(i).getString("authoredBy");
	//                String messageAuthorName = messageAuthor[0] + " " + messageAuthor[1];
	//                String messageAuthorRole = messageAuthor[2];
	                Date cDate = new Date(Long.parseLong(itemArray.getJSONObject(i).getString("createdOn")));
	                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	                String createdDate = df.format(cDate);
	                
	                
	                SakaiForumMessage item = new SakaiForumMessage();
	                item.setId(messageId);
	                item.setTitle(messageTitle);
	                item.setBody(messageBody);
	                item.setCreatedBy(messageAuthorName);
	//                item.setRole(messageAuthorRole);
	                item.setCreatedDate(createdDate);
	                item.setMessageHeader(false);
	                item.setIsRead(isRead);
	                messages.add(item);
            	}
            }
        } catch (JSONException e) {
        	LOG.error(e.getMessage(), e);
        } 
    	return messages;   
	}
}
