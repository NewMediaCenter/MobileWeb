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
 
package org.kuali.mobility.people.service;

import java.util.Comparator;

import org.kuali.mobility.people.entity.Person;

public class PersonSort implements Comparator<Person> {

	private final String EMPTY_STRING = "";
	
	public int compare(Person o1, Person o2) {
	    int comparison = this.compareString(o1.getLastName(), o2.getLastName());
	    if (comparison == 0) {
	    	comparison = this.compareString(o1.getFirstName(), o2.getFirstName());
	    	if (comparison == 0) {
	    		comparison = this.compareString(o1.getDisplayName(), o2.getDisplayName());
		    	if (comparison == 0) {
		    		comparison = this.compareString(o1.getUserName(), o2.getUserName());
		    	}
	    	}
	    }
	    return comparison;
	}
	
	private int compareString(String s1, String s2) {
		if (s1 == null) {
			s1 = EMPTY_STRING;
		}
		if (s2 == null) {
			s2 = EMPTY_STRING;
		}
		return s1.compareTo(s2);
	}
}
