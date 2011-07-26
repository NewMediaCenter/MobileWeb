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

package org.kuali.mobility.athletics.controllers;

import org.kuali.mobility.athletics.entity.Athletics;
import org.kuali.mobility.athletics.service.AthleticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/athletics")
public class AthleticsController {

	@Autowired
	private AthleticsService athleticsService;

	public void setAthleticsService(AthleticsService athleticsService) {
		this.athleticsService = athleticsService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getList(Model uiModel) throws Exception {
		Athletics athletics = athleticsService.retrieveAthletics();
		uiModel.addAttribute("athletics", athletics);
		return "athletics/list";
	}

}
