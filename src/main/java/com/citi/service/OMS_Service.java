package com.citi.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Timestamp;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.stereotype.Service;

import com.citi.dao.BidDAO;
import com.citi.dao.ExecutedDAO;
import com.citi.dao.OfferDAO;
import com.citi.dao.OrderDAO;
import com.citi.dao.PendingDAO;
import com.citi.dao.RejectedDAO;
import com.citi.entity.BidTable;
import com.citi.entity.ExecutedTable;
import com.citi.entity.OfferTable;
import com.citi.entity.OrderGenerator;
import com.citi.entity.PendingTable;
import com.citi.entity.RejectedTable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;

@Service
public class OMS_Service implements IBidService, IOfferService 
{

	//Logger logger = LoggerFactory.getLogger(OMS_Service.class);

	// to insert data in db

	@Autowired
	private OrderDAO orderdao;

	@Autowired
	private static BidDAO biddao;

	@Autowired
	private static OfferDAO offerdao;

	@Autowired
	private static ExecutedDAO executeddao;
	
	@Autowired
	private static RejectedDAO rejecteddao;
	
	
	@Autowired
	private static PendingDAO pendingdao;
	
	
	public static int bidcount=0;
	public static int offercount=0;

	//apply Mapping
	public static double ltp=0;
	public static double ltq=0;
	

	

	public OMS_Service(OrderDAO orderdao, BidDAO biddao, OfferDAO offerdao, ExecutedDAO executeddao,
			RejectedDAO rejecteddao, PendingDAO pendingdao) {
		this.orderdao = orderdao;
		this.biddao = biddao;
		this.offerdao = offerdao;
		this.executeddao = executeddao;
		this.rejecteddao = rejecteddao;
		this.pendingdao = pendingdao;
	}

	@PostConstruct
	public void checkdatabase() {
/*
		int orderId = 1;
		boolean exists = orderdao.existsById(orderId);

		if (exists == false) {
			loaddata();
	 	}
		
		
		//separatedata();

		int bidId = 1;
		boolean exists1 = biddao.existsById(orderId);

		if (exists1 == false) {
			separatedata();
		}
		System.out.println("***********************************************************************************************8in post construct");
		*/
		//OrderBook();

		// ************

	}

	public void loaddata() {        

		long startTime = System.currentTimeMillis();
		long endTime = startTime + (10000);

		while (System.currentTimeMillis() < endTime) {

			OrderGenerator order = new OrderGenerator();
			// get from generate order method
			RandomOrders ro = new RandomOrders();
			
			
			order.setBid_offer(ro.getBid_offer());
			order.setOrderType(ro.getOrderType());
			if(ro.orderType=="market")
			{
				order.setPrice(0.00);
				order.setAon("No");
			}
			else
			{
				order.setPrice(ro.getPrice());
				order.setAon(ro.getAon());
			}
			
			order.setQuantity(ro.getQuantity());
			
			
			order.setDate(ro.getDate());

			orderdao.save(order);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	// put in bid / offer

	public static void separatedata() {

		// orderdao.findAll();
		
	
		List<PendingTable> order = pendingdao.findAll();
		
		
		for (PendingTable i : order) {
			int id = i.getOrderId();
			String category = i.getBid_offer();
			String type = i.getOrderType();
			double price = i.getPrice();
			int q = i.getQuantity();
			String aon = i.getAon();	// Included for Aon
			Date d = i.getDate();

			if (i.getBid_offer().equals("bid")) {
				System.out.println(i.getBid_offer() + " if");
				BidTable bid = new BidTable();
				bid.setOrderType(type);
				bid.setPrice(price);
				bid.setQuantity(q);
				bid.setAon(aon);		// Included for Aon
				bid.setDate(d);
				bid.setOrderId(i.getOrderId());

				biddao.save(bid);
			} else {
				System.out.println(i.getBid_offer() + " else");
				OfferTable offer = new OfferTable();
				offer.setOrderType(type);
				offer.setPrice(price);
				offer.setQuantity(q);
				offer.setAon(aon);  	// Included for Aon
				offer.setDate(d);
				offer.setOrderId(i.getOrderId());

				offerdao.save(offer);
			}

		}

	}

	@Override
	public List<BidTable> findAllOrderByPriceDescDateAsc() {
		// TODO Auto-generated method stub
		//logger.debug("in list of bid table method");
		return biddao.findByOrderByPriceDescDateAsc();
	}

	@Override
	public List<OfferTable> findAllOrderByPriceAscDateAsc() {
		// TODO Auto-generated method stub
		return offerdao.findAllOrderByPriceAscDateAsc();
	}

	public void saveExecuted() {

	}
	
	
	public void export_to_excel()
	
	{
		Date d=orderdao.findById(1).get().getDate();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss a");  
		String strDate = dateFormat.format(d);  
		
		String efp = strDate+".xlsx";
		
		System.out.println(efp);
		String excelFilePath = efp;
		
		List<OrderGenerator> result = new ArrayList<>();
		result = orderdao.findAll();
		
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("RandomOrders");
        
        writeHeaderLine(sheet);
        FileOutputStream outputStream;
		
		
        try {
        	outputStream = new FileOutputStream(excelFilePath);
			writeDataLines(result, workbook, sheet);
			workbook.write(outputStream);
			workbook.close();
		} 
        
        catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
 	}
	private void writeHeaderLine(XSSFSheet sheet) {
		 
        Row headerRow = sheet.createRow(0);
 
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Order Id");
 
        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("Order Type");
 
        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("Bid/Offer");
 
        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("Price");
 
        headerCell = headerRow.createCell(4);
        headerCell.setCellValue("Quantity");
        
        headerCell = headerRow.createCell(5);
        headerCell.setCellValue("Aon");
        
        headerCell = headerRow.createCell(6);
        headerCell.setCellValue("Timestamp");
    }
	
	private void writeDataLines(List<OrderGenerator> result, XSSFWorkbook workbook,
            XSSFSheet sheet) throws SQLException {
        int rowCount = 1;
        
        for(int i=0; i<result.size();i++)
         {
        	
        	int oid = result.get(i).orderId;
        	String otype = result.get(i).orderType;
        	String bo = result.get(i).bid_offer;
        	double p = result.get(i).price;
        	int q = result.get(i).quantity;
        	String an = result.get(i).aon;
        	Date d = result.get(i).getDate();
        	
    
            Row row = sheet.createRow(rowCount++);
 
            int columnCount = 0;
            Cell cell = row.createCell(columnCount++);
            cell.setCellValue(oid);
 
            cell = row.createCell(columnCount++);
            cell.setCellValue(otype);
 
            cell = row.createCell(columnCount++);
            cell.setCellValue(bo);
            
            cell = row.createCell(columnCount++);
            cell.setCellValue(p);
            
            cell = row.createCell(columnCount++);
            cell.setCellValue(q);
            
            cell = row.createCell(columnCount++);
            cell.setCellValue(an);
            
                  
            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell(columnCount++);
            cell.setCellValue(String.valueOf(d));
             
        }
    }
 

	
	public void OrderBook()
	{
		List<OrderGenerator> order = orderdao.findAll();
		List<BidTable> bid=new ArrayList<>();
		List<OfferTable> offer=new ArrayList<>();
		System.out.println("\n Importing Orders: ");
		
		int id=0;
		String category=null;
		String type=null;
		double price=0;
		int q=0;
		String aon="No";		// Included for Aon
		Date d=null;
		int offerid = 1;
		int bidid = 1;

		
		for(OrderGenerator o: order)
		{
			System.out.println("\n*********************************************************** order o");
			id=o.getOrderId();
			category=o.getBid_offer();
			type=o.getOrderType();
			price=o.getPrice();
			q=o.getQuantity();
			aon=o.getAon();		// Included for Aon
			d=o.getDate();
			
			
		
			
			
			// if bid
			if (category.equals("bid")) 
			{
				System.out.println("\n***********************inside bid");
				if(offercount==0)
				{
					if(type.equals("limit"))
					{
						System.out.println("\n***********************inside limit");
					BidTable temp = new BidTable(bidid,type, price, q,aon,d,id);	// Included for Aon
					bid.add(temp);
					bidid++;
					bidcount++;
					Collections.sort(bid,Comparator.comparingDouble(BidTable::getPrice).reversed());

					}
					else
					{
						//put in rejected
						RejectedTable rejected=new RejectedTable();
						
						rejected.setBid_offer("bid");
						rejected.setDate(d);
						rejected.setOrderType(o.orderType);
						rejected.setPrice(0);
						rejected.setQuantity(o.quantity);
						rejected.setAon(aon);				// Included for Aon
						rejecteddao.save(rejected);
						
						
						
						System.out.println("***********************************No Offer - order is rejected");
					}
				}
				else
				{
					BidTable temp = new BidTable(bidid,type, price, q,aon,d,id);		// Included for Aon
					bid.add(temp);
					bidid++;
					bidcount++;
					Collections.sort(bid,Comparator.comparingDouble(BidTable::getPrice).reversed());

					System.out.println("**********************************************yamuna****"+ temp.getAon());
					OrderMatching(bid, offer,"bid", temp.getAon());
				}
				
			}

			else // if offer
			{
				System.out.println("\n***********************inside offer");
				if(bidcount==0)
				{
					if(type.equals("limit"))
					{
					System.out.println("\n***********************inside offer limit");
					OfferTable temp = new OfferTable(offerid,type, price, q,aon,d,id);	// Included for Aon
					offer.add(temp);
					offercount++;
					offerid++;
					
					Collections.sort(offer,Comparator.comparingDouble(OfferTable::getPrice));

					}
					else
					{
						//put in rejected
						RejectedTable rejected=new RejectedTable();
						
						rejected.setBid_offer("offer");
						rejected.setDate(new Date());
						rejected.setOrderType(o.orderType);
						rejected.setPrice(0);
						rejected.setQuantity(o.quantity);
						rejected.setAon(aon);	// Included for Aon
 
						rejecteddao.save(rejected);
						
						System.out.println("*************************No Bid - order is rejected");

					}
				}
				else
				{
					OfferTable temp = new OfferTable(offerid,type, price, q,aon,d,id);	// Included for Aon
					offer.add(temp);
					offerid++;
					offercount++;

					Collections.sort(offer,Comparator.comparingDouble(OfferTable::getPrice));
					OrderMatching(bid, offer, "offer",temp.aon);
				}
				
			}

			System.out.println("**********"+bid);
			System.out.println("**********"+offer);
		
		}
		
		
		for(int i=0;i<bid.size();i++)
		{
			PendingTable pendingtable=new PendingTable();
			
			pendingtable.setOrderId(bid.get(i).orderId);
			pendingtable.setBid_offer("bid");
			pendingtable.setDate(new Date());
			pendingtable.setOrderType(bid.get(i).orderType);
			pendingtable.setPrice(bid.get(i).price);
			pendingtable.setQuantity(bid.get(i).quantity);
			pendingtable.setAon(bid.get(i).aon);		// Included for Aon
			
			System.out.println(bid.get(i)+"\n saving to bid");
			
			pendingdao.save(pendingtable);
		}
		for(int j=0;j<offer.size();j++)
		{
			PendingTable pendingtable=new PendingTable();
			
			pendingtable.setOrderId(offer.get(j).orderId);
			pendingtable.setBid_offer("offer");
			pendingtable.setDate(new Date());
			pendingtable.setOrderType(offer.get(j).orderType);
			pendingtable.setPrice(offer.get(j).price);
			pendingtable.setQuantity(offer.get(j).quantity);
			pendingtable.setAon(offer.get(j).aon);		// Included for Aon

			
			System.out.println(offer.get(j)+"\n saving to offer");
			
			pendingdao.save(pendingtable);
		}
		
		
	}

	
	public static void aonFunc(List<BidTable> bid, List<OfferTable> offer, String cat) {

		
		if (cat.equals("bid")) 
		{
			int flag=0;
			Iterator<OfferTable> i = offer.iterator();
			while (i.hasNext()) {
				OfferTable of = i.next();
				if (of.price <= bid.get(bid.size() - 1).price) {
				
					if (bid.get(bid.size() - 1).quantity == of.quantity) {
//All or None execution
						System.out.println(
								"Bid id" + bid.get(bid.size() - 1).bidId + "matched w offer id: " + of.offerId);
						System.out.println("HAHA Peter *************");
						ExecutedTable executed=new ExecutedTable();
						executed.setBidId(bid.get(bid.size() - 1).bidId);
						executed.setOfferId(of.offerId);
						executed.setPrice(of.price);
						executed.setQuantity(of.quantity);
						executed.setDate(new Date());
						executeddao.save(executed);
						ltp=of.price;
						ltq=of.quantity;
						
						
						i.remove();
						bid.remove(bid.size() - 1);
						bidcount--;
						offercount--;
						flag = 1;
						//put in executed list
						
									
						break;

					} else if (bid.get(bid.size() - 1).quantity > of.getQuantity()) {
					int qty_offers = 0;
					
					for (OfferTable o : offer) 
					{
						
					 if(o.price<=bid.get(bid.size() - 1).price)
						{
						 
						 if(o.aon.equals("Yes"))
						 {
	//Harish test case changes		// if((offer.get(0).quantity-qty_offers)>o.quantity) //
							 if((bid.get(bid.size() - 1).quantity-qty_offers)>o.quantity)
							 {
								 qty_offers=qty_offers+ o.quantity;
								 
							 }
						 }
						 else
						 {
							 qty_offers=qty_offers+ o.quantity;
						 }
						 
							
						 }
					 }
					if (qty_offers < bid.get(bid.size() - 1).quantity) {
						System.out.println("Bid id:" + bid.get(bid.size() - 1).bidId
								+ "  rejected since the quantity can't be satisfied");
		
						 System.out.println("No match found for Bid id: " + bid.get(bid.size() - 1).bidId
									+ " Entering into Bid pending list");
						 
	//yamuna					 RejectedTable rejected=new RejectedTable();
						/*	
							rejected.setBid_offer("bid");
							rejected.setDate(new Date());
							rejected.setOrderType(bid.get(bid.size() - 1).orderType);
							rejected.setPrice(bid.get(bid.size() - 1).price);
							rejected.setQuantity(bid.get(bid.size() - 1).quantity);
							rejecteddao.save(rejected);
						 */
						 //remove from bid
//						enter into rejected q
						 
 			//	bid.remove( bid.get(bid.size() - 1));
					//	bidcount--;
						
					} else {
						System.out.println("Partial execution of Bid w bid id: " + bid.get(bid.size() - 1).bidId
								+ "has been matched w offerId: " + of.getOfferId());
						int qty_rem = bid.get(bid.size() - 1).quantity - of.getQuantity();
						// bid.get(bid.size() - 1).quantity=qty_rem;
						// of_min.setQuantity(qty_rem);
						bid.get(bid.size() - 1).quantity = qty_rem;
						
						ExecutedTable executed=new ExecutedTable();
						executed.setBidId(bid.get(bid.size() - 1).bidId);
						executed.setOfferId(of.offerId);
						executed.setPrice(of.price);
						executed.setQuantity(of.quantity);
						executed.setDate(new Date());
						executeddao.save(executed);
						ltp=of.price;
						ltq=of.quantity;
						
						offer.removeIf(obj -> obj.offerId == of.getOfferId());
						//put in executed list
						offercount--;
					}

				} else if (bid.get(bid.size() - 1).quantity < of.getQuantity() && of.aon.equals("No")) {
					
					System.out.println("Bid w bid id: " + bid.get(bid.size() - 1).bidId + "has been matched w offerId: "
							+ of.getOfferId());
					int qty_rem = of.quantity - bid.get(bid.size() - 1).quantity;
					of.quantity = qty_rem;
					
					ExecutedTable executed=new ExecutedTable();
					executed.setBidId(bid.get(bid.size() - 1).bidId);
					executed.setOfferId(of.offerId);
					executed.setPrice(of.price);
					executed.setQuantity(bid.get(bid.size() - 1).quantity);
					executed.setDate(new Date());
					executeddao.save(executed);
					ltp=of.price;
					ltq=bid.get(bid.size() - 1).quantity;
					
					bid.remove(bid.size() - 1);
					bidcount--;
					flag = 1;
					//put in executed list
					break;
				}

			}
//			if (flag == 0) {
//				System.out.println("No match found for bid id: " + bid.get(bid.size() - 1).bidId
//						+ " Entering into Bid pending list");

//			}

		}
	}
		// 
		else if(cat.equals("offer"))
		{
			int flag1=0;
			Iterator<BidTable> i = bid.iterator();

			while (i.hasNext()) {
				BidTable bi = i.next();
				if (offer.get(0).price <= bi.price) {

					if (offer.get(0).quantity == bi.quantity) {
						System.out.println(
								"Offer id" + offer.get(0).offerId + "matched w bid id: " + bi.bidId);
						
						ExecutedTable executed=new ExecutedTable();
						executed.setBidId(bi.bidId);
						executed.setOfferId(offer.get(0).offerId);
						executed.setPrice(bi.price);
						executed.setQuantity(bi.quantity);
						executed.setDate(new Date());
						executeddao.save(executed);
						ltp=bi.price;
						ltq=bi.quantity;
						
						i.remove();
						offer.remove(0);
						bidcount--;
						offercount--;
						
						flag1 = 1;
						//put in executed list
						break;

					} else if (offer.get(0).quantity > bi.quantity ) {
						 int qty_bid=0;


						for(BidTable b:bid){
							
						 if(offer.get(0).price<=b.price)
							{
							 
							 if(b.aon.equals("Yes"))
							 {
								 if((offer.get(0).quantity-qty_bid)>=b.quantity)
								 {
									 qty_bid=qty_bid+ b.quantity;
									 
								 }
							 }
							 else
							 {
								 qty_bid=qty_bid+ b.quantity;
							 }
							 
								
							 }
						}
//							 
					//}		 
//							 ****
//							 if((offer.get(0).quantity>b.quantity ))
//							 {
//								 qty_bid+=b.quantity;
//							 }
//							 else if(offer.get(0).quantity<b.quantity && b.aon.equals("No"))
//							 {
//								 qty_bid+=offer.get(0).quantity;
//							 }
//							 else if(offer.get(0).quantity==b.quantity)
//							 {
//								 qty_bid+=offer.get(0).quantity;
//							 }

							 
							
						 
						 if(qty_bid<offer.get(0).quantity )
						 {
//						 System.out.println("Bid id: "+bid.get(bid.size() - 1).bidId+"rejected since the quantity can't be satisfied");
						 System.out.println(" No match found for Offer id: " + offer.get(0).offerId
									+ " Entering into Offer pending list");
//						 offer.remove(offer.get(0));
//						 put in rejected orders
//						 offercount--;
						 }
						 else{
						System.out.println("Partial execution of offer w bid id: " + bi.bidId);
						int qty_rem = offer.get(0).quantity - bi.quantity;
						offer.get(0).quantity = qty_rem;
						
						ExecutedTable executed=new ExecutedTable();
						executed.setBidId(bi.bidId);
						executed.setOfferId(offer.get(0).offerId);
						executed.setPrice(bi.price);
						executed.setQuantity(bi.quantity);
						executed.setDate(new Date());
						executeddao.save(executed);
						ltp=bi.price;
						ltq=bi.quantity;
						
						
						i.remove();
						//put in executed list
						bidcount--;
						 }

					}
			
					else if (offer.get(0).quantity < bi.getQuantity() && bi.aon.equals("No")) {
//						System.out.println("loop :");
						System.out.println(
								"***Here 2 Offer id" + offer.get(0).offerId + "matched w bid id: " + bi.bidId);
						int qty_rem = bi.getQuantity() - offer.get(0).quantity;
						bi.setQuantity(qty_rem);

						ExecutedTable executed=new ExecutedTable();
						executed.setBidId(bi.bidId);
						executed.setOfferId(offer.get(0).offerId);
						executed.setPrice(offer.get(0).price);
						executed.setQuantity(offer.get(0).quantity);
						executed.setDate(new Date());
						executeddao.save(executed);
						ltp=bi.price;
						ltq=offer.get(0).quantity;
						
						offer.remove(0);
						offercount--;
						//put in executed list
						flag1 = 1;
						break;
//					}
//					else
//					{
//						("")
//					}
					
				}
					else if (offer.get(0).quantity < bi.getQuantity() && bi.aon.equals("Yes")) 
					{
						System.out.println("haha");
						continue;
					}
					
//				if (flag1 == 0) {
//					System.out.println("No match found for offer id: " + offer.get(0).offerId
//							+ " Entering into offer pending list");
//				}
			

					
		}
			}}
//################			if(offer.get(offer.size()-1).orderType=="m")
			

		}

	
	
	public static void OrderMatching(List<BidTable> bid, List<OfferTable> offer, String str, String aon) {

		if (str.equals("bid")) {
			int flag = 0;
			if (bid.get(bid.size() - 1).orderType.equals("limit")) {
//				System.out.println("*********\n in bid");
				if (aon.equals("Yes")) {
//					System.out.println("*********\n in bid aon");
					aonFunc(bid, offer, "bid"); 
				} else if (aon.equals("No")) {
				
					Collections.sort(offer,Comparator.comparingDouble(OfferTable::getPrice));
					Iterator<OfferTable> i = offer.iterator();
					
					while (i.hasNext()) {
						OfferTable of = i.next();
						if (of.price <= bid.get(bid.size() - 1).price) {

							if (bid.get(bid.size() - 1).quantity == of.quantity) {
								System.out.println(
										"Bid id" + bid.get(bid.size() - 1).bidId + "matched w offer id: " + of.offerId);
								
								ExecutedTable executed=new ExecutedTable();
								executed.setBidId(bid.get(bid.size() - 1).bidId);
								executed.setOfferId(of.offerId);
								executed.setPrice(of.price);
								executed.setQuantity(of.quantity);
								executed.setDate(new Date());
								executeddao.save(executed);
								ltp=of.price;
								ltq=of.quantity;
								
								i.remove();
								bid.remove(bid.size() - 1);
								bidcount--;
								offercount--;
								flag = 1;
								break;

							} else if (bid.get(bid.size() - 1).quantity > of.quantity /*&& of.aon.equals("No")*/) {

								System.out.println("Partial execution of bid w offer id: " + of.offerId);
								int qty_rem = bid.get(bid.size() - 1).quantity - of.quantity;
								bid.get(bid.size() - 1).quantity = qty_rem;
								
								ExecutedTable executed=new ExecutedTable();
								executed.setBidId(bid.get(bid.size() - 1).bidId);
								executed.setOfferId(of.offerId);
								executed.setPrice(of.price);
								executed.setQuantity(of.quantity);
								executed.setDate(new Date());
								
								executeddao.save(executed);
								ltp=of.price;
								ltq=of.quantity;
								
								i.remove();

								offercount--;

							} else if (bid.get(bid.size() - 1).quantity < of.quantity && of.aon.equals("No")) {
								System.out.println("Partial execution");
								int qty_rem = of.quantity - bid.get(bid.size() - 1).quantity;
								of.quantity = qty_rem;
								
								ExecutedTable executed=new ExecutedTable();
								executed.setBidId(bid.get(bid.size() - 1).bidId);
								executed.setOfferId(of.offerId);
								executed.setPrice(of.price);
								executed.setQuantity(bid.get(bid.size() - 1).quantity);
								executed.setDate(new Date());
								
								executeddao.save(executed);
								ltp=of.price;
								ltq=bid.get(bid.size() - 1).quantity;
								
								bid.remove(bid.size() - 1);
								bidcount--;
								flag = 1;
								break;
							}

						}

					}
					if (flag == 0) {
						System.out.println("No match found for bid id: " + bid.get(bid.size() - 1).bidId
								+ " Entering into Bid pending list");

					}
				}

			}

			// Market bid
			else if (bid.get(bid.size() - 1).orderType.equals("market")) {
				List<OfferTable> off_asc=new ArrayList<>();
				
				 for (int i = 0; i < offer.size(); i++) {
				        off_asc.add(offer.get(i));
				    }
				Collections.sort(off_asc,Comparator.comparingDouble(OfferTable::getPrice));
				
				System.out.println("offer_asc= "+off_asc.toString());
				Iterator<OfferTable> i = off_asc.iterator();

				while (i.hasNext()) {
//					Offer of_min = Collections.min(offer, Comparator.comparing(s -> s.getPrice()));
					OfferTable of_min=i.next();

					if (bid.get(bid.size() - 1).quantity == of_min.quantity) {

						System.out.println("Bid w bid id: " + bid.get(bid.size() - 1).bidId
								+ "has been matched w offerId: " + of_min.getOfferId());
						
						ExecutedTable executed=new ExecutedTable();
						executed.setBidId(bid.get(bid.size() - 1).bidId);
						executed.setOfferId(of_min.offerId);
						executed.setPrice(of_min.price);
						executed.setQuantity(of_min.quantity);
						executed.setDate(new Date());
						
						executeddao.save(executed);
						ltp=of_min.price;
						ltq=of_min.quantity;
						
						bid.remove(bid.size() - 1);
						offer.removeIf(obj -> obj.offerId == of_min.getOfferId());
						bidcount--;
						offercount--;
						flag = 1;
						break;

					} else if (bid.get(bid.size() - 1).quantity > of_min.getQuantity()) {
						int qty_offers = 0;
//						for (Offer o : off_asc) {
//							qty_offers += o.quantity;
//						}
						for (OfferTable o : off_asc) {//bid_max
							if(o.aon.equals("Yes"))
							{
								if(bid.get(bid.size() - 1).quantity-qty_offers>o.quantity)
									qty_offers+= o.quantity;
							}
							else
								qty_offers+= o.quantity;
						}
						if (qty_offers < bid.get(bid.size() - 1).quantity) {
							
							System.out.println("Bid id: " + bid.get(bid.size() - 1).bidId
									+ "rejected since the quantity can't be satisfied");
							
							RejectedTable rejected=new RejectedTable();
							
							rejected.setBid_offer("bid");
							rejected.setDate(new Date());
							rejected.setOrderType(bid.get(bid.size() - 1).orderType);
							rejected.setPrice(bid.get(bid.size() - 1).price);
							rejected.setQuantity(bid.get(bid.size() - 1).quantity);
							rejected.setAon(bid.get(bid.size() - 1).aon);
							rejecteddao.save(rejected);
							
							bid.remove(bid.size() - 1);

							bidcount--;
							break;
						} else {
							System.out.println("Partial execution of Bid w bid id: " + bid.get(bid.size() - 1).bidId
									+ "has been matched w offerId: " + of_min.getOfferId());
							int qty_rem = bid.get(bid.size() - 1).quantity - of_min.getQuantity();
							System.out.println("qty remaining for bidid: "+bid.get(bid.size() - 1).bidId+" = "+qty_rem);
							
							// bid.get(bid.size() - 1).quantity=qty_rem;
							// of_min.setQuantity(qty_rem);
							bid.get(bid.size() - 1).quantity = qty_rem;
							
							ExecutedTable executed=new ExecutedTable();
							executed.setBidId(bid.get(bid.size() - 1).bidId);
							executed.setOfferId(of_min.offerId);
							executed.setPrice(of_min.price);
							executed.setQuantity(of_min.quantity);
							executed.setDate(new Date());
									
							executeddao.save(executed);
							ltp=of_min.price;
							ltq=of_min.quantity;
							
							offer.removeIf(obj -> obj.offerId == of_min.getOfferId());
							offercount--;
						}

//									System.out.println("Partial execution of Bid w bid id: " + bid.get(bid.size() - 1).bidId
//											+ "has been matched w offerId: " + of_min.getOfferId());
//									int qty_rem = bid.get(bid.size() - 1).quantity - of_min.getQuantity();
//									//bid.get(bid.size() - 1).quantity=qty_rem;
//									//of_min.setQuantity(qty_rem);
//									bid.get(bid.size() - 1).quantity = qty_rem;
//									offer.removeIf(obj -> obj.offerId == of_min.getOfferId());
//									offercount--;

					} else if (bid.get(bid.size() - 1).quantity < of_min.getQuantity() && of_min.aon.equals("No")) {
						System.out.println("Bid w bid id: " + bid.get(bid.size() - 1).bidId
								+ "has been matched w offerId: " + of_min.getOfferId());
						int qty_rem = of_min.quantity - bid.get(bid.size() - 1).quantity;
						of_min.quantity = qty_rem;
			
						//change lala					
					/*	for (OfferTable o: offer) {
						    if (of_min.offerId==o.getOfferId()) {
						        o.setQuantity(of_min.quantity);//Id(bid_max.bid);
						    }
						}*/
						
						ExecutedTable executed=new ExecutedTable();
						executed.setBidId(bid.get(bid.size() - 1).bidId);
						executed.setOfferId(of_min.offerId);
						executed.setPrice(of_min.price);
						executed.setQuantity(bid.get(bid.size()-1).quantity);
						executed.setDate(new Date());
						
						executeddao.save(executed);
						ltp=of_min.price;
						ltq=bid.get(bid.size()-1).quantity;
						
						bid.remove(bid.size() - 1);
						bidcount--;
						flag = 1;
						break;
					}
					else if (bid.get(bid.size() - 1).quantity < of_min.getQuantity() && of_min.aon.equals("Yes") )
					{
						
						//System.out.println("dei dei 1 "+i.hasNext()+"i="+i.toString());
						 
//						i.next();
						if(i.hasNext()==false)
						{
							
							System.out.println("No matches found, rejecting");
//							System.out.println("in has next\n");
//							offer.remove(0);
//							offercount--;
		
						RejectedTable rejected =new RejectedTable();
													
							rejected.setQuantity(bid.get(bid.size()-1).quantity);
							rejected.setDate(new Date());
							rejected.setOrderType(bid.get(bid.size()-1).getOrderType());
							rejected.setAon((bid.get(bid.size()-1).aon));
							rejected.setPrice(bid.get(bid.size()-1).price);
							rejected.setBid_offer("bid");
							rejecteddao.save(rejected);
							
				//			ltp=of_min.price;
				//			ltq=bid.get(bid.size()-1).quantity;
							
							bid.remove(bid.size() - 1);
							bidcount--;
							break;
						
						}
						else
						{
							continue;
						}
					}

				}
//							if (flag == 0) {
//								System.out.println("No match found for bid id: " + bid.get(bid.size() - 1).bidId
//										+ " Entering into Bid pending list");
//
//							}

			}
		}
		//offer
		else if (str.equals("offer"))// offer
		{
			if (aon.equals("Yes")) {
				aonFunc(bid, offer, "offer");
			}
			//Limit offer without aon
			else if (offer.get(0).orderType.equals("limit"))// && aon.equals("No"))
			{
				int flag1 = 0;
				
				Collections.sort(bid,Comparator.comparingDouble(BidTable::getPrice).reversed());

				Iterator<BidTable> i = bid.iterator();

				while (i.hasNext()) {
					BidTable bi = i.next();
					if (offer.get(0).price <= bi.price) {

						if (offer.get(0).quantity == bi.quantity) {
							
							System.out.println(
									"Offer id" + offer.get(0).offerId + "matched w bid id: " + bi.bidId);
							flag1 = 1;
							
							ExecutedTable executed=new ExecutedTable();
							executed.setBidId(bi.bidId);
							executed.setOfferId(offer.get(0).offerId);
							executed.setPrice(offer.get(0).price);
							executed.setQuantity((offer.get(0)).quantity);
							executed.setDate(new Date());
							executeddao.save(executed);
							
							ltp=bi.price;
							ltq=offer.get(0).quantity;
							
							i.remove();
							offer.remove(0);
							bidcount--;
							offercount--;
							break;

						} else if (offer.get(0).quantity > bi.quantity && bi.aon.equals("No")) {

							System.out.println("Partial execution of offer w bid id: " + bi.bidId);
							int qty_rem = offer.get(0).quantity - bi.quantity;
							offer.get(0).quantity = qty_rem;
							
							ExecutedTable executed=new ExecutedTable();
							executed.setBidId(bi.bidId);
							executed.setOfferId(offer.get(0).offerId);
							executed.setPrice(offer.get(0).price);
							executed.setQuantity(bi.quantity);
							executed.setDate(new Date());
							executeddao.save(executed);
							
							ltp=offer.get(0).price;
							ltq=bi.quantity;
												
							i.remove();
							bidcount--;
						} 
						else if (offer.get(0).quantity > bi.quantity && bi.aon.equals("Yes")) {

							System.out.println("Partial Execution of offer id" + offer.get(offer.size()-1).offerId + "w bid id: " + bi.bidId);
							int qty_rem = offer.get(0).quantity - bi.quantity;
							offer.get(0).quantity = qty_rem;
							
							ExecutedTable executed=new ExecutedTable();
							executed.setBidId(bi.bidId);
							executed.setOfferId(offer.get(0).offerId);
							executed.setPrice(offer.get(0).price);
							executed.setQuantity(bi.quantity);
							executed.setDate(new Date());
							executeddao.save(executed);
							
							ltp=offer.get(0).price;
							ltq=bi.quantity;
							
							i.remove();
							bidcount--;
		
						} 
						
						else if (offer.get(0).quantity < bi.quantity && bi.aon.equals("No")) {
							System.out.println();
							int qty_rem = bi.quantity - offer.get(bid.size() - 1).quantity;
							bi.quantity = qty_rem;
							
							ExecutedTable executed=new ExecutedTable();
							executed.setBidId(bi.bidId);
							executed.setOfferId(offer.get(0).offerId);
							executed.setPrice(offer.get(0).price);
							executed.setQuantity((offer.get(0)).quantity);
							executed.setDate(new Date());
							executeddao.save(executed);
							
							ltp=offer.get(0).price;
							ltq=offer.get(0).quantity;
							
							offer.remove(0);
							offercount--;
							flag1 = 1;
							break;

						}

					}
				}

				if (flag1 == 0) {
					System.out.println("No match found for offer id: " + offer.get(0).offerId
							+ " Entering into offer pending list");

				}

			}

	/////////////		// market offer
			else if (offer.get(0).orderType.equals("market")) {
				int flag1 = 0;
				
//				int qty_rem=offer.get(0).quantity;
//				Bid bid_max = Collections.max(bid, Comparator.comparing(s -> s.getPrice()));//set bid max at first
//				Collections.sort(bid,Comparator.comparingDouble(Bid::getPrice).reversed());
				List<BidTable> bid_desc=new ArrayList<>();
				
				 for (int i = 0; i < bid.size(); i++) {
				        bid_desc.add(bid.get(i));
				    }
				Collections.sort(bid_desc,Comparator.comparingDouble(BidTable::getPrice).reversed());
//				System.out.println("bid_desc= "+bid_desc.toString());
				Iterator<BidTable> i= bid_desc.iterator();
				while (i.hasNext()) {
					BidTable bid_max=i.next();
//					System.out.println("Bid max= "+bid_max);
					if (offer.get(0).quantity == bid_max.getQuantity()) {
						System.out.println("Bid w bid id: " + bid.get(bid.size() - 1).bidId
								+ "has been matched w offerId: " + bid_max.getBidId());
						
						ExecutedTable executed=new ExecutedTable();
						executed.setBidId(bid_max.bidId);
						executed.setOfferId(offer.get(0).offerId);
						executed.setPrice(bid_max.price);
						executed.setQuantity((offer.get(0)).quantity);
						executed.setDate(new Date());
						executeddao.save(executed);
						
						ltp=bid_max.price;
						ltq=offer.get(0).quantity;
						
						offer.remove(0);
						bid.removeIf(obj -> obj.bidId == bid_max.getBidId());
						bidcount--;
						offercount--;
						flag1 = 1;
//						qty_rem=0;
						break;

					} else if (offer.get(0).quantity > bid_max.getQuantity()) {
						int qty_bid = 0;
		
						for (BidTable b : bid) {//bid_max
							if(b.aon.equals("Yes"))
							{
								if(offer.get(offer.size()-1).quantity-qty_bid>b.quantity)
									qty_bid += b.quantity;
							}
							else
							qty_bid += b.quantity;
						}
						if (qty_bid < offer.get(0).quantity) {
							System.out.println("Bid id: " + bid.get(bid.size() - 1).bidId
									+ "rejected since the quantity can't be satisfied");
							
							RejectedTable rejected=new RejectedTable();
							rejected.setBid_offer("offer");
							rejected.setDate(new Date());
							rejected.setOrderType(offer.get(0).orderType);
							rejected.setPrice(offer.get(0).price);
							rejected.setQuantity(offer.get(0).quantity);
							rejected.setAon(offer.get(0).aon);
							rejecteddao.save(rejected);
							
							offer.remove(0);
							offercount--;
							break;
						} else {
							System.out.println("Partial execution Offer w bid id: " + bid_max.getBidId()
									+ "has been matched w offerId: " + bid.get(bid.size() - 1).bidId);
							int qty_rem = offer.get(0).quantity - bid_max.getQuantity();
							offer.get(0).quantity = qty_rem;
							
							ExecutedTable executed=new ExecutedTable();
							executed.setBidId(bid_max.bidId);
							executed.setOfferId(offer.get(0).offerId);
							executed.setPrice(bid_max.price);
							executed.setQuantity(bid_max.getQuantity());
							executed.setDate(new Date());
							executeddao.save(executed);
							
							ltp=bid_max.price;
							ltq=bid_max.quantity;
							
							bid.removeIf(obj -> obj.bidId == bid_max.getBidId());
							bidcount--;
						}

//							System.out.println("Partial execution Offer w bid id: " + bid_max.getBidId()
//									+ "has been matched w offerId: " + bid.get(bid.size() - 1).bidId);
//							int qty_rem = offer.get(0).quantity - bid_max.getQuantity();
//							offer.get(0).quantity = qty_rem;
//							bid.removeIf(obj -> obj.bidId == bid_max.getBidId());
//							bidcount--;

					} else if (offer.get(0).quantity < bid_max.getQuantity() && bid_max.aon.equals("No")) {
//						System.out.println("loop :");
						System.out.println("Bid w bid id: " + bid.get(bid.size() - 1).bidId
								+ "has been matched w offerId: " + bid_max.getBidId());
						int qty_rem = bid_max.getQuantity() - offer.get(0).quantity;
						bid_max.setQuantity(qty_rem);
//						Bid bid_og=bid.get(p -> p.user.name.equals("Peter")
						for (BidTable b: bid) {
						    if (bid_max.bidId==b.getBidId()) {
						        b.setQuantity(bid_max.quantity);//Id(bid_max.bid);
						    }
						}
						
						ExecutedTable executed=new ExecutedTable();
						executed.setBidId(bid_max.bidId);
						executed.setOfferId(offer.get(0).offerId);
						executed.setPrice(bid_max.price);
						executed.setQuantity(offer.get(0).quantity);
						executed.setDate(new Date());
						executeddao.save(executed);
						
						ltp=bid_max.price;
						ltq=offer.get(0).quantity;
						
						offer.remove(0);
						offercount--;

						flag1 = 1;
						break;
					}
					else if (offer.get(0).quantity < bid_max.getQuantity() && bid_max.aon.equals("Yes") )
					{
						
//						System.out.println("dei dei 1 "+i.hasNext()+"i="+i.toString());
						 
//						i.next();
						if(i.hasNext()==false)
						{
							RejectedTable rejected=new RejectedTable();
							rejected.setBid_offer("offer");
							rejected.setDate(new Date());
							rejected.setOrderType(offer.get(0).orderType);
							rejected.setPrice(offer.get(0).price);
							rejected.setQuantity(offer.get(0).quantity);
							rejected.setAon(offer.get(0).aon);
							rejecteddao.save(rejected);
							
							
							offer.remove(0);
							offercount--;
							break;
						}
						else
						{
							continue;
						}
					}
//					System.out.println("here again");
				}

//					if(flag1==0)
//					{
//						System.out.println("No match found for offer id: " + offer.get(0).offerId
//								+ " Entering into offer pending list");
//					}
			}

		}
	}
}




