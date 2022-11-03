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
//Comment
public class AdminAPI {

	

	//=======================================Reservation change for admin()=========================================

	@GetMapping("/FoodVibes/change")
	
	public String previousday(HttpServletRequest req)throws ClassNotFoundException,SQLException{
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/resturantProject","root","Mysql@1225");
		String Day=req.getParameter("dayNo");
		int d=Integer.parseInt(Day);
		d--;
		String query="select *  from reservationSlot where week_no="+d+"";
		PreparedStatement psmt= connect.prepareStatement(query);
		ResultSet rs = psmt.executeQuery();
		while(rs.next()) {
			String query2="update reservationSlot set slot1=10,slot2=10,slot3=10,slot4=10,slot5=10,slot6=10 where week_no="+d+"";
			PreparedStatement psmt2= connect.prepareStatement(query2);
			int s =psmt2.executeUpdate();
			
		}
		return "previous day's data change succefully";
					
	}
	//==========================================Reservation table for Admin(5)=============================================
	@GetMapping("/FoodVibes/ReservationDetails")
	public Map ReservationDetails(HttpServletRequest req)throws ClassNotFoundException,SQLException{
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/resturantProject","root","Mysql@1225");
		String day=req.getParameter("day");
		String query = "select * from adminreservationTable where day='"+day+"'";
		HashMap<String,String> mp=new HashMap<String,String>();
		PreparedStatement psmt = connect.prepareStatement(query);
		ResultSet rs = psmt.executeQuery();
		while(rs.next()) {
			mp.put("Name:-"+rs.getString("name")+"  Day:- "+rs.getString("day"),"Slot:- "+rs.getString("slot")+"   No of people:- "+rs.getString("nooftable"));
		}
		return mp;
	
	}

	
}
