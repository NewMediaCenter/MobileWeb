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

package org.kuali.mobility.emergencyinfo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.kuali.mobility.emergencyinfo.entity.EmergencyInfo;
import org.springframework.stereotype.Repository;

@Repository
public class EmergencyInfoDaoImpl implements EmergencyInfoDao {

    @PersistenceContext
    private EntityManager entityManager;
    
    @SuppressWarnings("unchecked")
    public List<EmergencyInfo> findAllEmergencyInfo() {
        Query query = entityManager.createQuery("select ei from MaintEmergencyInfo ei order by ei.order");
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public EmergencyInfo findEmergencyInfoById(Long id) {
        Query query = entityManager.createQuery("select ei from MaintEmergencyInfo ei where ei.emergencyInfoId = :id");
        query.setParameter("id", id);
        try {
            return (EmergencyInfo) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<EmergencyInfo> findAllEmergencyInfoByCampus(String campus) {
        try {
            Query query = entityManager.createQuery("select ei from MaintEmergencyInfo ei where ei.campus like :campus order by ei.order");
            query.setParameter("campus", campus);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public Long saveEmergencyInfo(EmergencyInfo emergencyInfo) {
        if (emergencyInfo == null) {
            return null;
        }
        try {
            if (emergencyInfo.getEmergencyInfoId() == null) {
                entityManager.persist(emergencyInfo);
            } else {
                entityManager.merge(emergencyInfo);
            }
        } catch (OptimisticLockException oe) {
            return null;
        }
        return emergencyInfo.getEmergencyInfoId();
    }

    public void deleteEmergencyInfoById(Long id) {
        Query query = entityManager.createQuery("delete from MaintEmergencyInfo ei where ei.emergencyInfoId = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public void reorder(Long id, boolean up) {
        List<EmergencyInfo> list = findAllEmergencyInfo();
        EmergencyInfo last = null;
        boolean flag = false;
        int index = -1;
        int count = list.get(0).getOrder();
        for (EmergencyInfo emergencyInfo : list) {
            index++;
            if (emergencyInfo.getEmergencyInfoId().equals(id)) {            
                if (up && last != null) {
                    swap(last, emergencyInfo);
                    count = last.getOrder() + 1;
                    continue;
                } else if (!up) {
                    EmergencyInfo next = list.get(index + 1);
                    swap(emergencyInfo, next);
                    count = next.getOrder() + 1;
                    continue;                    
                }
                flag = true;
            }
            if (flag) {
                emergencyInfo.setOrder(count);  
                entityManager.merge(emergencyInfo);
            }
            count++;
            last = emergencyInfo;
        }
    }

    private void swap(EmergencyInfo one, EmergencyInfo two) {
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
