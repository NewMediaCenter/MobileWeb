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
 
package org.kuali.mobility.maps.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.kuali.mobility.maps.dao.LocationDao;
import org.kuali.mobility.maps.entity.Location;
import org.kuali.mobility.maps.entity.LocationCompare;
import org.kuali.mobility.maps.entity.LocationSort;
import org.kuali.mobility.maps.entity.LocationSortShort;
import org.kuali.mobility.maps.entity.MapsGroup;
import org.kuali.mobility.maps.entity.MapsGroupSortShort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Service
public class LocationServiceImpl implements LocationService {
	
//    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LocationServiceImpl.class);

    @Autowired
    private LocationDao locationDao;
    public void setLocationDao(LocationDao locationDao) {
        this.locationDao = locationDao;
    }
    public LocationDao getLocationDao() {
        return locationDao;
    }
    
//	public List<Location> getLocationsByCampusCode(String campusCode) {
//		return this.getLocationDao().findLocationsByCampusCode(campusCode);
//	}
	
    @Transactional
	public Location getLocationById(Long locationId) {
		Location location = this.getLocationDao().findLocationById(locationId);
//		Hibernate.initialize(location.getMapsGroups());
		return location;
	}
	
//	private Location getLocationByIdLazy(Long locationId) {
//		Location location = this.getLocationDao().findLocationById(locationId);
//		return location;
//	}
	
    @Transactional
	public List<Location> searchLocations(String search) {
		List<Location> locations = this.getLocationDao().searchLocations(search);
		/*
		 * Only add the location by Id if it's not already in the search
		 */
//		try {
//			Long locationId = new Long(search);
//			Location location = this.getLocationById(locationId);
//			locations.add(0, location);
//		} catch (Exception e) {}
		return locations;
	}
	
    @Transactional
	public List<Location> getAllLocations() {
		return this.getLocationDao().getAllLocations();
	}
	
    @Transactional
	public List<Location> getAllActiveLocations() {
		return this.getLocationDao().getAllActiveLocations();
	}
	
    @Transactional
	public List<Location> getAllUngroupedLocations() {
		return this.getLocationDao().getAllUngroupedLocations();
	}
	
    @Transactional
	public void deleteLocation(Long locationId) {
		this.getLocationDao().deleteLocationById(locationId);
	}

    @Transactional
	public void saveLocation(Location location) {
		this.getLocationDao().saveLocation(location);
	}
	
    @Transactional
	public List<Location> getLocationsByCode(String code) {
		return this.getLocationDao().findLocationsByCode(code);
	}
	
    @Transactional
	public List<Location> getLocationsByBuildingCode(String buildingCode) {
		return this.getLocationDao().getLocationsByBuildingCode(buildingCode);
	}

    @Transactional
	public void importXml(String campusCode, String xml) {
//		String xml = this.getPageAsStringFromUrl(url);
		List<Location> locations = this.buildLocationsByCampusCodeFromXml(campusCode, xml);
//		Collections.shuffle(locations);
		this.importLocations(locations);
	}
	
    @Transactional
	public List<LocationCompare> importXmlAnalysis(String campusCode, String xml) {
		List<Location> locations = this.buildLocationsByCampusCodeFromXml(campusCode, xml);
		return this.importAnalysis(locations);
	}
	
    @Transactional
	private List<LocationCompare> importAnalysis(List<Location> locations) {
		List<LocationCompare> locationCompares = new ArrayList<LocationCompare>();
		for (Location location : locations) {
			LocationCompare locationCompare = new LocationCompare();
			locationCompare.setLocationImport(location);

			if (location.getCode() != null) {
				// We're importing an update based on an MIU-specific code
				List<Location> storedLocations = this.getLocationsByCode(location.getCode());
				if (storedLocations.size() == 1) {
					Location storedLocation = storedLocations.get(0);
					locationCompare.setLocationStored(storedLocation);
					if (!storedLocation.isOverride()) {
						locationCompare.setShouldSaveUpdate(true);
					} else {
						locationCompare.setNotes("Should not update location due to override flag: " + location.getCode());
					}
				} else if (storedLocations.size() == 0) {
					// If the mobile code is set, the location should have an active setting on it already.
					locationCompare.setShouldSaveNew(true);
				} else {
					locationCompare.setNotes("Did not update location due to multiple locations containing the same code: " + location.getCode());
				}
			} 
			else if (location.getBuildingCode() != null) {
				 // This update is based on data from the buildings database.
				List<Location> storedLocations = this.getLocationsByBuildingCode(location.getBuildingCode());
				if (storedLocations.size() == 1) {
					Location storedLocation = storedLocations.get(0);
					locationCompare.setLocationStored(storedLocation);
					if (!storedLocation.isOverride()) {
						locationCompare.setShouldSaveUpdate(true);
					} else {
						locationCompare.setNotes("Did not update location due to override flag: " + location.getCode());
					}
				} else if (storedLocations.size() == 0) {
					// Code is null, it should be assigned.
					locationCompare.setShouldSaveExtraConfiguration(true);
				} else {
					locationCompare.setNotes("Did not update location due to multiple locations containing the same building code: " + location.getBuildingCode());
				}
			}
			else {
				// Code is null, it should be assigned.
				locationCompare.setShouldSaveExtraConfiguration(true);
			}
			locationCompares.add(locationCompare);
		}
		return locationCompares;
	}
	
    @Transactional
	private void importLocations(List<Location> locations) {
		for (Location location : locations) {
			if (location.getCode() != null) {
				// We're importing an update based on an MIU-specific code
				List<Location> storedLocations = this.getLocationsByCode(location.getCode());
				if (storedLocations.size() == 1) {
					Location storedLocation = storedLocations.get(0);
					if (!storedLocation.isOverride()) {
						this.importUpdateLocation(storedLocation, location);
					} else {
//						LOG.info("Did not update location due to override flag: " + location.getCode());
					}
				} else if (storedLocations.size() == 0) {
					// If the mobile code is set, the location should have an active setting on it already.
					this.saveLocation(location);
				} else {
//					LOG.info("Did not update location due to multiple locations containing the same code: " + location.getCode());
				}
			} 
			else if (location.getBuildingCode() != null) {
				 // This update is based on data from the buildings database.
				List<Location> storedLocations = this.getLocationsByBuildingCode(location.getBuildingCode());
				if (storedLocations.size() == 1) {
					Location storedLocation = storedLocations.get(0);
					if (!storedLocation.isOverride()) {
						this.importUpdateLocation(storedLocation, location);
					} else {
//						LOG.info("Did not update location due to override flag: " + location.getCode());
					}
				} else if (storedLocations.size() == 0) {
					// Code is null, it should be assigned.
					this.saveLocation(location);
					location.setCode("M" + location.getLocationId());
//					LOG.info("Imported Location: " + location.getLocationId());
					location.setActive(true);
					this.saveLocation(location);
				} else {
//					LOG.info("Did not update location due to multiple locations containing the same building code: " + location.getBuildingCode());
				}
			}
			else {
				// Code is null, it should be assigned.
				this.saveLocation(location);
				location.setCode("M" + location.getLocationId());
//				LOG.info("Imported Location: " + location.getLocationId());
				location.setActive(true);
				this.saveLocation(location);
			}
		}
	}
	
	private void importUpdateLocation(Location storedLocation, Location location) {
		// We probably shouldn't write over data that exists, in case the datasource doesn't have it. (for example, latitude/longitude coordinates)
		// We might want to put this in the location object, minus the call to save. That way we can update this easily when we need to modify the location object.
		storedLocation.setActive(location.isActive());
		storedLocation.setOverride(location.isOverride());
		storedLocation.setCity(location.getCity());
		if (location.getLatitude() != null && !"".equals(location.getLatitude())) {
			storedLocation.setLatitude(location.getLatitude());
		}
		if (location.getLongitude() != null && !"".equals(location.getLongitude())) {
			storedLocation.setLongitude(location.getLongitude());	
		}
		if (location.getBuildingCode() != null && !"".equals(location.getBuildingCode())) {
			storedLocation.setBuildingCode(location.getBuildingCode());
		}
		if (location.getShortCode() != null && !"".equals(location.getShortCode())) {
			storedLocation.setShortCode(location.getShortCode());
		}
		storedLocation.setName(location.getName());
		storedLocation.setShortName(location.getShortName());
		storedLocation.setState(location.getState());
		storedLocation.setStreet(location.getStreet());
		storedLocation.setZip(location.getZip());
		if (storedLocation.getMapsGroups() != null && storedLocation.getMapsGroups().size() > 0) {
			if (location.getMapsGroups() != null) {
				storedLocation.getMapsGroups().addAll(location.getMapsGroups());
			}
		} else {
			storedLocation.setMapsGroups(location.getMapsGroups());			
		}
		this.saveLocation(storedLocation);
	}
	
    @Transactional
	public List<MapsGroup> getAllMapsGroups() {
		return this.getLocationDao().getAllMapsGroups();
	}
	
    @Transactional
	public List<MapsGroup> getAllActiveMapsGroups() {
		List<MapsGroup> mapsGroups = this.getLocationDao().getAllActiveMapsGroups();
//		for (MapsGroup mapsGroup : mapsGroups) {
//			Hibernate.initialize(mapsGroup.getMapsLocations());
//			Hibernate.initialize(mapsGroup.getMapsGroupChildren());
//			Hibernate.initialize(mapsGroup.getMapsGroupParent());
//		}
		return mapsGroups;
	}
	
    @Transactional
	public MapsGroup getMapsGroupById(Long groupId) {
		MapsGroup mapsGroup = this.getLocationDao().getMapsGroupById(groupId);
//		Hibernate.initialize(mapsGroup.getMapsLocations());
//		Hibernate.initialize(mapsGroup.getMapsGroupChildren());
//		Hibernate.initialize(mapsGroup.getMapsGroupParent());
		return mapsGroup;
	}
	
	private MapsGroup getMapsGroupByIdLazy(Long groupId) {
		MapsGroup mapsGroup = this.getLocationDao().getMapsGroupById(groupId);
		return mapsGroup;
	}
	
    @Transactional
	public MapsGroup getMapsGroupByCode(String groupCode) {
		MapsGroup mapsGroup = this.getLocationDao().getMapsGroupByCode(groupCode);
		return mapsGroup;
	}
	
    @Transactional
	public void saveMapsGroup(MapsGroup group) {
		this.getLocationDao().saveMapsGroup(group);
	}
	
    @Transactional
	public void deleteMapsGroup(Long groupId) {
		this.getLocationDao().deleteMapsGroup(groupId);
	}
	
    @Transactional
	public void addMapsGroupToLocation(Long groupId, Long locationId) {
		Location location = this.getLocationById(locationId);
		Set<MapsGroup> mapsGroups = null;
		boolean addGroup = true;
		if (location.getMapsGroups().size() == 0) {
			location.setMapsGroups(new HashSet<MapsGroup>());
		} else {
			mapsGroups = location.getMapsGroups();
			for (MapsGroup mapsGroup : mapsGroups) {
				if (groupId.equals(mapsGroup.getGroupId())) {
					addGroup = false;
				}
			}			
		}
		if (addGroup) {
			MapsGroup mapsGroupAdd = this.getMapsGroupByIdLazy(groupId);
			if (mapsGroupAdd != null) {
				location.getMapsGroups().add(mapsGroupAdd);
				this.saveLocation(location);
			}
		}
	}
	
    @Transactional
	public void removeMapsGroupFromLocation(Long groupId, Long locationId) {
		Location location = this.getLocationById(locationId);
		Set<MapsGroup> mapsGroups = location.getMapsGroups();
		MapsGroup mapsGroupRemove = null;
		for (MapsGroup mapsGroup : mapsGroups) {
			if (groupId.equals(mapsGroup.getGroupId())) {
				mapsGroupRemove = mapsGroup;
				break;
			}
		}
		if (mapsGroupRemove != null) {
			location.getMapsGroups().remove(mapsGroupRemove);
			this.saveLocation(location);
		}
	}
	
    @Transactional
	public void addMapsGroupChild(Long groupId, Long addGroupId) {
		MapsGroup group = this.getMapsGroupById(groupId);
		if (group != null) {
			MapsGroup mapsGroupAdd = this.getMapsGroupByIdLazy(addGroupId);
			if (mapsGroupAdd != null) {
//				Set<MapsGroup> children = group.getMapsGroupChildren();
//				if (children != null) {
//					children.add(mapsGroupAdd);
//					this.saveMapsGroup(group);
//				}				
				mapsGroupAdd.setMapsGroupParent(group);
				this.saveMapsGroup(mapsGroupAdd);
			}
		}
	}
	
    @Transactional
	public void removeMapsGroupChild(Long groupId, Long removeGroupId) {
		MapsGroup group = this.getMapsGroupById(groupId);
		if (group != null) {
			MapsGroup mapsGroupRemove = this.getMapsGroupByIdLazy(removeGroupId);
			if (mapsGroupRemove != null) {
				mapsGroupRemove.setMapsGroupParent(null);
				this.saveMapsGroup(mapsGroupRemove);
			}
		}
	}
	
    @Transactional
	public String exportXmlOneStartFormat(Long groupId) {
		MapsGroup group = this.getMapsGroupById(groupId);
		List<Location> locations = new ArrayList<Location>();
		for (Location location : group.getMapsLocations()) {
			if (location.getBuildingCode() != null && !"".equals(location.getBuildingCode()) && location.isActive()) {
				locations.add(location);
			}
		}
		Collections.sort(locations, new LocationSort());
		return this.documentToString(this.writeLocationsToDocument(locations, group.getGroupCode(), true));
	}
	
    @Transactional
	public String exportXmlMobileFormat(Long groupId) {
		MapsGroup group = this.getMapsGroupById(groupId);
		List<Location> locations = new ArrayList<Location>();
		for (Location location : group.getMapsLocations()) {
			if (location.getCode() != null && !"".equals(location.getCode())) {
				locations.add(location);
			}
		}
		Collections.sort(locations, new LocationSort());
		return this.documentToString(this.writeLocationsToDocument(locations, group.getGroupCode(), false));
	}
	
    @Transactional
	public String exportGroupXmlMobileFormat(Long groupId) {
		MapsGroup group = this.getMapsGroupById(groupId);
//		List<Location> locations = new ArrayList<Location>();
//		for (Location location : group.getMapsLocations()) {
//			if (location.getCode() != null && !"".equals(location.getCode())) {
//				locations.add(location);
//			}
//		}
//		Collections.sort(locations, new LocationSort());
//		return this.documentToString(this.writeLocationsToDocument(locations, group.getGroupCode(), false));
		Document doc = this.writeMapsGroupToDocument(group, group.getGroupCode(), false);
		return this.documentToString(doc);
	}
	
	private Document writeLocationsToDocument(List<Location> locations, String groupCode, boolean isOneStart) {
		DocumentBuilderFactory dbf;
		Document xmlDoc = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			xmlDoc = createDocument(dbf);
			if (xmlDoc != null) {
				// Create Root
				Element rootEle = xmlDoc.createElement("markers");
				xmlDoc.appendChild(rootEle);
				for (Location loc : locations) {
					// Create child element, attach to root
					Element marker = xmlDoc.createElement("marker");
					// XML escaping does not appear to be required here
					marker.setAttribute("lat", "" + loc.getLatitude());
					marker.setAttribute("lng", "" + loc.getLongitude());
					marker.setAttribute("cd", loc.getBuildingCode());
					marker.setAttribute("nm", loc.getName());
					marker.setAttribute("snm", loc.getShortName());
					marker.setAttribute("cty", loc.getCity());
					marker.setAttribute("sta", loc.getState());
					marker.setAttribute("str", loc.getStreet());
					marker.setAttribute("zip", loc.getZip());
					marker.setAttribute("miu", loc.getCode());
					if (!isOneStart) {
						marker.setAttribute("scd", loc.getShortCode());
						marker.setAttribute("active", "" + loc.isActive());
						marker.setAttribute("override", "" + loc.isOverride());
						marker.setAttribute("grp", groupCode);						
					}
					rootEle.appendChild(marker);
				}
			}
		} catch (Exception e) {
//			LOG.error("Failed to create document: ", e);
		}
		return xmlDoc;
	}
	
	private Document createDocument(DocumentBuilderFactory dbf) {
		Document dom;
		try {
			//Create Document
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.newDocument();
			return dom;
		} catch (ParserConfigurationException e) {
//			LOG.info("Error instantiating DocumentBuilder " + e);
		}
		return null;
	}
	
	private String documentToString(Document doc) {
		String xml = "";
		try {
			OutputFormat format = new OutputFormat(doc);
			format.setIndenting(true);
			format.setLineWidth(80);
			OutputStream out = new ByteArrayOutputStream();
			XMLSerializer serializer = new XMLSerializer(format);
			serializer.setOutputByteStream(out);
			serializer.serialize(doc);
			xml = out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xml;
	}
	
	private String getPageAsStringFromUrl(String link) {
        String html = "";
        BufferedReader in = null;
        try {
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	html += inputLine + " \n";
            }
        } catch (Exception e) {
//            LOG.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {}
            }
        }
        return html;
	}
    
	private List<Location> buildLocationsByCampusCodeFromXml(String groupCode, String xml) {
		List<Location> locations = new ArrayList<Location>();
		MapsGroup mapsGroup = this.getMapsGroupByCode(groupCode);
		if (mapsGroup != null) {
			if (!"".equals(xml)) {
				XPathFactory factory = XPathFactory.newInstance();
				XPath xPath = factory.newXPath();
				DocumentBuilderFactory dbf;
				try {
					dbf = DocumentBuilderFactory.newInstance();
					Document doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
					//Get all markers from XML
					NodeList nodes = (NodeList) xPath.evaluate("/markers/marker", doc, XPathConstants.NODESET);
					for (int i = 0; i < nodes.getLength(); i++) {
						Node node = nodes.item(i);
						if (node != null) {
							NamedNodeMap nodeMap = node.getAttributes();
							Location loc = new Location();
//							loc.setCampus(campusCode);
							// Code should be updated to assign an MIU-specific code
							if (isNotNullNotEmptyAttribute(nodeMap, "miu")) {
								loc.setCode(nodeMap.getNamedItem("miu").getNodeValue());
							}
							if (isNotNullNotEmptyAttribute(nodeMap, "scd")) {
								loc.setShortCode(nodeMap.getNamedItem("scd").getNodeValue());
							}
							if (isNotNullNotEmptyAttribute(nodeMap, "cd")) {
								loc.setBuildingCode(nodeMap.getNamedItem("cd").getNodeValue());	
							}
							if (isNotNullNotEmptyAttribute(nodeMap, "nm")) {
								loc.setName(nodeMap.getNamedItem("nm").getNodeValue());	
							}
							if (isNotNullNotEmptyAttribute(nodeMap, "snm")) {
								loc.setShortName(nodeMap.getNamedItem("snm").getNodeValue());	
							}
							if (isNotNullNotEmptyAttribute(nodeMap, "cty")) {
								loc.setCity(nodeMap.getNamedItem("cty").getNodeValue());	
							}
							if (isNotNullNotEmptyAttribute(nodeMap, "sta")) {
								loc.setState(nodeMap.getNamedItem("sta").getNodeValue());	
							}
							if (isNotNullNotEmptyAttribute(nodeMap, "str")) {
								loc.setStreet(nodeMap.getNamedItem("str").getNodeValue());	
							}
							if (isNotNullNotEmptyAttribute(nodeMap, "zip")) {
								loc.setZip(nodeMap.getNamedItem("zip").getNodeValue());	
							}
							if (isNotNullNotEmptyAttribute(nodeMap, "active")) {
								Boolean active = new Boolean(nodeMap.getNamedItem("active").getNodeValue());
								loc.setActive(active);
							} else {
								loc.setActive(true);
							}
							if (isNotNullNotEmptyAttribute(nodeMap, "override")) {
								Boolean override = new Boolean(nodeMap.getNamedItem("override").getNodeValue());
								loc.setOverride(override);
							} else {
								loc.setOverride(false);
							}
							
							try {
								loc.setLatitude(new Double(nodeMap.getNamedItem("lat").getNodeValue()));	
							} catch (Exception e) {}
							try {
								loc.setLongitude(new Double(nodeMap.getNamedItem("lng").getNodeValue()));
							} catch (Exception e) {}
							
							Set<MapsGroup> mapsGroups = new HashSet<MapsGroup>();
							try {
								if (isNotNullNotEmptyAttribute(nodeMap, "grp")) {
									MapsGroup mapsGroupForThisLocation = this.getMapsGroupByCode(nodeMap.getNamedItem("grp").getNodeValue());
									if (mapsGroupForThisLocation != null) {
										mapsGroups.add(mapsGroupForThisLocation);	
									} else {
//										LOG.info("Location " + loc.getName() + " (" + loc.getShortName() + ") assigned to default group because group " + nodeMap.getNamedItem("grp").getNodeValue() + " does not exist.");
										mapsGroups.add(mapsGroup);
									}
								} else {
									mapsGroups.add(mapsGroup);					
								}								
							} catch (Exception e) {}
							loc.setMapsGroups(mapsGroups);
							locations.add(loc);
						}
					}
				} catch (Exception e) {
//					LOG.info("Error loading data to group code: " + groupCode);
				}
			}
		} else {
//			LOG.info("Error loading data to group code due to missing group: " + groupCode);
		}
		return locations;
	}
	
	private boolean isNotNullNotEmptyAttribute(NamedNodeMap nodeMap, String attributeName) {
		return nodeMap.getNamedItem(attributeName) != null && !"".equals(nodeMap.getNamedItem(attributeName).getNodeValue());
	}
	
    @Transactional
	public void importMapsGroupsFromXml(String xml) {
		List<MapsGroup> mapsGroups = this.buildMapsGroupsFromXml(xml);
		this.importMapsGroups(mapsGroups);
	}
	
	private void importMapsGroups(List<MapsGroup> mapsGroups) {
		for (MapsGroup group : mapsGroups) {
//			LOG.info("GROUP: " + group.getGroupCode() + " " + group.getName());
			this.importMapsGroup(group);
			// Assume that saving the group above this makes its ID available to save as parent.
			
		}
	}
	
	private void importMapsGroup(MapsGroup group) {
		// A MapsGroup object created for importation, such as the kind passed to this method, is not safe to save to the DB on its own. Parent and child objects will be incomplete.
		MapsGroup dbMapsGroup = this.getMapsGroupByCode(group.getGroupCode());
		if (dbMapsGroup == null) {
			// Group does not exist in DB. Save group.
			MapsGroup newMapsGroup = new MapsGroup();
			newMapsGroup.setGroupCode(group.getGroupCode());
			newMapsGroup.setName(group.getName());
			newMapsGroup.setShortName(group.getShortName());
			newMapsGroup.setActive(group.isActive());
			this.saveMapsGroup(newMapsGroup);
			dbMapsGroup = newMapsGroup;
		} else {
			dbMapsGroup.setName(group.getName());
			dbMapsGroup.setShortName(group.getShortName());
			dbMapsGroup.setActive(group.isActive());
			this.saveMapsGroup(dbMapsGroup);
		}
//		LOG.info("DB GROUP: " + dbMapsGroup.getGroupCode() + " " + dbMapsGroup.getName());
		// Add locations.
		if (group.getMapsLocations() != null) {
			// A MapsGroup import object will contain child locations that only contain codes.
			Set<Location> mapsLocations = dbMapsGroup.getMapsLocations();
			if (mapsLocations == null) {
				mapsLocations = new HashSet<Location>();
			}
			for (Location location : group.getMapsLocations()) {
//				LOG.info("LOCATION: " + location.getCode() + " " + location.getName());
				List<Location> dbLocations = this.getLocationsByCode(location.getCode());
				if (dbLocations != null) {
					if (dbLocations.size() > 1) {
//						LOG.info("There is more than one location with the code " + location.getCode() + ", so it won't be imported.");
						for (Location dbLocation : dbLocations) {
//							LOG.info("Duplicate Location: " + dbLocation.getLocationId() + " " + dbLocation.getCode() + " " + dbLocation.getName());	
						}
					} else {
						mapsLocations.add(dbLocations.get(0));
						this.addMapsGroupToLocation(dbMapsGroup.getGroupId(), dbLocations.get(0).getLocationId());
					}
				} else {
//					LOG.info("Location with code " + location.getCode() + " does not exist.");
				}
			}				
		}
		// Set parent
		if (group.getMapsGroupParent() != null && group.getMapsGroupParent().getGroupCode() != null && !"".equals(group.getMapsGroupParent().getGroupCode())) {
			MapsGroup dbMapsGroupParent = this.getMapsGroupByCode(group.getMapsGroupParent().getGroupCode());
			dbMapsGroup.setMapsGroupParent(dbMapsGroupParent);
		}
		this.saveMapsGroup(dbMapsGroup);
		// Handle child groups
		if (group.getMapsGroupChildren() != null) {
			for (MapsGroup mapsGroupChild : group.getMapsGroupChildren()) {
				// Call this method again
				this.importMapsGroup(mapsGroupChild);
			}
		}
	}
	
	private List<MapsGroup> buildMapsGroupsFromXml(String xml) {
		List<MapsGroup> mapsGroups = new ArrayList<MapsGroup>();
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		DocumentBuilderFactory dbf;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			Document doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
			//Get all markers from XML
			NodeList nodes = (NodeList) xPath.evaluate("/root/group", doc, XPathConstants.NODESET);
			/*
			 * Iterate nodes, should only find one group but if there are many they'll just be many root nodes.
			 */
			Map<String, MapsGroup> map = new HashMap<String, MapsGroup>();
			mapsGroups = this.iterateMapsGroupNodes(nodes, map, null);
		} catch (Exception e) {
//			LOG.info("Error loading data: ");
		}
		return mapsGroups;
	}
	
	private List<MapsGroup> iterateMapsGroupNodes(NodeList nodes, Map<String, MapsGroup> map, MapsGroup mapsGroupParent) {
		List<MapsGroup> mapsGroups = new ArrayList<MapsGroup>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			NamedNodeMap nodeMap = node.getAttributes();
			try {
				if (node.getNodeName().equals("group")) {
					String mapsGroupCode = nodeMap.getNamedItem("groupCode").getNodeValue();
					if (map.get(mapsGroupCode) == null) {
						MapsGroup mapsGroup = new MapsGroup();
						mapsGroup.setGroupCode(nodeMap.getNamedItem("groupCode").getNodeValue());
						mapsGroup.setName(nodeMap.getNamedItem("name").getNodeValue());
						mapsGroup.setShortName(nodeMap.getNamedItem("shortName").getNodeValue());
						if (mapsGroupParent != null) {
							mapsGroup.setMapsGroupParent(mapsGroupParent);
						}
						// Store group to map so we don't run it more than once
						map.put(mapsGroup.getGroupCode(), mapsGroup);
						// Store group to list for returning processed MapsGroups to calling method
						mapsGroups.add(mapsGroup);
						// Done processing mapsgroup node, find children of this mapsgroup
						NodeList childNodes = node.getChildNodes();
						Set<MapsGroup> mapsGroupChildren = new HashSet<MapsGroup>();
						mapsGroupChildren.addAll(this.iterateMapsGroupNodes(childNodes, map, mapsGroup));
						mapsGroup.setMapsGroupChildren(mapsGroupChildren);
					} else {
//						LOG.info("MapsGroup code already loaded in this run: " + mapsGroupCode);
					}
				} else if (node.getNodeName().equals("location")) {
					Location location = new Location();
					location.setCode(nodeMap.getNamedItem("code").getNodeValue());
					if (mapsGroupParent != null) {
						if (mapsGroupParent.getMapsLocations() == null) {
							mapsGroupParent.setMapsLocations(new HashSet<Location>());
						}
						mapsGroupParent.getMapsLocations().add(location);
					}
				}				
			} catch (Exception e) {
//				LOG.error("Error loading data from node: ", e);
			}
		}
		return mapsGroups;
	}
	
	private Document writeMapsGroupToDocument(MapsGroup mapsGroup, String groupCode, boolean isOneStart) {
		DocumentBuilderFactory dbf;
		Document xmlDoc = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			xmlDoc = createDocument(dbf);
			if (xmlDoc != null) {
				// Create Root
				Element rootElement = xmlDoc.createElement("root");
				xmlDoc.appendChild(rootElement);
				// Handle the maps groups
				Map<Long, Long> map = new HashMap<Long, Long>();
				Element groupElement = mapsGroupToElement(xmlDoc, mapsGroup, map);
				if (groupElement != null) {
					rootElement.appendChild(groupElement);	
				}
			}
		} catch (Exception e) {
//			LOG.error("Failed to create document: ", e);
		}
		return xmlDoc;
	}
	
	private Element mapsGroupToElement(Document xmlDoc, MapsGroup mapsGroup, Map<Long, Long> map) {
		if (map.get(mapsGroup.getGroupId()) == null) {
			map.put(mapsGroup.getGroupId(), mapsGroup.getGroupId());
			Element groupElement = xmlDoc.createElement("group");
			groupElement.setAttribute("groupCode", mapsGroup.getGroupCode());
			groupElement.setAttribute("name", mapsGroup.getName());
			groupElement.setAttribute("shortName", mapsGroup.getShortName());
			groupElement.setAttribute("active", "" + mapsGroup.isActive());
			List<Location> locations = new ArrayList<Location>(mapsGroup.getMapsLocations());
			Collections.sort(locations, new LocationSortShort());
			for (Location location : locations) {
				Element locationElement = xmlDoc.createElement("location");
				locationElement.setAttribute("code", location.getCode());
				groupElement.appendChild(locationElement);
			}
			List<MapsGroup> children = new ArrayList<MapsGroup>(mapsGroup.getMapsGroupChildren());
			Collections.sort(children, new MapsGroupSortShort());
			for (MapsGroup child : children) {
				Element childGroup = mapsGroupToElement(xmlDoc, child, map);
				if (childGroup != null) {
					groupElement.appendChild(childGroup);	
				}
			}
			return groupElement;			
		} else {
//			LOG.info("MapsGroup " + mapsGroup.getGroupId() + " " + mapsGroup.getGroupCode() + " has already been appended.");
		}
		return null;
	}

}
