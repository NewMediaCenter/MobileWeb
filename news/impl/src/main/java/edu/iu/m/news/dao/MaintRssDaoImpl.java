package edu.iu.m.news.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import edu.iu.m.news.entity.MaintRss;

public class MaintRssDaoImpl implements MaintRssDao {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<MaintRss> findAllRss() {
        Query query = entityManager.createQuery("select r from MaintRss r order by r.order");
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<MaintRss> findAllRssOrderBy(String order) {
        Query query = entityManager.createQuery("select r from MaintRss r order by " + order);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<MaintRss> rssDaofindAllRss(String clause) {
        Query query = entityManager.createQuery("select r from MaintRss r " + clause);
        return query.getResultList();
    }
    
    public MaintRss findRssById(Long id) {
        Query query = entityManager.createQuery("select r from MaintRss r where r.rssId = :id");
        query.setParameter("id", id);
        return (MaintRss) query.getSingleResult();
    }

    public MaintRss findRssByUrl(String url) {
        Query query = entityManager.createQuery("select r from MaintRss r where r.rssId = :url");
        query.setParameter("url", url);
        return (MaintRss) query.getSingleResult();
    }
    
    public MaintRss findRssByShortCode(String code) {
        Query query = entityManager.createQuery("select r from MaintRss r where r.shortCode = :code");
        query.setParameter("code", code);
        return (MaintRss) query.getSingleResult();
    }
    
    @SuppressWarnings("unchecked")
	public List<MaintRss> findAllNewsRssByCampusCode(String campusCode) {
        Query query = entityManager.createQuery("select r from MaintRss r where r.type like 'NEWS' and (r.campus like :campusCode or r.campus like 'UA') order by r.order");
        query.setParameter("campusCode", campusCode);
        return query.getResultList();
    }

    public void saveRss(MaintRss rss) {
        if (rss == null) {
            return;
        }
        if (rss.getRssId() == null) {
            entityManager.persist(rss);
            rss.setOrder(rss.getRssId().intValue());
            entityManager.merge(rss);
        } else {
            entityManager.merge(rss);
        }
    }

    public void deleteRssById(Long id) {
        Query query = entityManager.createQuery("delete from MaintRss r where r.rssId = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public String findUrlByShortCode(String code) {
        Query query = entityManager.createQuery("select r from MaintRss r where r.shortCode = :code");
        query.setParameter("code", code);
        MaintRss rss = (MaintRss) query.getSingleResult();
        return rss.getUrl();
    }
    
    public void reoder(Long id, boolean up) {
        List<MaintRss> list = findAllRss();
        MaintRss last = null;
        boolean flag = false;
        int index = -1;
        int count = list.get(0).getOrder();
        for (MaintRss rss : list) {
            index++;
            if (rss.getRssId().equals(id)) {            
                if (up && last != null) {
                    swap(last, rss);
                    count = last.getOrder() + 1;
                    continue;
                } else if (!up) {
                    MaintRss next = list.get(index + 1);
                    swap(rss, next);
                    count = next.getOrder() + 1;
                    continue;                    
                }
                flag = true;
            }
            if (flag) {
                rss.setOrder(count);  
                entityManager.merge(rss);
            }
            count++;
            last = rss;
        }
    }

    private void swap(MaintRss one, MaintRss two) {
        int temp = one.getOrder();
        one.setOrder(two.getOrder());
        two.setOrder(temp);
        entityManager.merge(one);
        entityManager.merge(two);
    }
    
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
