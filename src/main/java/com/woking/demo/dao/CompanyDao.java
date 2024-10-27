package com.woking.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.woking.demo.dto.CompanyResponseFindDto;
import com.woking.demo.entity.CompanyEntity;

/*
 *	The getTopCompanies method use to find 4 companies have the largest number of recruited employees 
 *			and show in home page.
 * 
 * 	The findCompanybyKeyword method using find company entity in home page. 
 * 
 *  
 */

@Repository
public interface CompanyDao extends JpaRepository<CompanyEntity, Integer> {

	Optional<CompanyEntity> findByName(String nameCompany);

	@Query(value = "SELECT c.id, c.name_company, c.logo, SUM(r.quantity) AS total_quantity " +
			"FROM Company c " +
			"INNER JOIN recruitment r ON c.id = r.company_id " +
			"GROUP BY c.id, c.name_company " +
			"ORDER BY total_quantity DESC LIMIT 3", nativeQuery = true)
	List<Object[]> getTopCompanies();

	@Query("SELECT c FROM CompanyEntity c " +
			"WHERE " +
			"c.name LIKE %?1% OR c.email LIKE %?1% OR c.phoneNumber LIKE %?1% " +
			"OR c.address LIKE %?1% OR c.description LIKE %?1% " +
			"OR (CONCAT(c.name, ' ', c.email, ' ', c.phoneNumber, ' ', c.address, ' ', c.description) LIKE %?1%)")
	Page<CompanyEntity> findCompanybyKeyword(String input, Pageable pageable);

	// Using test nothing to do in this project
	@Query("SELECT c FROM CompanyEntity c " +
			"WHERE (CONCAT(c.name, ' ', c.email, ' ', c.phoneNumber, ' ', c.address, ' ', c.description) LIKE %?1%) " +
			"OR c.name LIKE %?1% OR c.email LIKE %?1% OR c.phoneNumber LIKE %?1% " +
			"OR c.address LIKE %?1% OR c.description LIKE %?1%")
	List<CompanyEntity> findCompanybyKeyword(String input);

	/*
	 * Better ways to get Company form data.
	 */
	@Query("SELECT new com.woking.demo.dto.CompanyResponseFindDto(c.id, c.name, c.phoneNumber,c.address, c.email, c.logo) "
			+
			"FROM CompanyEntity c " +
			"WHERE " +
			"c.name LIKE %?1% OR c.email LIKE %?1% OR c.phoneNumber LIKE %?1% " +
			"OR c.address LIKE %?1% OR c.description LIKE %?1% " +
			"OR (CONCAT(c.name, ' ', c.email, ' ', c.phoneNumber, ' ', c.address, ' ', c.description) LIKE %?1%)")
	Page<CompanyResponseFindDto> findCompanybyKeywordToDTO(String input, Pageable pageable);
}
