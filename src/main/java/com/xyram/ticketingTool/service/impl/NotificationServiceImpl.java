package com.xyram.ticketingTool.service.impl;

import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Notification;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.xyram.ticketingTool.Repository.NotificationRepository;
import com.xyram.ticketingTool.apiresponses.ApiResponse;
import com.xyram.ticketingTool.controller.NotificationController;
import com.xyram.ticketingTool.entity.Notifications;
import com.xyram.ticketingTool.request.CurrentUser;
import com.xyram.ticketingTool.service.NotificationService;
import com.xyram.ticketingTool.util.ResponseMessages;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

	private final Logger logger = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	CurrentUser currentUser;

	@Autowired
	NotificationRepository notificationRepository;

	/*
	 * @Override public Page<Notification> getActiveNotifications(Pageable pageable)
	 * { // TODO Auto-generated method stub return null; }
	 */
	@Override
	public ApiResponse getAllNotifications(Pageable pageable) {

		ApiResponse response = new ApiResponse(false);

		Page<Notifications> AllNotifications = notificationRepository.getAllNotifications(currentUser.getUserId(),
				pageable);

		if (AllNotifications != null) {
			response.setSuccess(true);
			response.setMessage(ResponseMessages.NOTIFICATIONS_EXISTS);

			Map content = new HashMap();

			content.put("AllNotifications", AllNotifications);
			response.setContent(content);
		} else {
			response.setSuccess(false);
			response.setMessage(ResponseMessages.NOTIFICATIONS_NOT_EXISTS);
			response.setContent(null);
		}

		return response;
	}

}