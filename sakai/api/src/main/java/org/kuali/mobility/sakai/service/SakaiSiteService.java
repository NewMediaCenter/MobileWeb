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

import org.kuali.mobility.sakai.entity.Announcement;
import org.kuali.mobility.sakai.entity.Assignment;
import org.kuali.mobility.sakai.entity.Home;
import org.kuali.mobility.sakai.entity.Resource;
import org.kuali.mobility.sakai.entity.Roster;
import org.kuali.mobility.sakai.entity.Site;

public interface SakaiSiteService {
	public Home findSakaiHome(String user);
	public Site findSite(String siteId, String user);
	
	public Announcement findAnnouncementDetails(String json);
	public List<Announcement> findAllAnnouncements(String siteId, String user);
	public byte[] findAnnouncementAttachment(String siteId, String attachmentId, String userId);
	
	public List<Assignment> findAllAssignments(String siteId, String userId);
	public List<Assignment> findAssignmentDetails(String json);
	public String findCourseGrade(String json);
	
	public List<Roster> findRoster(String json);
	public Roster findParticipantDetails(String json, String displayId);
	
	public List<Resource> findSiteResources(String siteId, String userId);
	public byte[] getResource(String resId, String userId);
}
