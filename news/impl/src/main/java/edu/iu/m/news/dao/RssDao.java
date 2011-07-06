package edu.iu.m.news.dao;

import java.util.List;

import edu.iu.m.news.entity.Rss;

public interface RssDao {
	
    public Rss findRssById(Long id);
    public Rss findRssByUrl(String url);
    public Rss findRssByMaintRssId(Long maintRssId);
    public void saveRss(Rss rss);
    public List<Rss> findAllRss();
    public void deleteRss(Rss rss);
    public void deleteRssById(Long id);
}
