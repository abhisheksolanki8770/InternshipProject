package com.Resturant.MyResturant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class FoodDeliveryPayment {

	//===============================================Billing API(1)============================================================
	
		@GetMapping("/FoodVibes/BillGenrator")
		public Map resturant_Bill(HttpServletRequest req) throws ClassNotFoundException, SQLException{
			Class.forName("com.mysql.cj.jdbc.Driver");		
			Connection connectDB= DriverManager.getConnection("jdbc:mysql://localhost:3306/resturantProject","root","Mysql@1225");
			String name=req.getParameter("name");
			String n=req.getParameter("order_number");
			String discount=req.getParameter("couponCode");
			int j=Integer.parseInt(n);
			int sum=0;
			HashMap<String,String> mp=new HashMap<String,String>();
			for(int i=1;i<=j;i++) {
				String it=req.getParameter("item"+i);
				String query="select Item_price as price from resturantMenu where Food_item='"+it+"'";
				PreparedStatement psmt = connectDB.prepareStatement(query);
				ResultSet rs = psmt.executeQuery();
				while(rs.next()) {
					sum=sum+rs.getInt("price");
					
				}
			}
			String mailid=req.getParameter("userid");
			String checkUser="select * from resturantMember where customer_mailId='"+mailid+"'";
			PreparedStatement smt2= connectDB.prepareStatement(checkUser);
			ResultSet rs2 = smt2.executeQuery();
			String orderid="Select count(bill_no) as number from BillGenrator";
			PreparedStatement order=connectDB.prepareStatement(orderid);
			ResultSet bill_id= order.executeQuery();
			int bill_no=0;
			while(bill_id.next()) {
				System.out.println(bill_id.getInt("number"));
			bill_no=bill_id.getInt("number");
			bill_no++;
			System.out.println(bill_no);
			}
			
			int bill=0;
			if(rs2.next()) {
				name=rs2.getString("customer_name");
				if(rs2.getString("subscription_type").equals("Prime")) {
					if(discount==null) {
						mp.put("Dear "+rs2.getString("customer_mailId")+" as a prime member we have coupon Code for You please insert discount coupon for 20% discount ","Discount Code is:- Prime20 ");
					return mp;	
					}
						else if(discount.equals("Prime20")) {
							sum=(int)(sum*0.80);
							bill=1;
							mp.put("Dear "+rs2.getString("customer_name")+" Total Cost of ordered Food is :- "+sum+" rps" ,"with Order id= "+bill_no+" Thanks");
						}
						else {
							mp.put(rs2.getString("customer_name")," please enter valid coupon code");
						}
				}
				else if(rs2.getString("subscription_type").equals("Normal")) {
					if(discount==null) {
						mp.put("Dear "+rs2.getString("customer_name")+" as a Silver member we have coupon Code for You please insert discount coupon for 10% discount ","Discount Code is:- First10 ");
					}else if(discount.equals("First10")) {
							sum=(int)(sum*0.90);
							bill=1;

							mp.put("Dear "+rs2.getString("customer_name")+" Total Cost of ordered Food is :- "+sum+" rps" ,"order id:- "+bill_no+"Thanks");
					}
					
						else {
							mp.put(rs2.getString("customer_mailId")," please enter valid coupon code");
						}
				}
			}
			else {
				bill=1;
				mp.put("Dear "+name+" Your total cost of food is ",sum+" rps with Oder id= "+bill_no);
			}
			if(bill==1) {
			String insertbill="insert into BillGenrator(bill_no,customer_name,bill_amt,bill_date) values(?,?,?,Now())";
			PreparedStatement smt3 = connectDB.prepareStatement(insertbill);
			smt3.setInt(1,bill_no);
			smt3.setString(2,name);
			smt3.setInt(3,sum);
			int i = smt3.executeUpdate();

			if(i>0)
				System.out.println(i+" row Inserted");	//print order id

			else
				System.out.println("No row Inserted");
				
			}
			return mp;
		
		}
		//=======================================Food Delivery/Take away===================================================
		@PostMapping("/FoodVibes/FoodDelivery")
		public String FoodDelivery(HttpServletRequest req) throws ClassNotFoundException,SQLException{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connectDB=DriverManager.getConnection("jdbc:mysql://localhost:3306/resturantProject","root","Mysql@1225");
			String orderid=req.getParameter("order_id");
			String distance=req.getParameter("distance");
			int dis=Integer.parseInt(distance);
			HashMap mp=new HashMap();
			if(dis<=6) {
				String paid= req.getParameter("pay");
				String bill="update BillGenrator set paid_unpaid='paid' where bill_no="+orderid+"";
				PreparedStatement psmt=connectDB.prepareStatement(bill);
				int i =psmt.executeUpdate();
				if(i>0) {
				mp.put("Dear "+"cutomer_name"," Payment Succecfull");
				}
				if(paid.equals("paid"))
				return "Payment Successfull";
				else
					return "something went wrong";
			}
			else {
				String dlt="delete from Billgenrator where bill_no="+orderid+""; 
				PreparedStatement psmt2=connectDB.prepareStatement(dlt);
				psmt2.executeUpdate();
				return "Food delivery is not available in your provided distance";
				
			}
		}
		
		
}
