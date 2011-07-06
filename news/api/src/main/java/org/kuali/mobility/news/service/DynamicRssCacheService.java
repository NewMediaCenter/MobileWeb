package org.kuali.mobility.news.service;

import java.util.List;

import org.kuali.mobility.news.entity.DynamicRss;
import org.kuali.mobility.news.entity.LinkFeed;
import org.kuali.mobility.news.entity.Rss;

public interface DynamicRssCacheService {

	public void reload();
	public Rss getRss(String feedUrl, Integer updateIntervalSeconds);
	public LinkFeed getLinkFeed(String url, Rss rss);
	public List<DynamicRss> getAllDynamicRss();
	public List<LinkFeed> getAllLinkFeeds();
	
}
