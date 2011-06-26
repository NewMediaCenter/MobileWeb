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

package org.kuali.mobility.configparams.entity;

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

@Entity(name="ConfigParam")
@Table(name="CONFIG_PARAM_MAINT_T")
public class ConfigParam implements Serializable {

    private static final long serialVersionUID = -7425581809827657649L;

    @Id
    @SequenceGenerator(name="config_param_maint_sequence", sequenceName="SEQ_CONFIG_PARAM_MAINT_T", initialValue=1000, allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="config_param_maint_sequence")
    @Column(name="CONFIG_PARAM_ID")
    private Long configParamId;

    @Column(name="NAME")
	private String name;

    @Column(name="VALUE")
    private String value;

    @Version
    @Column(name="VER_NBR")
    protected Long versionNumber;
	
	public ConfigParam() {
	}
	
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public Long getConfigParamId() {
        return configParamId;
    }

    public void setConfigParamId(Long configParamId) {
        this.configParamId = configParamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
