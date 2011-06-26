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
 
package org.kuali.mobility.sakai.service;

import java.util.List;

import org.kuali.mobility.sakai.entity.SakaiAnnouncement;
import org.springframework.stereotype.Service;

@Service
public class SakaiAnnouncementServiceImpl implements SakaiAnnouncementService {

	public List<SakaiAnnouncement> findAllCourseAnnouncements(String json) {
		SakaiAnnouncementParser parser = new SakaiAnnouncementParser();
		List<SakaiAnnouncement> anns = parser.parseAnnouncements(json);
		return anns;
//		return new ArrayList<ComputerLab>();
	}
	
	public List<SakaiAnnouncement> findAnnouncementDetails(String json) {
		SakaiAnnouncementParser parser = new SakaiAnnouncementParser();
		List<SakaiAnnouncement> anns = parser.parseAnnouncementDetails(json);
		return anns;
	}
	
//    public String toJson(Collection<ComputerLab> collection) {
//        return new JSONSerializer().exclude("*.class").include("avList").serialize(collection);
////    	return new JSONSerializer().include("", "").serialize(collection);
//    }
	
}
