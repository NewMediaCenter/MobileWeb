package org.kuali.mobility.news.service;

import org.kuali.mobility.news.entity.LinkFeed;
import org.kuali.mobility.news.entity.MaintRss;
import org.kuali.mobility.news.entity.Rss;

public interface RssService {
	public Rss getRss(MaintRss maintRss, boolean useCache, Rss cacheRss);
	
	public void deleteRss(Rss rss);
	public void saveRss(Rss rss);
	public void updateRss(Rss rss);
	public Rss findRssById(Long rssId);

	public Rss fetch(String url) throws Exception;
	public LinkFeed parseLinkFeedFromUrl(LinkFeed lf);
}
