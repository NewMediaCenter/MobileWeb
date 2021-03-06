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
 
package org.kuali.mobility.itnotices.service;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.kuali.mobility.itnotices.entity.ITNotice;
import org.springframework.stereotype.Service;

import flexjson.JSONSerializer;

@Service
public class ITNoticesServiceImpl implements ITNoticesService {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ITNoticesServiceImpl.class);

	public List<ITNotice> findAllITNotices() {
		return getITNoticesFromFeed();
	}
	
    public String toJson(Collection<ITNotice> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    private List<ITNotice> getITNoticesFromFeed() {
		List<ITNotice> notices = new ArrayList<ITNotice>();
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		URL urlObj;
		try {
			urlObj = new URL("http://itnotices.iu.edu/rss.aspx");
			URLConnection urlConnection = urlObj.openConnection();
			urlConnection.setConnectTimeout(5000);
			urlConnection.setReadTimeout(5000);
			urlConnection.connect();
			doc = builder.build(urlConnection.getInputStream());
			
			if (doc != null) {
				Element root = doc.getRootElement();
				List items = root.getChild("channel").getChildren("item");
				for (Iterator iterator = items.iterator(); iterator.hasNext();) {
					Element item = (Element) iterator.next();
					String services = "";
					List service = item.getChildren("service");
					for (Iterator iterator2 = service.iterator(); iterator2.hasNext();) {
						Element s = (Element) iterator2.next();
						services += s.getContent(0).getValue() + ", ";					
					}
					if (services.endsWith(", ")) {
						services = services.substring(0, services.length() - 2);
					}
					ITNotice notice = new ITNotice(item.getChildTextTrim("lastUpdated"), item.getChildTextTrim("noticeType"), item.getChildTextTrim("title"), services, item.getChildTextTrim("message"));
					determineImage(notice);
					notices.add(notice);
				}				
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
		return notices;
    }
    
	private void determineImage(ITNotice notice) {
		if ("Service News".equals(notice.getNoticeType())) {
			notice.setImageUrl("./images/itnotices/news.png");
		} else if ("Announcement".equals(notice.getNoticeType())) {
			notice.setImageUrl("./images/itnotices/announcement.png");
		} else if ("Alert".equals(notice.getNoticeType())) {
			notice.setImageUrl("./images/itnotices/error.png");
		} else if ("Key Alert".equals(notice.getNoticeType())) {
			notice.setImageUrl("./images/itnotices/critical.png");
		} else if ("Maintenance".equals(notice.getNoticeType())) {
			notice.setImageUrl("./images/itnotices/warning.png");
		}
	}

}
