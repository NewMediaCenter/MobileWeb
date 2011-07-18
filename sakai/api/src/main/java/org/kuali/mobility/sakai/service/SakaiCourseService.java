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
import java.util.Map;

import org.kuali.mobility.sakai.entity.SakaiAnnouncement;
import org.kuali.mobility.sakai.entity.SakaiAssignment;
import org.kuali.mobility.sakai.entity.SakaiSite;
import org.kuali.mobility.sakai.entity.SakaiHome;
import org.kuali.mobility.sakai.entity.SakaiRoster;

public interface SakaiCourseService {
	public SakaiHome findSakaiHome(String user);
	public SakaiSite findCourse(String siteId, String user);
	
	public List<SakaiAnnouncement> findAnnouncementDetails(String json);
	public List<SakaiAnnouncement> findAllCourseAnnouncements(String siteId, String user);
	
	public List<SakaiAssignment> findAllCourseAssignments(String siteId, String userId);
	public List<SakaiAssignment> findAssignmentDetails(String json);
	public String findCourseGrade(String json);
	
	public List<SakaiRoster> findCourseRoster(String json);
	public List<SakaiRoster> findCourseParticipantDetails(String json, String displayId);
}
