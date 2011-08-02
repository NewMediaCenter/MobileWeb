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

package org.kuali.mobility.events.service;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.events.entity.Category;
import org.kuali.mobility.events.entity.Day;
import org.kuali.mobility.events.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import edu.iu.m.news.entity.LinkFeed;
import edu.iu.m.news.entity.MaintRss;
import edu.iu.m.news.entity.Rss;
import edu.iu.m.news.entity.RssItem;
import edu.iu.m.news.service.DynamicRssCacheService;
import edu.iu.m.news.service.RssCacheService;
import edu.iu.m.news.service.RssService;

@Service
public class EventsServiceImpl implements EventsService {
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(EventsServiceImpl.class);
	@Autowired
	private RssService rssService;

	@Autowired
	private RssCacheService rssCacheService;

	@Autowired
	private DynamicRssCacheService dynamicRssCacheService;

	@Autowired
	private ConfigParamService configParamService;

	private static final String RSS_TYPE_EVENTS_ONESTART = "EVENTS-ONESTART";
	private static final String RSS_TYPE_EVENTS_CALENDAR = "EVENTS-IUEVENTS";
	private static final String RSS_TYPE_EVENTS_IUPUI = "EVENTS-IUPUI";
	private static final String RSS_TYPE_EVENTS_OTHER = "EVENTS-OTHER";
	private static final String RSS_TYPE_EVENTS_GENERAL = "EVENTS-GENERAL";
	private static final String DATA_XML_REPLACEMENT_TOKENS = "Data.XML.Replacement.Tokens";
	private static final String EVENTS_CCL_PARSER_HEADER = "Events.CCL.Parser.Header";
	private static final String EVENTS_CCL_PARSER_FOOTER = "Events.CCL.Parser.Footer";
	private static final String EVENTS_CCL_PARSER_DELIMITER = "Events.CCL.Parser.Delimiter";
	private static final String EVENTS_CCL_PARSER_LINEFORMAT = "Events.CCL.Parser.LineFormat";
	private static final String EVENTS_URL_MATCHER_REGEX = "Events.Url.Matcher.Regex";
	private static final String EVENTS_URL_MATCHER_FORMAT = "Events.Url.Matcher.Format";
	private static final String EVENTS_DEFAULT_PARSER_HEADER = "Events.Default.Parser.Header";
	private static final String EVENTS_DEFAULT_PARSER_FOOTER = "Events.Default.Parser.Footer";

	public Event getEvent(String campus, String categoryId, String eventId) {
		Event event = new Event();

		if (eventId != null && !"".equals(eventId.trim())) {
			if (this.isIUPUIEventsCalendar(categoryId)) {
				String replacementTokens = configParamService.findValueByName(DATA_XML_REPLACEMENT_TOKENS);
				// IUPUIEventsParser parser = new
				// IUPUIEventsParser(replacementTokens);
				// EventsModel eventsModel = new EventsModel();
				// parser.parseEvent(eventsModel, link);
				// event.setTitle(eventsModel.getEventTitle());
				// event.setDescription(eventsModel.getEventHtml());
				// event = parser.parseEventById(eventId);
			} else {
				MaintRss maintRss = this.getRssCacheService().getMaintRssByCampusAndShortCode(campus, categoryId);
				String timezone = null;
				if (maintRss.getCampus() != null && (maintRss.getCampus().equals("NW") || maintRss.getCampus().equals("ZZ"))) {
					timezone = "CST";
				}
				Rss rss = null;
				try {
					rss = this.getRssCacheService().getRssByMaintRssId(maintRss.getRssId());
					if (rss != null && rss.getRssItems() != null) {
						Collections.sort(rss.getRssItems());
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
				List<RssItem> items = rss.getRssItems();
				String processedLink = eventId;
				// // Special Setup for IU Events Calendar (old system)
				// if (this.isIUEventsCalendar(maintRss)) {
				// processedLink = this.eventsCalendarUrlParser(eventId.trim());
				// }
				// Process RSS Items
				SimpleDateFormat sdfNormal = new SimpleDateFormat("M/d/yyyy h:mm a");
				SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE, MMM dd, yyyy");
				SimpleDateFormat sdfAllDay = new SimpleDateFormat("M/d/yyyy");
				SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm a");
				for (RssItem item : items) {
					if (item.getLink() != null) {
						String itemLink = item.getLinkUrlEncoded();
						// Special Setup for IU Events Calendar (old system)
						// if (this.isIUEventsCalendar(maintRss)) {
						// itemLink =
						// this.eventsCalendarUrlParser(item.getLink());
						// }
						// Normal handling
						if (itemLink.trim().equals(processedLink)) {
							event.setTitle(item.getTitle());
							String desc = item.getDescription();
							// if (this.isIUEventsCalendar(maintRss)) {
							// desc =
							// desc.replaceAll("&lt;br /&gt;&lt;delim/&gt;",
							// "||VALIDLINETAG||");
							// desc =
							// desc.replaceAll("\\|\\|VALIDLINETAG\\|\\|",
							// "<br/>");
							if (this.isOneStartCalendar(maintRss)) {
								parseEvent(sdfNormal, sdfAllDay, item, event);
							} else if (this.isNorthwestMarketingCalendar(categoryId)) {
								LinkFeed lf = this.getDynamicRssCacheService().getLinkFeed(eventId, rss);
								desc = lf.getBodyText();
							} else if (this.isEventsGeneral(maintRss)) {
								try {
									// desc =
									// this.transformEventDescription(desc);
								} catch (Exception e) {
									desc = StringEscapeUtils.escapeHtml(desc);
									desc = desc.replaceAll("\n", "<br/>");
								}
							} else {
								desc = StringEscapeUtils.escapeHtml(desc);
								desc = desc.replaceAll("\n", "<br/>");
							}
							/*
							 * if (event.getFields() != null &&
							 * event.getFields().size() < 1) { // Hack to add
							 * fields for iPhone Map<String, String> fields =
							 * new HashMap<String, String>();
							 * fields.put("Description", desc);
							 * fields.put("Url", event.getLink());
							 * event.setFields(fields); }
							 */

							// event.setPubDate(item.getPublishDate());
							if (item.getPublishDate() != null) {
								event.setStartDate(new Date(item.getPublishDate().getTime()));
							}
							event.setDisplayStartDate(sdfDate.format(event.getStartDate()));
							Calendar cal = null;
							if (timezone == null) {
								cal = Calendar.getInstance();
							} else {
								cal = new GregorianCalendar(TimeZone.getTimeZone(timezone));
							}
							cal.set(Calendar.HOUR_OF_DAY, 0);
							cal.set(Calendar.MINUTE, 0);
							cal.set(Calendar.SECOND, 0);
							cal.set(Calendar.MILLISECOND, 0);

							cal.setTimeInMillis(event.getStartDate().getTime());
							sdfTime.setTimeZone(cal.getTimeZone());
							if (event.isAllDay()) {
								event.setDisplayStartTime("All Day");
							} else {
								event.setDisplayStartTime(sdfTime.format(cal.getTime()));
								if (event.getEndDate() != null) {
									cal.setTimeInMillis(event.getEndDate().getTime());
									event.setDisplayEndDate(sdfDate.format(event.getEndDate()));
									event.setDisplayEndTime(sdfTime.format(cal.getTime()));
								}
							}
							break;
						}
					}
				}
			}
		}
		return event;

	}

	public Category getAllEvents(String campus, String categoryId) {
		boolean deepLinked = false;
		MaintRss maintRss = null;
		if (deepLinked) {
			maintRss = this.getRssCacheService().getMaintRssByCampusAndShortCode("ZZ", categoryId);
			// if ("nw_shuttle".equals(code)) {
			// page.setNavbarImage("bc-icons-iunshuttle");
			// }
		} else {
			maintRss = this.getRssCacheService().getMaintRssByCampusAndShortCode(campus, categoryId);
		}
		if (maintRss != null) {
			String timezone = null;
			if (maintRss.getCampus() != null && (maintRss.getCampus().equals("NW") || maintRss.getCampus().equals("ZZ"))) {
				timezone = "CST";
			}
			List<Event> events = this.handleEventsList(maintRss);
			List<Day> days = this.parseEventDaysFromEvents(events, timezone);

			Rss rss = getRssCacheService().getRssByMaintRssId(maintRss.getRssId());
			Category category = new Category();
			category.setCategoryId(maintRss.getShortCode());
			category.setDays(days);
			if (rss != null) {
				category.setTitle(rss.getTitle());
			}
			return category;
		}
		return null;
	}

	public List<Category> getCategoriesByCampus(String campus) {
		List<MaintRss> feeds = this.getRssCacheService().getAllMaintRssByCampus(campus);
		Collections.sort(feeds);
		List<Category> categories = new ArrayList<Category>();
		for (MaintRss maintRss : feeds) {
			if (maintRss.getType() != null && maintRss.getType().toUpperCase().startsWith("EVENTS")) {
				Category category = new Category();
				category.setCategoryId(maintRss.getShortCode());
				category.setTitle(maintRss.getDisplayName());
				categories.add(category);
			}
		}
		return categories;
	}

	private List<Day> parseEventDaysFromEvents(List<Event> events, String timezone) {
		Map<String, Day> eventDaysMap = new HashMap<String, Day>();
		List<Day> eventDays = new ArrayList<Day>();

		Calendar cal = null;
		if (timezone == null) {
			cal = Calendar.getInstance();
		} else {
			cal = new GregorianCalendar(TimeZone.getTimeZone(timezone));
		}

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm a");
		SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE, MMM dd, yyyy");

		sdfTime.setTimeZone(cal.getTimeZone());
		for (Event event : events) {
			try {
				cal.setTimeInMillis(event.getStartDate().getTime());
				event.setDisplayStartDate(sdfDate.format(event.getStartDate()));
				if (event.isAllDay()) {
					event.setDisplayStartTime("All Day");
				} else {
					event.setDisplayStartTime(sdfTime.format(cal.getTime()));
				}
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);

				Date processDate = cal.getTime();
				Day eventDay = eventDaysMap.get(processDate.toString());
				if (eventDay == null) {
					eventDay = new Day();
					eventDay.setDate(processDate);
					eventDaysMap.put(processDate.toString(), eventDay);
				}
				eventDay.getEvents().add(event);
			} catch (Exception e) {
				LOG.error("", e);
			}
		}

		eventDays.addAll(eventDaysMap.values());
		Collections.sort(eventDays);
		return eventDays;
	}

	private List<Event> handleEventsList(MaintRss maintRss) {
		List<Event> events = null;
		Rss rss = getRssCacheService().getRssByMaintRssId(maintRss.getRssId());
		if (rss != null) {
			List<RssItem> items = rss.getRssItems();
			Collections.sort(items);
			if (this.isOneStartCalendar(maintRss)) {
				events = parseEvents(rss);
			} else {
				// StandardEventsParser standardParser = new
				// StandardEventsParser();
				// events = standardParser.parseEvents(rss);
			}
		} else {
			Date date = new Date();
			// String replacementTokens =
			// getConfigParamService().findValueByName(DATA_XML_REPLACEMENT_TOKENS);
			// IUPUIEventsParser parser = new
			// IUPUIEventsParser(replacementTokens);
			// events = parser.parseEventsList(maintRss.getUrl(), date);
		}
		return events;
	}

	private List<Event> parseEvents(Rss rss) {
		List<Event> events = new ArrayList<Event>();
		SimpleDateFormat sdfNormal = new SimpleDateFormat("M/d/yyyy h:mm a");
		SimpleDateFormat sdfAllDay = new SimpleDateFormat("M/d/yyyy");
		for (RssItem item : rss.getRssItems()) {
			try {
				Event event = new Event();
				parseEvent(sdfNormal, sdfAllDay, item, event);
				events.add(event);
			} catch (Exception e) {
				LOG.error("Error converting event for Rss: " + rss.getRssId(), e);
			}
		}
		return events;
	}

	private void parseEvent(SimpleDateFormat sdfNormal, SimpleDateFormat sdfAllDay, RssItem item, Event event) {
		event.setTitle(item.getTitle());
		event.setLink(item.getLinkUrlEncoded());
		event.setEventId(item.getLinkUrlEncoded());
		Map<String, String> fields = parseOneStartEvent(item.getDescription());
		if (fields != null) {
			String fieldStr = fields.get("Time");
			String dateStr = null;
			if (fieldStr == null) {
				// Regular event
				dateStr = fields.get("Start Time");
				Date startDate = this.convertDate(dateStr, sdfNormal);
				event.setStartDate(startDate);
				if (fields.get("End Time") != null) {
					event.setEndDate(this.convertDate(fields.get("End Time"), sdfNormal));
				}
			} else {
				event.setAllDay(true);
				// All Day event
				if (fieldStr.contains(" All Day")) {
					int breakPoint = fieldStr.indexOf(" All Day");
					dateStr = fieldStr.substring(0, breakPoint);
					Date allDayDate = this.convertDate(dateStr, sdfAllDay);
					event.setStartDate(allDayDate);
				}
			}
			if (fields.get("Location") != null) {
				event.setLocation(fields.get("Location"));
			}
			if (fields.get("Description") != null) {
				event.setDescription(fields.get("Description"));
			}
			if (fields.get("Contact Email") != null) {
				event.setContact(fields.get("Contact Email"));
			}
			if (fields.get("Cost") != null) {
				event.setCost(fields.get("Cost"));
			}
			if (fields.get("Other Info") != null) {
				event.setOtherInfo(fields.get("Other Info"));
			}
		}
	}

	private Map<String, String> parseOneStartEvent(String xml) {
		String documentXml = this.getConfigParamService().findValueByName(EVENTS_CCL_PARSER_HEADER) + xml + this.getConfigParamService().findValueByName(EVENTS_CCL_PARSER_FOOTER);
		Map<String, String> event = new HashMap<String, String>();
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new InputSource(new StringReader(documentXml)));
			Element root = doc.getRootElement();
			List items = root.getChildren("field");
			for (Iterator iterator = items.iterator(); iterator.hasNext();) {
				Element item = (Element) iterator.next();
				String name = item.getAttributeValue("name");
				String value = item.getValue();
				if (name != null && !"".equals(name.trim())) {
					event.put(name, value);
				}
			}
		} catch (JDOMException e) {
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}

		return event;
	}

	private Date convertDate(String dateStr, SimpleDateFormat sdf) {
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
		}
		return date;
	}

	private boolean isOneStartCalendar(MaintRss maintRss) {
		return RSS_TYPE_EVENTS_ONESTART.equals(maintRss.getType());
	}

	private boolean isIUEventsCalendar(MaintRss maintRss) {
		return RSS_TYPE_EVENTS_CALENDAR.equals(maintRss.getType());
	}

	private boolean isEventsGeneral(MaintRss maintRss) {
		return RSS_TYPE_EVENTS_GENERAL.equals(maintRss.getType());
	}

	private boolean isIUPUIEventsCalendar(String code) {
		return code.startsWith("iupui_");
	}

	private boolean isNorthwestMarketingCalendar(String code) {
		return code.startsWith("nwm_");
	}

	public RssService getRssService() {
		return rssService;
	}

	public void setRssService(RssService rssService) {
		this.rssService = rssService;
	}

	public RssCacheService getRssCacheService() {
		return rssCacheService;
	}

	public void setRssCacheService(RssCacheService rssCacheService) {
		this.rssCacheService = rssCacheService;
	}

	public DynamicRssCacheService getDynamicRssCacheService() {
		return dynamicRssCacheService;
	}

	public void setDynamicRssCacheService(DynamicRssCacheService dynamicRssCacheService) {
		this.dynamicRssCacheService = dynamicRssCacheService;
	}

	public ConfigParamService getConfigParamService() {
		return configParamService;
	}

	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
}
