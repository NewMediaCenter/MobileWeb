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
 
package org.kuali.mobility.emergencyinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import flexjson.JSONSerializer;

@Entity(name="MaintEmergencyInfo")
@Table(name="EM_INFO_MAINT_T")
public class EmergencyInfo implements Serializable {

    private static final long serialVersionUID = 8753764116073085733L;

    @Id
    @SequenceGenerator(name="em_info_maint_sequence", sequenceName="SEQ_EM_INFO_MAINT_T", initialValue=1000, allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="em_info_maint_sequence")
    @Column(name="EM_INFO_ID")
    private Long emergencyInfoId;

    @Column(name="EM_INFO_TYPE")
	private String type;

    @Column(name="TITLE")
    private String title;

    @Column(name="LINK")
	private String link;

    @Column(name="CAMPUS")
    private String campus;
    
    @Column(name="EM_INFO_ORDER")
    private int order;

    @Version
    @Column(name="VER_NBR")
    protected Long versionNumber;
	
	public EmergencyInfo() {
	}
    	
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

    public Long getEmergencyInfoId() {
        return emergencyInfoId;
    }

    public void setEmergencyInfoId(Long emergencyInfoId) {
        this.emergencyInfoId = emergencyInfoId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    
}
