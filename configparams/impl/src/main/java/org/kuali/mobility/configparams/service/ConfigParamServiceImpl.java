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
 
package org.kuali.mobility.configparams.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kuali.mobility.configparams.dao.ConfigParamDao;
import org.kuali.mobility.configparams.entity.ConfigParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Service
public class ConfigParamServiceImpl implements ConfigParamService {

	@Autowired
    private ConfigParamDao configParamDao;
    public void setConfigParamDao(ConfigParamDao configParamDao) {
        this.configParamDao = configParamDao;
    }
    
    @Transactional
    public void deleteConfigParamById(Long id) {
        configParamDao.deleteConfigParamById(id);
    }

    @Transactional
    public List<ConfigParam> findAllConfigParam() {
        return configParamDao.findAllConfigParam();
    }

    @Transactional
    public ConfigParam findConfigParamById(Long id) {
        return configParamDao.findConfigParamById(id);
    }

    @Transactional
    public ConfigParam findConfigParamByName(String name) {
        return configParamDao.findConfigParamByName(name);
    }

    @Transactional
    public String findValueByName(String name) {
        String value = findConfigParamByName(name).getValue();
        return value != null ? value.trim() : "";
    }

    @Transactional
    public Long saveConfigParam(ConfigParam configParam) {
        return configParamDao.saveConfigParam(configParam);
    }
    
    public ConfigParam fromJsonToEntity(String json) {
        return new JSONDeserializer<ConfigParam>().use(null, ConfigParam.class).deserialize(json);
    }

    public String toJson(Collection<ConfigParam> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public Collection<ConfigParam> fromJsonToCollection(String json) {
        return new JSONDeserializer<List<ConfigParam>>().use(null, ArrayList.class).use("values", ConfigParam.class).deserialize(json);
    } 

}
