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

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity(name="MapsGroup")
@Table(name="MAPS_GROUP_T")
public class MapsGroup implements Serializable {

	private static final long serialVersionUID = -4775149005202188253L;

	@Id
    @SequenceGenerator(name="maps_group_maint_sequence", sequenceName="SEQ_MAPS_GROUP_T", initialValue=1000, allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="maps_group_maint_sequence")
    @Column(name="GROUP_ID")
	private Long groupId;
	
    @Column(name="GROUP_CODE")
	private String groupCode;
	
    @Column(name="SHORT_NAME")
	private String shortName;
	
    @Column(name="LONG_NAME")
	private String name;
    
    @Column(name="ACTIVE")
	private boolean active;
	
    @Version
    @Column(name="VER_NBR")
    protected Long versionNumber;
    
    @ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.MERGE})
    @JoinTable(
    		name="MAPS_LOCATION_GROUP_T",
    		joinColumns={@JoinColumn(name="GROUP_ID", referencedColumnName="GROUP_ID")},
    		inverseJoinColumns={@JoinColumn(name="LOCATION_ID", referencedColumnName="LOCATION_ID")}
    )
    private Set<Location> mapsLocations;

    @ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.MERGE})
    @JoinColumn(
    	name="PARENT_ID", nullable=true
    )
    private MapsGroup mapsGroupParent;
    
    @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.MERGE}, mappedBy="mapsGroupParent")
    private Set<MapsGroup> mapsGroupChildren;
    
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Set<Location> getMapsLocations() {
		return mapsLocations;
	}

	public void setMapsLocations(Set<Location> mapsLocations) {
		this.mapsLocations = mapsLocations;
	}

	public Set<MapsGroup> getMapsGroupChildren() {
		return mapsGroupChildren;
	}

	public void setMapsGroupChildren(Set<MapsGroup> mapsGroupChildren) {
		this.mapsGroupChildren = mapsGroupChildren;
	}

	public MapsGroup getMapsGroupParent() {
		return mapsGroupParent;
	}

	public void setMapsGroupParent(MapsGroup mapsGroupParent) {
		this.mapsGroupParent = mapsGroupParent;
	}

}
