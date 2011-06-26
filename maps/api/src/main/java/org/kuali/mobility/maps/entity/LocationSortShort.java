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
 
package org.kuali.mobility.maps.entity;

import java.util.Comparator;

public class LocationSortShort implements Comparator<Location> {

	public int compare(Location loc1, Location loc2) {
		String cmp1 = loc1.getShortName();
		String cmp2 = loc2.getShortName();
		for (int cmp1pos = 0; cmp1pos < cmp1.length(); cmp1pos++) {
			char chr1 = cmp1.charAt(cmp1pos);
			if (cmp1pos < cmp2.length()) {
				// Still possible to compare at same position
				char chr2 = cmp2.charAt(cmp1pos);
				boolean chr1IsDigit = Character.isDigit(chr1);
				boolean chr2IsDigit = Character.isDigit(chr2);
				if (chr1IsDigit && chr2IsDigit) {
					// Compare numbers
					if (chr1 != chr2) {
						return chr1 - chr2;
					}
				} else if (chr1IsDigit && !chr2IsDigit) {
					// chr1 is a number and chr2 is not.
					return 1;
				} else if (!chr1IsDigit && chr2IsDigit) {
					return -1;
				} else {
					// Compare strings
					if (chr1 != chr2) {
						return chr1 - chr2;
					}
				}
			} else {
				//Our position in cmp1 can no longer be compared to cmp2. loc1 > loc2
				return 1;
			}
		}
		if (cmp2.length() > cmp1.length()) {
			// cmp2 was able to be compared further but cmp1 ran out of characters. loc2 > loc1
			return -1;
		}
		return 0;
	}

}
