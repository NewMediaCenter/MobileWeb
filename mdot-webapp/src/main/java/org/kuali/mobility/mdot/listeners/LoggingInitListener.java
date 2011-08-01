package org.kuali.mobility.mdot.listeners;

import javax.servlet.ServletContextEvent;

public class LoggingInitListener {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LoggingInitListener.class);

	public void contextInitialized(ServletContextEvent event) {
		org.apache.log4j.PropertyConfigurator.configureAndWatch(event.getServletContext().getInitParameter("FullPathToLog4jProperties"), 5 * 60 * 1000);
		LOG.info("LoggingInitListener finished initializing log4j");
	}

	public void contextDestroyed(ServletContextEvent event) {}

}
