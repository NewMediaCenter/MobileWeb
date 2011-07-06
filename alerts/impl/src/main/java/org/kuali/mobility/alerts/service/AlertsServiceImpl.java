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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.kuali.mobility.alerts.entity.Alert;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import flexjson.JSONDeserializer;

@Service
public class AlertsServiceImpl implements AlertsService {
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AlertsServiceImpl.class);
	
	private static final String CAMPUS_STATUS_URL_PARAM = "CAMPUS_STATUS_XML_URL";
	
	@Autowired
	private ConfigParamService configParamService;
	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
	
	@Override
	public int findAlertCountByCriteria(Map<String, String> criteria) {
		// TODO Move to cached copy of feeds
		return findAllAlertsByCriteria(criteria).size();
	}
	
	@Override
	public List<Alert> findAllAlertsByCriteria(Map<String, String> criteria) {
		return parseAlerts(criteria, true);
	}
	
    public List<Alert> findAllAlertsFromJson(String url) {
        String json = "";           
        
        BufferedReader in = null;
        try {
            URL feed = new URL(url);
            in = new BufferedReader(new InputStreamReader(feed.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                json += inputLine;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            try {            
                in.close();
            } catch (Exception e) {}
        }
        
        if (json == null || "".equals(json)) {
            return new ArrayList<Alert>();
        }
                
        return new JSONDeserializer<ArrayList<Alert>>().deserialize(json);
    }
	
	private List<Alert> parseAlerts(Map<String, String> criteria, boolean ignoreXmlCampus) {  
	    String campus = criteria.get("campus");
	    
		List<Alert> alerts = new ArrayList<Alert>();
		try {
			String url = configParamService.findValueByName(CAMPUS_STATUS_URL_PARAM) + campus;
			
			if ("IN".equals(campus)) {
			    url = "http://www.iupui.edu/rss/jagalert_iu.xml";
			} else if ("EA".equals(campus)) {
			    url = "http://www.iue.edu/emergency/feed.php"; 
			}
			
			Document doc = retrieveDocumentFromUrl(url, 5000, 5000);
			if (doc != null) {
				Element root = doc.getRootElement();
				List items = root.getChildren("status");
				for (Iterator iterator = items.iterator(); iterator.hasNext();) {
					Element item = (Element) iterator.next();
					String campusXml = item.getChildTextTrim("campus");
					//String compareCampus = Campus.valueOf(campus).campusAlertName();
	                //if (ignoreXmlCampus || compareCampus.equalsIgnoreCase(campusXml) || Campus.UA.campusAlertName().equals(campusXml)) {
	    				Alert alert = new Alert();
	    				alert.setCampus(item.getChildTextTrim("campus"));
	                    alert.setMobileText(item.getChildTextTrim("mobile-text"));
	                    alert.setPriority(item.getChildTextTrim("priority"));
	                    alert.setTitle(item.getChildTextTrim("title"));
	                    alert.setType(item.getChildTextTrim("type"));
	                    alert.setUrl(item.getChildTextTrim("url"));
	                    String keyStr = item.getChildTextTrim("key");
	                    int key = -1;
	                    if (keyStr != null && keyStr.length() > 0 && !keyStr.equals("null")) {
	                    	try {
	                    		key = Integer.parseInt(keyStr);	
	                    	} catch (NumberFormatException e) {
	                    		LOG.error("Error parsing Alert key: (" + keyStr + ") for Alert: " + alert.getTitle(), e);
	                    	}
	                    }
	                    alert.setKey(key);
	    				alerts.add(alert);			
	                //}
				}
			}
		} catch (JDOMException e) {
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}

		return alerts;
	}
	
	private Document retrieveDocumentFromUrl(String urlStr, int connectTimeout, int readTimeout) throws IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		URL urlObj = new URL(urlStr);
		URLConnection urlConnection = urlObj.openConnection();
		urlConnection.setConnectTimeout(connectTimeout);
		urlConnection.setReadTimeout(readTimeout);
		doc = builder.build(urlConnection.getInputStream());
		return doc;
	}

}


