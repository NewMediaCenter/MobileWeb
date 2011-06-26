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

package org.kuali.mobility.user.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.kuali.mobility.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserDaoImpl() {}
    
    public User findUserByDeviceId(String deviceId) {
        Query query = entityManager.createQuery("select u from User u where u.deviceId = :deviceId");
        query.setParameter("deviceId", deviceId);
        try {
            return (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public User findUserByUserId(String userId) {
        Query query = entityManager.createQuery("select u from User u where u.userId = :userId");
        query.setParameter("userId", userId);
        try {
            return (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public User findUserByGuid(Long guid) {
        Query query = entityManager.createQuery("select u from User u where u.guid = :guid");
        query.setParameter("guid", guid);
        try {
            return (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void saveUser(User user) {
        if (user == null) {
            return;
        }
        if (user.getGuid() == null) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }
    
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

} 
