package com.xyram.ticketingTool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.xyram.ticketingTool.baseData.model.AuditModel;
import com.xyram.ticketingTool.id.generator.IdGenerator;
import com.xyram.ticketingTool.id.generator.IdPrefix;


@Entity
@Table(name = "country")
public class Country extends AuditModel{ 

	
	@Id
	@IdPrefix(value = "CTY")
	@GeneratedValue(generator = IdGenerator.ID_GENERATOR)
	@GenericGenerator(name = IdGenerator.ID_GENERATOR, strategy = "com.xyram.ticketingTool.id.generator.IdGenerator")
	@Column(name = "country_id")
	private String countryId;
	
	@Column(name =  "country_name")
	private String countryName;
	
	@Column(name = "country_code" ,unique = true)
	private String countryCode;
	 
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Column(name = "country_status")
	private boolean countryStatus;

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public boolean isCountryStatus() {
		return countryStatus;
	}

	public void setCountryStatus(boolean countryStatus) {
		this.countryStatus = countryStatus;
	}

	
}