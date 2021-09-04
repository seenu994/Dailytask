package com.xyram.ticketingTool.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyram.ticketingTool.Repository.EmployeeRepository;
import com.xyram.ticketingTool.Repository.TicketAssignRepository;
import com.xyram.ticketingTool.Repository.TicketRepository;
import com.xyram.ticketingTool.entity.Employee;
import com.xyram.ticketingTool.entity.Ticket;
import com.xyram.ticketingTool.entity.TicketAssignee;
import com.xyram.ticketingTool.enumType.TicketAssigneeStatus;
import com.xyram.ticketingTool.enumType.TicketStatus;
import com.xyram.ticketingTool.exception.ResourceNotFoundException;
import com.xyram.ticketingTool.service.TicketAssignService;

/**
 * 
 * @author sahana.neelappa
 *
 */

@Service
@Transactional
public class TicketAssignServiceImpl implements TicketAssignService {

	@Autowired
	TicketAssignRepository ticketAssignRepository;
	@Autowired
	TicketRepository ticketRepository;
	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public Map<String,String> assignTicketToInfraTeam(Map<String, Object> requestMap) {
		TicketAssignee ticketAssignee = new TicketAssignee();
		Map<String,String> responseMAp=new HashMap<>();
		if (requestMap.containsKey("employeeId") && requestMap.containsKey("ticketId")) {
			Employee employee = employeeRepository.getById((Integer) requestMap.get("employeeId"));
			List<Integer> ticketIds=(List<Integer>) requestMap.get("ticketId");
			if(ticketIds!=null && ticketIds.size()>0)
			{
				for (Integer tId : ticketIds) {
					
			
		
			Ticket ticket = ticketRepository.getById(tId);
			
			if(ticket.getStatus().equals(TicketStatus.INITIATED)) {
			if(employee.getRole().getRoleName().equalsIgnoreCase("Infra User"))
			{

			ticketAssignee.setEmployee(employee);
			
			ticketAssignee.setTicket(ticket);
			//ticketAssignee.setTicket(ticketRepository.getById(requestMap.get("ticketId")));
			ticketAssignee.setStatus(TicketAssigneeStatus.ACTIVE);
			ticketAssignee.setCreatedAt(new Date());
			 ticketAssignRepository.save(ticketAssignee);
			}else {
				throw new ResourceNotFoundException("cannot assign to " +employee.getRole().getRoleName()+  " role ");
			}
			}else {
				throw new ResourceNotFoundException("ticket is not initiated");
			}
			}
				responseMAp.put("message","Assigning Tickets to employee");
				return responseMAp;
			}else {
				throw new ResourceNotFoundException("ticket list is empty");
			}
		} else {
			if (requestMap.containsKey("employeId")) {
				throw new ResourceNotFoundException("provide the EmployeeId to assign ticket for employee");
			} else {
				throw new ResourceNotFoundException("provide the ticketId to assign ticket to employee");
			}
		}
		

	}

}