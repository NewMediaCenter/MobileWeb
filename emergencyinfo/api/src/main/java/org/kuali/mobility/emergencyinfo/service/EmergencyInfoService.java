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

import java.util.Collection;
import java.util.List;

import org.kuali.mobility.emergencyinfo.entity.EmergencyInfo;

public interface EmergencyInfoService {

	public EmergencyInfo findEmergencyInfoById(Long id);
    public List<EmergencyInfo> findAllEmergencyInfo();
    public List<EmergencyInfo> findAllEmergencyInfoByCampus(String campus);
    public Long saveEmergencyInfo(EmergencyInfo emergencyInfo);
	public void deleteEmergencyInfoById(Long id);
    public void reorder(Long id, boolean up);

    public String toJson(Collection<EmergencyInfo> collection);
    public EmergencyInfo fromJsonToEntity(String json);
    public Collection<EmergencyInfo> fromJsonToCollection(String json);

}
