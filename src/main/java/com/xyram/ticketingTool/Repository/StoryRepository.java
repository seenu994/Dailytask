package com.xyram.ticketingTool.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xyram.ticketingTool.entity.Story;

@Repository
public interface StoryRepository extends JpaRepository<Story, String> {
	
	
	
	
    @Query("select  count(s.id)  from Story s where s.projectId=:projectId ")
	Integer getTotalTicketByprojectId(String projectId);
	
    
    @Query("select s from Story s where s.id=:id")
    Story getStoryById(String id );

    
    
    @Query("select   new Map( s.storyNo as storyNo ,s.title as title , p.projectName as projectName,f.featureName as storyStatus,  "
    		+ " s.id as id, s.storyType as storyType,CONCAT(e1.firstName ,' ', e1.lastName) as owner, "
    		+ "CONCAT(e2.firstName ,' ', e2.lastName) as assignedTo )" 
    		+ " From Story s left join Projects p on p.pId =s.projectId "
    		+ "left join Feature f on f.featureId=s.storyStatus "
    		+ "left join Employee e1 on e1.eId=s.owner "
    		+ "left join Employee e2 on e2.eId=s.assignTo ")
  List<Map>  getAllStories(String projectId);
}