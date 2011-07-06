package edu.iu.m.news.service;

import java.util.List;

import edu.iu.m.news.entity.MaintRss;

public interface MaintRssCache {

	void put(String shortCode, MaintRss maintRss);
	MaintRss get(String shortCode);
	List<MaintRss> getAllMaintRssByType(String type);
	List<MaintRss> getAllMaintRss();
	void remove(String shortCode);
	
}
