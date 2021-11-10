package com.xyram.ticketingTool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.xyram.ticketingTool.baseData.model.AuditModel;

@Entity
@Table(name = "application_comments")
public class ApplicationComments extends AuditModel {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Size( max = 8)
	@Column(name="application_comment_id")
	private String Id;
	
	
	@Column(name = "application_id")
	private String applicationId;
	
	@Column(name="auto_description")
	private String autoDescription;
	
	@Column(name="given_description")
	private String givenDescription;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getAutoDescription() {
		return autoDescription;
	}

	public void setAutoDescription(String autoDescription) {
		this.autoDescription = autoDescription;
	}

	public String getGivenDescription() {
		return givenDescription;
	}

	public void setGivenDescription(String givenDescription) {
		this.givenDescription = givenDescription;
	}
	
	
}