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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.kuali.mobility.events.entity.Category;
import org.kuali.mobility.events.entity.Event;
import org.kuali.mobility.events.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/events")
public class EventsController {

	@Autowired
	private EventsService eventsService;

	public void setEventsService(EventsService eventsService) {
		this.eventsService = eventsService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getList(Model uiModel) {
		List<Category> categories = eventsService.getCategoriesByCampus("NW");
		uiModel.addAttribute("categories", categories);
		uiModel.addAttribute("campus", "NW");
		return "events/list";
	}

	@RequestMapping(value = "/viewEvents", method = RequestMethod.GET)
	public String viewEvents(HttpServletRequest request, Model uiModel, @RequestParam(required = true) String categoryId, @RequestParam(required = false) String campus) throws Exception {
		Category category = eventsService.getAllEvents(campus, categoryId);
		uiModel.addAttribute("category", category);
		uiModel.addAttribute("campus", campus);
		return "events/eventsList";
	}

	@RequestMapping(value = "/viewEvent", method = RequestMethod.GET)
	public String viewEvent(HttpServletRequest request, Model uiModel, @RequestParam(required = true) String categoryId, @RequestParam(required = false) String campus, @RequestParam(required = true) String eventId) throws Exception {
		Event event = eventsService.getEvent(campus, categoryId, eventId);
		uiModel.addAttribute("event", event);
		uiModel.addAttribute("categoryId", categoryId);
		uiModel.addAttribute("campus", campus);
		return "events/event";
	}

}
