package com.citi.service;

import java.util.List;

import com.citi.entity.BidTable;
import com.citi.entity.OfferTable;

public interface IBidService {

	List<BidTable> findAllOrderByPriceDescDateAsc();	
	//List<OfferTable> findAllOrderByPriceDescDateAsc();	
	
	
}
