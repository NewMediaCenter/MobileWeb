package edu.iu.m.news.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import edu.iu.m.news.entity.Rss;

public class RssDaoImpl implements RssDao {

	@PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Rss> findAllRss() {
        Query query = entityManager.createQuery("select r from Rss r");
        return query.getResultList();
    }

    public Rss findRssById(Long id) {
        Query query = entityManager.createQuery("select r from Rss r where r.rssId = :id");
        query.setParameter("id", id);
        return (Rss) query.getSingleResult();
    }

    public Rss findRssByUrl(String url) {
        Query query = entityManager.createQuery("select r from Rss r where r.url = :url");
        query.setParameter("url", url);
        return (Rss) query.getSingleResult();
    }
    
    public Rss findRssByMaintRssId(Long maintRssId) {
        Query query = entityManager.createQuery("select r from Rss r where r.rssMaintId = :rssMaintId");
        query.setParameter("rssMaintId", maintRssId);
        return (Rss) query.getSingleResult();
    }

    public void saveRss(Rss rss) {
        if (rss == null) {
            return;
        }
        if (rss.getRssId() == null) {
            entityManager.persist(rss);
        } else {
            entityManager.merge(rss);
        }
    }

    public void deleteRssById(Long id) {
    	this.deleteRss(this.findRssById(id));
    }
    
    public void deleteRss(Rss rss) {
    	if (rss != null) {
    		entityManager.remove(rss);	
    	}
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
