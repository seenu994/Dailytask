package com.xyram.ticketingTool.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xyram.ticketingTool.apiresponses.ApiResponse;
import com.xyram.ticketingTool.entity.CompanyLocation;
import com.xyram.ticketingTool.service.CompanyLocationService;
import com.xyram.ticketingTool.util.AuthConstants;

@RestController
public class CompanyLocationController {

	private final Logger logger = LoggerFactory.getLogger(CompanyLocationController.class);

	@Autowired
	CompanyLocationService companyLocationService;

	@PostMapping(value = { AuthConstants.ADMIN_BASEPATH + "/createLocation",
			AuthConstants.HR_ADMIN_BASEPATH + "/createLocation", AuthConstants.HR_BASEPATH + "/createLocation" })
	public ApiResponse createLocation(@RequestBody CompanyLocation location) {
		logger.info("Request for creating Location");
		return companyLocationService.createLocation(location);
	}

	/// updateLocation{/id}/location{/location}
	@PutMapping(value = { AuthConstants.ADMIN_BASEPATH + "/updateLocation/{id}",
			AuthConstants.HR_ADMIN_BASEPATH + "/updateLocation/{id}", AuthConstants.HR_BASEPATH + "/updateLocation/{id}" })
	public ApiResponse updateLocation(@PathVariable String id, @RequestBody CompanyLocation location) {
		logger.info("Request for updating Location");
		return companyLocationService.updateLocation(id, location);
	}

//	@DeleteMapping(value = { AuthConstants.ADMIN_BASEPATH, AuthConstants.HR_ADMIN_BASEPATH, AuthConstants.HR_BASEPATH })
//	public ApiResponse deleteLocation(@RequestParam String id) {
//		logger.info("Request for delete location");
//		return employeeLocationService.deleteLocation(id);
//	}
	
	@GetMapping(value = {AuthConstants.ADMIN_BASEPATH+ "/getAllLocation", AuthConstants.HR_ADMIN_BASEPATH+ "/getAllLocation", AuthConstants.HR_BASEPATH+ "/getAllLocation" })
	public ApiResponse getAllLocation(Map<String,Object> filter) {
		logger.info("Request for get all location");
		return companyLocationService.getAllLocation(filter);
	}
}