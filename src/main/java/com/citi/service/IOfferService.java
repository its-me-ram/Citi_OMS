package com.citi.service;

import java.util.List;

import com.citi.entity.OfferTable;

public interface IOfferService {

	List<OfferTable> findAllOrderByPriceAscDateAsc();	
}
