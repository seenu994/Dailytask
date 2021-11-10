package com.xyram.ticketingTool.ticket.config;

import org.springframework.stereotype.Component;

@Component
public class PermissionConfig {
	
	public static final int EMPLOYEE_MODULE = 1;
	public static final int  PROJECT_MODULE = 2;
	public static final int  TICKET_MODULE = 3;
	public static final int  JOBOPENING_MODULE = 4;
	public static final int  JOBAPPLICATION_MODULE = 5;
	public static final int  JOBINTERVIEW_MODULE = 6;
	public static final int  JOBOFFER_MODULE = 7;
	public static final int  JOBVENDOR_MODULE = 8;
	
	private int NOTHING = 1;
	private int ADD = 2;
	private int VIEW_SELF = 4;
	private int VIEW_ALL = 8;
	private int EDIT_SELF = 16;
	private int EDIT_ALL = 32;
	private int DELETE_SELF = 64;
	private int DELETE_ALL = 128;
	private int STATUS_CHANGE = 256;
	private int ALL = 512;
	
	private int EMPLOYEES_PERMISSION = NOTHING;
	private int PROJECTS_PERMISSION = NOTHING;
	private int TICKETS_PERMISSION = NOTHING;
	private int JOBOPENINGS_PERMISSION = NOTHING;
	private int JOBAPPLICATIONS_PERMISSION = NOTHING;
	private int JOBINTERVIEWS_PERMISSION = NOTHING;
	private int JOBOFFERS_PERMISSION = NOTHING;
	private int JOBVENDORS_PERMISSION = NOTHING;
	
	
	
	
	public int getEMPLOYEES_PERMISSION() {
		return EMPLOYEES_PERMISSION;
	}

	public void setEMPLOYEES_PERMISSION(int eMPLOYEES_PERMISSION) {
		EMPLOYEES_PERMISSION = eMPLOYEES_PERMISSION;
	}

	public int getPROJECTS_PERMISSION() {
		return PROJECTS_PERMISSION;
	}

	public void setPROJECTS_PERMISSION(int pROJECTS_PERMISSION) {
		PROJECTS_PERMISSION = pROJECTS_PERMISSION;
	}

	public int getTICKETS_PERMISSION() {
		return TICKETS_PERMISSION;
	}

	public void setTICKETS_PERMISSION(int tICKETS_PERMISSION) {
		TICKETS_PERMISSION = tICKETS_PERMISSION;
	}

	public int getJOBOPENINGS_PERMISSION() {
		return JOBOPENINGS_PERMISSION;
	}

	public void setJOBOPENINGS_PERMISSION(int jOBOPENINGS_PERMISSION) {
		JOBOPENINGS_PERMISSION = jOBOPENINGS_PERMISSION;
	}

	public int getJOBAPPLICATIONS_PERMISSION() {
		return JOBAPPLICATIONS_PERMISSION;
	}

	public void setJOBAPPLICATIONS_PERMISSION(int jOBAPPLICATIONS_PERMISSION) {
		JOBAPPLICATIONS_PERMISSION = jOBAPPLICATIONS_PERMISSION;
	}

	public int getJOBINTERVIEWS_PERMISSION() {
		return JOBINTERVIEWS_PERMISSION;
	}

	public void setJOBINTERVIEWS_PERMISSION(int jOBINTERVIEWS_PERMISSION) {
		JOBINTERVIEWS_PERMISSION = jOBINTERVIEWS_PERMISSION;
	}

	public int getJOBOFFERS_PERMISSION() {
		return JOBOFFERS_PERMISSION;
	}

	public void setJOBOFFERS_PERMISSION(int jOBOFFERS_PERMISSION) {
		JOBOFFERS_PERMISSION = jOBOFFERS_PERMISSION;
	}

	public int getJOBVENDORS_PERMISSION() {
		return JOBVENDORS_PERMISSION;
	}

	public void setJOBVENDORS_PERMISSION(int jOBVENDORS_PERMISSION) {
		JOBVENDORS_PERMISSION = jOBVENDORS_PERMISSION;
	}

	public PermissionConfig() {
		// TODO Auto-generated constructor stub
	}
	
//	public PermissionConfig(Integer access) {
//		// TODO Auto-generated constructor stub
//		this.access = access;
//	}
//	
//	public void addPermissions(Integer permission[]) {
//		// TODO Auto-generated constructor stub
//		for(int i=0;i <permission.length;i++)
//			this.access |= permission[i];
//	}
//	
//	public void removePermissions(Integer permission[]) {
//		// TODO Auto-generated constructor stub
//		for(int i=0;i <permission.length;i++)
//			this.access &= ~permission[i];
//	}
	
	public Boolean hasPermission(int permission, int module) {
		// TODO Auto-generated constructor stub
		switch(module) {
			case EMPLOYEE_MODULE:
				return (this.EMPLOYEES_PERMISSION & permission) == permission;
			case PROJECT_MODULE:
				return (this.PROJECTS_PERMISSION & permission) == permission;
			case TICKET_MODULE:
				return (this.TICKETS_PERMISSION & permission) == permission;
			case JOBOPENING_MODULE:
				return (this.JOBOPENINGS_PERMISSION & permission) == permission;
			case JOBAPPLICATION_MODULE:
				return (this.JOBAPPLICATIONS_PERMISSION & permission) == permission;
			case JOBOFFER_MODULE:
				return (this.JOBOFFERS_PERMISSION & permission) == permission;
			case JOBINTERVIEW_MODULE:
				return (this.JOBINTERVIEWS_PERMISSION & permission) == permission;
			case JOBVENDOR_MODULE:
				return (this.JOBVENDORS_PERMISSION & permission) == permission;
		}
		return false;
		
	}
	
	public Integer setDefaultPermissions(String role) {
		
		Integer access = 0;
		switch(role) {
			case "SUPER_ADMIN":
				this.EMPLOYEES_PERMISSION = ALL;
				this.PROJECTS_PERMISSION = ALL;
				this.TICKETS_PERMISSION = ALL;
				this.JOBOPENINGS_PERMISSION = ALL;
				this.JOBAPPLICATIONS_PERMISSION = ALL;
				this.JOBINTERVIEWS_PERMISSION = ALL;
				this.JOBOFFERS_PERMISSION = ALL;
				this.JOBVENDORS_PERMISSION = ALL;
			break;
			case "TICKETINGTOOL_ADMIN":
				this.EMPLOYEES_PERMISSION = ALL;
				this.PROJECTS_PERMISSION = ALL;
				this.TICKETS_PERMISSION = ALL;
				this.JOBOPENINGS_PERMISSION = VIEW_ALL;
				this.JOBAPPLICATIONS_PERMISSION = (ADD | VIEW_SELF | EDIT_SELF | DELETE_SELF);
				this.JOBINTERVIEWS_PERMISSION = (VIEW_SELF | STATUS_CHANGE);
				this.JOBOFFERS_PERMISSION = NOTHING;
				this.JOBVENDORS_PERMISSION = NOTHING;
			break;
			case "INFRA":
				this.EMPLOYEES_PERMISSION = VIEW_ALL;
				this.PROJECTS_PERMISSION = VIEW_ALL;
				this.TICKETS_PERMISSION = (VIEW_SELF | STATUS_CHANGE);
				this.JOBOPENINGS_PERMISSION = VIEW_ALL;
				this.JOBAPPLICATIONS_PERMISSION = (ADD | VIEW_SELF | EDIT_SELF | DELETE_SELF);
				this.JOBINTERVIEWS_PERMISSION = (VIEW_SELF | STATUS_CHANGE);
				this.JOBOFFERS_PERMISSION = NOTHING;
				this.JOBVENDORS_PERMISSION = NOTHING;
			break;
			case "HR_ADMIN":
				this.EMPLOYEES_PERMISSION = VIEW_ALL;
				this.PROJECTS_PERMISSION = VIEW_ALL;
				this.TICKETS_PERMISSION = (ADD | VIEW_SELF | EDIT_SELF | DELETE_SELF | STATUS_CHANGE);
				this.JOBOPENINGS_PERMISSION = ALL;
				this.JOBAPPLICATIONS_PERMISSION = (ADD | VIEW_ALL | EDIT_SELF | DELETE_SELF | STATUS_CHANGE);
				this.JOBINTERVIEWS_PERMISSION = ALL;
				this.JOBOFFERS_PERMISSION = ALL;
				this.JOBVENDORS_PERMISSION = ALL;
			break;
			case "HR":
				this.EMPLOYEES_PERMISSION = VIEW_ALL;
				this.PROJECTS_PERMISSION = VIEW_ALL;
				this.TICKETS_PERMISSION = (ADD | VIEW_SELF | EDIT_SELF | DELETE_SELF | STATUS_CHANGE);
				this.JOBOPENINGS_PERMISSION = VIEW_ALL;
				this.JOBAPPLICATIONS_PERMISSION = (ADD | VIEW_ALL | EDIT_SELF | DELETE_SELF | STATUS_CHANGE);
				this.JOBINTERVIEWS_PERMISSION = (ADD | VIEW_ALL | EDIT_SELF | DELETE_SELF | STATUS_CHANGE);
				this.JOBOFFERS_PERMISSION = VIEW_ALL;
				this.JOBVENDORS_PERMISSION = VIEW_ALL;
			break;
			case "DEVELOPER":
				this.EMPLOYEES_PERMISSION = VIEW_ALL;
				this.PROJECTS_PERMISSION = VIEW_SELF;
				this.TICKETS_PERMISSION = (ADD | VIEW_SELF | EDIT_SELF | DELETE_SELF | STATUS_CHANGE);
				this.JOBOPENINGS_PERMISSION = VIEW_ALL;
				this.JOBAPPLICATIONS_PERMISSION = (ADD | VIEW_ALL | EDIT_SELF | DELETE_SELF | STATUS_CHANGE);
				this.JOBINTERVIEWS_PERMISSION = (VIEW_SELF | EDIT_SELF | DELETE_SELF | STATUS_CHANGE);
				this.JOBOFFERS_PERMISSION = NOTHING;
				this.JOBVENDORS_PERMISSION = NOTHING;
			break;
			case "JOB_VENDOR":
				this.EMPLOYEES_PERMISSION = NOTHING;
				this.PROJECTS_PERMISSION = NOTHING;
				this.TICKETS_PERMISSION = NOTHING;
				this.JOBOPENINGS_PERMISSION = VIEW_SELF;
				this.JOBAPPLICATIONS_PERMISSION = (ADD | VIEW_SELF | EDIT_SELF | DELETE_SELF | STATUS_CHANGE);
				this.JOBINTERVIEWS_PERMISSION = VIEW_SELF;
				this.JOBOFFERS_PERMISSION = VIEW_SELF;
				this.JOBVENDORS_PERMISSION = NOTHING;
			break;
		}
		
		return access;
	}
	
	

}