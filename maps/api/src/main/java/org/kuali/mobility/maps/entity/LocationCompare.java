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

public class LocationCompare {
	private Location locationImport;
	private Location locationStored;
	
	private String notes;
	
	private boolean shouldSaveNew;
	private boolean shouldSaveUpdate;
	private boolean shouldSaveExtraConfiguration;

	public Location getLocationImport() {
		return locationImport;
	}

	public void setLocationImport(Location locationImport) {
		this.locationImport = locationImport;
	}

	public Location getLocationStored() {
		return locationStored;
	}

	public void setLocationStored(Location locationStored) {
		this.locationStored = locationStored;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isShouldSaveNew() {
		return shouldSaveNew;
	}

	public void setShouldSaveNew(boolean shouldSaveNew) {
		this.shouldSaveNew = shouldSaveNew;
	}

	public boolean isShouldSaveUpdate() {
		return shouldSaveUpdate;
	}

	public void setShouldSaveUpdate(boolean shouldSaveUpdate) {
		this.shouldSaveUpdate = shouldSaveUpdate;
	}

	public boolean isShouldSaveExtraConfiguration() {
		return shouldSaveExtraConfiguration;
	}

	public void setShouldSaveExtraConfiguration(boolean shouldSaveExtraConfiguration) {
		this.shouldSaveExtraConfiguration = shouldSaveExtraConfiguration;
	}
	
}
