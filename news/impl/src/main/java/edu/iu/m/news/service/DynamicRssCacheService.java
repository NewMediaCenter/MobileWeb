package edu.iu.m.news.service;

import java.util.List;

import edu.iu.m.news.entity.DynamicRss;
import edu.iu.m.news.entity.LinkFeed;
import edu.iu.m.news.entity.Rss;

public interface DynamicRssCacheService {

	public void reload();
	public Rss getRss(String feedUrl, Integer updateIntervalSeconds);
	public LinkFeed getLinkFeed(String url, Rss rss);
	public List<DynamicRss> getAllDynamicRss();
	public List<LinkFeed> getAllLinkFeeds();
	
}
