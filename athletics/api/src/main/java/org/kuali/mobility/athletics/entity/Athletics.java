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

package org.kuali.mobility.athletics.entity;

import java.io.Serializable;

public class Athletics implements Serializable {

	private static final long serialVersionUID = -8017395981407716607L;

	private MatchData matchData;
	
	private SportData sportData;

	private NewsData newsData;
	
	public NewsData getNewsData() {
		return newsData;
	}

	public void setNewsData(NewsData newsData) {
		this.newsData = newsData;
	}

	public SportData getSportData() {
		return sportData;
	}

	public void setSportData(SportData sportData) {
		this.sportData = sportData;
	}

	public MatchData getMatchData() {
		return matchData;
	}

	public void setMatchData(MatchData matchData) {
		this.matchData = matchData;
	}

}
