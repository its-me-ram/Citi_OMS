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

@Entity(name="oms_offer")
public class OfferTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int offerId;
	
	public String orderType;
	public double price;
	public int quantity;
	public String aon;
	
	//2020-09-27 20:16:49.441
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.sss")
	Date date=new Date();
	
	public int orderId;
	
	
	/*@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name="orderId")
	private OrderGenerator ordergenerator;*/


	
	public OfferTable() {
		
	}




	public OfferTable(int offerId, String orderType, double price, int quantity, String aon, Date date, int orderId) {
		super();
		this.offerId = offerId;
		this.orderType = orderType;
		this.price = price;
		this.quantity = quantity;
		this.aon = aon;
		this.date = date;
		this.orderId = orderId;
	}




	public String getAon() {
		return aon;
	}




	public void setAon(String aon) {
		this.aon = aon;
	}




	public int getOfferId() {
		return offerId;
	}



	public String getOrderType() {
		return orderType;
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



	

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}



	public void setOrderType(String orderType) {
		this.orderType = orderType;
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







	@Override
	public String toString() {
		return "OfferTable [offerId=" + offerId + ", orderType=" + orderType + ", price=" + price + ", quantity="
				+ quantity + ", aon=" + aon + ", date=" + date + ", orderId=" + orderId + "]";
	}



	

	
	
	
	
}
