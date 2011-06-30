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

import org.kuali.mobility.sakai.entity.SakaiAssignment;
import org.springframework.stereotype.Service;

@Service
public class SakaiAssignmentServiceImpl implements SakaiAssignmentService {

	public List<SakaiAssignment> findAllCourseAssignments(String json) {
		SakaiAssignmentParser parser = new SakaiAssignmentParser();
		List<SakaiAssignment> anns = parser.parseAssignments(json);
		return anns;
	}
	
	public List<SakaiAssignment> findAssignmentDetails(String json) {
		SakaiAssignmentParser parser = new SakaiAssignmentParser();
		List<SakaiAssignment> anns = parser.parseAssignmentDetails(json);
		return anns;
	}
	
	public String findCourseGrade(String json) {
		SakaiAssignmentParser parser = new SakaiAssignmentParser();
		String courseGrade = parser.parseCourseGrade(json);
		return courseGrade;
	}

}
