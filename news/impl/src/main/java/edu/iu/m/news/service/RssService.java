package edu.iu.m.news.service;

import edu.iu.m.news.entity.LinkFeed;
import edu.iu.m.news.entity.MaintRss;
import edu.iu.m.news.entity.Rss;

public interface RssService {
	public Rss getRss(MaintRss maintRss, boolean useCache, Rss cacheRss);
	
	public void deleteRss(Rss rss);
	public void saveRss(Rss rss);
	public void updateRss(Rss rss);
	public Rss findRssById(Long rssId);

	public Rss fetch(String url) throws Exception;
	public LinkFeed parseLinkFeedFromUrl(LinkFeed lf);
}
