package edu.iu.m.news.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.iu.m.news.entity.MaintRss;

@Service
@Transactional 
public class MaintRssCacheImpl implements MaintRssCache {

	// String represents the maintenance Rss's short code
	private ConcurrentMap<String, MaintRss> cachedMaintRssMap;	
	
	public MaintRssCacheImpl() {
		this.cachedMaintRssMap = new ConcurrentHashMap<String, MaintRss>();
	}
	
	public void put(String shortCode, MaintRss maintRss) {
		this.cachedMaintRssMap.put(shortCode, maintRss);
	}
	
	public MaintRss get(String shortCode) {
		MaintRss maintRss = this.cachedMaintRssMap.get(shortCode);
		if (maintRss != null) {
			maintRss = maintRss.copy();
		}
		return maintRss;
	}
	
	public List<MaintRss> getAllMaintRssByType(String type) {
		List<MaintRss> allMaintRss = new ArrayList<MaintRss>();
		Set<String> cachedSet = this.cachedMaintRssMap.keySet();
		Iterator<String> itr = cachedSet.iterator();
		while (itr.hasNext()) {
			MaintRss maintRss = this.cachedMaintRssMap.get(itr.next());
			if (maintRss != null && type.equals(maintRss.getType())) {
				MaintRss maintRssCopy = maintRss.copy();
				allMaintRss.add(maintRssCopy);
			}
		}
		return allMaintRss;
	}
	
	public List<MaintRss> getAllMaintRss() {
		List<MaintRss> allMaintRss = new ArrayList<MaintRss>();
		Set<String> cachedSet = this.cachedMaintRssMap.keySet();
		Iterator<String> itr = cachedSet.iterator();
		while (itr.hasNext()) {
			MaintRss maintRss = this.cachedMaintRssMap.get(itr.next());
			if (maintRss != null) {
				MaintRss maintRssCopy = maintRss.copy();
				allMaintRss.add(maintRssCopy);
			}
		}
		return allMaintRss;
	}

	public void remove(String shortCode) {
		this.cachedMaintRssMap.remove(shortCode);
	}
	
}
