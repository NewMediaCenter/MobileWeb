package edu.iu.m.news.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.iu.m.news.entity.MaintRss;
import edu.iu.m.news.entity.Rss;
import edu.iu.m.news.entity.RssItem;

@Service
@Transactional 
public class RssCacheServiceImpl implements RssCacheService {
	
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RssCacheServiceImpl.class);
	
	@Autowired
	private RssService rssService;
	
	@Autowired
	private MaintRssService rssMaintService;

	// String represents the Rss's parent Maint Rss Id
	private ConcurrentMap<Long, Rss> cachedRssMap;
	
	// String represents the campus code
	private ConcurrentMap<String, MaintRssCache> storedMaintRssCaches;
	
	// String represents the url from which the feed is referred to in the RSS, before it is modified to link to what we actually need
	//private ConcurrentMap<String, LinkFeed> cachedLinkFeeds;
	
	private static final String RSS_TYPE_EVENTS_IUPUI = "EVENTS-IUPUI";

	public RssCacheServiceImpl() {		
		this.cachedRssMap = new ConcurrentHashMap<Long, Rss>();
		this.storedMaintRssCaches = new ConcurrentHashMap<String, MaintRssCache>();
		//this.cachedLinkFeeds = new ConcurrentHashMap<String, LinkFeed>();
	}
	
	/*
	 * Update methods: DoWork methods are called by the bus, the others notify the bus.
	 */

	public void updateMaintRssDoWork(Long maintRssId) {
		if (maintRssId != null) {
			MaintRss newMaintRss = rssMaintService.findRssById(maintRssId);
			if (newMaintRss != null) {
				if (newMaintRss.isActive()) {
					MaintRss maintRss = newMaintRss.copy();
					this.putMaintRss(maintRss, false, false);					
				} else {
					this.removeMaintRss(newMaintRss.getCampus(), newMaintRss.getShortCode());
				}
			}
		}
	}
	
	public void updateRssDoWork(Long rssId) {
		if (rssId != null) {
			Rss newRss = getRssService().findRssById(rssId);
			if (newRss != null) {
				Rss rss = CachedCopies.copy(newRss);
				this.cachedRssMap.put(rss.getRssMaintId(), rss);
			} else {
				LOG.warn("Rss in saveRssDoWork is null");
			}
		} else {
			LOG.warn("rssId in saveRssDoWork is null");
		}
	}
	
	public void deleteMaintRssDoWork(Long maintRssId, String maintRssShortCode, String campusCode) {
		this.removeMaintRss(campusCode, maintRssShortCode);
		this.cachedRssMap.remove(maintRssId);
	}
	
	@Transactional
	private void updateRss(Rss rss) {
		Rss rssCopy = CachedCopies.copy(rss);
		if (rss.isDelete()) {
			this.getRssService().deleteRss(rss);
		}
		rssCopy.setRssId(null);
		rssCopy.setVersionNumber(0L);
		this.getRssService().updateRss(rssCopy);
	}
	
	/*
	 * Cache reloading methods
	 */
	
	public List<MaintRss> reload(boolean isSavingServer) {
		Date now = new Date();
//		List<MaintRss> maintRssList = this.rssMaintService.findAllRss();
//		this.putAllMaintRss(maintRssList, isSavingServer, false);
		List<MaintRss> maintRssList = new ArrayList<MaintRss>();
		Collection<MaintRssCache> maintRssCaches = this.storedMaintRssCaches.values();
		for (MaintRssCache cache : maintRssCaches) {
			maintRssList.addAll(cache.getAllMaintRss());
		}
		List<MaintRss> maintRssNotify = new ArrayList<MaintRss>();
		String log = "";
		for (MaintRss maintRss : maintRssList) {
			boolean notify = false;
			notify = loadRss(maintRss.getShortCode(), maintRss.getCampus(), isSavingServer, false);
			if (notify && isSavingServer) {
				maintRssNotify.add(maintRss);
				log = log + maintRss.getRssId() + " ";
			}
		}
//		LOG.info("TEST: Notify: " + log);
//		this.reloadLinkFeeds();
		Date end = new Date();
		LOG.info("Rss Cache Reloaded. Start: " + now.toString() + " >> End: " + end.toString() + " Notify:" + log);
		return maintRssNotify;
	}

	public MaintRss testReload(boolean isSavingServer, MaintRss maintRss) {
		// This is a test method, don't use this for regular work.
		MaintRss maintRssCopy = maintRss.copy();
//		this.adjustMaintRssUrl(maintRssCopy);
		boolean notify = false;
		try {
			Rss rss = this.getRssService().fetch(maintRssCopy.getUrl());
			Rss rssCopy = CachedCopies.copy(rss);
			if (rss.getRssMaintId() != null) {
				this.cachedRssMap.put(rss.getRssMaintId(), rssCopy);	
			} else {
				LOG.info("Can't load RSS to cache without RssMaintId. RssId: " + rss.getRssId() + " url:" + rss.getUrl());
			}
			notify = true;
		} catch (Exception e) {
			LOG.error("Couldn't retrieve Rss in testReload for maintRssId: " + maintRss.getRssId(), e);
		}
		if (notify) {
			return maintRss;
		}
		return null;
	}
	
	public void load(boolean isSavingServer) {
		Date now = new Date();
		List<MaintRss> maintRssList = this.rssMaintService.findAllRss();
		this.putAllMaintRss(maintRssList, isSavingServer, true);
		// Test save/download/convert
//		int start = 33;
//		int num = 34;
//		int num = maintRssList.size() - 1;
//		if (maintRssList.size() > num) {
//			for (int x = start ; x < num ; x++) {
//				edu.iu.mobileweb.domain.maint.MaintRss maintRss = maintRssList.get(x);
//				LOG.info("MaintRss: " + maintRss.getShortCode());
//				if (maintRss != null) {
//					loadRss(maintRss.getShortCode());
//				}
//			}			
//		}
//		for (MaintRss maintRss : maintRssList) {
//			LOG.info("MaintRss: " + maintRss.getShortCode());
//			if (maintRss != null) {
//				loadRss(maintRss.getShortCode(), maintRss.getCampus());
//			}
//		}
		Date end = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat();
		LOG.info("Rss Cache Loaded. Start: " + now.toString() + " >> End: " + end.toString());
	}
	
	/*
	 * Put
	 */
	
	private void putAllMaintRss(List<MaintRss> maintRssList, boolean isSavingServer, boolean isInitialLoad)  {
		List<Long> maintRssNotify = new ArrayList<Long>();
		String log = "";
		for (MaintRss maintRss : maintRssList) {
			boolean notify = false;
			// Copy for cache is handled in individual put operation
			if (maintRss.isActive()) {
				notify = this.putMaintRss(maintRss, isSavingServer, isInitialLoad);
			}
			if (notify) {
				maintRssNotify.add(maintRss.getRssId());
				log = log + maintRss.getRssId() + " ";
			}
		}
		LOG.info("TEST: Notify: " + log);
	}
	
	private boolean putMaintRss(MaintRss maintRss, boolean isSavingServer, boolean isInitialLoad) {
		// Return boolean tells us if the rss needs to notify the bus for an update
		boolean notify = false;
		MaintRss maintRssCopy = maintRss.copy();
		LOG.info("LOADING: MaintRSS " + maintRss.getDisplayName());
		if (maintRssCopy != null && maintRssCopy.getShortCode() != null && !"".equals(maintRssCopy.getShortCode())) {
//			this.adjustMaintRssUrl(maintRssCopy);
			if (maintRssCopy.getCampus() != null && !"".equals(maintRssCopy.getCampus())) {
				MaintRssCache maintRssCache = this.getOrCreateMaintRssCache(maintRssCopy.getCampus());
				maintRssCache.put(maintRssCopy.getShortCode(), maintRssCopy);
				if (maintRss.isActive()) {
					notify = loadRss(maintRss.getShortCode(), maintRssCopy.getCampus(), isSavingServer, isInitialLoad);	
				} else {
					this.cachedRssMap.remove(maintRss.getRssId());
				}
			} else {
				LOG.info("MaintRss missing campus, not adding to cache: " + maintRssCopy.getShortCode());
			}
		}
		return notify;
	}
	
	/*
	 * Remove
	 */
	
	private void removeMaintRss(String campusCode, String maintRssShortCode) {
		MaintRssCache maintRssCache = this.getOrCreateMaintRssCache(campusCode);
		maintRssCache.remove(maintRssShortCode);
	}
	
	/*
	 * Load
	 */
	
	private MaintRssCache getOrCreateMaintRssCache(String campusCode) {
		MaintRssCache cache = this.storedMaintRssCaches.get(campusCode);
		if (cache == null) {
			// Cache does not yet exist, we'll try to create it.
			MaintRssCache newCache = new MaintRssCacheImpl();
			cache = this.storedMaintRssCaches.putIfAbsent(campusCode, newCache);
			if (cache == null) {
				// Cache creation and put succeeded, use the new cache object.
				cache = newCache;
			}
		}
		// We'll always return cache here. If the cache already exists, return it. If another thread beat us to the punch and created the cache first, we'll use theirs.
		return cache;
	}
	
	private boolean loadRss(String maintRssShortCode, String campusCode, boolean isSavingServer, boolean isInitialLoad) {
		// Return boolean tells us if the rss needs to notify the bus for an update
		boolean notify = false;
		MaintRssCache maintRssCache = this.getOrCreateMaintRssCache(campusCode);
		if (maintRssCache != null) {
			MaintRss maintRss = maintRssCache.get(maintRssShortCode);
			if (maintRss != null && maintRss.getUrl() != null) {
				if (maintRss.isActive()) {
					try {
						if (isIUPUIEventsCalendar(maintRss)) {
							// Don't do anything yet, we don't have a way to handle these at the moment.
						} else {
							Rss cachedRss = null;
							Rss rss = null;
							// If this is the first load on startup, no need to try to use the cached version.
							if (isInitialLoad) {
								rss = this.getRssService().getRss(maintRss, false, null);
							} else {
								cachedRss = this.getRssByMaintRssId(maintRss.getRssId());
								rss = this.getRssService().getRss(maintRss, true, cachedRss);
							}
							if (rss != null && rss.isUpdate() && isSavingServer) {
								try {
//									if (rss.getRssId() == null) {
//										rss = this.getRssService().getRss(maintRss, false, null);
//									}
									this.updateRss(rss);
								} catch (Exception e) {
									LOG.info("Rss failure on save. It may have been saved on another server first. Short code: " + maintRssShortCode);
								}
							}
							if (rss != null) {
								if (isInitialLoad) {
									Rss rssCopy = CachedCopies.copy(rss);
									if (rss.getRssMaintId() != null) {
										this.cachedRssMap.put(rss.getRssMaintId(), rssCopy);	
									} else {
										LOG.info("Can't load RSS to cache without RssMaintId. RssId: " + rss.getRssId() + " url:" + rss.getUrl());
									}
								} else if (rss.isPutInCache()) {
									Rss rssCopy = CachedCopies.copy(rss);
									if (rss.getRssMaintId() != null) {
										this.cachedRssMap.put(rss.getRssMaintId(), rssCopy);	
									} else {
										LOG.info("Can't load RSS to cache without RssMaintId. RssId: " + rss.getRssId() + " url:" + rss.getUrl());
									}
									notify = true;
								}							
							}
						}
					} catch (Exception e) {
						LOG.error("Error loading rss: " + maintRss.getRssId() + " " + maintRss.getUrl(), e);
					}	
				}
			} else {
				LOG.info("Attempted to load Rss from a short code that is not cached: " + maintRssShortCode);
			}			
		} else {
			LOG.info("Attempted to load Rss from a cache that does not exist: " + campusCode);
		}
		return notify;
	}

	/*
	 * Get
	 */
	
	public List<MaintRss> getAllMaintRssByCampusAndType(String campusCode, String type) {
		// maintRssCache should never be null, as the getOrCreate should always return an object
		MaintRssCache maintRssCache = this.getOrCreateMaintRssCache(campusCode);
		List<MaintRss> maintRssForDisplay = maintRssCache.getAllMaintRssByType(type);
		Collections.sort(maintRssForDisplay);
		return maintRssForDisplay;
	}
	
	public List<MaintRss> getAllMaintRssByCampus(String campusCode) {
		// maintRssCache should never be null, as the getOrCreate should always return an object
		MaintRssCache maintRssCache = this.getOrCreateMaintRssCache(campusCode);
		List<MaintRss> maintRssForDisplay = maintRssCache.getAllMaintRss();
		Collections.sort(maintRssForDisplay);
		return maintRssForDisplay;
	}
	
	public MaintRss getMaintRssByCampusAndShortCode(String campusCode, String shortCode) {
		// maintRssCache should never be null, as the getOrCreate should always return an object
		MaintRssCache maintRssCache = this.getOrCreateMaintRssCache(campusCode);
		MaintRss maintRss = maintRssCache.get(shortCode);
		return maintRss;
	}
	
//	public Rss getRssByUrl(String url) {
//		Rss rss = this.cachedRssMap.get(url);
//		if (rss != null) {
//			rss = CachedCopies.copy(rss);
//		}
//		return rss;
//	}
	
	public Rss getRssByMaintRssId(Long maintRssId) {
		Rss rss = this.cachedRssMap.get(maintRssId);
		if (rss != null) {
			rss = CachedCopies.copy(rss);
		}
		return rss;
	}

	/*
	 * Support Methods
	 */

	private boolean isIUPUIEventsCalendar(MaintRss maintRss) {
		return RSS_TYPE_EVENTS_IUPUI.equals(maintRss.getType());
	}
	
	/*
	 * Service Methods
	 */
	
	public RssService getRssService() {
		return rssService;
	}

	public void setRssService(RssService rssService) {
		this.rssService = rssService;
	}
	
	/*
	 * Copier
	 */

	private static class CachedCopies {
		
		public static Rss copy(Rss rss) {
			Rss rssCopy = new Rss();
	
			if (rss.getRssId() != null) {
				rssCopy.setRssId(new Long(rss.getRssId()));
			}
			if (rss.getVersionNumber() != null) {
				rssCopy.setVersionNumber(new Long(rss.getVersionNumber()));
			}
			if (rss.getFormDescription() != null) {
				rssCopy.setFormDescription(new String(rss.getFormDescription()));
			}
			if (rss.getFormLink() != null) {
				rssCopy.setFormLink(new String(rss.getFormLink()));
			}
			if (rss.getFormName() != null) {
				rssCopy.setFormName(new String(rss.getFormName()));
			}
			if (rss.getFormTitle() != null) {
				rssCopy.setFormTitle(new String(rss.getFormTitle()));
			}
			if (rss.getImageLocation() != null) {
				rssCopy.setImageLocation(new String(rss.getImageLocation()));
			}
			if (rss.getLastUpdateDate() != null) {
				rssCopy.setLastUpdateDate(new Timestamp(rss.getLastUpdateDate().getTime()));
			}
			if (rss.getLink() != null) {
				rssCopy.setLink(new String(rss.getLink()));
			}
			if (rss.getTitle() != null) {
				rssCopy.setTitle(new String(rss.getTitle()));
			}
			if (rss.getUrl() != null) {
				rssCopy.setUrl(new String(rss.getUrl()));
			}
			if (rss.getRssMaintId() != null) {
				rssCopy.setRssMaintId(new Long(rss.getRssMaintId()));
			}
			if (rss.getRssItems() != null) {
				List<RssItem> rssItems = new ArrayList<RssItem>();
				for (RssItem rssItem : rss.getRssItems()) {
					RssItem newRssItem = copy(rssItem);
					newRssItem.setRss(rssCopy);
					rssItems.add(newRssItem);
				}
				rssCopy.setRssItems(rssItems);
			}
			return rssCopy;
		}
		
		private static RssItem copy(RssItem rssItem) {
			RssItem rssItemCopy = new RssItem();

			if (rssItem.getRssItemId() != null) {
				rssItemCopy.setRssItemId(new Long(rssItem.getRssItemId()));
			}
			if (rssItem.getVersionNumber() != null) {
				rssItemCopy.setVersionNumber(new Long(rssItem.getVersionNumber()));
			}
			if (rssItem.getTitle() != null) {
				rssItemCopy.setTitle(new String(rssItem.getTitle()));
			}
			if (rssItem.getDescription() != null) {
				rssItemCopy.setDescription(new String(rssItem.getDescription()));
			}
			if (rssItem.getLink() != null) {
				rssItemCopy.setLink(new String(rssItem.getLink()));
			}
			if (rssItem.getOrder() != null) {
				rssItemCopy.setOrder(new Long(rssItem.getOrder()));
			}
			if (rssItem.getPublishDate() != null) {
				rssItemCopy.setPublishDate(new Timestamp(rssItem.getPublishDate().getTime()));
			}
			if (rssItem.getEnclosureUrl() != null) {
				rssItemCopy.setEnclosureUrl(new String(rssItem.getEnclosureUrl()));
			}
			if (rssItem.getEnclosureType() != null) {
				rssItemCopy.setEnclosureType(new String(rssItem.getEnclosureType()));
			}
			if (rssItem.getEnclosureLength() != null) {
				rssItemCopy.setEnclosureLength(new Long (rssItem.getEnclosureLength()));
			}
//			if (rssItem.getRss() != null) {
//				rssItemCopy.setRssId(new Long(rssItem.getRssId()));
//			}
			return rssItemCopy;
		}
	}

}
