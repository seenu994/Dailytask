package com.xyram.ticketingTool.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.criterion.CriteriaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sun.mail.iap.Response;
import com.xyram.ticketingTool.Repository.JobApplicationRepository;
import com.xyram.ticketingTool.Repository.JobInterviewRepository;
import com.xyram.ticketingTool.Repository.JobRepository;
import com.xyram.ticketingTool.apiresponses.ApiResponse;
import com.xyram.ticketingTool.entity.CompanyWings;
import com.xyram.ticketingTool.entity.JobApplication;
import com.xyram.ticketingTool.entity.JobInterviews;
import com.xyram.ticketingTool.entity.JobOpenings;
import com.xyram.ticketingTool.entity.Ticket;
import com.xyram.ticketingTool.request.CurrentUser;
import com.xyram.ticketingTool.request.JobApplicationSearchRequest;
import com.xyram.ticketingTool.request.JobInterviewsRequest;
import com.xyram.ticketingTool.request.JobOpeningSearchRequest;
import com.xyram.ticketingTool.service.JobService;


@Service
@Transactional
public class JobServiceImpl implements JobService{
	
	
	@Autowired
	CurrentUser userDetail;
	
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	JobApplicationRepository jobAppRepository;
	
	@Autowired
	JobInterviewRepository jobInterviewRepository;
	
	static ChannelSftp channelSftp = null;
	static Session session = null;
	static Channel channel = null;
	static String PATHSEPARATOR = "/";
		
	@Override
	public ApiResponse createJob(JobOpenings jobObj) {
		// TODO Auto-generated method stub
		ApiResponse response = new ApiResponse(false);
		
		jobObj.setCreatedAt(new Date());
		jobObj.setCreatedBy(userDetail.getUserId());
		jobObj.setStatus("VACATE");
		if(jobRepository.save(jobObj) != null) {
			response.setSuccess(true);
			response.setMessage("New Job Opening Created");
		}else {
			response.setSuccess(false);
			response.setMessage("New Job Opening Not Created");
		}
		return response;
	} 

	@Override
	public ApiResponse getAllJobs(JobOpeningSearchRequest jobOpeningObj) {
		// TODO Auto-generated method stub
		ApiResponse response = new ApiResponse(false);
		Map<String, List<JobOpenings>> content = new HashMap<String, List<JobOpenings>>();
//		List<Map> allJobs = jobRepository.getAllJobOpenings();
		
		List<JobOpenings> allList =  jobRepository.findAll(new Specification<JobOpenings>() {
				@Override
				public Predicate toPredicate(Root<JobOpenings> root, javax.persistence.criteria.CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					// TODO Auto-generated method stub
					List<Predicate> predicates = new ArrayList<>();
	                if(jobOpeningObj.getStatus() != null && !jobOpeningObj.getStatus().equalsIgnoreCase("ALL")) {
	                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("jobStatus"), jobOpeningObj.getStatus())));
	                }
	                if(jobOpeningObj.getWing() != null && !jobOpeningObj.getWing().equalsIgnoreCase("ALL")) {
	                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal((root.get("wings").get("Id")), jobOpeningObj.getWing())));
	                }
	                //criteriaBuilder.upper(itemRoot.get("code"), code.toUpperCase()
	                if(jobOpeningObj.getSearchString() != null && !jobOpeningObj.getSearchString().equalsIgnoreCase("")) {
//	                	criteriaBuilder.like(root.get("title"), "%" + keyword + "%")
	                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("jobTitle"), "%" + jobOpeningObj.getSearchString() + "%")));
	                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("jobDescription"), "%" + jobOpeningObj.getSearchString() + "%")));
	                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("jobCode"), "%" + jobOpeningObj.getSearchString() + "%")));
	                }
	                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
	        });
		content.put("jobsList",allList);
		if(allList.size() > 0) {
			response.setSuccess(true);
			response.setMessage("Succesfully retrieved Jobs");
		}
		else {
			response.setSuccess(true);
			response.setMessage("No Records Found");
		}
		response.setContent(content);
		return response;
	} 
	
	@Override
	public ApiResponse getAllJobApplications(JobApplicationSearchRequest jobAppSearch) {
		// TODO Auto-generated method stub
		ApiResponse response = new ApiResponse(false);
		Map<String, List<JobApplication>> content = new HashMap<String, List<JobApplication>>();		
		List<JobApplication> allList =  jobAppRepository.findAll(new Specification<JobApplication>() {
				@Override
				public Predicate toPredicate(Root<JobApplication> root, javax.persistence.criteria.CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					// TODO Auto-generated method stub
					List<Predicate> predicates = new ArrayList<>();
	                if(jobAppSearch.getStatus() != null && !jobAppSearch.getStatus().equalsIgnoreCase("ALL")) {
	                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), jobAppSearch.getStatus())));
	                }
	                if(jobAppSearch.getVendor() != null && !jobAppSearch.getVendor().equalsIgnoreCase("ALL")) {
	                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("referredVendor"), jobAppSearch.getVendor())));
	                }
	                if(jobAppSearch.getSearchString() != null && !jobAppSearch.getSearchString().equalsIgnoreCase("")) {
	                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("candidateName"), "%" + jobAppSearch.getSearchString() + "%")));
	                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("candidateMobile"), "%" + jobAppSearch.getSearchString() + "%")));
	                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("candidateEmail"), "%" + jobAppSearch.getSearchString() + "%")));
	                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("jobCode"), "%" + jobAppSearch.getSearchString() + "%")));
	                }
	                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
	        });
		content.put("jobAppList",allList);
		if(allList.size() > 0) {
			response.setSuccess(true);
			response.setMessage("Succesfully retrieved Jobs");
		}
		else {
			response.setSuccess(true);
			response.setMessage("No Records Found");
		}
		response.setContent(content);
		return response;
	}

	@Override
	public ApiResponse getAllCompanyWingsAndSkills() {
		// TODO Auto-generated method stub
		ApiResponse response = new ApiResponse(false);
		Map<String, List<Map>> content = new HashMap<String, List<Map>>();
		List<Map> allWingsList = jobRepository.getAllCompanyWings();
		List<Map> skillsList = jobRepository.getAllTechSkills();
		
		content.put("wings", allWingsList);
		content.put("skills", skillsList);
		response.setSuccess(true);
		response.setMessage("No Records Found");
		response.setContent(content);
		
		return response;
	}

	@Override
	public ApiResponse createJobApplication(MultipartFile[] files, String jobAppString,String jobCode) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ApiResponse response = new ApiResponse(false);
		JobOpenings jobOpening = jobRepository.getJobOpeningFromCode(jobCode);
		if(jobOpening != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			JobApplication jobAppObj = null;
			try {
				jobAppObj = objectMapper.readValue(jobAppString, JobApplication.class);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (MultipartFile constentFile : files) {
				try {
				      String fileextension = constentFile.getOriginalFilename().substring(constentFile.getOriginalFilename().lastIndexOf("."));
				      String filename = getRandomFileName()+fileextension;//constentFile.getOriginalFilename();
				      if(addFileAdmin(constentFile,filename)!= null) {
				    	  jobAppObj.setResumePath(filename);
				      }
				}catch(Exception e) {
					
				}
			}
			jobAppObj.setJobCode(jobCode);
			jobAppObj.setJobOpenings(jobOpening);
			jobAppObj.setCreatedAt(new Date());
			jobAppObj.setCreatedBy(userDetail.getUserId());
			jobAppObj.setStatus("APPLIED");
			if(jobAppRepository.save(jobAppObj) != null) {
				response.setSuccess(true);
				response.setMessage("New Job Application Created");
			}else {
				response.setSuccess(false);
				response.setMessage("New Job Application Not Created");
			}
		}else {
			response.setSuccess(false);
			response.setMessage("Job Code Not Exist");
		}
		return response;
	}
	
	@Override
	public ApiResponse scheduleJobInterview(JobInterviews schedule, String applicationId) {
		// TODO Auto-generated method stub
		ApiResponse response = new ApiResponse(false);
		JobApplication jobApp  = jobAppRepository.getApplicationById(applicationId);
		if(jobApp != null) {
			schedule.setCreatedAt(new Date());
			schedule.setCreatedBy(userDetail.getUserId());
			schedule.setStatus("SCHEDULED");
			schedule.setJobApplication(jobApp);
			if(jobInterviewRepository.save(schedule) != null) {
				jobApp.setStatus("SCHEDULED");
				jobAppRepository.save(jobApp);
				response.setSuccess(true);
				response.setMessage("Interview Scheduled");
			}else {
				response.setSuccess(false);
				response.setMessage("Interview Not Scheduled");
			}
		}else {
			response.setSuccess(false);
			response.setMessage("Job Application Not Exist");
		}
		return response;
	}
	
	@Override
	public ApiResponse getAllJobInterviews(JobInterviewsRequest searchObj) {
		// TODO Auto-generated method stub
		ApiResponse response = new ApiResponse(false);
		Map<String, List<JobInterviews>> content = new HashMap<String, List<JobInterviews>>();		
		List<JobInterviews> allList =  jobInterviewRepository.findAll(new Specification<JobInterviews>() {
				@Override
				public Predicate toPredicate(Root<JobInterviews> root, javax.persistence.criteria.CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					// TODO Auto-generated method stub
					List<Predicate> predicates = new ArrayList<>();
	                if(searchObj.getStatus() != null && !searchObj.getStatus().equalsIgnoreCase("ALL")) {
	                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), searchObj.getStatus())));
	                }
	                
//	                if(searchObj.getSearchString() != null && !searchObj.getSearchString().equalsIgnoreCase("")) {
//	                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("candidateName"), "%" + searchObj.getSearchString() + "%")));
//	                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("candidateMobile"), "%" + searchObj.getSearchString() + "%")));
//	                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("candidateEmail"), "%" + searchObj.getSearchString() + "%")));
//	                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("jobCode"), "%" + searchObj.getSearchString() + "%")));
//	                }
	                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
	        });
		content.put("InterviewList",allList);
		if(allList.size() > 0) {
			response.setSuccess(true);
			response.setMessage("Succesfully retrieved Interviews");
		}
		else {
			response.setSuccess(true);
			response.setMessage("No Records Found");
		}
		response.setContent(content);
		return response;
	}
		
	public String getRandomFileName() {
	    int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	}
	public String addFileAdmin(MultipartFile file, String fileName){
		System.out.println("bjsjsjn");
	    String SFTPHOST = "13.229.55.43"; // SFTP Host Name or SFTP Host IP Address
	    int SFTPPORT = 22; // SFTP Port Number
	    String SFTPUSER = "ubuntu"; // User Name
	    String SFTPPASS = ""; // Password
	    String SFTPKEY = "/home/ubuntu/tomcat/webapps/Ticket_tool-0.0.1-SNAPSHOT/WEB-INF/classes/Covid-Phast-Prod.ppk";
	    String SFTPWORKINGDIRAADMIN = "/home/ubuntu/tomcat/webapps/image/resumes";// Source Directory on SFTP server
	    String fileNameOriginal = fileName;
	    try {
	          JSch jsch = new JSch();
	        if (SFTPKEY != null && !SFTPKEY.isEmpty()) {
				jsch.addIdentity(SFTPKEY);
			}
	        session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
//	        session.setPassword(SFTPPASS);
	        java.util.Properties config = new java.util.Properties();
	        config.put("StrictHostKeyChecking", "no");
	        session.setConfig(config);
	        session.connect(); // Create SFTP Session
	        channel = session.openChannel("sftp"); // Open SFTP Channel
	        channel.connect();
	        channelSftp = (ChannelSftp) channel;
	        channelSftp.cd(SFTPWORKINGDIRAADMIN);// Change Directory on SFTP Server
	        channelSftp.put(file.getInputStream(),fileName);
	        System.out.println("added");
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    } 
	        finally {
	        if (channelSftp != null)
	            channelSftp.disconnect();
	        if (channel != null)
	            channel.disconnect();
	        if (session != null)
	            session.disconnect();
	    }
		return fileNameOriginal;
	}

	

	

}