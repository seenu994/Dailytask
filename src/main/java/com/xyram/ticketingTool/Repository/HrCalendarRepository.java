package com.xyram.ticketingTool.Repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xyram.ticketingTool.entity.HrCalendar;

@Repository
@Transactional
public interface HrCalendarRepository extends JpaRepository<HrCalendar, String>{

//	getAllMySchedulesFromCalendarByStatus
	
	@Query("Select distinct new map( a.Id as id,a.candidateMobile as mobile,a.candidateName as name,a.status as status, "
			+ "a.createdAt as createdAt, a.scheduleDate as scheduleDate, a.searchedSource as searchedSource, "
			+ "a.jobId as jobId, a.closed as closed,a.callCount as callCount,a.reportingTo as reportingTo, "
			+ "a.createdAt as createdAt,a.lastUpdatedAt as lastUpdatedAt) from HrCalendar a where a.createdBy = :userId and "
			+ "(:toDate is null OR Date(a.scheduleDate) <= STR_TO_DATE(:toDate, '%Y-%m-%d')) AND "
			+ "(:fromDate is null OR Date(a.scheduleDate) >= STR_TO_DATE(:fromDate, '%Y-%m-%d')) AND "
			+ "(:status is null OR lower(a.status)=:status) AND "
			+ "(:jobId is null OR a.jobId=:jobId) AND "
			+ "(:closed is null OR a.closed=:closed) ORDER BY a.scheduleDate DESC")
	Page<Map> getAllMySchedulesFromCalendarByStatus(String userId,String jobId, 
			String fromDate, String toDate, String status,Boolean closed,Pageable pageable);
	
	@Query("Select distinct new map( a.Id as id,a.candidateMobile as mobile,a.candidateName as name,a.status as status, "
			+ "a.createdAt as createdAt, a.scheduleDate as scheduleDate, a.searchedSource as searchedSource, "
			+ "a.jobId as jobId, a.closed as closed,a.callCount as callCount,a.reportingTo as reportingTo, "
			+ "a.createdAt as createdAt,a.lastUpdatedAt as lastUpdatedAt) from HrCalendar a where a.createdBy = :userId and "
			+ "(:searchString is null "
			+ "or lower(a.candidateMobile) like %:searchString% "
			+ "or lower(a.candidateName) like %:searchString% "
			+ "or lower(a.jobId) like %:searchString%) "
			+ "ORDER BY a.scheduleDate DESC")
	List<Map> searchInMyShedule(String userId,String searchString);
	
	@Query("Select distinct new map( a.Id as id,a.candidateMobile as mobile,a.candidateName as name,a.status as status, "
			+ "a.createdAt as createdAt, a.scheduleDate as scheduleDate, a.searchedSource as searchedSource, "
			+ "a.jobId as jobId, a.closed as closed,a.callCount as callCount,a.reportingTo as reportingTo, "
			+ "a.createdAt as createdAt,a.lastUpdatedAt as lastUpdatedAt) from HrCalendar a where a.reportingTo = :userId and "
			+ "(:toDate is null OR Date(a.scheduleDate) <= STR_TO_DATE(:toDate, '%Y-%m-%d')) AND "
			+ "(:fromDate is null OR Date(a.scheduleDate) >= STR_TO_DATE(:fromDate, '%Y-%m-%d')) AND "
			+ "(:employeeId is null OR a.createdBy=:employeeId) AND "
			+ "(:status is null OR lower(a.status)=:status) AND "
			+ "(:jobId is null OR a.jobId=:jobId) AND "
			+ "(:closed is null OR a.closed=:closed) ORDER BY a.scheduleDate DESC")
	Page<Map> getAllMyTeamSchedulesFromCalendarByStatus(String userId,String employeeId,String jobId, 
			String fromDate, String toDate, String status,Boolean closed,Pageable pageable);
	
	@Query("Select distinct new map( a.Id as id,a.candidateMobile as mobile,a.candidateName as name,a.status as status, "
			+ "a.createdAt as createdAt, a.scheduleDate as scheduleDate, a.searchedSource as searchedSource, "
			+ "a.jobId as jobId, a.closed as closed,a.callCount as callCount,a.reportingTo as reportingTo, "
			+ "a.createdAt as createdAt,a.lastUpdatedAt as lastUpdatedAt) from HrCalendar a where a.createdBy = :userId and "
			+ "(:toDate is null OR Date(a.scheduleDate) <= STR_TO_DATE(:toDate, '%Y-%m-%d')) AND "
			+ "(:fromDate is null OR Date(a.scheduleDate) >= STR_TO_DATE(:fromDate, '%Y-%m-%d')) AND "
			+ "(:status is null OR lower(a.status)=:status) AND "
			+ "(:jobId is null OR a.jobId=:jobId) AND "
			+ "(:closed is null OR a.closed=:closed) ORDER BY a.scheduleDate DESC")
	List<Map> downloadAllMySchedulesFromCalendarByStatus(String userId,String jobId, 
			String fromDate, String toDate, String status,Boolean closed);
	
	@Query("Select distinct new map( a.Id as id,a.candidateMobile as mobile,a.candidateName as name,a.status as status, "
			+ "a.createdAt as createdAt, a.scheduleDate as scheduleDate, a.searchedSource as searchedSource, "
			+ "a.jobId as jobId, a.closed as closed,a.callCount as callCount,a.reportingTo as reportingTo, "
			+ "a.createdAt as createdAt,a.lastUpdatedAt as lastUpdatedAt) from HrCalendar a where a.reportingTo = :userId and "
			+ "(:toDate is null OR Date(a.scheduleDate) <= STR_TO_DATE(:toDate, '%Y-%m-%d')) AND "
			+ "(:fromDate is null OR Date(a.scheduleDate) >= STR_TO_DATE(:fromDate, '%Y-%m-%d')) AND "
			+ "(:employeeId is null OR a.createdBy=:employeeId) AND "
			+ "(:status is null OR lower(a.status)=:status) AND "
			+ "(:jobId is null OR a.jobId=:jobId) AND "
			+ "(:closed is null OR a.closed=:closed) ORDER BY a.scheduleDate DESC")
	List<Map> downloadAllMyTeamSchedulesFromCalendarByStatus(String userId,String employeeId,String jobId, 
		String fromDate, String toDate, String status,Boolean closed);
	
}
