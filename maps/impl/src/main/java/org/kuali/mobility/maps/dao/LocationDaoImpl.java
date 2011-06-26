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
 
package org.kuali.mobility.maps.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.kuali.mobility.maps.entity.Location;
import org.kuali.mobility.maps.entity.MapsGroup;
import org.springframework.stereotype.Repository;

@Repository
public class LocationDaoImpl implements LocationDao {

    @PersistenceContext
    private EntityManager entityManager;
	
	public void deleteLocationById(Long locationId) {
//        Query query = entityManager.createQuery("delete from Location loc where loc.locationId = :id");
//        query.setParameter("id", locationId);
//        query.executeUpdate();
		this.deleteLocation(this.findLocationById(locationId));
	}

	public Location findLocationById(Long locationId) {
        Query query = entityManager.createQuery("select loc from Location loc where loc.locationId = :id");
        query.setParameter("id", locationId);
        return (Location) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Location> getAllLocations() {
        Query query = entityManager.createQuery("select loc from Location loc order by loc.locationId");
        return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Location> getAllActiveLocations() {
        Query query = entityManager.createQuery("select loc from Location loc where loc.active = :active order by loc.locationId");
        query.setParameter("active", true);
        return query.getResultList();
	}
	
//	@SuppressWarnings("unchecked")
//	public List<Location> findLocationsByCampusCode(String campusCode) {
//        Query query = entityManager.createQuery("select loc from Location loc where loc.campus = :campus order by loc.locationId");
//        query.setParameter("campus", campusCode);
//        return query.getResultList();
//	}
	
	public List<Location> findLocationsByCode(String code) {
		try {
	        Query query = entityManager.createQuery("select loc from Location loc where loc.code = :code order by loc.locationId");
	        query.setParameter("code", code);
	        return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Location> getLocationsByBuildingCode(String buildingCode) {
        Query query = entityManager.createQuery("select loc from Location loc where loc.buildingCode = :buildingCode order by loc.locationId");
        query.setParameter("buildingCode", buildingCode);
        return query.getResultList();		
	}
	
	public List<Location> searchLocations(String search) {
		// TODO: Switched away from Hibernate temporarily. Plan out our persistence.
      Query query = entityManager.createQuery("select loc from Location loc where loc.code like :search or loc.name like :search or loc.shortName like :search order by loc.locationId");
      query.setParameter("search", "%" + search + "%");
      return query.getResultList();
		/*
        Session session = (Session) getEntityManager().getDelegate();
        Criteria crit = session.createCriteria(Location.class);
        // If this is not set in a hibernate many-to-many criteria query, we'll get multiple results of the same object
        crit.setResultTransformer(Criteria.ROOT_ENTITY);
        Criterion name = Restrictions.ilike("name", "%" + search + "%");
        Criterion shortName = Restrictions.ilike("shortName", "%" + search + "%");
        Criterion code = Restrictions.ilike("code", "%" + search + "%");
        Criterion shortCode = Restrictions.ilike("shortCode", "%" + search + "%");
        LogicalExpression orExpName = Restrictions.or(name,shortName);
        LogicalExpression orExpCode = Restrictions.or(code,shortCode);
        LogicalExpression orExpNameCode = Restrictions.or(orExpName,orExpCode);
        crit.add(orExpNameCode);
        return crit.list();
        */
	}
	
	public List<Location> getAllUngroupedLocations() {
        Query query = entityManager.createQuery("select loc from Location loc where loc.mapsGroups is empty");
        return query.getResultList();
//		return null;
	}
	
	public void saveLocation(Location location) {
		if (location == null) {
			return;
		}
        if (location.getLocationId() == null) {
            entityManager.persist(location);
        } else {
            entityManager.merge(location);
        }
	}
	
	private void deleteLocation(Location location) {
		if (location == null) {
			return;
		}
        if (location.getLocationId() != null) {
            entityManager.remove(location);
        }
	}
	
	public List<MapsGroup> getAllMapsGroups() {
        Query query = entityManager.createQuery("select group from MapsGroup group order by group.groupId");
        return query.getResultList();
	}
	
	public List<MapsGroup> getAllActiveMapsGroups() {
        Query query = entityManager.createQuery("select group from MapsGroup group where group.active = :active order by group.groupId");
        query.setParameter("active", true);
        return query.getResultList();		
	}
	
	public MapsGroup getMapsGroupById(Long groupId) {
        Query query = entityManager.createQuery("select group from MapsGroup group where group.groupId = :id");
        query.setParameter("id", groupId);
        return (MapsGroup) query.getSingleResult();
	}
	
	public MapsGroup getMapsGroupByCode(String groupCode) {
		try {
	        Query query = entityManager.createQuery("select group from MapsGroup group where group.groupCode = :code");
	        query.setParameter("code", groupCode);
	        return (MapsGroup) query.getSingleResult();			
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public void saveMapsGroup(MapsGroup group) {
		if (group == null) {
			return;
		}
        if (group.getGroupId() == null) {
            entityManager.persist(group);
        } else {
            entityManager.merge(group);
        }
	}
	
	public void deleteMapsGroup(Long groupId) {
//        Query query = entityManager.createQuery("delete from MapsGroup group where group.groupId = :id");
//        query.setParameter("id", groupId);
//        query.executeUpdate();
		this.deleteMapsGroup(this.getMapsGroupById(groupId));
	}
	
	private void deleteMapsGroup(MapsGroup mapsGroup) {
		if (mapsGroup == null) {
			return;
		}
        if (mapsGroup.getGroupId() != null) {
        	for (MapsGroup mapsGroupChild : mapsGroup.getMapsGroupChildren()) {
        		mapsGroupChild.setMapsGroupParent(null);
        		entityManager.merge(mapsGroupChild);
        	}
            entityManager.remove(mapsGroup);
        }
	}
	
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
