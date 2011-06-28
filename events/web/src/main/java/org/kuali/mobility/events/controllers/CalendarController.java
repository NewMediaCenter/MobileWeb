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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import edu.iu.es.espd.ccl.oauth.CalendarEventOAuthService;
import edu.iu.es.espd.ccl.oauth.CalendarViewEvent;
import edu.iu.es.espd.ccl.oauth.EditEvent;
import edu.iu.es.espd.ccl.oauth.ListViewEvents;
import edu.iu.es.espd.ccl.oauth.MeetingInvite;
import edu.iu.es.espd.ccl.oauth.MeetingStatusChange;
import edu.iu.es.espd.ccl.oauth.MonthViewEvents;
import edu.iu.es.espd.ccl.oauth.PageLevelException;
import edu.iu.es.espd.ccl.oauth.ViewDetailedEvent;
import edu.iu.uis.cas.filter.CASFilter;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

	@Autowired
	private CalendarEventOAuthService calendarEventOAuthService;

	public void setCalendarEventOAuthService(CalendarEventOAuthService calendarEventOAuthService) {
		this.calendarEventOAuthService = calendarEventOAuthService;
	}

	@RequestMapping(value = "/month", method = RequestMethod.GET)
	public String month(HttpServletRequest request, Model uiModel, @RequestParam(required = false) String filter, @RequestParam(required = false) String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat my = new SimpleDateFormat("yyyyMM");
		Calendar selectedDate = Calendar.getInstance();
		try {
			if (date != null) {
				selectedDate.setTime(my.parse(date));
			}
		} catch (ParseException e) {

		}
		try {
			MonthViewEvents monthEvents = calendarEventOAuthService.retrieveMonthEvents(CASFilter.getRemoteUser(request), selectedDate.getTime(), filter);
			uiModel.addAttribute("viewData", monthEvents.getViewData());
			uiModel.addAttribute("appData", monthEvents.getAppData());

			int days = selectedDate.getActualMaximum(Calendar.DATE);

			Calendar startDate = (Calendar) selectedDate.clone();
			startDate.set(Calendar.DATE, selectedDate.getActualMinimum(Calendar.DATE));
			days += startDate.get(Calendar.DAY_OF_WEEK) - 1;

			Calendar endDate = (Calendar) selectedDate.clone();
			endDate.set(Calendar.DATE, selectedDate.getActualMaximum(Calendar.DATE));
			days += 7 - endDate.get(Calendar.DAY_OF_WEEK);

			startDate.set(Calendar.DAY_OF_WEEK, 1);
			Map<String, MobileDayOfMonth> daysInMonth = new LinkedHashMap<String, MobileDayOfMonth>();

			uiModel.addAttribute("selectedDate", sdf.format(selectedDate.getTime()));
			for (int i = 0; i < days; i++) {
				MobileDayOfMonth mobileDayOfMonth = new MobileDayOfMonth(startDate.get(Calendar.DATE));
				mobileDayOfMonth.setCurrentMonth(startDate.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH));
				if (!mobileDayOfMonth.isCurrentMonth()) {
					mobileDayOfMonth.setBeforeCurrentMonth(startDate.get(Calendar.MONTH) < selectedDate.get(Calendar.MONTH));
				}
				mobileDayOfMonth.setMonthYear(my.format(startDate.getTime()));
				mobileDayOfMonth.setDayOfWeek(startDate.get(Calendar.DAY_OF_WEEK));
				daysInMonth.put(sdf.format(startDate.getTime()), mobileDayOfMonth);
				startDate.add(Calendar.DATE, 1);
			}

			for (Iterator iterator = monthEvents.getEvents().entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<String, List<CalendarViewEvent>> entry = (Map.Entry<String, List<CalendarViewEvent>>) iterator.next();
				MobileDayOfMonth dayInMonth = daysInMonth.get(entry.getKey());
				dayInMonth.setHasEvents(true);
				dayInMonth.setEvents(entry.getValue());
			}
			uiModel.addAttribute("events", daysInMonth);

			Calendar previousCalendar = (Calendar) selectedDate.clone();
			previousCalendar.set(Calendar.DATE, 1);
			previousCalendar.getTime();
			previousCalendar.add(Calendar.MONTH, -1);

			Calendar nextCalendar = (Calendar) selectedDate.clone();
			nextCalendar.set(Calendar.DATE, 1);
			nextCalendar.getTime();
			nextCalendar.add(Calendar.MONTH, 1);

			uiModel.addAttribute("previousMonth", my.format(previousCalendar.getTime()));
			uiModel.addAttribute("nextMonth", my.format(nextCalendar.getTime()));
			uiModel.addAttribute("monthYear", my.format(selectedDate.getTime()));
		} catch (PageLevelException pageLevelException) {
			uiModel.addAttribute("message", pageLevelException.getMessage());
			return "calendar/message";
		}
		return "calendar/month";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model uiModel, @RequestParam(required = false) String filter, @RequestParam(required = false) String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar selectedDate = Calendar.getInstance();
		try {
			if (date != null) {
				selectedDate.setTime(sdf.parse(date));
			}
		} catch (ParseException e) {

		}
		try {
			Calendar endDate = (Calendar) selectedDate.clone();
			Calendar previousDate = (Calendar) selectedDate.clone();
			ListViewEvents listViewEvents = calendarEventOAuthService.retrieveViewEventsList(CASFilter.getRemoteUser(request), selectedDate.getTime(), filter);

			Calendar currentEndDate = (Calendar) selectedDate.clone();
			currentEndDate.add(Calendar.DATE, listViewEvents.getAppData().getListViewFutureDaysLimit());
			endDate.add(Calendar.DATE, listViewEvents.getAppData().getListViewFutureDaysLimit() * 2);
			previousDate.add(Calendar.DATE, -listViewEvents.getAppData().getListViewFutureDaysLimit());

			SimpleDateFormat my = new SimpleDateFormat("yyyyMM");

			uiModel.addAttribute("selectedDate", sdf.format(selectedDate.getTime()));
			uiModel.addAttribute("monthSelectedDate", my.format(selectedDate.getTime()));
			uiModel.addAttribute("beginDate", sdf.format(selectedDate.getTime()));
			uiModel.addAttribute("endDate", sdf.format(endDate.getTime()));
			uiModel.addAttribute("currentEndDate", sdf.format(currentEndDate.getTime()));
			uiModel.addAttribute("previousDate", sdf.format(previousDate.getTime()));
			uiModel.addAttribute("days", listViewEvents.getAppData().getListViewFutureDaysLimit());
			uiModel.addAttribute("viewData", listViewEvents.getViewData());
			uiModel.addAttribute("appData", listViewEvents.getAppData());
			uiModel.addAttribute("events", listViewEvents.getEvents());
		} catch (PageLevelException pageLevelException) {
			uiModel.addAttribute("message", pageLevelException.getMessage());
			return "calendar/message";
		}
		return "calendar/list";
	}

	@RequestMapping(value = "/listEvents", method = RequestMethod.GET)
	public String listEvents(HttpServletRequest request, Model uiModel, @RequestParam(required = false) String filter, @RequestParam(required = true) String date, @RequestParam(required = true) String beginDate, @RequestParam(required = true) String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar selectedDate = Calendar.getInstance();
		Calendar beginCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		try {
			if (date != null) {
				selectedDate.setTime(sdf.parse(date));
			}
			if (beginDate != null) {
				beginCalendar.setTime(sdf.parse(beginDate));
			}
			if (endDate != null) {
				endCalendar.setTime(sdf.parse(endDate));
			}
		} catch (ParseException e) {
			return list(request, uiModel, filter, date);
		}
		try {
			ListViewEvents listViewEvents = calendarEventOAuthService.retrieveViewEventsList(CASFilter.getRemoteUser(request), selectedDate.getTime(), beginCalendar.getTime(), endCalendar.getTime(), filter);

			Calendar currentEndDate = (Calendar) endCalendar.clone();
			Calendar previousDate = (Calendar) beginCalendar.clone();
			previousDate.add(Calendar.DATE, -listViewEvents.getAppData().getListViewFutureDaysLimit());
			endCalendar.add(Calendar.DATE, listViewEvents.getAppData().getListViewFutureDaysLimit());

			SimpleDateFormat my = new SimpleDateFormat("yyyyMM");
			uiModel.addAttribute("selectedDate", sdf.format(selectedDate.getTime()));
			uiModel.addAttribute("monthSelectedDate", my.format(selectedDate.getTime()));
			uiModel.addAttribute("beginDate", sdf.format(beginCalendar.getTime()));
			uiModel.addAttribute("endDate", sdf.format(endCalendar.getTime()));
			uiModel.addAttribute("currentEndDate", sdf.format(currentEndDate.getTime()));
			uiModel.addAttribute("previousDate", sdf.format(previousDate.getTime()));
			uiModel.addAttribute("days", listViewEvents.getAppData().getListViewFutureDaysLimit());
			uiModel.addAttribute("viewData", listViewEvents.getViewData());
			uiModel.addAttribute("appData", listViewEvents.getAppData());
			uiModel.addAttribute("events", listViewEvents.getEvents());
		} catch (PageLevelException pageLevelException) {
			uiModel.addAttribute("message", pageLevelException.getMessage());
			return "calendar/message";
		}
		return "calendar/list";
	}

	@RequestMapping(value = "/event", method = RequestMethod.GET)
	public String event(HttpServletRequest request, Model uiModel, @RequestParam(required = true) Long eventId, @RequestParam(required = false) String date, @RequestParam(required = false) Long occurrenceId) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
		Date selectedDate = null;
		try {
			if (date != null) {
				selectedDate = sdf.parse(date);
				if (occurrenceId != null) {
					uiModel.addAttribute("occurrenceDate", date);
					selectedDate = null;
				}
			}
		} catch (ParseException e) {

		}
		try {
			ViewDetailedEvent event = calendarEventOAuthService.retrieveViewEventDetails(CASFilter.getRemoteUser(request), eventId, selectedDate);
			// uiModel.addAttribute("selectedDate",
			// sdf.format(selectedDate.getTime()));
			if (occurrenceId != null) {
				uiModel.addAttribute("occurrenceId", occurrenceId);
			}
			uiModel.addAttribute("event", event);
		} catch (PageLevelException pageLevelException) {
			uiModel.addAttribute("message", pageLevelException.getMessage());
			return "calendar/message";
		}
		return "calendar/eventView";
	}

	@RequestMapping(value = "/createEvent", method = RequestMethod.GET)
	public String createEvent(HttpServletRequest request, Model uiModel, @RequestParam(required = false) String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
		Date selectedDate = null;
		try {
			if (date != null) {
				selectedDate = sdf.parse(date);
			}
		} catch (ParseException e) {

		}
		try {
			EditEvent event = calendarEventOAuthService.retrieveNewEvent(CASFilter.getRemoteUser(request), selectedDate);
			uiModel.addAttribute("event", event);
		} catch (PageLevelException pageLevelException) {
			uiModel.addAttribute("message", pageLevelException.getMessage());
			return "calendar/message";
		}
		return "calendar/editEvent";
	}

	@RequestMapping(value = "/editEvent", method = RequestMethod.GET)
	public String editEvent(HttpServletRequest request, Model uiModel, @RequestParam(required = true) Long eventId, @RequestParam(required = false) Long seriesId, @RequestParam(required = false) String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
		Date selectedDate = null;
		try {
			if (date != null) {
				selectedDate = sdf.parse(date);
			}
		} catch (ParseException e) {

		}
		try {
			EditEvent event = calendarEventOAuthService.retrieveEditEvent(CASFilter.getRemoteUser(request), eventId, seriesId, selectedDate);
			uiModel.addAttribute("event", event);
			if (seriesId == null) {
				uiModel.addAttribute("seriesId", event.getSeriesId());
			} else {
				uiModel.addAttribute("seriesId", seriesId);
			}
		} catch (PageLevelException pageLevelException) {
			uiModel.addAttribute("message", pageLevelException.getMessage());
			return "calendar/message";
		}
		return "calendar/editEvent";
	}

	@RequestMapping(value = "/saveEvent", method = RequestMethod.POST)
	public String saveEvent(HttpServletRequest request, @ModelAttribute("event") EditEvent event, BindingResult result, SessionStatus status, Model uiModel) {
		EditEvent eventReturned = null;
		try {
			eventReturned = calendarEventOAuthService.saveEvent(CASFilter.getRemoteUser(request), event, event.getEventId());
			if (eventReturned.getResponseCode() == HttpStatus.UNPROCESSABLE_ENTITY.value()) {
				Errors errors = ((Errors) result);
				for (Iterator iterator = eventReturned.getErrors().entrySet().iterator(); iterator.hasNext();) {
					Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) iterator.next();
					for (String error : entry.getValue()) {
						errors.rejectValue(entry.getKey(), "", error);
					}
				}
				return "calendar/editEvent";
			}
		} catch (PageLevelException pageLevelException) {
			uiModel.addAttribute("message", pageLevelException.getMessage());
			return "calendar/message";
		}
		return "redirect:/calendar/month";
	}

	@RequestMapping(value = "/invite", method = RequestMethod.GET)
	public String invite(HttpServletRequest request, Model uiModel, @RequestParam(required = true) Long eventId, @RequestParam(required = false) Long seriesId, @RequestParam(required = false) String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
		Date selectedDate = null;
		try {
			if (date != null) {
				selectedDate = sdf.parse(date);
			}
		} catch (ParseException e) {

		}
		try {
			MeetingInvite invite = calendarEventOAuthService.retrieveMeeting(CASFilter.getRemoteUser(request), eventId, seriesId, selectedDate);
			uiModel.addAttribute("invite", invite);
		} catch (PageLevelException pageLevelException) {
			uiModel.addAttribute("message", pageLevelException.getMessage());
			return "calendar/message";
		}
		return "calendar/invite";
	}

	@RequestMapping(value = "/meetingAction", method = RequestMethod.GET)
	public String meetingAction(HttpServletRequest request, Model uiModel, @RequestParam(required = true) Long eventId, @RequestParam(required = true) String type, @RequestParam(required = false) Long seriesId, @RequestParam(required = false) String date) {
		try {
			MeetingStatusChange meetingStatusChange = new MeetingStatusChange();
			meetingStatusChange.setEventId(eventId);
			meetingStatusChange.setStatus(type);
			calendarEventOAuthService.updateMeetingStatus(CASFilter.getRemoteUser(request), meetingStatusChange);
		} catch (PageLevelException pageLevelException) {
			uiModel.addAttribute("message", pageLevelException.getMessage());
			return "calendar/message";
		}

		if ("D".equals(type)) {
			return "redirect:/calendar/month";
		} else if (seriesId != null && date != null) {
			return "redirect:/calendar/invite?eventId=" + eventId + "&seriesId=" + seriesId + "&date=" + date;
		}
		return "redirect:/calendar/invite?eventId=" + eventId;
	}

	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public String options(HttpServletRequest request, Model uiModel) {
		return "calendar/options";
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public String refresh(HttpServletRequest request, Model uiModel) {
		try {
			calendarEventOAuthService.reloadPersonalCache(CASFilter.getRemoteUser(request));
		} catch (PageLevelException pageLevelException) {
			uiModel.addAttribute("message", pageLevelException.getMessage());
			return "calendar/message";
		}
		uiModel.addAttribute("message", "Your calendar has been refreshed.");
		return "calendar/message";
	}

}
