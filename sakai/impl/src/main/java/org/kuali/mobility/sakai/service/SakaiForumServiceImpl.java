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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.sakai.entity.Forum;
import org.kuali.mobility.sakai.entity.ForumThread;
import org.kuali.mobility.sakai.entity.ForumTopic;
import org.kuali.mobility.sakai.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.iu.es.espd.oauth.OAuth2LegService;
import edu.iu.es.espd.oauth.OAuthException;

@Service
public class SakaiForumServiceImpl implements SakaiForumService {
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SakaiForumServiceImpl.class);
	
	@Autowired
	private ConfigParamService configParamService;
	
	@Autowired
	private OAuth2LegService oncourseOAuthService;

	public List<Forum> findForums(String siteId, String userId) {
		List<Forum> forums = new ArrayList<Forum>();
    	try {
    		String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_topic/site/" + siteId + ".json";
    		ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
    		String json = IOUtils.toString(is.getBody(), "UTF-8");
    		
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_topic_collection");

            for (int i = 0; i < itemArray.size(); i++) {
            	JSONObject object = itemArray.getJSONObject(i);
                Forum item = new Forum();
                item.setForumId(object.getString("forumId"));
                item.setTitle(object.getString("forumTitle"));
                
                JSONArray topicsArray = object.getJSONArray("topics");
                int unreadCount = 0;
                for (int j = 0; j < topicsArray.size(); j++) {
                    unreadCount += topicsArray.getJSONObject(j).getInt("unreadMessagesCount");
                }
                item.setUnreadCount(unreadCount);
                forums.add(item);
            }

    	} catch (Exception e) {
    		LOG.error(e.getMessage(), e);
        }
		return forums;
	}
	
	public Forum findForum(String forumId, String userId) {
		Forum forum = new Forum();
    	try {
    		String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_topic/forum/" + forumId + ".json";
    		ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
    		String json = IOUtils.toString(is.getBody(), "UTF-8");
    		
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_topic_collection");

            for (int i = 0; i < itemArray.size(); i++) {
            	JSONObject object = itemArray.getJSONObject(i);
                forum.setForumId(object.getString("forumId"));
                forum.setTitle(object.getString("forumTitle"));
                
                JSONArray topicsArray = object.getJSONArray("topics");
                List<ForumTopic> topics = forum.getTopics();
                for (int j = 0; j < topicsArray.size(); j++) {
                	JSONObject topic = topicsArray.getJSONObject(j);
                	ForumTopic forumTopic = new ForumTopic();
                	forumTopic.setId(topic.getString("topicId"));
                    forumTopic.setTitle(topic.getString("topicTitle"));
                    forumTopic.setUnreadCount(topic.getInt("unreadMessagesCount"));
                    forumTopic.setMessageCount(topic.getInt("messagesCount"));
                    topics.add(forumTopic);
                }
            }

    	} catch (Exception e) {
    		LOG.error(e.getMessage(), e);
        }
		return forum;
	}
	
	public ForumTopic findTopic(String topicId, String userId, String topicTitle) {
		ForumTopic topic = new ForumTopic();
		topic.setTitle(topicTitle);
		topic.setId(topicId);
    	try {
    		String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_message/topic/" + topicId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_message_collection");
            
            List<ForumThread> threads = topic.getThreads();
            Set<String> unreadMessages = new HashSet<String>();
            Map<String, List<String>> messageReplies = new HashMap<String, List<String>>();
            for (int i = 0; i < itemArray.size(); i++) {
            	JSONObject message = itemArray.getJSONObject(i);
            	if (message.getInt("indentIndex") == 0) {
            		ForumThread thread = new ForumThread();
            		thread.setId(message.getString("messageId"));
            		thread.setTopicId(message.getString("topicId"));
            		thread.setCreatedBy(message.getString("authoredBy"));
            		thread.setTitle(message.getString("title"));
            		
            		Date cDate = new Date(message.getLong("createdOn"));
	                DateFormat df = new SimpleDateFormat("MM/dd/yyyy  h:mm a");
	                thread.setCreatedDate(df.format(cDate));
	                
	                if (!message.getBoolean("read")) {
	                	unreadMessages.add(thread.getId());
	                }
            		
            		threads.add(thread);
            		
            		if (messageReplies.get(thread.getId()) == null) {
            			messageReplies.put(thread.getId(), new ArrayList<String>());
            		}
            	} else {
            		String replyToId = message.getString("replyTo");
            		String messageId = message.getString("messageId");
            		List<String> replyList = messageReplies.get(replyToId);
            		if (replyList == null) {
            			replyList = new ArrayList<String>();
            			messageReplies.put(replyToId, replyList);
            		}
            		replyList.add(messageId);
            		
            		if (!message.getBoolean("read")) {
	                	unreadMessages.add(messageId);
	                }
            	}
            }
            computeUnreadCounts(threads, messageReplies, unreadMessages);
        } catch (Exception e) {
        	LOG.error(e.getMessage(), e);
        }
    	return topic;
	}
	
	private void computeUnreadCounts(List<ForumThread> threads, Map<String, List<String>> messageReplies, Set<String> unreadMessages) {
		for (ForumThread thread : threads) {
			thread.setUnreadCount(computeUnreadCount(thread.getId(), messageReplies, unreadMessages, 0));
		}
	}
	
	private int computeUnreadCount(String messageId, Map<String, List<String>> messageReplies, Set<String> unreadMessages, int currentCount) {
		List<String> replies = messageReplies.get(messageId);
		
		if (unreadMessages.contains(messageId)) {
			currentCount++;
		}
		if (replies != null && !replies.isEmpty()) {
			for (String message : replies) {
				currentCount += computeUnreadCount(message, messageReplies, unreadMessages, currentCount);
			}
		}
		
		return currentCount;
	}

	@Override
	public ForumThread findThread(String topicId, String threadId, String userId) {
		ForumThread thread = new ForumThread();
		thread.setId(threadId);
		thread.setTopicId(topicId);
    	try {
    		String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_message/topic/" + topicId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_message_collection");
            
            Map<String, Message> messages = new HashMap<String, Message>();
            for (int i = 0; i < itemArray.size(); i++) {
            	JSONObject message = itemArray.getJSONObject(i);

            	String messageId = message.getString("messageId");

            	Message m = messages.get(messageId);
            	if (m == null) {
            		m = new Message();
            	}
            	
        		m.setId(message.getString("messageId"));
        		m.setTopicId(message.getString("topicId"));
        		m.setCreatedBy(message.getString("authoredBy"));
        		m.setTitle(message.getString("title"));
        		m.setBody(message.getString("body"));
        		m.setIsRead((message.getBoolean("read")));
        		m.setIndentIndex(message.getInt("indentIndex"));
        		
        		if (m.getBody().equals("null")) {
        			m.setBody("(No message)");
        		}

        		long time = message.getLong("createdOn");
        		m.setCreatedTime(time);
        		Date cDate = new Date(time);
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy  h:mm a");
                m.setCreatedDate(df.format(cDate));
        		
        		if (m.getIndentIndex() > 0) {
        			String replyToId = message.getString("replyTo");
        			m.setIndentIndex(message.getInt("indentIndex"));
        			m.setInReplyTo(replyToId);
        			
            		Message parent = messages.get(replyToId);
            		if (parent == null) {
            			parent = new Message();
            			messages.put(replyToId, parent);
            		}
            		parent.getReplies().add(m);
        		}
        		messages.put(messageId, m);
            }
            
            Message m = messages.get(threadId);
            thread.setTitle(m.getTitle());
            getThreadMessages(m, thread.getMessages());
        } catch (Exception e) {
        	LOG.error(e.getMessage(), e);
        }
    	return thread;
	}
	
	private void getThreadMessages(Message m, List<Message> threadMessages) {
		if (m != null) {
			threadMessages.add(m);
			
			List<Message> replies = m.getReplies();
			if (replies != null && !replies.isEmpty()) {
				for (Message reply : replies) {
					getThreadMessages(reply, threadMessages);
				}
			}
		}
	}
	
	@Override
	public Message findMessage(String messageId, String topicId, String userId) {
		Message m = new Message();
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_message/topic/" + topicId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_message_collection");
            
            for (int i = 0; i < itemArray.size(); i++) {
            	JSONObject message = itemArray.getJSONObject(i);

            	if (message.getString("messageId").equals(messageId)) {
	        		m.setId(message.getString("messageId"));
	        		m.setTopicId(message.getString("topicId"));
	        		m.setCreatedBy(message.getString("authoredBy"));
	        		m.setTitle(message.getString("title"));
	        		m.setBody(message.getString("body"));
	        		
	        		if (m.getBody().equals("null")) {
	        			m.setBody("(No message)");
	        		}
	        		
	        		Date cDate = new Date(message.getLong("createdOn"));
	                DateFormat df = new SimpleDateFormat("MM/dd/yyyy  h:mm a");
	                m.setCreatedDate(df.format(cDate));
	                break;
            	}
            }
            
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return m;
	}
	
	@Override
	public ResponseEntity<String> postMessage(Message message, String userId) {
		try {
			String jsonString = "{";
			jsonString += "\"attachments\": [],";
			jsonString += "\"authoredBy\":\"" + userId + "\", ";
			jsonString += "\"body\":\"" + message.getBody() + "\", ";
			jsonString += "\"label\":\"" + "" + "\", ";
			jsonString += "\"recipients\":\"" + "" + "\", ";
			jsonString += "\"replies\":" + "null" + ", ";
			jsonString += "\"replyTo\":" + message.getInReplyTo() + ", ";
			jsonString += "\"title\":\"" + message.getTitle() + "\", ";
			jsonString += "\"topicId\":\"" + message.getTopicId() + "\", ";
			jsonString += "\"forumId\":\"" + message.getForumId() + "\", ";
			jsonString += "\"read\":" + "false" + ", ";
			jsonString += "\"entityReference\":\"" + "\\/forum_message" + "\"";
			//jsonString += "\"entityURL\":\"" + "http:\\/\\/localhost:8080\\/direct\\/forum_message" + "\", ";
			//jsonString += "\"entityTitle\":\"" + subject + "\"";

			jsonString += "}";

			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_message/new.json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthPostRequest(userId, url, "application/json", jsonString);
			return new ResponseEntity<String>(is.getStatusCode());
		} catch (OAuthException e) {
			BufferedReader br = new BufferedReader(new InputStreamReader(e.getResponseBody()));
			String body = "";
			try {
				body = br.readLine();
			} catch (IOException e1) {
			}
			LOG.error(e.getResponseCode() + ", " + body, e);
			return new ResponseEntity<String>(HttpStatus.valueOf(e.getResponseCode()));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.METHOD_FAILURE);
		}
	}
	
	@Override
	public ResponseEntity<String> markMessageRead(String siteId, String messageId, String userId) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_message/markread/" + messageId + "/site/" + siteId;
			ResponseEntity<InputStream> response = oncourseOAuthService.oAuthPostRequest(userId, url, "text/html", "");
			return new ResponseEntity<String>(response.getStatusCode());
		} catch (OAuthException e) {
			if (e.getResponseBody() != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(e.getResponseBody()));
				String body = "";
				try {
					body = br.readLine();
				} catch (IOException e1) {
				}
				LOG.error(e.getResponseCode() + ", " + body, e);
			} else {
				LOG.error(e.getMessage(), e);
			}
			return new ResponseEntity<String>(HttpStatus.valueOf(e.getResponseCode()));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.METHOD_FAILURE);
		}
	}
	
	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
	
	public void setOncourseOAuthService(OAuth2LegService oncourseOAuthService) {
		this.oncourseOAuthService = oncourseOAuthService;
	}
}
