package com.xyram.ticketingTool.service;




import java.util.Map;


import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.xyram.ticketingTool.apiresponses.ApiResponse;
import com.xyram.ticketingTool.entity.AssetBilling;
import com.xyram.ticketingTool.entity.AssetIssues;
import com.xyram.ticketingTool.request.AssetBillingRequest;

@Service

public interface AssetBillingService 
{
   ApiResponse getAllAssestVendor(String vendorID);
	
	ApiResponse addPurchaseAssetBill(AssetBilling assetBilling);

	//ApiResponse editPurchaseAssetBill(AssetBilling assetBilling);
	ApiResponse editPurchaseAssetBill(AssetBilling assetBilling, String assetBillId);


	ApiResponse addRepairAssetBill(AssetBilling assetBilling);

	ApiResponse editRepairAssetBill(AssetBilling assetBilling, String assetBillId);

	//ApiResponse getAllAssetBilling(Map<String, Object> filter, Pageable pageable);

	//ApiResponse returnFromRepair(AssetBilling assetBilling);

	//ApiResponse getAllAssetBillingById(Map<String, Object> filter, Pageable pageable);

	//ApiResponse getAllAssetBillingById(Map<String, Object> filter, Pageable pageable);

	//ApiResponse getAllAssetBillingById(String assetId);

	ApiResponse getAllAssetBillingByAssetId(String assetId);

	ApiResponse returnFromRepair(AssetBilling assetBilling, String assetBillId);

	

		

	//ApiResponse getAllAssetBillingList(Map<String, Object> filter, Pageable pageable);

	/*ApiResponse editPurchaseAssetBill(AssetBillingRequest assetBilling);

	ApiResponse addRepairAssetBill(AssetBillingRequest assetBilling);

	ApiResponse editRepairAssetBill(AssetBillingRequest assetBilling);

	ApiResponse returnFromRepair(String assetBillId);*/

	//ApiResponse getAllAssetBillingList();

	//ApiResponse getAllAssetBillingList(Pageable pageable);

	//ApiResponse getAllAssetBillingList(org.springframework.data.domain.Pageable pageable);

	//ApiResponse getAllAssetBillingList(Pageable pageable);

	//ApiResponse getAllAssetBillingList(org.springframework.data.domain.Pageable pageable);

	
	
}