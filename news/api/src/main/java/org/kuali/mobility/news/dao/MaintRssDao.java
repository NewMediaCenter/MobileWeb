package org.kuali.mobility.news.dao;
import java.util.List;

import org.kuali.mobility.news.entity.MaintRss;

public interface MaintRssDao {
	
    public MaintRss findRssById(Long id);
    public MaintRss findRssByUrl(String url);
    public MaintRss findRssByShortCode(String code);
    public List<MaintRss> findAllNewsRssByCampusCode(String campusCode);
    public void saveRss(MaintRss rss);
    public List<MaintRss> findAllRss();
    public void deleteRssById(Long id);
    public String findUrlByShortCode(String code);
    public void reoder(Long id, boolean up);
    public List<MaintRss> findAllRssOrderBy(String order);
    public List<MaintRss> rssDaofindAllRss(String clause);

}
