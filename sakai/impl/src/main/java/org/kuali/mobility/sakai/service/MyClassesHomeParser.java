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

import java.util.ArrayList;
import java.util.List;

import org.kuali.mobility.sakai.entity.MyClassesHome;

public class MyClassesHomeParser {

	


	public List<MyClassesHome> parseHome(String campus, String instructorId, String instructorName) {
		List<MyClassesHome> home = new ArrayList<MyClassesHome>();
		home = parse(campus, instructorId, instructorName);
		return home;
	}
	
	    private List<MyClassesHome> parse(String campus, String instructorId, String instructorName) {
    	List<MyClassesHome> home = new ArrayList<MyClassesHome>();
    	
    	for (int i = -1; i < 7; i++) {
    		MyClassesHome item = new MyClassesHome();
			item.setCode(i);
			switch (i){
			case -1:
				item.setTitle(instructorName);
				item.setDisplayId(instructorId);
				break;
			case 0:
				item.setTitle("Announcements");
				break;
			case 1:
				item.setTitle("Assignments");
				break;
			case 2:
				item.setTitle("Gradebook");
				break;
			case 3:
				item.setTitle("Roster");
				break;
			case 4:
				item.setTitle("Forums");
				break;
			case 5:
				item.setTitle("Resources");
				break;
			case 6:
				item.setTitle("Messages");
				break;
			}		
			home.add(item);
		}
		return home;
    }
}
