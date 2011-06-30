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

package org.kuali.mobility.maps.controllers;

import java.util.List;

import org.kuali.mobility.maps.entity.Location;
import org.kuali.mobility.maps.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 
@RequestMapping("/maps")
public class MapsController {
    
    @Autowired
    private LocationService locationService;
    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String getHome(Model uiModel) {
    	uiModel.addAttribute("test", "test2");
    	return "maps/location";
    }
    
    @RequestMapping(value = "/{buildingCode}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(Model uiModel, @PathVariable("buildingCode") String buildingCode) {
        List<Location> locations = locationService.getLocationsByBuildingCode(buildingCode);
        if (locations == null || locations.size() < 1) {
            // Error?
        } else {
        	Location location = locations.get(0);
        	uiModel.addAttribute("location", location);
        }
        return "maps/location";
    }
    
    @RequestMapping(value = "/{buildingCode}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@PathVariable("buildingCode") String buildingCode) {
        List<Location> locations = locationService.getLocationsByBuildingCode(buildingCode);
        if (locations == null || locations.size() < 1) {
            // Error?
        } else {
        	Location location = locations.get(0);
        	return location.toJson();
        }
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }
    
}