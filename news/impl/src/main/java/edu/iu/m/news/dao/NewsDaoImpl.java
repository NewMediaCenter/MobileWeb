/**
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package edu.iu.m.news.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.kuali.mobility.news.dao.NewsDao;
import org.kuali.mobility.news.entity.NewsSource;
import org.springframework.transaction.annotation.Transactional;

import edu.iu.m.news.entity.MaintRss;

public class NewsDaoImpl implements NewsDao {

    @PersistenceContext
    private EntityManager entityManager;
  
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

	@Override
	@Transactional
	public List<NewsSource> getAllActiveNewsSourcesByLocationCode(String locationCode) {
		Query query = entityManager.createQuery("select r from MaintRss r where r.active = 1 and r.type like 'NEWS' and (r.campus like :campusCode or r.campus like 'UA') order by r.order");
        query.setParameter("campusCode", locationCode);
        @SuppressWarnings("unchecked")
		List<MaintRss> maintRss =  query.getResultList();
        List<NewsSource> sources = new ArrayList<NewsSource>();
        for (MaintRss rss : maintRss) {
        	sources.add(convertMaintRssToNewsSource(rss));
        }
        return sources;
	}

	@Override
	public NewsSource getNewsSourceById(String rssShortCode) {
		Query query = entityManager.createQuery("select r from MaintRss r where r.shortCode like :rssShortCode and r.type like 'NEWS'");
		query.setParameter("rssShortCode", rssShortCode);
		try {
            return convertMaintRssToNewsSource((MaintRss)query.getSingleResult());
        } catch (NoResultException e) {
            return null;
        }
	}
	
	private NewsSource convertMaintRssToNewsSource(MaintRss rss) {
		NewsSource source = new NewsSource();
    	source.setLocationCode(rss.getCampus());
    	source.setSourceId(rss.getShortCode());
    	source.setSourceName(rss.getDisplayName());
    	source.setSourceUrl(rss.getUrl());
    	return source;
	}
}
