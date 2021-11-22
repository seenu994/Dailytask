package com.xyram.ticketingTool.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.stringtemplate.v4.compiler.CodeGenerator.list_return;

import com.xyram.ticketingTool.Repository.FeatureRepository;
import com.xyram.ticketingTool.Repository.ProjectFeatureRepository;
import com.xyram.ticketingTool.entity.Feature;
import com.xyram.ticketingTool.entity.ProjectFeature;
import com.xyram.ticketingTool.request.AssignFeatureRequest;
import com.xyram.ticketingTool.service.FeatureService;
import com.xyram.ticketingTool.service.ProjectFeatureService;

@Service
@Transactional
public class ProjectFeatureServiceImpl implements ProjectFeatureService {

	@Autowired
	ProjectFeatureRepository projectFeatureRepository;

	@Autowired
	FeatureService featureService;

	@Override
	public ProjectFeature addProjectFeature(ProjectFeature projectFeature) {

		return projectFeatureRepository.save(projectFeature);
	}

	@Override
	public List<Map> getAllfeatureByProject(String projectId) {
		return projectFeatureRepository.getfeatureAvailableByproject(projectId);
	}

	@Override
	public Map getFeatureByProjectAndFeatureId(String projectId, String featureId) {
		return projectFeatureRepository.getFeatureAvailable(featureId, projectId);
	}

	@Override
	public List<ProjectFeature> assignFeatureToProject(AssignFeatureRequest request)

	{
		List<ProjectFeature> projectFeatures = new ArrayList<ProjectFeature>();
		for (String id : request.getStoryStatusIds()) {

			Feature feature = featureService.getFeaturesByFeatureId(id);
			if (feature != null) {
				ProjectFeature projectFeature = new ProjectFeature();
				projectFeature.setFeatureId(feature.getFeatureId());
				projectFeature.setProjectId(request.getProjectId());
				projectFeature.setStatus("ACTIVE");
				if (projectFeatureRepository.hasFeatureAlreadyExist(request.getProjectId(), id) != null) {
					projectFeatures.add(projectFeature);
				}
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "feature id not found " + id);
			}

		}
		return projectFeatureRepository.saveAll(projectFeatures);
	}

	@Override
	public List<ProjectFeature> unAssignFeatureToProject(AssignFeatureRequest request)

	{
		List<ProjectFeature> projectFeatures = new ArrayList<ProjectFeature>();
		
		for (String id : request.getStoryStatusIds()) {

 			ProjectFeature projectFeature = projectFeatureRepository.hasFeatureAlreadyExist(request.getProjectId(), id);
			
			if(projectFeature!=null)
			{
				projectFeature.setStatus("INACTIVE");
				projectFeatures.add(projectFeature);

			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "feature id not found " + id);
			}

		}
		return projectFeatureRepository.saveAll(projectFeatures);
	}

}