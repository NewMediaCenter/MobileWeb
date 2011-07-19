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

import org.kuali.mobility.sakai.entity.ForumMessage;

public class SakaiPrivateTopicParser {
    
    public List<ForumMessage> parsePrivateMessages(String json) {
		List<ForumMessage> messages = new ArrayList<ForumMessage>();
		try {
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_message_collection");
            
            for (int i = 0; i < itemArray.size(); i++) {
                String messageId = itemArray.getJSONObject(i).getString("messageId");
                String messageTitle = itemArray.getJSONObject(i).getString("title");
                String messageBody = itemArray.getJSONObject(i).getString("body");
                Boolean isRead = itemArray.getJSONObject(i).getBoolean("read");
                
                String messageAuthor = itemArray.getJSONObject(i).getString("authoredBy");
//                String messageAuthorName = messageAuthor[0] + " " + messageAuthor[1];
//                String messageAuthorRole = messageAuthor[2];
                Date cDate = new Date(Long.parseLong(itemArray.getJSONObject(i).getString("createdOn")));
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String createdDate = df.format(cDate);
                
                
                ForumMessage item = new ForumMessage();
                item.setId(messageId);
                item.setTitle(messageTitle);
                item.setBody(messageBody);
                item.setCreatedBy(messageAuthor);
                item.setRole(messageAuthor);
                item.setCreatedDate(createdDate);
                item.setMessageHeader(false);
                item.setIsRead(isRead);
                messages.add(item);
                }
		} catch (JSONException e) {
            
        } catch (Exception e) {
        }
		return messages;
	}
}
