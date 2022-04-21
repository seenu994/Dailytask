package com.xyram.ticketingTool.Repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xyram.ticketingTool.entity.AssetSoftware;

@Repository
public interface AssetSoftwareRepository extends JpaRepository<AssetSoftware, String> {

	
	@Query("Select distinct new map(m.softwareName as softwareName,s.installDate as installDate,"		
        + "s.uninstallDate as uninstallDate,s.assetSoftwareStatus as assetSoftwareStatus) "
  	    + " from AssetSoftware s left join SoftwareMaster m on s.softwareId = m.softwareId "
  	    + " where s.assetId =:assetId")
	Page<Map> getAssetSoftwareById(String assetId, Pageable pageable);

	@Query("SELECT distinct s from AssetSoftware s where s.assetId =:assetId AND s.softwareId =:softwareId")
	AssetSoftware getByAssetId(String assetId, String softwareId);

	@Query("SELECT distinct s from AssetSoftware s where s.assetId =:assetId AND s.softwareId =:softwareId")
	AssetSoftware getSoftByAssetId(String assetId, String softwareId);

	

}