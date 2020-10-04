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

@Entity(name="oms_bid")
public class BidTable {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int bidId;
	
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
	private OrderGenerator ordergenerator;
*/
	

	
	public BidTable() {
		
	}




	public BidTable(int bidId, String orderType, double price, int quantity, String aon, Date date, int orderId) {
		super();
		this.bidId = bidId;
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




	public int getBidId() {
		return bidId;
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







	public void setBidId(int bidId) {
		this.bidId = bidId;
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
		return "BidTable [bidId=" + bidId + ", orderType=" + orderType + ", price=" + price + ", quantity=" + quantity
				+ ", aon=" + aon + ", date=" + date + ", orderId=" + orderId + "]";
	}





	
	
	
	
}
