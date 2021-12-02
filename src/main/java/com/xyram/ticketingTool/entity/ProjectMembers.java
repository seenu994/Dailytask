package com.xyram.ticketingTool.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xyram.ticketingTool.baseData.model.AuditModel;
import com.xyram.ticketingTool.enumType.ProjectMembersStatus;
import com.xyram.ticketingTool.id.generator.IdGenerator;
import com.xyram.ticketingTool.id.generator.IdPrefix;

@Entity
@Table(name = "project_members")

public class ProjectMembers extends AuditModel {	

	@Id
	@IdPrefix(value = "PROM_")
	@GeneratedValue(generator = IdGenerator.ID_GENERATOR)
	@GenericGenerator(name = IdGenerator.ID_GENERATOR, strategy = "com.xyram.ticketingTool.id.generator.IdGenerator")
	@Column(name="projectMember_id")
	private String id;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "projectmemberstatus")
	private ProjectMembersStatus status = ProjectMembersStatus.ACTIVE;
//@JsonIgnore
//	@OneToOne(cascade = { CascadeType.MERGE })
//	@JoinColumn(name = "employee_id")
//	private  Employee employee;
//@JsonIgnore
//	@OneToOne(cascade = { CascadeType.MERGE })
//	@JoinColumn(name = "project_id")
//	private  Projects project;

	@Column(name = "employee_id")
	private String employeeId;
	
	@Column(name = "project_id")
	private  String projectId;
	
	@Column(name="is_admin")
	private String isAdmin;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		id = id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public ProjectMembersStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectMembersStatus status) {
		this.status = status;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	
	
	
//	public Employee getEmployee() {
//		return employee;
//	}
//
//	public void setEmployee(Employee employee) {
//		this.employee = employee;
//	}
//
//	public Projects getProject() {
//		return project;
//	}
//
//	public void setProject(Projects project) {
//		this.project = project;
//	}


}
	
	
	
	
