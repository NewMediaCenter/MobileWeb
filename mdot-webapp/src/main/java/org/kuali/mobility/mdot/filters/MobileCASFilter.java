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
 
package org.kuali.mobility.mdot.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import edu.iu.uis.cas.filter.CASFilter;

public class MobileCASFilter extends CASFilter {

	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest hrequest = (HttpServletRequest) request;
		if (needsAuthenticated(hrequest)) {
			super.doFilter(request, response, chain);
		} else {
			chain.doFilter(request, response);
		}
	}

	private boolean needsAuthenticated(final HttpServletRequest request) {
		final String servletPath = request.getServletPath();
		if (servletPath.startsWith("/oauth") 
				|| servletPath.startsWith("/events") 
				|| servletPath.startsWith("/sakaiannouncements")
				|| servletPath.startsWith("/sakaiassignments")
				|| servletPath.startsWith("/sakaiassignmentdetails")
				|| servletPath.startsWith("/sakaigradebook")
				|| servletPath.startsWith("/myclasseshome") 
				|| servletPath.startsWith("/myclasses") 
				|| servletPath.startsWith("/forums") 
				|| servletPath.startsWith("/sakaiannouncementdetails") 
				|| servletPath.startsWith("/sakaiforumsmessages") 
				|| servletPath.startsWith("/sakaiforumsmessagedetails")
				|| servletPath.startsWith("/sakaiprivatetopics") 
				|| servletPath.startsWith("/sakaiprivatemessages") 
				|| servletPath.startsWith("/sakaiprivatemessagedetails") 
				|| servletPath.startsWith("/roster") 
				|| servletPath.startsWith("/resources") 
				|| servletPath.startsWith("/sakairesourcedetails") 
				|| servletPath.startsWith("/calendar") 
				|| servletPath.startsWith("/sakaiparticipantdetails")) {
			return true;
		}
		return false;
	}
}
