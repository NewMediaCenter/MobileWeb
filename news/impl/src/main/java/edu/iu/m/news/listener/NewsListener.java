package edu.iu.m.news.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.iu.m.news.service.CacheService;

public class NewsListener implements ServletContextListener {
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(NewsListener.class);

    private CacheService cacheService;

	public void contextInitialized(final ServletContextEvent event) {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		cacheService = (CacheService) ctx.getBean("newsCacheService");
		
		LOG.info("Starting the news cache thread");
		cacheService.startCache();
		LOG.info("News cache thread started");
	}

	public void contextDestroyed(final ServletContextEvent event) {
		if (cacheService != null) {
			LOG.info("Stopping the news cache thread");
			cacheService.stopCache();
			LOG.info("news cache thread should be completely dead");
		}
	}
	
	public CacheService getCacheService() {
		return cacheService;
	}

	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}
}
