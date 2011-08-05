package org.kuali.mobility.mdot.interceptors;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kuali.mobility.shared.Constants;
import org.kuali.mobility.user.entity.User;
import org.kuali.mobility.user.entity.UserImpl;
import org.kuali.mobility.util.HttpUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import edu.iu.uis.cas.filter.CASFilter;

public class LoginInterceptor implements HandlerInterceptor {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LoginInterceptor.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (HttpUtil.needsAuthenticated(request.getServletPath())) {
			login(request);
		}

		User user = (User) request.getSession(true).getAttribute(Constants.KME_USER_KEY);

		if (user != null && user.getUserAttribute("acked") == null && HttpUtil.needsAuthenticated(request.getServletPath())) {
			try {
				user.setUserAttribute("service", request.getServletPath());
				response.sendRedirect(request.getContextPath() + "/mobileCasAck");
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		
		return true;
	}

	private User login(HttpServletRequest request) {		
		User user = (User) request.getSession(true).getAttribute(Constants.KME_USER_KEY);
		if (user == null) {
			user = new UserImpl();
			user.setUserId(CASFilter.getRemoteUser(request));
			
			// TODO: Get person attributes and set on User Object. Save to database.
			
			
			
			request.getSession().setAttribute(Constants.KME_USER_KEY, user);
			LOG.info("User id: " + user.getUserId() + " logging in."); 
		}
		return user;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView e) throws Exception {}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {}

}
