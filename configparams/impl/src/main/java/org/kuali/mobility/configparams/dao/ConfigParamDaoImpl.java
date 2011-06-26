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
 
package org.kuali.mobility.configparams.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.kuali.mobility.configparams.entity.ConfigParam;
import org.springframework.stereotype.Repository;

@Repository
public class ConfigParamDaoImpl implements ConfigParamDao {

    @PersistenceContext
    private EntityManager entityManager;
    
    public void deleteConfigParamById(Long id) {
        Query query = entityManager.createQuery("delete from ConfigParam cp where cp.configParamId = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<ConfigParam> findAllConfigParam() {
        Query query = entityManager.createQuery("select cp from ConfigParam cp order by cp.name");
        return query.getResultList();
    }

    public ConfigParam findConfigParamById(Long id) {
        Query query = entityManager.createQuery("select cp from ConfigParam cp where cp.configParamId = :id");
        query.setParameter("id", id);
        return (ConfigParam) query.getSingleResult();
    }
    
    public ConfigParam findConfigParamByName(String name) {
        Query query = entityManager.createQuery("select cp from ConfigParam cp where cp.name like :name");
        query.setParameter("name", name);
        return (ConfigParam) query.getSingleResult();
    }

    public Long saveConfigParam(ConfigParam configParam) {
        if (configParam == null) {
            return null;
        }
        if (configParam.getName() != null) {
            configParam.setName(configParam.getName().trim());
        }
        if (configParam.getValue() != null) {
            configParam.setValue(configParam.getValue().trim());
        }
        try {
	        if (configParam.getConfigParamId() == null) {
	            entityManager.persist(configParam);
	        } else {
	            entityManager.merge(configParam);
	        }
        } catch (OptimisticLockException oe) {
            return null;
        }
        return configParam.getConfigParamId();
    }
    
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
}