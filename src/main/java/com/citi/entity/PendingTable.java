package com.citi.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="oms_pending")
public class PendingTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int pendingId;
	
	public int orderId;
	
	public String orderType;
	public String bid_offer;
	public double price;
	public int quantity;
	public String aon;
	
	//2020-09-27 20:16:49.441
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	Date date=new Date();


	public PendingTable() {
		
	}


	




	public PendingTable(int pendingId, int orderId, String orderType, String bid_offer, double price, int quantity,
			String aon, Date date) {
		super();
		this.pendingId = pendingId;
		this.orderId = orderId;
		this.orderType = orderType;
		this.bid_offer = bid_offer;
		this.price = price;
		this.quantity = quantity;
		this.aon = aon;
		this.date = date;
	}







	public int getPendingId() {
		return pendingId;
	}


	public String getOrderType() {
		return orderType;
	}


	public String getBid_offer() {
		return bid_offer;
	}


	public double getPrice() {
		return price;
	}


	public int getQuantity() {
		return quantity;
	}


	public Date getDate() {
		return date;
	}


	public void setPendingId(int pendingId) {
		this.pendingId = pendingId;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}


	public void setBid_offer(String bid_offer) {
		this.bid_offer = bid_offer;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public void setDate(Date date) {
		this.date = date;
	}











	public int getOrderId() {
		return orderId;
	}











	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}











	public String getAon() {
		return aon;
	}







	public void setAon(String aon) {
		this.aon = aon;
	}







	@Override
	public String toString() {
		return "PendingTable [pendingId=" + pendingId + ", orderId=" + orderId + ", orderType=" + orderType
				+ ", bid_offer=" + bid_offer + ", price=" + price + ", quantity=" + quantity + ", aon=" + aon
				+ ", date=" + date + "]";
	}


	
	
	
	
	
}
