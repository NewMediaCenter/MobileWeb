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

package org.kuali.mobility.alerts.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kuali.mobility.alerts.entity.Alert;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import flexjson.JSONDeserializer;

@Service
public class AlertsServiceImpl implements AlertsService {
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AlertsServiceImpl.class);
	
    private static final String CP_JSON_ALERTS_URL = "Alerts.Json.Url";
	
	@Autowired
	private ConfigParamService configParamService;
	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
	
	/**
	 * @see org.kuali.mobility.alerts.service.AlertsService#findAllAlertsByCriteria(java.util.Map)
	 */
	@Override
	public int findAlertCountByCriteria(Map<String, String> criteria) {
		return findAllAlertsByCriteria(criteria).size();
	}
	
	@Override
	public List<Alert> findAllAlertsByCriteria(Map<String, String> criteria) {
        // Note: RI does not use the criteria parameter
	    return findAllAlertsFromJson(configParamService.findConfigParamByName(CP_JSON_ALERTS_URL).getValue());
	}
	
	@Override
    public List<Alert> findAllAlertsFromJson(String url) {
        String json = HttpUtil.stringFromUrl(url);
        
        if (json == null || "".equals(json)) {
            return new ArrayList<Alert>();
        }
                
        return new JSONDeserializer<ArrayList<Alert>>().deserialize(json);
    }
	
}


