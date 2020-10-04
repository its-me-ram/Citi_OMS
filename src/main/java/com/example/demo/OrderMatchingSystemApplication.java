package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.citi.dao.BidDAO;
import com.citi.dao.ExecutedDAO;
import com.citi.dao.OfferDAO;
import com.citi.dao.OrderDAO;
import com.citi.dao.PendingDAO;
import com.citi.dao.RejectedDAO;

@SpringBootApplication(scanBasePackages = "com.citi")
@EntityScan(basePackages = "com.citi")
@EnableJpaRepositories(basePackages = "com.citi")
public class OrderMatchingSystemApplication {

	
	@Autowired
	OrderDAO orderDAO;
	
	@Autowired
	BidDAO biddao;
	
	@Autowired
	OfferDAO offerdao;
	
	@Autowired
	ExecutedDAO executeddao;
	
	@Autowired
	RejectedDAO rejecteddao;
	
	@Autowired
	PendingDAO pendingdao;
	
	public static void main(String[] args) {
		SpringApplication.run(OrderMatchingSystemApplication.class, args);
	
	}

}
