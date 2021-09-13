
package com.xyram.ticketingTool.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.xyram.ticketingTool.Repository.ProjectRepository;
import com.xyram.ticketingTool.apiresponses.ApiResponse;
import com.xyram.ticketingTool.entity.Projects;
import com.xyram.ticketingTool.exception.ResourceNotFoundException;
import com.xyram.ticketingTool.request.CurrentUser;
import com.xyram.ticketingTool.service.ProjectService;
import com.xyram.ticketingTool.util.ResponseMessages;

/**
 * 
 * @author sahana.neelappa
 *
 */

@Service

@Transactional
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	CurrentUser userDetail;

	@Override
	public ApiResponse addproject(Projects project) {		
		ApiResponse response = validateClientId(project);
		if (response.isSuccess()) { 
			//projectRepository.save(project);
			String inHouse = project.getInHouse();
			if (!inHouse.equals("") && !inHouse.equals("null")) {
				if (!inHouse.equalsIgnoreCase("Yes") && !inHouse.equalsIgnoreCase("NO")) 
					project.setInHouse("No");
			} else
				project.setInHouse("No");
				
			project.setCreatedAt(new Date());
			project.setCreatedBy(userDetail.getUserId());
			project.setLastUpdatedAt(new Date());
			project.setUpdatedBy(userDetail.getUserId());
			//System.out.println("project.getCreatedBy - " + project.getCreatedBy());
			//System.out.println("userDetail.getUserId() - " + userDetail.getUserId());
			Projects projetAdded = projectRepository.save(project);
			
			response.setSuccess(true);
			response.setMessage(ResponseMessages.PROJECT_ADDED);
			Map<String, String> content = new HashMap<String, String>();
			content.put("projectId", projetAdded.getpId());
			response.setContent(content);
			return response;
		}
		return response;
	}

	private ApiResponse validateClientId(Projects projects) {
		ApiResponse response = new ApiResponse(false);
		if (projects.getClientId()!= null) {
			response.setMessage("success");
			response.setSuccess(true);
			response.setContent(null);
		} else {
			response.setMessage(ResponseMessages.ClIENT_ID_VALID);
			response.setSuccess(false);
			response.setContent(null);
		}
		return response;
	}

	@Override
	public ApiResponse getAllProjects(Pageable pageable) {
		//Page<Map> projectList =   projectRepository.getAllProjectLsit(pageable);
		List<Map> projectList = projectRepository.getAllProjectsList();
		Map content = new HashMap();
	    content.put("projectList", projectList);
	    ApiResponse response = new ApiResponse(true);
	    response.setSuccess(true);
	    response.setContent(content);
	    return  response;
	}
	
	
	public Projects getProjectById(String projectId) {
		
		return  projectRepository.findById(projectId).map(project->{
			
			return project;
		}).orElseThrow(() -> new ResourceNotFoundException("project not found for id: " + projectId));
	}
	
	
	@Override
	public ApiResponse editEmployee( Projects projectRequest) {

		ApiResponse response = validateClientIdProjectId(projectRequest);
		if (response.isSuccess()) {
		
			if (projectRequest != null) { 
				projectRequest.setpId(projectRequest.getpId());
				projectRequest.setUpdatedBy(projectRequest.getUpdatedBy());
				projectRequest.setLastUpdatedAt(new Date());
				projectRequest.setInHouse(projectRequest.getInHouse());
				projectRequest.setProjectDescritpion(projectRequest.getProjectDescritpion());
				projectRequest.setProjectName(projectRequest.getProjectName());
				projectRequest.setStatus(projectRequest.getStatus()); 
				//projectRepository.save(projectRequest);			
				Projects projetAdded = projectRepository.save(projectRequest);

				response.setSuccess(true);
				response.setMessage(ResponseMessages.PROJECT_EDIT);
				Map content = new HashMap();
				content.put("projectId", projetAdded.getpId());
				response.setContent(content);
			}
			else {
				response.setSuccess(false);
				response.setMessage(ResponseMessages.PROJECT_ID_VALID);
				response.setContent(null);
			}
		}
		return response;
	}
	public ApiResponse validateClientIdProjectId(Projects projects) {
		ApiResponse response = new ApiResponse(false);
		if (projects.getClientId()== null ) {
			/*response.setMessage("success");
			response.setSuccess(true);
			response.setContent(null);*/
		
			response.setMessage(ResponseMessages.ClIENT_ID_VALID );
			response.setSuccess(false);
			response.setContent(null);
		}
	
		System.out.println("projects.getClientId() " + projects.getClientId());
		System.out.println("projects.getpId() " + projects.getpId());
		Projects projectid=projectRepository.getById(projects.getpId());
		if ( projectid==null ) {
			/*
			 * response.setMessage("success"); response.setSuccess(true);
			 * response.setContent(null); } else {
			 */
			response.setMessage(ResponseMessages.PROJECT_ID_VALID );
			response.setSuccess(false);
			response.setContent(null);
		} 
		else {
			response.setMessage(ResponseMessages.PROJECT_EDIT);
			response.setSuccess(true);
		}
		return response;
	}
	
	private ApiResponse validatePatientId(Projects projects) {
		ApiResponse response = new ApiResponse(false);
		if (projects.getpId()== null) {
			response.setMessage("success");
			response.setSuccess(true);
			response.setContent(null);
		} else {
			response.setMessage(ResponseMessages.PROJECT_ID_VALID);
			response.setSuccess(false);
			response.setContent(null);
		}
		return response;
	}

		
}