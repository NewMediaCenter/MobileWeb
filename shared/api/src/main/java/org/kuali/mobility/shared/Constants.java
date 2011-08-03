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

package org.kuali.mobility.shared;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class Constants {

	public static final String KME_USER_KEY = "kme.user";
	public static final String KME_BACKDOOR_USER_KEY = "kme.backdoor.user";
	
	public static final String URL_MIME_TYPE = "text/url";
	
	public static final String SAKAI_FOLDER_EXTENSION = "fldr";
	public static final String SAKAI_URL_EXTENSION = "url";
	
	public static final Map<String, String> CAMPUS_NAMES = new LinkedHashMap<String, String>();
	
	static {
		CAMPUS_NAMES.put("Any", "Any Campus");
		CAMPUS_NAMES.put("BL", "IU Bloomington");
		CAMPUS_NAMES.put("IN", "IUPUI");
		CAMPUS_NAMES.put("CO", "IUPUC");
		CAMPUS_NAMES.put("EA", "IU East");
		CAMPUS_NAMES.put("FW", "IPFW");
		CAMPUS_NAMES.put("KO", "IU Kokomo");
		CAMPUS_NAMES.put("NW", "IU Northwest");
		CAMPUS_NAMES.put("SB", "IU South Bend");
		CAMPUS_NAMES.put("SE", "IU Southeast");
	}
	
	public enum DateFormat {
		
		queryStringDateFormat("yyyyMMdd"),
		displayDateFormat("MMMM dd, yyyy"),
		buttonDateFormat("MMM dd");
		
		private String format;
		
		DateFormat(String format) {
			this.format = format;
		}
		
		public SimpleDateFormat getFormat() {
			return new SimpleDateFormat(format);
		}
		
	}
	
	public enum FileType {
		
		GENERIC, IMAGE, VIDEO, TEXT, PRESENTATION, SPREADSHEET, PDF, AUDIO, LINK, FOLDER;
	
	}
		
	public enum FileTypes {
		
		txt(FileType.TEXT),
		rtf(FileType.TEXT),
		doc(FileType.TEXT),
		docx(FileType.TEXT),
		odt(FileType.TEXT),
		wpd(FileType.TEXT),
		jpg(FileType.IMAGE),
		jpeg(FileType.IMAGE),
		png(FileType.IMAGE),
		gif(FileType.IMAGE),
		bmp(FileType.IMAGE),
		psd(FileType.IMAGE),
		tiff(FileType.IMAGE),
		wav(FileType.AUDIO),
		wma(FileType.AUDIO),
		mpa(FileType.AUDIO),
		mp3(FileType.AUDIO),
		mid(FileType.AUDIO),
		midi(FileType.AUDIO),
		m4a(FileType.AUDIO),
		m3u(FileType.AUDIO),
		aif(FileType.AUDIO),
		avi(FileType.VIDEO),
		flv(FileType.VIDEO),
		mov(FileType.VIDEO),
		mp4(FileType.VIDEO),
		mpg(FileType.VIDEO),
		swf(FileType.VIDEO),
		vob(FileType.VIDEO),
		wmv(FileType.VIDEO),
		wks(FileType.SPREADSHEET),
		xls(FileType.SPREADSHEET),
		xlsx(FileType.SPREADSHEET),
		ods(FileType.SPREADSHEET),
		ppt(FileType.PRESENTATION),
		pptx(FileType.PRESENTATION),
		odp(FileType.PRESENTATION),
		pdf(FileType.PDF),
		fldr(FileType.FOLDER),
		url(FileType.LINK);
		
		private FileType fileType;
		
		FileTypes(FileType fileType) {
			this.fileType = fileType;
		}
		
		public FileType getFileType() {
			return fileType;
		}
		
	}
	
}
