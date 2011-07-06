package org.kuali.mobility.news.service;

import java.util.List;

import org.kuali.mobility.news.entity.MaintRss;

public interface MaintRssService {

	public void saveRss(MaintRss rss);

	public List<MaintRss> findAllRss();

	public MaintRss findRssById(Long rssId);
	
    public MaintRss findRssByShortCode(String code);

	public void deleteRssById(Long id);

    public String findUrlByShortCode(String code);

    public void reorder(Long id, boolean up);

    public List<MaintRss> findAllRssOrderBy(String order);

    public List<MaintRss> findAllRss(String clause);
    
    public List<MaintRss> findAllNewsRssByCampusCode(String campusCode);

}
