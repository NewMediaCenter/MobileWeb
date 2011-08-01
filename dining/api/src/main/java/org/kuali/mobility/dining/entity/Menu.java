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

package org.kuali.mobility.dining.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Menu implements Serializable {

	private static final long serialVersionUID = -2096908398187406294L;

	private Date date;
	
	private List<FoodItem> items;
	
	public Menu() {
		this.items = new ArrayList<FoodItem>();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<FoodItem> getItems() {
		return items;
	}

	public void setItems(List<FoodItem> items) {
		this.items = items;
	}
	
	public String getDateFormatted() {
		if (this.getDate() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
			String date = sdf.format(new Date(this.getDate().getTime()));
			return date;			
		} 
		return "";
	}
	
}
