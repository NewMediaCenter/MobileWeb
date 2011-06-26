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

import java.util.HashMap;
import java.util.Map;

public class CodeToBuildingConverter {

    private static Map<String, String> singleDigitCodesToBuildings = new HashMap<String, String>();
    private static Map<String, String> twoDigitCodesToBuildings = new HashMap<String, String>();
    
    static {
        twoDigitCodesToBuildings.put("AB", "Cyclotron Facility");
        twoDigitCodesToBuildings.put("AD", "Auditorium");
        twoDigitCodesToBuildings.put("AS", "Assembly Hall");
        twoDigitCodesToBuildings.put("BH", "Ballantine Hall");
        twoDigitCodesToBuildings.put("BL", "Barnes Lounge (Ashton Center)");
        twoDigitCodesToBuildings.put("BQ", "Briscoe Quad");
        twoDigitCodesToBuildings.put("BR", "Brown Hall (Collins Center)");
        twoDigitCodesToBuildings.put("BU", "Business, Kelley School of");
        twoDigitCodesToBuildings.put("CA", "Cravens Hall (Collins Center)");
        twoDigitCodesToBuildings.put("CB", "809 E. 8th Street");
        twoDigitCodesToBuildings.put("CG", "Godfrey Grad & Exec Ed Ctr");
        twoDigitCodesToBuildings.put("CH", "Chemistry Building");
        twoDigitCodesToBuildings.put("CR", "Creative Arts");
        twoDigitCodesToBuildings.put("CS", "Arts Annex");
        twoDigitCodesToBuildings.put("CX", "Chemistry Addition");
        twoDigitCodesToBuildings.put("C2", "Classroom-Office Building");
        twoDigitCodesToBuildings.put("C3", "Carmichael Center");
        twoDigitCodesToBuildings.put("ED", "Wendell W. Wright Education Building");
        twoDigitCodesToBuildings.put("EG", "Eigenmann Hall");
        twoDigitCodesToBuildings.put("EO", "Edmondson Hall (Collins Center)");
        twoDigitCodesToBuildings.put("EP", "Ernie Pyle Hall");
        twoDigitCodesToBuildings.put("ER", "Smith Research Center (2805 E. 10th)");
        twoDigitCodesToBuildings.put("FA", "Fine Arts Building");
        twoDigitCodesToBuildings.put("FQ", "Foster Quad (Shea Hall, Martin Hall)");
        twoDigitCodesToBuildings.put("FR", "Forest Quad");
        twoDigitCodesToBuildings.put("FX", "McCalla School (Fine Arts Annex)");
        twoDigitCodesToBuildings.put("FY", "501 N. Park");
        twoDigitCodesToBuildings.put("GB", "Goodbody Hall");
        twoDigitCodesToBuildings.put("GD", "Ashton Hall");
        twoDigitCodesToBuildings.put("GG", "Griggs Lounge (Ashton)");
        twoDigitCodesToBuildings.put("GH", "Green Hall (Collins Center)");
        twoDigitCodesToBuildings.put("GL", "Glenn A. Black Laboratory (9th and Fess)");
        twoDigitCodesToBuildings.put("GR", "Gresham Hall (Foster Quad)");
        twoDigitCodesToBuildings.put("GY", "Geology Building");
        twoDigitCodesToBuildings.put("HH", "Hershey Hall ( Ashton Center)");
        twoDigitCodesToBuildings.put("HL", "801 N. Jordan Avenue");
        twoDigitCodesToBuildings.put("HP", "HPER Building");
        twoDigitCodesToBuildings.put("HQ", "Harper Hall (Foster Quad)");
        twoDigitCodesToBuildings.put("HU", "Hutton Honors College (811 E. 7th Street)");
        twoDigitCodesToBuildings.put("IS", "Jenkinson Hall (Foster Quad)");
        twoDigitCodesToBuildings.put("JH", "Jordan Hall");
        twoDigitCodesToBuildings.put("JO", "Johnston Hall (Ashton Center)");
        twoDigitCodesToBuildings.put("KH", "Kirkwood Hall");
        twoDigitCodesToBuildings.put("LH", "Lindley Hall");
        twoDigitCodesToBuildings.put("LI", "Library");
        twoDigitCodesToBuildings.put("LL", "Lilly Library");
        twoDigitCodesToBuildings.put("LW", "Law Building");
        twoDigitCodesToBuildings.put("MA", "Music Annex");
        twoDigitCodesToBuildings.put("MC", "Musical Arts Center");
        twoDigitCodesToBuildings.put("ME", "Collins Living Learning Center (10th and Woodlawn)");
        twoDigitCodesToBuildings.put("MF", "Moffatt Hall (Ashton Center)");
        twoDigitCodesToBuildings.put("MG", "Wildermuth Intramural Center");
        twoDigitCodesToBuildings.put("MH", "Martin Hall (Foster Quad)");
        twoDigitCodesToBuildings.put("MM", "Memorial Hall");
        twoDigitCodesToBuildings.put("MN", "McNutt Quad");
        twoDigitCodesToBuildings.put("MO", "Morrison Hall");
        twoDigitCodesToBuildings.put("MR", "Morgan Hall");
        twoDigitCodesToBuildings.put("MS", "Memorial Stadium");
        twoDigitCodesToBuildings.put("MU", "Merrill Hall");
        twoDigitCodesToBuildings.put("MX", "Maxwell Hall");
        twoDigitCodesToBuildings.put("MY", "Myers Hall");
        twoDigitCodesToBuildings.put("MZ", "Indiana Institute on Disability and Community (2853 E. 10th)");
        twoDigitCodesToBuildings.put("M2", "Mathers Museum");
        twoDigitCodesToBuildings.put("NF", "Fieldhouse");
        twoDigitCodesToBuildings.put("OA", "Admissions (300 N. Jordan)");
        twoDigitCodesToBuildings.put("OP", "Optometry Building");
        twoDigitCodesToBuildings.put("PC", "Career Develpment Center (625 N. Jordan)");
        twoDigitCodesToBuildings.put("PO", "Poplars (400 E. 7th)");
        twoDigitCodesToBuildings.put("PV", "SPEA");
        twoDigitCodesToBuildings.put("PY", "Psychology");
        twoDigitCodesToBuildings.put("RB", "Student Recreational Sports Center");
        twoDigitCodesToBuildings.put("RE", "Read Center");
        twoDigitCodesToBuildings.put("RH", "Rawles Hall");
        twoDigitCodesToBuildings.put("RU", "Magee Hall (Foster Quad)");
        twoDigitCodesToBuildings.put("SB", "Student Building");
        twoDigitCodesToBuildings.put("SE", "Swain East");
        twoDigitCodesToBuildings.put("SG", "Speech and Hearing Clinic");
        twoDigitCodesToBuildings.put("SH", "Shea Hall (Foster Quad)");
        twoDigitCodesToBuildings.put("SI", "Simon Hall");
        twoDigitCodesToBuildings.put("SK", "Student Academic Center (316 N. Jordan)");
        twoDigitCodesToBuildings.put("SP", "IU Research Park");
        twoDigitCodesToBuildings.put("SW", "Swain West");
        twoDigitCodesToBuildings.put("SY", "Sycamore Hall");
        twoDigitCodesToBuildings.put("S7", "Inst. for Social Research (1022 E. 3rd.)");
        twoDigitCodesToBuildings.put("S8", "Smith Hall (Collins Center)");
        twoDigitCodesToBuildings.put("TA", "Theatre/Drama Studio (306 N. Union)");
        twoDigitCodesToBuildings.put("TE", "Teter Quad");
        twoDigitCodesToBuildings.put("TH", "Lee Norvell Theatre and Drama Center/Marcellus Neal and Frances Marshall Black Culture Center");
        twoDigitCodesToBuildings.put("TP", "Tennis Center");
        twoDigitCodesToBuildings.put("TV", "Radio and TV Building");
        twoDigitCodesToBuildings.put("UB", "Union Building");
        twoDigitCodesToBuildings.put("VO", "Vos Hall (Ashton Center)");
        twoDigitCodesToBuildings.put("WA", "Weatherly Hall (Ashton Center)");
        twoDigitCodesToBuildings.put("WH", "Woodburn Hall");
        twoDigitCodesToBuildings.put("WI", "Willkie Quad");
        twoDigitCodesToBuildings.put("WT", "Wright Quad");
        twoDigitCodesToBuildings.put("WY", "Wylie Hall");
        twoDigitCodesToBuildings.put("X4", "Auxiliary Library Facility (ALF)");
        twoDigitCodesToBuildings.put("WS", "Willkie South");
        
        twoDigitCodesToBuildings.put("IC", "Information Commons");
        twoDigitCodesToBuildings.put("CV", "Campus View");
        twoDigitCodesToBuildings.put("EV", "Evermann");
        twoDigitCodesToBuildings.put("HC", "Health Center");
        twoDigitCodesToBuildings.put("TT", "Tulip Tree");
        twoDigitCodesToBuildings.put("UW", "University West");
        twoDigitCodesToBuildings.put("WN", "Willkie North");
        
        singleDigitCodesToBuildings.put("I", "Informatics");
        singleDigitCodesToBuildings.put("M", "Music Library & Recital Center");
        singleDigitCodesToBuildings.put("M", "Music Library and Recital Center, Simon");
        singleDigitCodesToBuildings.put("P", "Business/SPEA");
        singleDigitCodesToBuildings.put("S", "Collins Smith");

    };
    
    public static String convertToBuilding(String lab) {
    	String code = lab.substring(0, 2);
    	String name = isInteger(code.substring(1,2)) ? singleDigitCodesToBuildings.get(code.substring(0,1)) : twoDigitCodesToBuildings.get(code);
    	if (name == null) {
    		name = lab;
    	}
        return name;
    }

	public static boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
