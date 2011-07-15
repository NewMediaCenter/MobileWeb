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

package org.kuali.mobility.feedback.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="FEEDBACK_T")
public class Feedback implements Serializable {

	private static final long serialVersionUID = 7273789153652061359L;

	@Id
    @SequenceGenerator(name="feedback_sequence", sequenceName="SEQ_FEEDBACK_T", initialValue=1000, allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="feedback_sequence")
    @Column(name="FEEDBACK_ID")
    private Long feedbackId;

    @Column(name="POST_TS")
    private Timestamp postedTimestamp;

    @Column(name="TXT")
    private String noteText;
    
    @Column(name="CAMPUS")
    private String campus;

    @Column(name="AFFL")
    private String affiliation;

    @Column(name="DEVICE")
    private String deviceType;

    @Column(name="UA")
    private String userAgent;
    
    @Column(name="SRVC")
    private String service;
    
    @Column(name="EMAIL")
    private String email;
    
    @Column(name="USER_ID")
    private String userId;
    
    @Version
    @Column(name="VER_NBR")
    protected Long versionNumber;
    
    public Feedback() {}

    public String getNoteTextShort() {
    	if (this.getNoteText() != null) {
    		return this.getNoteText().length() > 50 ? this.getNoteText().substring(0, 50) + "..." : this.getNoteText();	
    	} 
    	return "";
    }

    public String getNoteFormatted() {
        return this.getNoteText().replaceAll("\\n", "<br/>");
    }
    
    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Timestamp getPostedTimestamp() {
        return postedTimestamp;
    }

    public void setPostedTimestamp(Timestamp postedTimestamp) {
        this.postedTimestamp = postedTimestamp;
    }

    public String getNoteText() {
    	if (noteText != null) {
    		return noteText; 
    	}
        return "";
    }
    
    public String getNoteTextExport() {
    	if (noteText != null) {
    		String text = new String(noteText);
    		text = text.replaceAll("\t", " ");
    		text = text.replaceAll("\r", " ");
    		text = text.replaceAll("\n", " ");
    		return text; 
    	}
        return "";
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public Long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public String toString() {
    	String newline = "\r\n";
    	String str = "Campus: "+ this.getCampus();
    	str = str + newline + "Affiliation: " + this.getAffiliation();
    	str = str + newline + "User Id: " + this.getUserId();
    	str = str + newline + "Email: " + this.getEmail();
    	str = str + newline + "Device: " + this.getDeviceType();
    	str = str + newline + "User Agent: " + this.getUserAgent();
    	str = str + newline + "Timestamp: " + this.getPostedTimestamp();
    	str = str + newline + "Service: " + this.getService();
    	str = str + newline + newline + this.getNoteTextExport();
    	return str;
    }
}
