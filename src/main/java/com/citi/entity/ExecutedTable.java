package com.citi.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="oms_executed")
public class ExecutedTable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int tradeId;
	
	
	
	public String orderType;
	
	public double price;
	public int quantity;
	public int bidId;
	public int offerId;
	
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	Date date=new Date();
	
	
	/*@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name="orderId")
	private OrderGenerator ordergenerator;*/


	public ExecutedTable() {
		//super();
	}


	


	
	


	public ExecutedTable(int tradeId, String orderType, double price, int quantity, int bidId, int offerId, Date date) {
		
		this.tradeId = tradeId;
		this.orderType = orderType;
		this.price = price;
		this.quantity = quantity;
		this.bidId = bidId;
		this.offerId = offerId;
		this.date = date;
	}









	

	public int getBidId() {
		return bidId;
	}









	public int getOfferId() {
		return offerId;
	}









	public void setBidId(int bidId) {
		this.bidId = bidId;
	}









	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}









	@Override
	public String toString() {
		return "ExecutedTable [tradeId=" + tradeId + ", orderType=" + orderType + ", price=" + price + ", quantity="
				+ quantity + ", bidId=" + bidId + ", offerId=" + offerId + ", date=" + date + "]";
	}









	public int getTradeId() {
		return tradeId;
	}


	public void setTradeId(int tradeId) {
		this.tradeId = tradeId;
	}


	public String getOrderType() {
		return orderType;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}


	


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	

	
	
}
