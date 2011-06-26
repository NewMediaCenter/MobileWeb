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
 
package org.kuali.mobility.sakai.controllers;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.sakai.entity.SakaiResource;
import org.kuali.mobility.sakai.service.SakaiResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.iu.es.espd.oauth.OAuth2LegService;
import edu.iu.uis.cas.filter.CASFilter;

@Controller
@RequestMapping("/sakairesourcedetails")
public class ResourceDetailsController {

	@Autowired
	private ConfigParamService configParamService;

	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}

	@Autowired
	private OAuth2LegService oncourseOAuthService;

	public void setOncourseOAuthService(OAuth2LegService oncourseOAuthService) {
		this.oncourseOAuthService = oncourseOAuthService;
	}

	@Autowired
	private SakaiResourceService sakaiResourceService;

	public void setSakaiResourceService(SakaiResourceService sakaiResourceService) {
		this.sakaiResourceService = sakaiResourceService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getList(HttpServletRequest request, HttpServletResponse response, @RequestParam("siteId") String siteId, @RequestParam("sessionId") String sessionId, @RequestParam("resId") String resId, Model uiModel) {
		try {
			//check if the last char is / that means it is a folder else its a file
			char lastChar = resId.charAt(resId.length()-1);
			
			if(lastChar == '/'){
				String url = configParamService.findValueByName("Sakai.Url.Base") + "resources.json?siteId=" + siteId;
				ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "text/html");
				String json = IOUtils.toString(is.getBody(), "UTF-8");
				List<SakaiResource> resources = sakaiResourceService.findChildResources(json, resId);
				uiModel.addAttribute("resources", resources);
				uiModel.addAttribute("sessionId", sessionId);
				return "sakairesources/list";
			}
			else {
				String url = configParamService.findValueByName("Sakai.Url.Base") + "resources/getresource" + resId;
				ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(CASFilter.getRemoteUser(request), url, "application/pdf");
				byte [] fileData = IOUtils.toByteArray(is.getBody());
				response.setContentType("application/pdf");
				response.setContentLength(fileData.length);
				response.getOutputStream().write(fileData, 0, fileData.length);
				return null;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	return null;	
	}

}
