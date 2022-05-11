package com.xyram.ticketingTool.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xyram.ticketingTool.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository< Role, String>{
	@Query("Select new map(e.Id as id,e.roleName as roleName,e.status as status) from Role e WHERE e.id NOT IN ('R1', 'R7') ")
	List<Map> getAllRoleList();

	
	@Query("select e from Role e where e.Id =:roleId")
	Role getRoleName(String roleId);

}



//@Query("Select new map(e.Id as id,e.designationName as designationName) from Designation e where Id != 'D7'")
//Page<Map> getAllDesignationList(Pageable pageable);
