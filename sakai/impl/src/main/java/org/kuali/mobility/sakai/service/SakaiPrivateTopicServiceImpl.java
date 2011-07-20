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

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.sakai.entity.ForumTopic;
import org.kuali.mobility.sakai.entity.Message;
import org.kuali.mobility.sakai.entity.MessageFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.iu.es.espd.oauth.OAuth2LegService;

@Service
public class SakaiPrivateTopicServiceImpl implements SakaiPrivateTopicService {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SakaiPrivateTopicServiceImpl.class);
	
	@Autowired
	private ConfigParamService configParamService;
	
	@Autowired
	private OAuth2LegService oncourseOAuthService;
	
	public List<ForumTopic> findPrivateTopics(String siteId, String userId) {
		List<ForumTopic> forumTopics = null;
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_topic/private/" + siteId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_topic_collection");

            forumTopics = new ArrayList<ForumTopic>();
            for (int i = 0; i < itemArray.size(); i++) {
            	JSONObject object = itemArray.getJSONObject(i);
                JSONArray topicsArray = object.getJSONArray("topics");
                for (int j = 0; j < topicsArray.size(); j++) {
                	JSONObject topic = topicsArray.getJSONObject(j);
                    ForumTopic fTopic = new ForumTopic();
                    String title = topic.getString("topicTitle");
                    if (title.startsWith("pvt_")) {
                    	title = title.substring(4);
                    }
                    fTopic.setId(topic.getString("topicId"));
                    fTopic.setTitle(title);
                    fTopic.setMessageCount(topic.getInt("messagesCount"));
                    fTopic.setUnreadCount(topic.getInt("unreadMessagesCount"));
                    fTopic.setTypeUuid(topic.getString("typeUuid"));
                    forumTopics.add(fTopic);
                }
            }
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
		return forumTopics;
	}
	
	public MessageFolder findPrivateMessages(String siteId, String typeUuid, String userId) {
		MessageFolder messageFolder = new MessageFolder();
		List<Message> messages = messageFolder.getMessages();
		
		messageFolder.setTypeUuid(typeUuid);
		
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_message/private/" + typeUuid + "/site/" + siteId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_message_collection");
            
            for (int i = 0; i < itemArray.size(); i++) {
                String messageId = itemArray.getJSONObject(i).getString("messageId");
                String messageTitle = itemArray.getJSONObject(i).getString("title");
                String messageBody = itemArray.getJSONObject(i).getString("body");
                Boolean isRead = itemArray.getJSONObject(i).getBoolean("read");
                
                String messageAuthor = itemArray.getJSONObject(i).getString("authoredBy");
//              String messageAuthorName = messageAuthor[0] + " " + messageAuthor[1];
//              String messageAuthorRole = messageAuthor[2];
                Date cDate = new Date(Long.parseLong(itemArray.getJSONObject(i).getString("createdOn")));
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String createdDate = df.format(cDate);
                
                
                Message item = new Message();
                item.setId(messageId);
                item.setTitle(messageTitle);
                item.setBody(messageBody);
                item.setCreatedBy(messageAuthor);
                item.setRole(messageAuthor);
                item.setCreatedDate(createdDate);
                item.setIsRead(isRead);
                messages.add(item);
            }
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return messageFolder;
	}
	
	public Message findPrivateMessageDetails(String userId, String siteId, String typeUuid, String messageId) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_message/private/" + typeUuid + "/site/" + siteId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_message_collection");
            Message item = new Message();
            
            for (int i = 0; i < itemArray.size(); i++) {
                String mId = itemArray.getJSONObject(i).getString("messageId");
                if(!messageId.equalsIgnoreCase(mId)) {
                	continue;
                }
                String messageTitle = itemArray.getJSONObject(i).getString("title");
                String messageBody = itemArray.getJSONObject(i).getString("body");
                Boolean isRead = itemArray.getJSONObject(i).getBoolean("read");
                
                String messageAuthor = itemArray.getJSONObject(i).getString("authoredBy");
//              String messageAuthorName = messageAuthor[0] + " " + messageAuthor[1];
//              String messageAuthorRole = messageAuthor[2];
                Date cDate = new Date(Long.parseLong(itemArray.getJSONObject(i).getString("createdOn")));
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String createdDate = df.format(cDate);
                
                item.setId(messageId);
                item.setTitle(messageTitle);
                item.setBody(messageBody);
                item.setCreatedBy(messageAuthor);
                item.setRole(messageAuthor);
                item.setCreatedDate(createdDate);
                item.setIsRead(isRead);
            }
            return item;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}

	public void setOncourseOAuthService(OAuth2LegService oncourseOAuthService) {
		this.oncourseOAuthService = oncourseOAuthService;
	}

}
