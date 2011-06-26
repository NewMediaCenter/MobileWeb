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

package org.kuali.mobility.emergencyinfo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kuali.mobility.emergencyinfo.dao.EmergencyInfoDao;
import org.kuali.mobility.emergencyinfo.entity.EmergencyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Service
public class EmergencyInfoServiceImpl implements EmergencyInfoService {
  
    @Transactional
    public void deleteEmergencyInfoById(Long id) {
        emergencyInfoDao.deleteEmergencyInfoById(id);
    }

    @Transactional
    public List<EmergencyInfo> findAllEmergencyInfo() {
        return emergencyInfoDao.findAllEmergencyInfo();
    }

    @Transactional
    public EmergencyInfo findEmergencyInfoById(Long id) {
        return emergencyInfoDao.findEmergencyInfoById(id);
    }

    @Transactional
    public Long saveEmergencyInfo(EmergencyInfo emergencyInfo) {
        return emergencyInfoDao.saveEmergencyInfo(emergencyInfo);
    }
    
    @Transactional
    public void reorder(Long id, boolean up) {
        emergencyInfoDao.reorder(id, up); 
    }

    @Transactional
    public EmergencyInfoDao getEmergencyInfoDao() {
        return emergencyInfoDao;
    }

    @Transactional
    public List<EmergencyInfo> findAllEmergencyInfoByCampus(String campus) {
        return emergencyInfoDao.findAllEmergencyInfoByCampus(campus);
    }

    public EmergencyInfo fromJsonToEntity(String json) {
        return new JSONDeserializer<EmergencyInfo>().use(null, EmergencyInfo.class).deserialize(json);
    }

    public String toJson(Collection<EmergencyInfo> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public Collection<EmergencyInfo> fromJsonToCollection(String json) {
        return new JSONDeserializer<List<EmergencyInfo>>().use(null, ArrayList.class).use("values", EmergencyInfo.class).deserialize(json);
    } 
    
    @Autowired
    private EmergencyInfoDao emergencyInfoDao;
    public void setEmergencyInfoDao(EmergencyInfoDao emergencyInfoDao) {
        this.emergencyInfoDao = emergencyInfoDao;
    }

}
