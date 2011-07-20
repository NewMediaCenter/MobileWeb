package org.kuali.mobility.sakai.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageFolder implements Serializable{

	private static final long serialVersionUID = -3339548861083280739L;
	
	private String title;
	private String typeUuid;
	private List<Message> messages;
	
	public MessageFolder() {
		messages = new ArrayList<Message>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public String getTypeUuid() {
		return typeUuid;
	}

	public void setTypeUuid(String typeUuid) {
		this.typeUuid = typeUuid;
	}
}
