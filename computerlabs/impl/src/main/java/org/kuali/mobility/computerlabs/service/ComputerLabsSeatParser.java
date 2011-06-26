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

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.kuali.mobility.computerlabs.entity.ComputerLab;
import org.kuali.mobility.computerlabs.entity.LabAvailability;

public class ComputerLabsSeatParser {

	protected Document retrieveDocumentFromUrl(String urlStr, int connectTimeout, int readTimeout) throws IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		URL urlObj = new URL(urlStr);
		URLConnection urlConnection = urlObj.openConnection();
		urlConnection.setConnectTimeout(connectTimeout);
		urlConnection.setReadTimeout(readTimeout);
//		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//		if (bufferedReader != null) {
//			doc = builder.build(bufferedReader);
//		}
		doc = builder.build(urlConnection.getInputStream());
		return doc;
	}
	
	public List<ComputerLab> parseSeats(String campus) {
		return getComputerLabsForCampus(campus);
	}
	
	private List<ComputerLab> getComputerLabsForCampus(String campus) {
		List<ComputerLab> labs = new ArrayList<ComputerLab>();
		if ("BL".equals(campus)) {
			labs = getComputerLabsFromUrl("http://stcweb.stc.indiana.edu/public/Seatfinder/seatfinder.xml", campus, true);
		} else if ("IN".equals(campus)) {
			labs = getComputerLabsFromUrl("http://stc.iupui.edu/seatfinderxml.php", campus, false);
			labs.addAll(getComputerLabsFromUrl("http://ulib.iupui.edu/utility/seats.php?show=locations&type=data", campus, false));
		} else if ("CO".equals(campus)) {
			labs = getComputerLabsFromUrl("http://www.iupuc.edu/SeatFinderService/Default.aspx", campus, false);
		} else if ("EA".equals(campus)) {
			labs = getComputerLabsFromUrl("http://www.iue.edu/it/STC/stc_machines.xml", campus, false);			
		} else if ("SB".equals(campus)) {
			labs = getComputerLabsFromUrl("http://it.iusb.edu/miuxml/mcpu.php", campus, false);
		} else if ("SE".equals(campus)) {
			labs = getComputerLabsFromUrl("http://seatfinder.ius.edu/feed/", campus, false);
		}
		return labs;
	}
	
	private List<ComputerLab> getComputerLabsFromUrl(String url, String campus, boolean convert) {
		List<ComputerLab> labs = new ArrayList<ComputerLab>();
		labs = parse(campus, convert, url);
		return labs;
	}
	
	/*
	public StcModel parseSeats(String campus) {
	    boolean convert = true;

	    String url = ServiceLocator.getCacheService().findConfigParamValueByName("STC_SEAT_FINDER_URL");
        
	    if (Campus.EA.name().equals(campus)) {
            convert = false;
            url = ServiceLocator.getCacheService().findConfigParamValueByName("STC_SEAT_FINDER_URL_EA");
        } else if (Campus.SE.name().equals(campus)) {
            convert = false;
            url = ServiceLocator.getCacheService().findConfigParamValueByName("STC_SEAT_FINDER_URL_SE") ;
        } else if (Campus.IN.name().equals(campus)) {
            convert = false;
            url = ServiceLocator.getCacheService().findConfigParamValueByName("STC_SEAT_FINDER_URL_IN") ;
        } else if (Campus.SB.name().equals(campus)) {
            convert = false;
            url = ServiceLocator.getCacheService().findConfigParamValueByName("STC_SEAT_FINDER_URL_SB") ;
        } else if (Campus.CO.name().equals(campus)) {
            convert = false;
            url = ServiceLocator.getCacheService().findConfigParamValueByName("STC_SEAT_FINDER_URL_CO") ;
        }
	    //http://it.iusb.edu/miuxml/mcpu2.php

        StcModel model = new StcModel();
		parse(campus, convert, url, model);

		// Special case for IUPUI Libraries
		if (Campus.IN.name().equals(campus) && "true".equals(ServiceLocator.getCacheService().findConfigParamValueByName("STC_IUPUI_LIBRARY_ACTIVE"))) {
		    parse(campus, convert, "http://ulib.iupui.edu/utility/seats.php?show=locations&type=data", model);		
		}
		
		return model;
	}
	*/
    private List<ComputerLab> parse(String campus, boolean convert, String url) {
    	List<ComputerLab> labs = new ArrayList<ComputerLab>();
        try {
			Document doc = retrieveDocumentFromUrl(url, 5000, 5000);
			Element root = doc.getRootElement();
			List items = root.getChildren("seat");
			for (Iterator iterator = items.iterator(); iterator.hasNext();) {
				Element item = (Element) iterator.next();
				String lab = item.getAttributeValue("lab");
				String availability = item.getAttributeValue("availability");
				String name = "";
				if (convert) {
				    name = CodeToBuildingConverter.convertToBuilding(lab);
				} else {
				    name = item.getAttributeValue("building");
				}
				String campusXml = item.getAttributeValue("campus");
				if (campus.equalsIgnoreCase(campusXml)) {
				    ComputerLab seat = new ComputerLab(campusXml, name != null ? name + " (" + lab + ")" : lab, availability);
					String buildingCode = item.getAttributeValue("building-code");
					seat.setBuildingCode(buildingCode);
					// Try to figure out the building if the building code is unavailable.
					if (buildingCode == null || buildingCode.length() < 1) {
						String buildingShortCode = this.convertBuildingShortCodeFromLab(lab);
					    seat.setBuildingShortCode(buildingShortCode);
					}
				    seat.setLabCode(lab);
				    
				    seat.setLabOnly(lab);
				    seat.setBuildingNameOnly(name);
				    
				    String floor = item.getAttributeValue("floor");
				    if (floor != null && floor.length() > 0) {
				    	seat.setFloor(floor);
				    }
				    
				    List<LabAvailability> labAvList = new ArrayList<LabAvailability>();
				    
				    String overallAv = item.getAttributeValue("availability");
				    if (overallAv != null && overallAv.length() > 0) {
				    	LabAvailability labAv = new LabAvailability("overall", "Overall", overallAv, 1);
				    	labAvList.add(labAv);
				    }
				    
				    String winAv = item.getAttributeValue("windows-availability");
				    if (winAv != null && winAv.length() > 0) {
				    	LabAvailability labAv = new LabAvailability("windows", "Windows", winAv, 1);
				    	labAvList.add(labAv);
				    }
				    String macAv = item.getAttributeValue("mac-availability");
				    if (macAv != null && macAv.length() > 0) {
				    	LabAvailability labAv = new LabAvailability("mac", "Apple", macAv, 2);
				    	labAvList.add(labAv);
				    }
				    String dualAv = item.getAttributeValue("dual-availability");
				    if (dualAv != null && dualAv.length() > 0) {
				    	LabAvailability labAv = new LabAvailability("dual", "Dual-boot", dualAv, 3);
				    	labAvList.add(labAv);
				    }
				    String wheelchairAv = item.getAttributeValue("wheelchair-availability");
				    if (wheelchairAv != null && wheelchairAv.length() > 0) {
				    	LabAvailability labAv = new LabAvailability("wheelchair", "Wheelchair accessible", wheelchairAv, 4);
				    	labAvList.add(labAv);
				    }
				    String softwareAv = item.getAttributeValue("software-availability");
				    if (softwareAv != null && softwareAv.length() > 0) {
				    	LabAvailability labAv = new LabAvailability("software", "Software install", softwareAv, 5);
				    	labAvList.add(labAv);
				    }
				    String richAv = item.getAttributeValue("rich-availability");
				    if (richAv != null && richAv.length() > 0) {
				    	LabAvailability labAv = new LabAvailability("rich", "Rich media", richAv, 6);
				    	labAvList.add(labAv);
				    }
				    seat.setAvList(labAvList);
				    
				    labs.add(seat);
				}
			}
		} catch (JDOMException e) {
//			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
//			LOG.error(e.getMessage(), e);
		}
		return labs;
    }

	private String convertBuildingShortCodeFromLab(String lab) {
		/* Note: This won't work for buildings that contain numbers in their short codes, for example: I2
		 * If the string contains numbers, remove characters from the end until there are only letters remaining.
		 */
		if (lab != null) {
			String shortCode = new String(lab);
			if (containsDigit(lab)) {
				for (int i = lab.length() - 1; i >= 0; i--) {
					if (containsDigit(shortCode)) {
						shortCode = lab.substring(0, i);
					} else {
						break;
					}
				}
			}
			return shortCode;
		}
		return "";
	}
	

	private boolean containsDigit(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}
	/*
	public static void main(String[] args) {
	    SeatParser parser = new SeatParser();
	    StcModel model = parser.parseSeats("BL");
	    for (Seat seat : model.getSeats()) {
	        System.out.println("[" + seat.getLab() + ", " + seat.getAvailability() + ", " + seat.getCampus() + "]");
        }
    }
    */
	
}
