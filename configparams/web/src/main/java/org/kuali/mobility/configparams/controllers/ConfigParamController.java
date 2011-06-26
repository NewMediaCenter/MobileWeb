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

package org.kuali.mobility.configparams.controllers;

import java.util.List;

import org.kuali.mobility.configparams.entity.ConfigParam;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 
@RequestMapping("/configparams")
public class ConfigParamController {
    
    @Autowired
    private ConfigParamService configParamService;
    public void setConfigParamService(ConfigParamService configParamService) {
        this.configParamService = configParamService;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String home(Model uiModel) {
    	return "configparams/home";
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String getList(Model uiModel) {
    	List<ConfigParam> params = configParamService.findAllConfigParam();
    	uiModel.addAttribute("configparams", params);
    	return "configparams/list";
    }

    /*
     * JSON Retrieve Single
     */
    @RequestMapping(value = "/{configParamId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@PathVariable("configParamId") Long configParamId) {
    	ConfigParam param = configParamService.findConfigParamById(configParamId);
        if (param == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        return param.toJson();
    }
    
    /*
     * JSON Retrieve All
     */
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public String getAll() {
        return configParamService.toJson(configParamService.findAllConfigParam());
    } 
    
    /*
     * JSON Save
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> post(@RequestBody String json) {
    	ConfigParam cp = configParamService.fromJsonToEntity(json);
    	if (cp.getName() != null && cp.getValue() != null && cp.getName().length() > 0 && cp.getValue().length() > 0) {
    		Long id = configParamService.saveConfigParam(cp);
            if (id == null) {
                return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<String>(HttpStatus.CREATED);
    	}
    	return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED); 
    }

    /*
     * JSON Update
     */
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> put(@RequestBody String json) {
    	ConfigParam cp = configParamService.fromJsonToEntity(json);
        if (configParamService.findConfigParamById(cp.getConfigParamId()) == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        } else {
            if (configParamService.saveConfigParam(cp) == null) {
                return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
            }
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    /*
     * JSON Delete
     */
    @RequestMapping(value = "/{configParamId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> delete(@PathVariable("configParamId") Long configParamId) {
    	ConfigParam param = configParamService.findConfigParamById(configParamId);
        if (param == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        configParamService.deleteConfigParamById(param.getConfigParamId());
        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
}