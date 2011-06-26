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

package org.kuali.mobility.events.controllers;

import java.util.ArrayList;
import java.util.List;

import edu.iu.es.espd.ccl.oauth.CalendarViewEvent;

public class MobileDayOfMonth {

	private int day;

	private boolean hasEvents;

	private boolean currentMonth;

	private String monthYear;

	private int dayOfWeek;

	private boolean beforeCurrentMonth;

	private List<CalendarViewEvent> events;

	public MobileDayOfMonth(int day) {
		this.day = day;
		events = new ArrayList<CalendarViewEvent>();
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public List<CalendarViewEvent> getEvents() {
		return events;
	}

	public void setEvents(List<CalendarViewEvent> events) {
		this.events = events;
	}

	public boolean isHasEvents() {
		return hasEvents;
	}

	public void setHasEvents(boolean hasEvents) {
		this.hasEvents = hasEvents;
	}

	public boolean isCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(boolean currentMonth) {
		this.currentMonth = currentMonth;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public boolean isBeforeCurrentMonth() {
		return beforeCurrentMonth;
	}

	public void setBeforeCurrentMonth(boolean beforeCurrentMonth) {
		this.beforeCurrentMonth = beforeCurrentMonth;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}
	
}
