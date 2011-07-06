package edu.iu.m.news.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.iu.m.news.dao.MaintRssDao;
import edu.iu.m.news.entity.MaintRss;

public class MaintRssServiceImpl implements MaintRssService {

    //private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RssServiceImpl.class);

	@Autowired
    private MaintRssDao rssDao;

    public MaintRssDao getRssDao() {
        return rssDao;
    }

    public void setRssDao(MaintRssDao rssDao) {
        this.rssDao = rssDao;
    }

    public MaintRssServiceImpl() {}

    public MaintRss findRssById(Long rssId) {
        return rssDao.findRssById(rssId);
    }
    
    public MaintRss findRssByShortCode(String code) {
        return rssDao.findRssByShortCode(code);
    }

    public void saveRss(MaintRss rss) {
        rssDao.saveRss(rss);
    }

    public List<MaintRss> findAllRss() {
        return rssDao.findAllRss();
    }

    public void deleteRssById(Long id) {
        rssDao.deleteRssById(id);
    }

    public String findUrlByShortCode(String code) {
        return rssDao.findUrlByShortCode(code);
    }

    public void reorder(Long id, boolean up) {
        rssDao.reoder(id, up);
    }

    public List<MaintRss> findAllRssOrderBy(String order) {
        return rssDao.findAllRssOrderBy(order);
    }

    public List<MaintRss> findAllRss(String clause) {
        return rssDao.rssDaofindAllRss(clause);
    }
    
    public List<MaintRss> findAllNewsRssByCampusCode(String campusCode) {
    	return rssDao.findAllNewsRssByCampusCode(campusCode);
    }

}
