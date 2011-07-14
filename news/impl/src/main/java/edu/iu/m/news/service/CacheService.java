package edu.iu.m.news.service;

import edu.iu.m.news.entity.MaintRss;
import edu.iu.m.news.entity.Rss;


public interface CacheService {
    
    void startCache();
    void stopCache();
    void reloadCache();
    void reloadCacheDoWork();
    
    // RSS
    void updateRss(Rss rss);
    void updateRssDoWork(Long rssId);
    void updateMaintRss(MaintRss maintRss);
    void updateMaintRssDoWork(Long maintRssId);
    void deleteMaintRss(MaintRss maintRss);
    void deleteMaintRssDoWork(Long maintRssId, String maintRssShortCode, String campusCode);
}
