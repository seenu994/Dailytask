package com.xyram.ticketingTool.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xyram.ticketingTool.apiresponses.ApiResponse;
import com.xyram.ticketingTool.apiresponses.IssueTrackerResponse;
import com.xyram.ticketingTool.entity.Employee;
import com.xyram.ticketingTool.entity.Projects;
import com.xyram.ticketingTool.enumType.ProjectStatus;

public interface ProjectService {

	ApiResponse addproject(Projects project);

	ApiResponse getAllProjects(Pageable pageable);
	
	ApiResponse getAllProjectsForTickets(String serachString);

	ApiResponse editEmployee(Projects projectRequest);

	ApiResponse getAllProjectsByDeveloper(Pageable pageable);
	

	IssueTrackerResponse findById(String id);

	ApiResponse searchProject(String searchString);

	Projects getprojectById(String projectId);

	ApiResponse getgenericIssues();

	ApiResponse getAllProjectList();

	ApiResponse updateProjectStatus(String projectId, ProjectStatus projectStatus); 


}
