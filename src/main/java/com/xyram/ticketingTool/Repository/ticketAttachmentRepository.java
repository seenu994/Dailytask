package com.xyram.ticketingTool.Repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import com.xyram.ticketingTool.entity.Ticket;
import com.xyram.ticketingTool.entity.TicketAttachment;

public interface ticketAttachmentRepository extends JpaRepository<TicketAttachment, Integer> {

	//TicketAttachment delete(Integer ticketId);

 @Modifying
	@Query("Delete  From TicketAttachment  WHERE id = :ticketId")
	
	 void  deletByTicket(Integer ticketId);

	//void deleteImage(MultipartFile file, Ticket ticketId);

}