package org.kuali.mobility.news.service;

import java.util.List;

import org.kuali.mobility.news.entity.MaintRss;
import org.kuali.mobility.news.entity.Rss;

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
