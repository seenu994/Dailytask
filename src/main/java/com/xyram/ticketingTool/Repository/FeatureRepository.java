package com.xyram.ticketingTool.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xyram.ticketingTool.entity.Feature;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, String> {

	
	
	@Query("select f from Feature f where f.featureType='Default' ")
	public List<Feature> getAllDefaultFeatures();

	@Query("select f from Feature f where f.featureId=:featureId")
	public Feature getFeaturesByFeatureId(String featureId);
	
	
	
	
	
}