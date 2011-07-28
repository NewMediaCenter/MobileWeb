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

package org.kuali.mobility.athletics.service;

import org.kuali.mobility.athletics.entity.Athletics;
import org.kuali.mobility.athletics.entity.MatchData;
import org.kuali.mobility.athletics.entity.Player;
import org.kuali.mobility.athletics.entity.RosterData;
import org.kuali.mobility.athletics.entity.Sport;

public interface AthleticsService {
	public Athletics retrieveAthletics() throws Exception;

	public MatchData retrieveScheduleForSeason(Long sportId, Long seasonId) throws Exception;

	public Sport retrieveSport(Long sportId) throws Exception;

	public RosterData retrieveRosterForSeason(Long sportId, Long seasonId) throws Exception;

	public Player retrievePlayer(Long sportId, Long seasonId, Long playerId) throws Exception;
}
