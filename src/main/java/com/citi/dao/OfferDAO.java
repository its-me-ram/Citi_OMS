package com.citi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.citi.entity.OfferTable;

@Repository
public interface OfferDAO extends CrudRepository<OfferTable, Integer> {

	@Query("FROM oms_offer ORDER BY price asc,date asc")
	List<OfferTable> findAllOrderByPriceAscDateAsc();
}
