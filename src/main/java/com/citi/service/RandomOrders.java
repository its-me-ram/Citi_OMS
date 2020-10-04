package com.citi.service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RandomOrders {
	

	public String orderType;
	public String bid_offer;
	public double price;
	public int quantity;
	public String aon;
	
	//@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	Date date=new Date();
	
	
	static Random r = new Random();
	
	//ordertype
	public String getOrderType() {
		boolean type = r.nextBoolean();
		
		if(type==true)
			orderType="limit";
		else
			orderType="market";
		
		
		return orderType;
	}


	//offerbid
	public String getBid_offer() {
		
		boolean type = r.nextBoolean();
		if(type==true)
			bid_offer="bid";
		else
			bid_offer="offer";
		
		return bid_offer;
	}

	//Aon
	public String getAon() {
		
		boolean type = r.nextBoolean();
		if(type==true)
			aon="Yes";
		else
			aon="No";
		
		return aon;
	}
	
	//price //circuit check satisfied
	public double getPrice() {
		double p = roundTwoDecimals(getRandomDoubleBetweenRange(3000.00,3020.00));
		return Math.round(p * 2) / 2.0;
	}


	public int getQuantity() {
		
		int q=generateRandomIntIntRange(1,5000);
		return q;
	}


	public Date getDate() {
		//2020-09-27 20:16:49.441
		Date date=new Date();
		
		long time = date.getTime();
        //Passed the milliseconds to constructor of Timestamp class 
	    Timestamp ts = new Timestamp(time);
	    
		return ts;
	}


	public static int generateRandomIntIntRange(int min, int max) {
	    return r.nextInt((max - min) + 1) + min;
	}
	
	public static double getRandomDoubleBetweenRange(double min, double max){
	    double x = (Math.random()*((max-min)+1))+min;
	    return x;
	}
	
	public static double roundTwoDecimals(double d) 
	{ 
	 DecimalFormat twoDForm = new DecimalFormat("#.##"); 
	 
	 return Double.valueOf(twoDForm.format(d));  
	} 
	

	
	
	
	
}
