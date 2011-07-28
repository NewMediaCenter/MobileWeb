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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.kuali.mobility.maps.entity.Location;
import org.kuali.mobility.maps.entity.LocationSort;
import org.kuali.mobility.maps.entity.MapsFormSearch;
import org.kuali.mobility.maps.entity.MapsFormSearchResult;
import org.kuali.mobility.maps.entity.MapsFormSearchResultContainer;
import org.kuali.mobility.maps.entity.MapsGroup;
import org.kuali.mobility.maps.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

@Controller 
@RequestMapping("/maps")
public class MapsController {
    
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MapsController.class);
	
    @Autowired
    private LocationService locationService;
    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String getHome(Model uiModel) {
    	MapsFormSearch mapsFormSearch = new MapsFormSearch();
    	uiModel.addAttribute("mapsearchform", mapsFormSearch);
    	return "maps/home";
    }

    @RequestMapping(value = "/location", method = RequestMethod.GET)
    public Object get(Model uiModel, @RequestParam(required = false) String latitude, 
    		@RequestParam(required = false) String longitude) {
//        List<Location> locations = locationService.getLocationsByBuildingCode(buildingCode);
//        if (locations == null || locations.size() < 1) {
//            // Error?
//        } else {
//        	Location location = locations.get(0);
//        	uiModel.addAttribute("location", location);
//        }
        return "maps/location";
    }
    
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Object getForm(Model uiModel) {
		MapsFormSearch form = new MapsFormSearch();
//		MapsGroup group = locationService.getMapsGroupByCode(groupCode);
    	uiModel.addAttribute("mapsearchform", form);
        return "maps/formtest";
    }
    
/*    @RequestMapping(value = "/{buildingCode}", method = RequestMethod.GET)
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
    }*/
    
    /*
     * Group with Buildings JSON, get by Group Code
     */
    @RequestMapping(value = "/group/{groupCode}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object getBuildings(@PathVariable("groupCode") String groupCode) {
    	try {
    		MapsGroup group = locationService.getMapsGroupByCode(groupCode);
    		return group.toJson();
    	} catch (Exception e) {
    		LOG.error(e.getMessage(), e);
    	}
    	return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }
    
    /*
     * Buildings JSON, get by Building Code
     */
    @RequestMapping(value = "/building/{buildingCode}", method = RequestMethod.GET, headers = "Accept=application/json")
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
    
    @RequestMapping(value = "/building/{buildingCode}", method = RequestMethod.GET)
    public Object get(Model uiModel, @PathVariable("buildingCode") String buildingCode) {
        List<Location> locations = locationService.getLocationsByBuildingCode(buildingCode);
        if (locations == null || locations.size() < 1) {
            // Error?
        } else {
        	Location location = locations.get(0);
        	uiModel.addAttribute("location", location);
        }
        return "maps/building";
    }
    
    /*
     * Buildings search form results
     */
	@RequestMapping(value = "/building/search", method = RequestMethod.POST)
	public String searchBuildings(HttpServletRequest request, @ModelAttribute("mapsearchform") MapsFormSearch mapsFormSearch, BindingResult bindingResult, SessionStatus status, Model uiModel) {
		String searchString = mapsFormSearch.getSearchText();
		searchString = searchString.trim();
		String searchCampus = mapsFormSearch.getSearchCampus();
    	try {
    		MapsFormSearchResultContainer container = new MapsFormSearchResultContainer();
    		List<MapsFormSearchResult> results = new ArrayList<MapsFormSearchResult>();
    		MapsGroup group = locationService.getMapsGroupByCode(searchCampus);
    		Set<Location> locationSet = group.getMapsLocations();
    		List<Location> locations = new ArrayList<Location>();
    		locations.addAll(locationSet);
    		Collections.sort(locations, new LocationSort());
    		for (Location location : locations) {
    			boolean addLocation = false;
    			if (location.getName() != null && location.getName().toLowerCase().indexOf(searchString.toLowerCase()) > -1) {
    				addLocation = true;
    			} else if (location.getShortName() != null && location.getShortName().toLowerCase().indexOf(searchString.toLowerCase()) > -1) {
    				addLocation = true;
    			} else if (location.getShortCode() != null && location.getShortCode().toLowerCase().trim().equals(searchString.toLowerCase().trim())) {
    				addLocation = true;
    			} else if (location.getCode() != null && location.getCode().toLowerCase().trim().equals(searchString.toLowerCase().trim())) {
    				addLocation = true;
    			}
    			if (addLocation) {
        			MapsFormSearchResult result = new MapsFormSearchResult();
        			result.setName(location.getName());
        			result.setCode(location.getBuildingCode());
    				results.add(result);
    			}
    		}
    		container.setResults(results);
    		uiModel.addAttribute("container", container);
    	} catch (Exception e) {
    		LOG.error(e.getMessage(), e);
    	}
//		uiModel.addAttribute("message", pageLevelException.getMessage());
		return "maps/home";
	}
    
}
