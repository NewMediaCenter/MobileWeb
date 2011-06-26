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
 
package org.kuali.mobility.maps.service;

import java.util.List;

import org.kuali.mobility.maps.entity.Location;
import org.kuali.mobility.maps.entity.LocationCompare;
import org.kuali.mobility.maps.entity.MapsGroup;

public interface LocationService {

//	public List<Location> getLocationsByCampusCode(String campusCode);
	public Location getLocationById(Long locationId);
//	public List<Location> searchLocations(String search, String campusCode);
	public List<Location> searchLocations(String search);
	public List<Location> getAllActiveLocations();
	public List<Location> getAllLocations();
	public List<Location> getAllUngroupedLocations();
	public void saveLocation(Location location);
	public void deleteLocation(Long locationId);
	public List<Location> getLocationsByCode(String code);
	public List<Location> getLocationsByBuildingCode(String buildingCode);
	public void importXml(String campusCode, String xml);
	public List<LocationCompare> importXmlAnalysis(String campusCode, String xml);
	public String exportXmlOneStartFormat(Long groupId);
	public String exportXmlMobileFormat(Long groupId);
	
	public List<MapsGroup> getAllMapsGroups();
	public List<MapsGroup> getAllActiveMapsGroups();
	public MapsGroup getMapsGroupById(Long groupId);
	public MapsGroup getMapsGroupByCode(String groupCode);
	public void saveMapsGroup(MapsGroup group);
	public void deleteMapsGroup(Long groupId);
	public String exportGroupXmlMobileFormat(Long groupId);
	public void importMapsGroupsFromXml(String xml);
	
	public void addMapsGroupToLocation(Long groupId, Long locationId);
	public void removeMapsGroupFromLocation(Long groupId, Long locationId);
	public void addMapsGroupChild(Long groupId, Long addGroupId);
	public void removeMapsGroupChild(Long groupId, Long removeGroupId);
	
}
