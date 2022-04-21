package com.xyram.ticketingTool.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import com.xyram.ticketingTool.baseData.model.AuditModel;
import com.xyram.ticketingTool.enumType.AssetIssueStatus;
import com.xyram.ticketingTool.id.generator.IdGenerator;
import com.xyram.ticketingTool.id.generator.IdPrefix;

@Entity
@Table(name="asset_issues")
public class AssetIssues extends AuditModel
{
	@Id
	@IdPrefix(value = "AI")
	@GeneratedValue(generator = IdGenerator.ID_GENERATOR)
	@GenericGenerator(name = IdGenerator.ID_GENERATOR, strategy = "com.xyram.ticketingTool.id.generator.IdGenerator")
    @Column(name="asset_issue_id")
    public String assetIssueId;
   
	@Column(name = "asset_id")
    private String assetId;
    
	@CreatedDate
    @Column(name="complaint_raised_date", nullable = false, updatable = false)
    public Date complaintRaisedDate;
    
    @Column(name="description")
    public String description;
    
    @Column(name="solution")
    public boolean solution;
    
    
    @Enumerated(EnumType.STRING)
    @Column(name="asset_issue_status")
    public AssetIssueStatus assetIssueStatus = AssetIssueStatus.OPEN;
    
    @Column(name = "vendor_id")
    private String vendorId;
    
    @Column(name="resolved_date")
    public Date resolvedDate;
    
    @Column(name = "comments")
    public String comments;

	public String getAssetIssueId() {
		return assetIssueId;
	}

	public void setAssetIssueId(String assetIssueId) {
		this.assetIssueId = assetIssueId;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public Date getComplaintRaisedDate() {
		return complaintRaisedDate;
	}

	public void setComplaintRaisedDate(Date complaintRaisedDate) {
		this.complaintRaisedDate = complaintRaisedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSolution() {
		return solution;
	}

	public void setSolution(boolean solution) {
		this.solution = solution;
	}

	public AssetIssueStatus getAssetIssueStatus() {
		return assetIssueStatus;
	}

	public void setAssetIssueStatus(AssetIssueStatus assetIssueStatus) {
		this.assetIssueStatus = assetIssueStatus;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public Date getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(Date resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
    
    
  
}