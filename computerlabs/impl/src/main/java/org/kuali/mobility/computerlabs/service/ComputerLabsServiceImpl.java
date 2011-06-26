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

package org.kuali.mobility.computerlabs.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kuali.mobility.computerlabs.entity.ComputerLab;
import org.kuali.mobility.computerlabs.entity.LabLocation;
import org.kuali.mobility.maps.entity.Location;
import org.kuali.mobility.maps.entity.MapsGroup;
import org.kuali.mobility.maps.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONSerializer;

@Service
public class ComputerLabsServiceImpl implements ComputerLabsService {

    @Autowired
    private LocationService locationService;
    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }
	
    @Transactional
	public List<ComputerLab> findAllComputerLabsByCampus(String campus) {
		ComputerLabsSeatParser parser = new ComputerLabsSeatParser();
		List<ComputerLab> labs = parser.parseSeats(campus);
		
		MapsGroup group = locationService.getMapsGroupByCode("BL");
		Set<Location> locations = group.getMapsLocations();
		Map<String, Location> locationMap = new HashMap<String, Location>();
		for (Location loc : locations) {
			if (loc.getShortCode() != null) {
				locationMap.put(loc.getShortCode(), loc);	
			}
		}

		for (ComputerLab lab : labs) {
			if (lab.getBuildingShortCode() != null) {
				Location loc = locationMap.get(lab.getBuildingShortCode());
				if (loc != null) {
					lab.setBuildingCode(loc.getBuildingCode());
				}
			}
		}
		
		return labs;
//		return new ArrayList<ComputerLab>();
	}
    
    @Transactional
	public List<LabLocation> findAllLabLocationsByCampus(String campus) {
		List<ComputerLab> labs = this.findAllComputerLabsByCampus(campus);
		Map<String, LabLocation> labMap = new HashMap<String, LabLocation>();
		for (ComputerLab lab : labs) {
			LabLocation labLocation = labMap.get(lab.getBuildingNameOnly());
			if (labLocation == null) {
//				Location location = this.getStcLocation(campus, lab.getBuildingCode(), lab.getBuildingShortCode());
				// Location can be null
				labLocation = new LabLocation(lab.getBuildingNameOnly());
				labMap.put(lab.getBuildingNameOnly(), labLocation);
			}
			labLocation.getComputerLabs().add(lab);	
		}
		List<LabLocation> labLocations = new ArrayList<LabLocation>();
		labLocations.addAll(labMap.values());
		Collections.sort(labLocations);
		return labLocations;
	}
	
    public String toJson(Collection<ComputerLab> collection) {
        return new JSONSerializer().exclude("*.class").include("avList").serialize(collection);
//    	return new JSONSerializer().include("", "").serialize(collection);
    }
    
    public String toJsonLabLocation(Collection<LabLocation> collection) {
    	return new JSONSerializer().exclude("*.class").deepSerialize(collection);
//        return new JSONSerializer().exclude("*.class").include("labs").serialize(collection);
//    	return new JSONSerializer().include("", "").serialize(collection);
    }
	
}
