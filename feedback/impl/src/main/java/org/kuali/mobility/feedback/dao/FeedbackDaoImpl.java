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

package org.kuali.mobility.feedback.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.kuali.mobility.feedback.entity.Feedback;
import org.springframework.stereotype.Repository;

@Repository
public class FeedbackDaoImpl implements FeedbackDao {

    @PersistenceContext
    private EntityManager entityManager;

    public FeedbackDaoImpl() {}
    
    public void saveFeedback(Feedback feedback) {
        if (feedback == null) {
            return;
        }
        if (feedback.getFeedbackId() == null) {
            entityManager.persist(feedback);
        } else {
            entityManager.merge(feedback);
        }
    }
    
//    public void deleteFeedbackById(Long id) {
//        Query query = entityManager.createQuery("delete from Feedback f where feedbackId = :id");
//        query.setParameter("id", id);
//        query.executeUpdate();
//    }
//    
//    public Feedback findFeedbackById(Long id) {
//        Query query = entityManager.createQuery("select f from Feedback f where feedbackId = :id");
//        query.setParameter("id", id);
//        return (Feedback) query.getSingleResult();
//    }
//    
//    @SuppressWarnings("unchecked")
//    public List<Feedback> findAllFeedback() {
//        Query query = entityManager.createQuery("select f from Feedback f order by f.feedbackId");
//        return query.getResultList();
//    }
//    
//    @SuppressWarnings("unchecked")
//    public List<Feedback> findAllFeedbackByService(String service) {
//        Query query = entityManager.createQuery("select f from Feedback f where service = :service order by f.feedbackId");
//        query.setParameter("service", service);
//        return query.getResultList();
//    }
        
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
} 
