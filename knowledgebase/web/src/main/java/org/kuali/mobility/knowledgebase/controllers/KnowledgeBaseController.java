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

package org.kuali.mobility.knowledgebase.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.kuali.mobility.knowledgebase.entity.KnowledgeBaseSearchResultContainer;
import org.kuali.mobility.knowledgebase.service.KnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 
@RequestMapping("/knowledgebase")
public class KnowledgeBaseController {
    
    @Autowired
    private KnowledgeBaseService knowledgeBaseService;
    public void setEmergencyInfoService(KnowledgeBaseService knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String getHome(Model uiModel) {
    	uiModel.addAttribute("test", "test2");
    	return "knowledgebase/home";
    }
    
    @RequestMapping(value = "/{documentId}", method = RequestMethod.GET)
    public String getKnowledgeBaseDocument(HttpServletRequest request, Model uiModel, @PathVariable("documentId") String documentId) {
    	uiModel.addAttribute("test", "test2");
    	String kbdoc = null;
    	try {
			Map<String, Object> transformerParameters = new HashMap<String, Object>();
			transformerParameters.put("linkurl", request.getContextPath() + "/knowledgebase/");
    		kbdoc = knowledgeBaseService.getConvertedKnowledgeBaseDocument(documentId, "knowledge_base", transformerParameters);
    		uiModel.addAttribute("kbdoc", kbdoc);
    	} catch (Exception e) {
    		
    	}
    	return "knowledgebase/document";
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object searchKnowledgeBaseDocuments(@RequestParam(value = "criteria", required = true) String criteria) {
//    	List<ComputerLab> labs = computerLabsService.findAllComputerLabsByCampus(campus);
//    	return computerLabsService.toJson(labs);
    	try {
    		KnowledgeBaseSearchResultContainer cont = knowledgeBaseService.searchKnowledgeBase(criteria, 0, 50);
    		cont.getNumberOfResults();
    		return knowledgeBaseService.toJson(cont);
    	} catch (Exception e) {
    		
    	}
    	return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }
    
    @RequestMapping(value = "/document", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object getKnowledgeBaseDocument(@RequestParam(value = "id", required = true) String documentId) {
//    	List<ComputerLab> labs = computerLabsService.findAllComputerLabsByCampus(campus);
//    	return computerLabsService.toJson(labs);
    	String kbdoc = null;
    	try {
    		kbdoc = knowledgeBaseService.getKnowledgeBaseDocument(documentId);
    		return kbdoc;
    	} catch (Exception e) {
    		
    	}
//    	return "id: " + id;
    	return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }
    
}
