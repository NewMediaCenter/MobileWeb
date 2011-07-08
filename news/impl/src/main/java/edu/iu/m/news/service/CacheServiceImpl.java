package edu.iu.m.news.service;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;

import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;

import edu.iu.m.news.entity.MaintRss;
import edu.iu.m.news.entity.Rss;

public class CacheServiceImpl implements CacheService {
    
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(CacheServiceImpl.class);
    private static final String CACHE_SRV = "CacheService";

    @Autowired
    private RssCacheService rssCacheService;
    
    @Autowired
    private DynamicRssCacheService dynamicRssCacheService;
    
    @Autowired
	private ConfigParamService configParamService;
	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
    
	public CacheServiceImpl() {
		startCache();
    }

    public void startCache() {
//        try {
//            reloadCacheDoWork();
//        } catch (Exception e) {
//            LOG.error("Error initializing cache.", e);
//        }
        
        Thread thread = new Thread(new CacheReloaderBackgroundReloaderCombined());
        thread.setDaemon(true);
        thread.start();
    }
    
    public synchronized void reloadCache() {
        CacheService cacheService = getCacheServiceKSB();
        cacheService.reloadCacheDoWork();
    }
    
    public synchronized void reloadCacheDoWork() {
        try {
        	LOG.info("loading rss cache");
        	this.loadRssCache();
        	LOG.info("finished loading rss cache");
        } catch (Exception e) {
        	LOG.error("Error loading rss cache", e);
        }
    }
    
    private void loadRssCache() {
    	// The isSavingServer boolean should only be sent as true if you want to save to the database on the load. We don't want to do this on startup.
    	this.rssCacheService.load(false);
    }
    
    private CacheService getCacheServiceKSB() {
        CacheService cacheService = (CacheService) KSBServiceLocator.getMessageHelper().getServiceAsynchronously(new QName("KME", CACHE_SRV));
        return cacheService;
    }

	public void updateMaintRss(MaintRss maintRss) {
		LOG.info("Updating MaintRss: " + maintRss.getRssId());
		CacheService cacheService = getCacheServiceKSB();
		cacheService.updateMaintRssDoWork(maintRss.getRssId());
	}
	
	public void updateMaintRssDoWork(Long maintRssId) {
		this.getRssCacheService().updateMaintRssDoWork(maintRssId);
	}
	
	public void updateRss(Rss rss) {
		CacheService cacheService = getCacheServiceKSB();
		cacheService.updateRssDoWork(rss.getRssId());
	}
	
	public void updateRssDoWork(Long rssId) {
		this.getRssCacheService().updateRssDoWork(rssId);
	}
	
	public void deleteMaintRss(MaintRss maintRss) {
		CacheService cacheService = getCacheServiceKSB();
		cacheService.deleteMaintRssDoWork(maintRss.getRssId(), maintRss.getShortCode(), maintRss.getCampus());
	}
	
	public void deleteMaintRssDoWork(Long maintRssId, String maintRssShortCode, String campusCode) {
		this.getRssCacheService().deleteMaintRssDoWork(maintRssId, maintRssShortCode, campusCode);
	}
	
    // Injections
    
    public RssCacheService getRssCacheService() {
		return rssCacheService;
	}

	public void setRssCacheService(RssCacheService rssCacheService) {
		this.rssCacheService = rssCacheService;
	}
	
	private void rssReload() {
    	List<MaintRss> maintRssNotify = this.getRssCacheService().reload(true);
    	for (MaintRss maintRss : maintRssNotify) {
    		if (maintRss != null && maintRss.getRssId() != null) {
    			this.updateMaintRss(maintRss);	
    		} else {
    			LOG.info("Problem in RSS reload. MaintRss null or has null Id.");
    		}
    	}
	}
	
	private void dynamicRssReload() {
    	this.dynamicRssCacheService.reload();
	}
	
    private class CacheReloaderBackgroundReloaderCombined implements Runnable {
        
        private Long getReloadCacheMinutes() {
            Long reloadTime = new Long(1);
            try {
                String configParam = configParamService.findValueByName("Core.ReloadCacheMinutes");
                if (configParam != null && !"".equals(configParam)) {
                    reloadTime = new Long(configParam);
                }
            } catch (Exception e) {
                LOG.error("Config Param: Core.ReloadCacheMinutes must exists and be a number. Using: " + reloadTime, e);
            }
            return reloadTime;
        }

        private boolean shouldRunSavingReloads() {
            boolean runSavingReloads = false;
            try {
            	LOG.info("Host: " + InetAddress.getLocalHost().getHostName());
                if (InetAddress.getLocalHost().getHostName().equals(configParamService.findValueByName("Core.Rss.Server"))) {
                    runSavingReloads = true;
                }
            } catch (Exception e) {
                LOG.error("Error starting reloader thread.  Starting reloader on all servers", e);
                runSavingReloads = true;
            }
            return runSavingReloads;
        }
        
        private Long getStartupDelay() {
            Long startupDelay = new Long(5);
            try {
                String configParam = configParamService.findValueByName("Core.CacheRSSXmlRemoteStartupDelayMinutes");
                if (configParam != null && !"".equals(configParam)) {
                    startupDelay = new Long(configParam);
                }
            } catch (Exception e) {
                LOG.error("Config Param: Core.CacheRSSXmlRemoteStartupDelayMinutes must exists and be a number. Defaulting to 5 minutes.", e);
            }
            return startupDelay;
        }
        
        public void run() {
            Long myReloadTime = getReloadCacheMinutes();
            Calendar updateCalendar = Calendar.getInstance();
            updateCalendar.add(Calendar.MINUTE, myReloadTime.intValue());
            Date nextCacheUpdate = new Date(updateCalendar.getTimeInMillis());

            boolean runSavingReloads = shouldRunSavingReloads();
            Long startupDelay = getStartupDelay();
            Calendar startupTime = Calendar.getInstance();
            // Cache loop
            while (true) {
                Date now = new Date();
                if (now.after(nextCacheUpdate)) {
                    updateCalendar.add(Calendar.MINUTE, myReloadTime.intValue());
                    nextCacheUpdate = new Date(updateCalendar.getTimeInMillis());
                    LOG.info("Calling reloadCacheDoWork(false): Now: " + now.toString() + "  nextCacheUpdate:" + nextCacheUpdate.toString());
                    try {
                    	reloadCacheDoWork();	
                    } catch (Exception e) {
                    	LOG.error("Error reloading cache.", e);
                    }
                } else {
                    if (runSavingReloads) {
                        if ((startupTime.getTimeInMillis() + 1000 * 60 * startupDelay) < System.currentTimeMillis()) {
                            try {
                                rssReload();
                            } catch (Exception e) {
                                LOG.error("Error:", e);
                            }
                        } else {
                            try {
                                String configParam = configParamService.findValueByName("Core.CacheRSSXmlRemoteStartupDelayMinutes");
                                if (configParam != null && !"".equals(configParam)) {
                                    startupDelay = new Long(configParam);
                                }
                            } catch (Exception e) {
                                LOG.error("Error changing Config Param: Core.CacheRSSXmlRemoteStartupDelayMinutes must exists and be a number. Using: " + startupDelay, e);
                            }
                            Long startin = new Long(1 + ((startupTime.getTimeInMillis() + 1000 * 60 * startupDelay - System.currentTimeMillis()) / 1000 / 60));
                            LOG.info("RSS Reload will start in " + startin + " minute(s).");
                        }
                    }

                    if ((startupTime.getTimeInMillis() + 1000 * 60 * startupDelay) < System.currentTimeMillis()) {
		                try {
		                    dynamicRssReload();
		                } catch (Exception e) {
		                    LOG.error("Error:", e);
		                }
                    }
                }
                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException e) {
                    LOG.error("Error:", e);
                }
                Long reloadCacheMinutes = getReloadCacheMinutes();
                LOG.info("Awake! cacheMinutes: " + reloadCacheMinutes.intValue() + " reloadTime: " + myReloadTime.intValue());
                if (reloadCacheMinutes.intValue() != myReloadTime.intValue()) {
                    now = new Date();
                    LOG.info("updateTimes: " + now.toString());
                    // A change has been made to the cache's reload time. Adjust the calendar.
                    updateCalendar.add(Calendar.MINUTE, -myReloadTime.intValue());
                    updateCalendar.add(Calendar.MINUTE, reloadCacheMinutes.intValue());
                    // If the calendar's new reload time is before now, just set it to a millisecond ago so it'll reload ASAP.
                    Date newUpdateTime = new Date(updateCalendar.getTimeInMillis());
                    if (newUpdateTime.before(now)) {
                        LOG.info("BEFORE Now: " + now.toString() + "  newUpdateTime:" + newUpdateTime.toString());
                        updateCalendar.setTime(now);
                        updateCalendar.add(Calendar.MILLISECOND, -1);
                        nextCacheUpdate = new Date(updateCalendar.getTimeInMillis());
                    } else {
                        LOG.info("AFTER Now: " + now.toString() + "  newUpdateTime:" + newUpdateTime.toString());
                        nextCacheUpdate = new Date(updateCalendar.getTimeInMillis());
                    }
                    myReloadTime = reloadCacheMinutes;
                }
            }
        }
        
    }

	public DynamicRssCacheService getDynamicRssCacheService() {
		return dynamicRssCacheService;
	}

	public void setDynamicRssCacheService(DynamicRssCacheService dynamicRssCacheService) {
		this.dynamicRssCacheService = dynamicRssCacheService;
	}

}
