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
 
package org.kuali.mobility.maps.dao;

import java.util.List;

import org.kuali.mobility.maps.entity.Location;
import org.kuali.mobility.maps.entity.MapsGroup;

public interface LocationDao {

	public List<Location> getAllActiveLocations();
//	public List<Location> findLocationsByCampusCode(String campusCode);
	public Location findLocationById(Long locationId);
	public List<Location> getAllLocations();
	public void saveLocation(Location location);
	public void deleteLocationById(Long locationId);
	public List<Location> findLocationsByCode(String code);
	public List<Location> getLocationsByBuildingCode(String buildingCode);
	public List<Location> searchLocations(String search);
	public List<Location> getAllUngroupedLocations();
	
	public List<MapsGroup> getAllMapsGroups();
	public List<MapsGroup> getAllActiveMapsGroups();
	public MapsGroup getMapsGroupById(Long groupId);
	public MapsGroup getMapsGroupByCode(String groupCode);
	public void saveMapsGroup(MapsGroup group);
	public void deleteMapsGroup(Long groupId);
	
}
