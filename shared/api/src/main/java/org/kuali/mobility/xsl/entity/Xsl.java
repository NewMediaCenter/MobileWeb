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

package org.kuali.mobility.xsl.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity(name="Xsl")
@Table(name="XSL_MAINT_T")
public class Xsl implements Serializable {

    private static final long serialVersionUID = -5761311057726925895L;

    @Id
    @SequenceGenerator(name="xsl_maint_sequence", sequenceName="SEQ_XSL_MAINT_T", initialValue=1000, allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="xsl_maint_sequence")
    @Column(name="XSL_ID")
    private Long xslId;

    @Lob
    @Column(name="VALUE_TXT")
    private String value;

    @Column(name="CODE")
    private String code;
    
    @Version
    @Column(name="VER_NBR")
    protected Long versionNumber;
	
	public Xsl() {}

    public Long getXslId() {
        return xslId;
    }

    public void setXslId(Long xslId) {
        this.xslId = xslId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
