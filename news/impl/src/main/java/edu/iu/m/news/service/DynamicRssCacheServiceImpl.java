package edu.iu.m.news.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.kuali.mobility.configparams.service.ConfigParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.iu.m.news.entity.Counter;
import edu.iu.m.news.entity.DynamicRss;
import edu.iu.m.news.entity.LinkFeed;
import edu.iu.m.news.entity.Rss;
import edu.iu.m.news.entity.RssItem;

@Service
@Transactional 
public class DynamicRssCacheServiceImpl implements DynamicRssCacheService {
	
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DynamicRssCacheServiceImpl.class);
	
	@Autowired
	private RssService rssService;
	
	@Autowired
	private ConfigParamService configParamService;
	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
	
	private Counter counter;

	// String represents the url the feed was originally retrieved from
	private ConcurrentMap<String, DynamicRss> dynamicRssMap;
	
	// String represents the url from which the feed is referred to in the RSS, before it is modified to link to what we actually need
	private ConcurrentMap<String, LinkFeed> cachedLinkFeeds;
	
	private static final String NEWS_UITS_QUERY_STRING = "News.UITSQueryString";
	
	/*
	 * There is currently no system for updating the feeds
	 * IDs will not be consistent across the servers, so they should not be used for updates across the KSB.
	 */
	
	public DynamicRssCacheServiceImpl() {
		this.counter = new Counter();
		this.dynamicRssMap = new ConcurrentHashMap<String, DynamicRss>();
		this.cachedLinkFeeds = new ConcurrentHashMap<String, LinkFeed>();
	}
	
	public void reload() {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		List<DynamicRss> dynamicRssList = this.getAllDynamicRss();
		for (DynamicRss dynRss : dynamicRssList) {
			try {
				cal.setTime(dynRss.getLastUpdate());
				cal.add(Calendar.SECOND, dynRss.getUpdateIntervalSeconds());
				Date nextUpdate = cal.getTime();
				if (now.after(nextUpdate)) {
					Rss rss = dynRss.getRss();
					String feedUrl = rss.getUrl();
					Rss newRss = this.getRssService().fetch(feedUrl);
					if (newRss != null) {
						dynRss.update(newRss);
					}
					LOG.info("Updated Dynamic RSS " + dynRss.getFeedUrl() + " next update in " + dynRss.getUpdateIntervalSeconds() + " seconds.");
				} 
//				else {
					LOG.info("Not yet updating Dynamic RSS " + dynRss.getFeedUrl() + " next update at " + nextUpdate);
//				}
			} catch (Exception e) {
				LOG.error("Error in dynamic rss cache reload for id:" + dynRss.getId() + " url:" + dynRss.getFeedUrl(), e);
			}
		}
		List<LinkFeed> linkFeedList = this.getAllLinkFeeds();
		for (LinkFeed lf : linkFeedList) {
			try {
				cal.setTime(lf.getLastUpdate());
				cal.add(Calendar.SECOND, lf.getUpdateIntervalSeconds());
				Date nextUpdate = cal.getTime();
				if (now.after(nextUpdate)) {
					this.loadLinkFeed(lf.getFeedUrl());
					LOG.info("Updated LinkFeed " + lf.getFeedUrl() + " next update in " + lf.getUpdateIntervalSeconds() + " seconds.");
				} 
//				else {
//					LOG.info("Not yet updating LinkFeed " + lf.getFeedUrl() + " next update at " + nextUpdate);
//				}
			} catch (Exception e) {
				LOG.error("Error in dynamic linkfeed cache reload for id:" + lf.getId() + " url:" + lf.getFeedUrl(), e);
			}
		}
	}
	
	public List<DynamicRss> getAllDynamicRss() {
		List<DynamicRss> dynamicRssList = new ArrayList<DynamicRss>();
		dynamicRssList.addAll(this.dynamicRssMap.values());
		return dynamicRssList;
	}
	
	public List<LinkFeed> getAllLinkFeeds() {
		List<LinkFeed> linkFeedList = new ArrayList<LinkFeed>();
		linkFeedList.addAll(this.cachedLinkFeeds.values());
		return linkFeedList;
	}
	
	public DynamicRss getOrCreateDynamicRss(String feedUrl, Integer updateIntervalSeconds) {
		DynamicRss cache = this.dynamicRssMap.get(feedUrl);
		if (cache == null) {
			// Cache does not yet exist, we'll try to create it.
			Rss rss = null;
			try {
				rss = this.getRssService().fetch(feedUrl);
				if (rss != null) {
					DynamicRss newCache = new DynamicRss(this.counter.getNext(), rss, feedUrl);
					if (updateIntervalSeconds != null) {
						newCache.setUpdateIntervalSeconds(updateIntervalSeconds);
					}
					cache = this.dynamicRssMap.putIfAbsent(feedUrl, newCache);
					if (cache == null) {
						// Cache creation and put succeeded, use the new cache object.
						cache = newCache;
					}				
				}
			} catch (Exception e) {
				LOG.error("Error creating dynamic rss cache for url " + feedUrl, e);
			}
		}
		// We'll always return cache here. If the cache already exists, return it. If another thread beat us to the punch and created the cache first, we'll use theirs.
		return cache;
	}

	public Rss getRss(String feedUrl, Integer updateIntervalSeconds) {
		DynamicRss dynamicRss = this.getOrCreateDynamicRss(feedUrl, updateIntervalSeconds);
		if (dynamicRss != null) {
			Rss rss = dynamicRss.getRss();
			if (rss != null) {
				return rss;
			}
		}
		return null;
	}
	
	public LinkFeed getLinkFeed(String url, Rss rss) {
		LinkFeed lf = this.cachedLinkFeeds.get(url);
		if (lf == null) {
			if (rss != null) {
				List<RssItem> rssItems = rss.getRssItems();
				for (RssItem rssItem : rssItems) {
					if (rssItem.getLink() != null) {
						if (url.trim().equals(rssItem.getLink().trim()) || url.trim().equals(rssItem.getLink().trim().replaceAll("newsinfo.iu.edu", "info.iu.edu"))) {
//							LOG.info("Test: Loading Matching RSS: " + url + " >> " + rss.getTitle() + " @ " + rss.getLink());
							lf = this.loadLinkFeed(url);
							break;
						}
					}
				}				
			} else {
				LOG.info("Did not pass RSS. Loading LinkFeed for URL: " + url);
				lf = this.loadLinkFeed(url);
			}
		}
		return lf;
	}
	
	private LinkFeed loadLinkFeed(String url) {
        boolean isIupuiNews = false;
        if (url.indexOf("http://newscenter.iupui.edu/") > -1) {
        	// IUPUI Newscenter at http://newscenter.iupui.edu/
        	isIupuiNews = true;
        }
        String modifiedUrl = this.getAdjustedUrl(url);
        
		LinkFeed lf = this.cachedLinkFeeds.get(modifiedUrl);
		if (lf == null) {
			lf = new LinkFeed(this.counter.getNext(), modifiedUrl);
		}
        
        if (isIupuiNews) {
        	IUPUINewsParser parser = new IUPUINewsParser();
        	lf.updateBodyText(parser.process(modifiedUrl));
        } else {
        	this.getRssService().parseLinkFeedFromUrl(lf);
        }
        
		// Only cache if we have some html
        if (lf.getBodyText() != null && lf.getBodyText().trim().length() > 0) {
        	this.cachedLinkFeeds.put(modifiedUrl, lf);
        }
        return lf;
	}
	
	private String getAdjustedUrl(String url) {
        if (url.indexOf("normal") > 0) {
        	// info.iu.edu
            url = url.replace("/normal/", "/rss/");
            url = url.replace(".html", ".rss?s=index&n=page");
        } else if (url.indexOf("http://newscenter.iupui.edu/") > -1) {
        	// IUPUI Newscenter at http://newscenter.iupui.edu/
        } else if (url.indexOf("http://uitsnews.iu.edu") > -1) {
        	//"?1=1&feed=mdot";	
//        	String uits = cacheService.findConfigParamValueByName("News.UITSQueryString");
        	String uits = configParamService.findValueByName(NEWS_UITS_QUERY_STRING);
        	if (!url.endsWith(uits)) {
        		url += uits;
        	}
        } else if (url.indexOf("http://www.iun.edu/") > -1 && url.endsWith(".shtml")) {
        	url = url.substring(0, url.length()-".shtml".length()) + ".xml";
        }
        return url;
	}

	public RssService getRssService() {
		return rssService;
	}

	public void setRssService(RssService rssService) {
		this.rssService = rssService;
	}
}
