
package com.xyram.ticketingTool.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xyram.ticketingTool.apiresponses.ApiResponse;
import com.xyram.ticketingTool.entity.Employee;
import com.xyram.ticketingTool.enumType.UserStatus;

public interface EmployeeService {

	ApiResponse addemployee(Employee employee);

	ApiResponse getAllEmployee(Pageable pageable);

	ApiResponse editEmployee(Integer employeeId, Employee employee);

	ApiResponse updateEmployeeStatus(int employeeID, UserStatus userstatus);

}
