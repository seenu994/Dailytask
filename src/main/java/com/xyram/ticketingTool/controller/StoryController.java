package com.xyram.ticketingTool.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.xyram.ticketingTool.entity.Sprint;
import com.xyram.ticketingTool.entity.Story;
import com.xyram.ticketingTool.modelMapper.StoryMapper;
import com.xyram.ticketingTool.service.SprintService;
import com.xyram.ticketingTool.service.StoryService;
import com.xyram.ticketingTool.util.AuthConstants;
import com.xyram.ticketingTool.vo.StoryVo;

@RestController
public class StoryController {

	@Autowired
	private StoryService storyService;

	@Autowired
	private StoryMapper storyMapper;

	@PostMapping(value = { AuthConstants.DEVELOPER_BASEPATH + "/CreateStory" })
	public Story createStory( @Valid @RequestBody StoryVo storyVo) {

		return storyService.createStory(storyMapper.getEntityFromVo(storyVo));
	}
	
	
	@PostMapping(value = { AuthConstants.DEVELOPER_BASEPATH + "/getAllStories/{projectId}" })
	public List<Map> createStory(@PathVariable String projectId) {

		return storyService.getAllStories(projectId);
	}
	
	
	public String changeStoryStatus(String storyStatusId ,String storyId )
	{
	
		return null;
	}
}