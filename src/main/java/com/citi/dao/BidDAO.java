package com.citi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.citi.entity.BidTable;

@Repository
public interface BidDAO extends CrudRepository<BidTable, Integer>{
	
	
	@Query("FROM oms_bid ORDER BY price desc,date asc")
	List<BidTable> findByOrderByPriceDescDateAsc();

}
