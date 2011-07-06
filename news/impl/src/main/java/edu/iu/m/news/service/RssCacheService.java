package edu.iu.m.news.service;

import java.util.List;

import edu.iu.m.news.entity.MaintRss;
import edu.iu.m.news.entity.Rss;

public interface RssCacheService {

	void load(boolean isSavingServer);
	List<MaintRss> reload(boolean isSavingServer);
	List<MaintRss> getAllMaintRssByCampusAndType(String campusCode, String type);
	List<MaintRss> getAllMaintRssByCampus(String campusCode);
	MaintRss getMaintRssByCampusAndShortCode(String campusCode, String shortCode);
	Rss getRssByMaintRssId(Long maintRssId);

	void updateRssDoWork(Long rssId);
	void updateMaintRssDoWork(Long maintRssId);
	void deleteMaintRssDoWork(Long maintRssId, String maintRssShortCode, String campusCode);
}
