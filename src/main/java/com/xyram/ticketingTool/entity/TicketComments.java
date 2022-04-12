	package com.xyram.ticketingTool.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.xyram.ticketingTool.baseData.model.AuditModel;
import com.xyram.ticketingTool.enumType.TicketCommentsStatus;
import com.xyram.ticketingTool.id.generator.IdGenerator;
import com.xyram.ticketingTool.id.generator.IdPrefix;

@Entity
@Table(name = "ticket_comments")
//@TypeDefs({ @TypeDef(name = "StringJsonObject", typeClass = JSONObjectUserType.class) })
public class TicketComments extends AuditModel {

	
	@Id
	@IdPrefix(value = "TIC_")
	@GeneratedValue(generator = IdGenerator.ID_GENERATOR)
	@GenericGenerator(name = IdGenerator.ID_GENERATOR, strategy = "com.xyram.ticketingTool.id.generator.IdGenerator")
	@Column(name="ticketComments_id")
	private String Id;
	
	
	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "ticket_id")
	private Ticket ticket;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ticketCommentsStatus")
	private TicketCommentsStatus status = TicketCommentsStatus.ACTIVE;
	
	@Column(name="Description")
	private String ticketCommentDescription;


	public String getTicketCommentDescription() {
		return ticketCommentDescription;
	}


	public void setTicketCommentDescription(String ticketCommentDescription) {
		this.ticketCommentDescription = ticketCommentDescription;
	}





	public String getId() {
		return Id;
	}


	public void setId(String id) {
		Id = id;
	}


	public Ticket getTicket() {
		return ticket;
	}


	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}


	public TicketCommentsStatus getStatus() {
		return status;
	}


	public void setStatus(TicketCommentsStatus status) {
		this.status = status;
	}
	
}
	
