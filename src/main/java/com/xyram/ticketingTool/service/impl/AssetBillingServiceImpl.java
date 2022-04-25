package com.xyram.ticketingTool.service.impl;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyram.ticketingTool.Repository.AssetBillingRepository;
import com.xyram.ticketingTool.Repository.AssetIssuesRepository;
import com.xyram.ticketingTool.Repository.AssetRepository;
import com.xyram.ticketingTool.Repository.AssetVendorRepository;
//import com.xyram.ticketingTool.Repository.AssetIssuesRepository;
import com.xyram.ticketingTool.apiresponses.ApiResponse;
import com.xyram.ticketingTool.entity.Asset;
//import com.xyram.ticketingTool.entity.Asset;
import com.xyram.ticketingTool.entity.AssetBilling;
import com.xyram.ticketingTool.entity.AssetIssues;
import com.xyram.ticketingTool.entity.AssetVendor;
import com.xyram.ticketingTool.enumType.AssetIssueStatus;
import com.xyram.ticketingTool.request.AssetBillingRequest;
import com.xyram.ticketingTool.request.CurrentUser;
import com.xyram.ticketingTool.service.AssetBillingService;
import com.xyram.ticketingTool.service.AssetIssuesService;
import com.xyram.ticketingTool.service.AssetService;
import com.xyram.ticketingTool.service.AssetvendorService;
//import com.xyram.ticketingTool.service.AssetIssuesService;
//import com.xyram.ticketingTool.service.AssetvendorService;
import com.xyram.ticketingTool.util.ResponseMessages;

import springfox.documentation.service.ResponseMessage;

@Service
@Transactional
public class AssetBillingServiceImpl implements AssetBillingService
{
	private static final Logger logger = LoggerFactory.getLogger(AssetBillingServiceImpl.class);

	@Autowired
	AssetBillingRepository  assetBillingRepository;
	
	@Autowired
	AssetRepository  assetRepository;
	
	//@Autowired
	//AssetBillingService assetBillingService;
	
	@Autowired
	AssetvendorService assetVendorService;
	
	@Autowired
	AssetVendorRepository assetVendorRepository;
	
	@Autowired
	AssetIssuesService assetIssuesService;
	
	@Autowired
	AssetService assetService;
	
	@Autowired
	CurrentUser currentUser;

	@Autowired
	AssetIssuesRepository assetIssuesRepository;
	
	//String billingType[] = {"purchase", "repair", "return"};
	@Override
	public ApiResponse addPurchaseAssetBill(AssetBilling assetBilling) 
	{
		ApiResponse response = new ApiResponse();
		
		response = validateAssetBilling(assetBilling);
		
		if(response.isSuccess())
		{
		 
			if(assetBilling != null)
			{
				assetBilling.setBillingType("purchase");
				assetBilling.setCreatedAt(new Date());
				assetBilling.setCreatedBy(currentUser.getUserId());
				
				    Date purchaseDate = assetRepository.getPurchaseDateById(assetBilling.getAssetId());
					Date WarrentyDate = assetRepository.getWarrentyDateById(assetBilling.getAssetId());
					//System.out.println(purchaseDate);
					//System.out.println(WarrentyDate);
					Date currentDate = new Date();
					if(WarrentyDate != null ) {
					if(WarrentyDate.after(purchaseDate))
					{
						assetBilling.setUnderWarrenty(true);
						response.setMessage(ResponseMessages.ASSET_PURCHASE_BILL_ADDED_SUCCESSFULLY);
					}
					else if(currentDate.after(WarrentyDate))
					{
						assetBilling.setUnderWarrenty(false);
						response.setMessage(ResponseMessages.BILL_ADDED_SUCCESSFULLY);
					}
					}
					else {
						assetBilling.setUnderWarrenty(false);
						response.setMessage(ResponseMessages.BILL_ADDED_SUCCESSFULLY);
					}
				
				assetBillingRepository.save(assetBilling);
				response.setSuccess(true);
				//response.setMessage(ResponseMessages.ASSET_PURCHASE_BILL_ADDED_SUCCESSFULLY);
				
			}
		}
		return response;
	}                                    


	public ApiResponse validateAssetBilling(AssetBilling assetBilling) 
	{
		ApiResponse response = new ApiResponse(false);
		 //validate asset id
          
		 if(assetBilling.getAssetId() == null || assetBilling.getAssetId().equals(""))
		 {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "asset id is mandatory !!");
		 }
		 else
		 {
			 Asset asset = assetRepository.getAssetById(assetBilling.getAssetId());
			 if(asset == null)
			 {
				 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid asset id!!!");
			 }
			 AssetBilling assetBillings = assetBillingRepository.getAssetById(assetBilling.getAssetId());
			 System.out.println(assetBillings);
			 if(assetBillings != null)
			 {
			   throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "asset id is already exist!!!");
			 }
			 
		 }
		 
		 //validate vendor id
		 if(assetBilling.getVendorId() == null || assetBilling.getVendorId().equals(""))
		 {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vendor id is mandatory !!");
		 }
		 else
		 {
			 Asset asset = assetRepository.getVendorById(assetBilling.getAssetId(), assetBilling.getVendorId());
			 if(asset == null)
			 {
				 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid vendor id");
			 }
			 
		 }
	
		
		 //validate billingtype
		 if(assetBilling.getBillingType() == null || assetBilling.getBillingType().equals(""))
		 {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "billing type is mandatory");
		 }
		 
		
	   //validate transactionDate
		if(assetBilling.getTransactionDate() == null || assetBilling.getTransactionDate().equals(""))
		 {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "transaction date is mandatory !!");
		 
		 }
		 else
		 {
			  //assetBilling.setTransactionDate(new Date());
			 validateTransactionDate(assetBilling.getTransactionDate(),assetBilling.getAssetId());
			 assetBilling.setTransactionDate(assetBilling.getTransactionDate());
		 }
			  //AssetBilling assetBilling = new AssetBilling();
	    	  /*Date d1 = assetBilling.getTransactionDate();
	    	  System.out.println(d1);
			  Date d2 = assetRepository.getPurchaseDateById(assetBilling.getAssetId());
			  System.out.println(d2);
			 if(d1.equals(d2))
			 {
				 assetBilling.setTransactionDate(assetBilling.getTransactionDate()); 
	    	   
			 }
			 else
			 {
				 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "transaction date should be equal to  purchase Date");
				 
			 }*/
		 //}
		 
		 if(assetBilling.getAssetAmount() == null || assetBilling.getAssetAmount().equals(""))
		 {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "asset amount is mandatory !!");
		 }
		 if(assetBilling.getGstAmount() == null || assetBilling.getGstAmount().equals(""))
		 {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "gst amount is mandatory !!");
		 }
		 if(assetBilling.getUnderWarrenty() == null || assetBilling.getUnderWarrenty().equals(""))
		 {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "underwarrenty is should be null or empty !!"); 
		 }
		 /*else
		 {
			    Date purchaseDate = assetRepository.getPurchaseDateById(assetBilling.getAssetId());
				Date WarrentyDate = assetRepository.getWarrentyDateById(assetBilling.getAssetId());
				Date currentDate = new Date();
				
				if(WarrentyDate.after(purchaseDate))
				{
					assetBilling.setUnderWarrenty(true);
					//response.setMessage("asset is underwarrenty");
				}
				else if(purchaseDate.after(WarrentyDate) || purchaseDate.equals(WarrentyDate) || currentDate.after(WarrentyDate))
				{
					assetBilling.setUnderWarrenty(false);
					//response.setMessage("asset is not underWarrenty");
				}
				
				//else if(purchaseDate.(WarrentyDate))
			
			 //validateUnderWarrenty1(assetBilling.getUnderWarrenty(), assetBilling.getAssetId());
			 
			 //Asset asset = assetRepository.getAssetById(assetBilling.getAssetId());
			// Date purchaseDate = assetRepository.getPurchaseDateById(assetId);
			 
		 }*/
			 assetBilling.setAmountPaid(true);
		 //String assetBillId = assetBilling.getAssetBillId();
		 //checkAmountPaid(assetBilling.isAmountPaid(),assetBillId);
		 
		
    	response.setSuccess(true);
		return response;
		}


	

	private boolean validateUnderWarrenty1(Boolean underWarrenty, String assetId) 
	{
		//ApiResponse response = new ApiResponse();
		Date purchaseDate = assetRepository.getPurchaseDateById(assetId);
		Date WarrentyDate = assetRepository.getWarrentyDateById(assetId);
		Date currentDate = new Date();
		
		if(WarrentyDate.after(purchaseDate))
		{
			underWarrenty = true;
			//response.setMessage(ResponseMessages.ASSET_IS_UNDERWARRENTY);
		}
		else if(purchaseDate.after(WarrentyDate) || purchaseDate.equals(WarrentyDate) || WarrentyDate.after(currentDate))
		{
			underWarrenty = false;
			//response.setMessage(ResponseMessages.ASSET_IS_NOT_UNDERWARRENTY);
		}
		
		//else if(purchaseDate.(WarrentyDate))
		return underWarrenty;
		
	}


	private String getBillingDetailByAssetId(String assetId) {
		
		return assetId;
	}


	
	private ApiResponse validateAssetIssueId(AssetBilling assetBilling) 
	{
		ApiResponse response = new ApiResponse(false);
		if(assetBilling.getAssetIssueId() == null || assetBilling.getAssetIssueId().equals(" "))
		 {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "issue id is mandatory !!");
		 }
		else
		{
			 AssetIssues assetIssues = assetIssuesRepository.getAssetIssueById(assetBilling.getAssetIssueId());
			 if(assetIssues== null)
			 {
				 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid asset issue");
			 }
			 //else {
			   //assetBilling.setAssetIssuesId(assetIssuesId);	 
			 //}
		}
		response.setSuccess(true);
		return response;
	}


	@Override
	public ApiResponse getAllAssestVendor(String vendorID) {
		ApiResponse response = new ApiResponse(false);
		return response;

	}

    @Override
	public ApiResponse editPurchaseAssetBill(AssetBilling assetBilling, String assetBillId) 
	{
    	ApiResponse response = new ApiResponse(false);
		
		AssetBilling assetBillingObject = assetBillingRepository.getAssetBillById(assetBillId);
		 
		if(assetBillingObject != null) 
	    {	
			if(assetBilling.getAssetId() != null)
			{
				 checkAssetId(assetBillingObject.getAssetId());
				 assetBillingObject.setAssetId(assetBilling.getAssetId());
			}
			if(assetBilling.getVendorId() != null) 
			{
		    	checkVendorId(assetBilling.getVendorId());
		    	assetBillingObject.setVendorId(assetBilling.getVendorId());
		    }
			
			if(assetBilling.getBillingType() == null || (assetBilling.getBillingType().equals("")))
			 {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bill type is mandatory");
				 
			 }
			else
			{
				assetBillingObject.setBillingType("purchase");
			}
			if(assetBilling.getTransactionDate() == null && assetBilling.getTransactionDate().equals(""))
			{
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "transactionDate ");
			}
			else
			{
				 validateTransactionDate(assetBilling.getTransactionDate(),assetBillingObject.getAssetId());
				 assetBillingObject.setTransactionDate(assetBilling.getTransactionDate());
			}
			if(assetBilling.getAssetAmount() != null)
			 {
				checkAssetAmount(assetBillingObject.getAssetAmount());
				 assetBillingObject.setAssetAmount(assetBilling.getAssetAmount());
			 }
			
		     if(assetBilling.getGstAmount() != null)
			 {
		    	 checkGstAmount(assetBillingObject.getGstAmount());
		    	 assetBillingObject.setGstAmount(assetBilling.getGstAmount());
			 }
		     //validateUnderWarrenty1(assetBilling.getUnderWarrenty(), assetBilling.getAssetId());
		     /*if(assetBilling.getUnderWarrenty() != false || assetBilling.getUnderWarrenty().equals(""))
		     {
		    	 checkUnderWarrenty(assetBillingObject.getUnderWarrenty());
		    	 assetBillingObject.setUnderWarrenty(assetBilling.getUnderWarrenty());
		     }*/
		        
			    assetBillingObject.setLastUpdatedAt(new Date());
			    assetBillingObject.setUpdatedBy(currentUser.getName());
		        Date purchaseDate = assetRepository.getPurchaseDateById(assetBilling.getAssetId());
				Date WarrentyDate = assetRepository.getWarrentyDateById(assetBilling.getAssetId());
				
				Date currentDate = new Date();
				if(WarrentyDate != null ) {
				if(WarrentyDate.after(purchaseDate))
				{
					assetBilling.setUnderWarrenty(true);
					assetBillingObject.setAmountPaid(true);
					response.setMessage(ResponseMessages.ASSET_PURCHASE_BILL_EDIT_SUCCESSFULLY);
				}
				else if(currentDate.after(WarrentyDate))
				{
					assetBilling.setUnderWarrenty(false);
					assetBillingObject.setAmountPaid(true);
					response.setMessage(ResponseMessages.BILL_EDITED_SUCCESSFULLY);
				}
				}
				else {
					assetBilling.setUnderWarrenty(false);
					assetBillingObject.setAmountPaid(true);
					response.setMessage(ResponseMessages.BILL_EDITED_SUCCESSFULLY);
				}
			
			assetBillingRepository.save(assetBillingObject);
			response.setSuccess(true);
			//response.setMessage(ResponseMessages.ASSET_PURCHASE_BILL_EDIT_SUCCESSFULLY);
			
		}

		else 
		{
			response.setSuccess(false);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid asset billId");
			
		}
       return response;
    }
		 

		


		private boolean checkUnderWarrenty(Boolean underWarrenty) 
		 {
			 if(underWarrenty == null || underWarrenty.equals(""))
			 {
				 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "underWarrenty cannot be null or empty");
			 }
			 
		       return false;
		
	     }


		/*if(assetBillingObject != null)
		 {
			 //System.out.println("IN IF");   
			 if(assetBillingObj.getAssetId() != null)
			 {
				 checkAssetId(assetBillingObject.getAssetId());
				 assetBillingObject.setAssetId(assetBillingObj.getAssetId());
			 }
			 if(assetBillingObj.getVendorId() != null || assetBillingObj.getAssetId().equals("")) 
			 {
			    	checkVendorId(assetBillingObject.getVendorId());
			    	assetBillingObject.setVendorId(assetBillingObj.getVendorId());
			 }
			 if(assetBillingObj.getBillingType() != null || (assetBillingObj.getBillingType().equals("")))
			  {
				 assetBillingObject.setBillingType("purchase");
			 }
			 if(assetBillingObj.getTransactionDate() != null && assetBillingObj.getTransactionDate().equals(""))
			 {
				 //validateTransactionDate(assetBillingObject.getTransactionDate(),assetBillingObject.getAssetId());
				 assetBillingObject.setTransactionDate(new Date());
			 }
			 if(assetBillingObj.getAssetAmount() != null && assetBillingObj.getAssetAmount().equals(""))
			 {
				 assetBillingObject.setAssetAmount(assetBillingObj.getAssetAmount());
			 }
			
		     if(assetBillingObj.getGstAmount() != null && assetBillingObj.getAssetAmount().equals(""))
			 {
		    	 assetBillingObject.setAssetAmount(assetBillingObj.getAssetAmount());
			 }
		        assetBillingObject.setAmountPaid(true);
		        
		        assetBillingObj.setLastUpdatedAt(new Date());
		        assetBillingObj.setUpdatedBy(currentUser.getUserId());
		        
			   assetBillingRepository.save(assetBillingObject);
				//AssetBilling assetBillingObject = assetBillingRepository.save(assetBilling);

				response.setSuccess(true);
				response.setMessage(ResponseMessages.ASSET_PURCHASE_BILL_EDIT_SUCCESSFULLY);
			  } 

          else {
				response.setSuccess(false);
				
				
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid asset bill id");
				//response.setContent(null);
			 
		 }
		 
		return response;*/


	


	private boolean checkGstAmount(Double gstAmount) 
	{
		if(gstAmount == null || gstAmount.equals(""))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "gst amount is mandatory");
		}
		return true;
		
	}


	private boolean checkAssetAmount(Double assetAmount) 
	{
		if(assetAmount == null || assetAmount.equals(""))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "asset amount is mandatory");
		}
		return true;
		
	}


	private boolean validateTransactionDate(Date transactionDate,String assetId) 
	{
		Date d1 =  transactionDate;
		Date d2 = new Date();
		Date purchaseDate = assetRepository.getPurchaseDateById(assetId);
		Date d3 = purchaseDate;
		System.out.println(purchaseDate);
		if(d1.after(d2))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "transactionDate should not be grater than current date");
		}
		else if(d3.after(d1))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "transactionDate should not be less than purchaseDate");
		}
		return true;
	}
		  /*Date purchaseDate = assetRepository.getPurchaseDateById(assetId);
		  String purchaseDateValidate = purchaseDate.toString();
		  String transactionDateValidate = purchaseDate.toString();
		  
		  Date parsePurchaseDate = null;
		  Date parseTransactionDate = null;
		  
		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		  if(purchaseDate != null && transactionDate != null )
		  {
			  try
				{
				  parsePurchaseDate = purchaseDateValidate != null ? dateFormat.parse(purchaseDateValidate) : null;
				  parseTransactionDate = transactionDateValidate != null ? dateFormat.parse(transactionDateValidate) : null;
				}
				catch(ParseException e)
				{
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid date format date should be yyyy-MM-dd");
				}
		  }
		  
		 System.out.println(transactionDate);
		 System.out.println(purchaseDate);
		 if(parsePurchaseDate.equals(parseTransactionDate))
		 {
			 return true;
		 }
		 else
		 {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "transaction date should be equal to  purchase Date"); 
		 }
		 /*
		  Date parsefromDate = null;
		  Date parsetoDate = null;
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(fromDate != null && toDate != null)
		{
			try
			{
				parsefromDate = fromDate != null ? dateFormat.parse(fromDate) : null;
				parsetoDate = toDate != null ? dateFormat.parse(toDate) : null;
			}
			catch(ParseException e)
			{
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid date format date should be yyyy-MM-dd");
			}
			
		}
		  */
		
	


	private boolean checkAssetId(String assetId) 
	{
		Asset asset = assetRepository.getByAssetId(assetId);
		if(asset == null || asset.equals(""))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "asset id is not valid");
		}
		else
		{
		   return true;
		}
    }

	private boolean checkVendorId(String vendorId) 
	{
		AssetVendor assetVendor = assetVendorRepository.getVendorById(vendorId);
		if(assetVendor == null || assetVendor.equals(""))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vendor id is not valid");
		}
		return true;
	}
	
@Override
public ApiResponse addRepairAssetBill(AssetBilling assetBilling) {
	
	ApiResponse response = new ApiResponse();
	
	
	response = validateAssetBillings(assetBilling);
	
	//assetBillingObj = assetBillingRepository.save(assetBillingObj);
		if(response.isSuccess())
		{
		    response = validateAssetIssueId(assetBilling);
			if(assetBilling != null)
			{
				assetBilling.setBillingType("repair");
				assetBilling.setCreatedAt(new Date());
				assetBilling.setCreatedBy(currentUser.getName());
				
				Date purchaseDate = assetRepository.getPurchaseDateById(assetBilling.getAssetId());
				Date WarrentyDate = assetRepository.getWarrentyDateById(assetBilling.getAssetId());
				//System.out.println(purchaseDate);
				//System.out.println(WarrentyDate);
				Date currentDate = new Date();
				if(WarrentyDate != null ) {
				if(WarrentyDate.after(purchaseDate))
				{
					assetBilling.setUnderWarrenty(true);
					assetBilling.setAmountPaid(false);
					response.setMessage(ResponseMessages.ASSET_REPAIR_BILL_ADDED_SUCCESSFULLY);
				}
				else if(currentDate.after(WarrentyDate))
				{
					assetBilling.setUnderWarrenty(false);
					assetBilling.setAmountPaid(false);
					response.setMessage(ResponseMessages.REPAIR_BILL_ADDED_SUCCESSFULLY);
				}
				}
				else {
					assetBilling.setUnderWarrenty(false);
					assetBilling.setAmountPaid(false);
					response.setMessage(ResponseMessages.REPAIR_BILL_ADDED_SUCCESSFULLY);
				}
			
			
				assetBilling = assetBillingRepository.save(assetBilling);
				response.setSuccess(true);
				//response.setMessage(ResponseMessages.ASSET_REPAIR_BILL_ADDED_SUCCESSFULLY);
			}
		}

	return response;
}

		private ApiResponse validateAssetBillings(AssetBilling assetBilling) {

		ApiResponse response = new ApiResponse(false);
		
		if(assetBilling.getAssetId() == null || assetBilling.getAssetId().equals(""))
		 {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "asset id is mandatory !!");
		 }
		 else
		 {
			 //Asset asset = assetRepository.getaId(assetIssues.getaId());
			 Asset asset = assetRepository.getAssetById(assetBilling.getAssetId());
			 if(asset == null)
			 {
				 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid asset reference");
			 }
		 }
		
		//validate vendor id
		if(assetBilling.getVendorId() == null || assetBilling.getVendorId().equals(" "))
		{
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vendor id is mandatory !!");
		}
		else
		{
			 AssetVendor assetVendor = assetVendorRepository.getAssetVendorById(assetBilling.getVendorId());
			 if(assetVendor == null)
			 {
				 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid vendor id");
			 }
		}

		//validate billingtype
		if(assetBilling.getBillingType() == null || assetBilling.getBillingType().equals(""))
		{
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "billing type is mandatory");
		}
		
		//assetBilling.setTransactionDate(new Date());
		//validate transactionDate
		if(assetBilling.getTransactionDate() == null || assetBilling.getTransactionDate().equals(""))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "transaction date is mandatory !!");
		
		}
		else
		{
			 validateTransactionDate(assetBilling.getTransactionDate(),assetBilling.getAssetId());
			 assetBilling.setTransactionDate(assetBilling.getTransactionDate());
		}
		//Asset asset = assetRepository.getAssetById(assetBilling.getAssetId());
		//validateUnderWarrenty1(assetBilling.getUnderWarrenty(), assetBilling.getAssetId());
		if(assetBilling.getUnderWarrenty() == true)
		 {
			    assetBilling.setAssetAmount(null);
			    assetBilling.setGstAmount(null);
			    assetBilling.setAmountPaid(false);
		 }
		 else
		 {
			 if(assetBilling.getAssetAmount() != null || assetBilling.getAssetAmount().equals(""))
			 {
			   assetBilling.setAssetAmount(assetBilling.getAssetAmount());
			 }
			 if(assetBilling.getGstAmount() != null || assetBilling.getGstAmount().equals(""))
			 {
			   assetBilling.setGstAmount(assetBilling.getGstAmount());
			 }
			 assetBilling.setAmountPaid(false);
			
		 }
		
		
		
		response.setSuccess(true);
		return response;
		
		}
	private Asset getAssetById(Asset assetId) {
			// TODO Auto-generated method stub
			return assetId;
		}


	@Override
	public ApiResponse editRepairAssetBill(AssetBilling assetBilling,String assetBillId) 
	{
		ApiResponse response = new ApiResponse();
		
		
		
		AssetBilling assetBillingObject = assetBillingRepository.getAssetBillById(assetBillId);
		 //System.out.println("assetBillingObject.getAssetBillId() - " + assetBillingObject.getAssetId());
		 
		 if(assetBillingObject != null)
		 {
			 //System.out.println("IN IF");   
			 if(assetBilling.getAssetId() != null)
				{
					 checkAssetId(assetBillingObject.getAssetId());
					 assetBillingObject.setAssetId(assetBilling.getAssetId());
				}
			 if(assetBilling.getVendorId() != null) 
			 {
			    	checkVendorId(assetBilling.getVendorId());
			    	assetBillingObject.setVendorId(assetBilling.getVendorId());
			 }
			 if(assetBilling.getBillingType() == null || assetBilling.getBillingType().equals(""))
			 {
				 throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"billing type should not be null or empty");
				 
			 }
			 else
			 {
				 assetBillingObject.setBillingType("repair"); 
			 }
			 if(assetBilling.getTransactionDate() != null)
			 {
				 validateTransactionDate(assetBilling.getTransactionDate(),assetBillingObject.getAssetId());
				 assetBillingObject.setTransactionDate(assetBilling.getTransactionDate());
			 }
			 if(assetBilling.getAssetIssueId() != null)
			 {
				 checkAssetIssueId(assetBilling.getAssetIssueId());
				 assetBillingObject.setAssetIssueId(assetBilling.getAssetIssueId());
			 }
			 assetBillingObject.setLastUpdatedAt(new Date());
		     assetBillingObject.setUpdatedBy(currentUser.getUserId());
		        
			    Date purchaseDate = assetRepository.getPurchaseDateById(assetBilling.getAssetId());
				Date WarrentyDate = assetRepository.getWarrentyDateById(assetBilling.getAssetId());
				
				Date currentDate = new Date();
				if(WarrentyDate != null ) {
				if(WarrentyDate.after(purchaseDate))
				{
					assetBilling.setUnderWarrenty(true);
					assetBillingObject.setAssetAmount(null);
					assetBillingObject.setGstAmount(null);
					assetBillingObject.setAmountPaid(true);
					response.setMessage(ResponseMessages.ASSET_REPAIR_BILL_EDITED_SUCCESSFULLY);
				}
				else if(currentDate.after(WarrentyDate))
				{
					assetBilling.setUnderWarrenty(false);
					assetBillingObject.setAmountPaid(true);
					response.setMessage(ResponseMessages.REPAIR_BILL_EDITED_SUCCESSFULLY);
				}
				}
				else {
					assetBilling.setUnderWarrenty(false);
					if(assetBilling.getAssetAmount() != null)
					 {
					   assetBillingObject.setAssetAmount(assetBilling.getAssetAmount());
					 }
					 if(assetBilling.getGstAmount() != null)
					 {
					   assetBillingObject.setGstAmount(assetBilling.getGstAmount());
					 }
					   assetBillingObject.setAmountPaid(false);
					assetBillingObject.setAmountPaid(false);
					response.setMessage(ResponseMessages.REPAIR_BILL_EDITED_SUCCESSFULLY);
				}
			
			assetBillingRepository.save(assetBillingObject);
			response.setSuccess(true);
			/* if(assetBilling.getUnderWarrenty() == true)
			 {
				    assetBillingObject.setAssetAmount(null);
					assetBillingObject.setGstAmount(null);
					assetBillingObject.setAmountPaid(false);
			 }
			 else
			 {
				 if(assetBilling.getAssetAmount() != null || assetBilling.getAssetAmount().equals(""))
				 {
				   assetBillingObject.setAssetAmount(assetBilling.getAssetAmount());
				 }
				 if(assetBilling.getGstAmount() != null || assetBilling.getGstAmount().equals(""))
				 {
				   assetBillingObject.setGstAmount(assetBilling.getGstAmount());
				 }
				   assetBillingObject.setAmountPaid(false);
				
			 }*/
			 //checkUnderWarrenty(assetBillingObject,assetBilling);
			 //assetBillingObject.setUnderWarrenty(false);
		     
		        //assetBillingObject.setLastUpdatedAt(new Date());
		       // assetBillingObject.setUpdatedBy(currentUser.getUserId());
		        
			   //assetBillingRepository.save(assetBillingObject);
				//AssetBilling assetBillingObject = assetBillingRepository.save(assetBilling);

				//response.setSuccess(true);
				//response.setMessage(ResponseMessages.ASSET_REPAIR_BILL_EDITED_SUCCESSFULLY);
			  } 

          else {
				response.setSuccess(false);
				
				
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid asset bill id");
				//response.setContent(null);
			 
		 }
		return response;
	}


	private boolean checkAssetIssueId(String assetIssueId) 
	{
		AssetIssues issueId = assetIssuesRepository.getAssetIssueById(assetIssueId);
		if(issueId == null || issueId.equals(""))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "asset issueId is not valid");
		}
		return true;
		
	}


	/*private AssetBilling checkUnderWarrenty1(AssetBilling assetBilling,AssetBillingRequest assetBillingObj) 
	{
		if(assetBilling.getUnderWarrenty())
		{
        	assetBilling.setUnderWarrenty(true);
        	//assetBilling.setAssetAmount(null);
        	//assetBilling.setGstAmount(null);
        	assetBilling.setAmountPaid(false);
        	return assetBilling;
        	
		}
		else
		{
			assetBilling.setAssetAmount(assetBilling.getAssetAmount());
			assetBilling.setGstAmount(assetBilling.getGstAmount());
			assetBilling.setAmountPaid(true);
			assetBilling.setUnderWarrenty(false);
			return assetBilling;
		}
			
}*/
    @Override
	public ApiResponse returnFromRepair(AssetBilling assetBilling, String assetBillId) 
	{
    	ApiResponse response = new ApiResponse();
		
		AssetBilling billingObj = assetBillingRepository.getAssetBillById(assetBillId);
		
		 if(billingObj != null) 
		    {	
				if(assetBilling.getAssetId() != null)
				{
					 checkAssetId(assetBilling.getAssetId());
					 billingObj.setAssetId(assetBilling.getAssetId());
				}
				if(assetBilling.getVendorId() != null)
				{
					checkVendorId(assetBilling.getVendorId());
					billingObj.setVendorId(assetBilling.getVendorId());
				}
				
				if(assetBilling.getBillingType() == null)
				{
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "billing type is manadatory");
				}
				else
				{
					billingObj.setBillingType("return");
				}
				if(assetBilling.getTransactionDate() != null)
				{
					validateTransactionDate(assetBilling.getTransactionDate(), assetBilling.getAssetId());
					billingObj.setTransactionDate(assetBilling.getTransactionDate());
				}
				
				
                if(assetBilling.getAssetIssueId() != null)
                {
                	checkAssetIssueId(assetBilling.getAssetIssueId());
                	billingObj.setAssetIssueId(assetBilling.getAssetIssueId());
                }
                billingObj.setReturnDate(new Date());
                billingObj.setLastUpdatedAt(new Date());
                billingObj.setUpdatedBy(currentUser.getName());
                
                Date purchaseDate = assetRepository.getPurchaseDateById(assetBilling.getAssetId());
				Date WarrentyDate = assetRepository.getWarrentyDateById(assetBilling.getAssetId());
				
				Date currentDate = new Date();
				if(WarrentyDate != null ) {
				if(WarrentyDate.after(purchaseDate))
				{
					billingObj.setUnderWarrenty(true);
					billingObj.setAssetAmount(null);
					billingObj.setGstAmount(null);
					billingObj.setAmountPaid(false);
					response.setMessage(ResponseMessages.ASSET_RETURN_FROM_REPAIR);
				}
				else if(currentDate.after(WarrentyDate))
				{
					 billingObj.setUnderWarrenty(false);
					if(assetBilling.getAssetAmount() != null)
					 {
						billingObj.setAssetAmount(assetBilling.getAssetAmount());
					 }
					 if(assetBilling.getGstAmount() != null)
					 {
						 billingObj.setGstAmount(assetBilling.getGstAmount());
					 }
					billingObj.setAmountPaid(true);
					response.setMessage(ResponseMessages.RETURN_FROM_REPAIR);
				}
				}
				else {
					assetBilling.setUnderWarrenty(false);
					if(assetBilling.getAssetAmount() != null)
					 {
						billingObj.setAssetAmount(assetBilling.getAssetAmount());
					 }
					 if(assetBilling.getGstAmount() != null)
					 {
						 billingObj.setGstAmount(assetBilling.getGstAmount());
					 }
					 billingObj.setAmountPaid(false);
					 billingObj.setAmountPaid(false);
					response.setMessage(ResponseMessages.RETURN_FROM_REPAIR);
				}
                /*if(assetBilling.getUnderWarrenty() == true)
				 {
					billingObj.setAssetAmount(null);
					billingObj.setGstAmount(null);
					billingObj.setAmountPaid(false);
				 }
				 else
				 {
					 if(assetBilling.getAssetAmount() == null || assetBilling.getAssetAmount().equals(""))
					 {
						 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "asset amount is mandatory");
					 }
					 billingObj.setAssetAmount(assetBilling.getAssetAmount());
					 if(assetBilling.getGstAmount() == null || assetBilling.getGstAmount().equals(""))
					 {
						 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "gst amount is mandatory");
					 }
					 billingObj.setGstAmount(assetBilling.getGstAmount());
					 billingObj.setAmountPaid(true);
					
				 }*/
				assetBillingRepository.save(billingObj);
				response.setSuccess(true);
				//response.setMessage(ResponseMessages.RETURN_REPAIR);
				
			}

			else 
			{
				response.setSuccess(false);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid assetBillId");
				
			}
	       return response;
	    }
		
	


	private boolean checkReturnDate(Date returnDate, String assetBillId) 
	{
		    Date transactionDate = assetBillingRepository.getTransationDate(assetBillId);
			Date d1 = transactionDate;
			Date d2 = returnDate;
			System.out.println("d1"+ transactionDate);
			if(d1.after(d2) || d1.equals(d2))
			{
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " returnDate is mandatory");
			}
			else
			{
				return true;
			}
		}
		



	@Override
	public ApiResponse getAllAssetBillingByAssetId(String assetId) {
		ApiResponse response = new ApiResponse();
		
		
		//List<AssetBilling> assetBilling = assetBillingRepository.getAllAssetBillingByAssetId(assetId);
		//List<AssetBilling> assetBilling = assetBillingRepository.getAllAssetBillingByAssetId(assetId);
		Map assetBilling = assetBillingRepository.getAllAssetBillingByAssetId(assetId);
		Map content = new HashMap();
		content.put("assetBilling", assetBilling);
		
		    if(content != null)
		    {
				response.setSuccess(true);
				response.setMessage("asset billing data Retrieved Successfully");
				response.setContent(content);
		   }
		else {
			response.setSuccess(false);
			response.setMessage("Could not retrieve data");
		}
		return response;
		
		
	}

}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
   







	
