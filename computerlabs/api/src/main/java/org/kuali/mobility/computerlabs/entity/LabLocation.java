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

package org.kuali.mobility.computerlabs.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LabLocation implements Serializable, Comparable<LabLocation> {

	private static final long serialVersionUID = -4991494626566555287L;
	
	private String name;
	private List<ComputerLab> labs;
	
	public LabLocation(String name) {
		this.name = name;
		this.labs = new ArrayList<ComputerLab>();
	}

	public List<ComputerLab> getComputerLabs() {
		return labs;
	}

	public void setComputerLabs(List<ComputerLab> labs) {
		this.labs = labs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int compareTo(LabLocation that) {
        if (this == null || that == null) {
            return -1;
        }
        if (this.getName() == that.getName()) {
        	return 0;
        }
		return this.getName().compareTo(that.getName());
	}

}
