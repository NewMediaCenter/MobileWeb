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

package org.kuali.mobility.computerlabs.service;

import java.util.Collection;
import java.util.List;

import org.kuali.mobility.computerlabs.entity.ComputerLab;
import org.kuali.mobility.computerlabs.entity.LabLocation;

public interface ComputerLabsService {

	public List<LabLocation> findAllLabLocationsByCampus(String campus);
	
	public List<ComputerLab> findAllComputerLabsByCampus(String campus);
	
	public String toJson(Collection<ComputerLab> collection);
	
	public String toJsonLabLocation(Collection<LabLocation> collection);
	
}
