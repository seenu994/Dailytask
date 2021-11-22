package com.xyram.ticketingTool.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xyram.ticketingTool.entity.Platform;
import com.xyram.ticketingTool.entity.Sprint;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, String> {

}