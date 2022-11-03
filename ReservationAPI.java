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
import org.springframework.web.bind.annotation.RestController;
	
@RestController
public class ReservationAPI {

	//==========================================Reservation for Customer(4)================================================
	
		@GetMapping("FoodVibes/Reservationfortable")
		public String ReservationForTable(HttpServletRequest req)throws ClassNotFoundException,SQLException{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/resturantProject","root","Mysql@1225");
			String name = req.getParameter("name");
			String people = req.getParameter("NoOfPeople");
			String day = req.getParameter("Day");
			String time =req.getParameter("slot");
			String query = "select * from reservationSlot where day='"+day+"'";
			PreparedStatement psmt = connect.prepareStatement(query);
			ResultSet rs = psmt.executeQuery();
			while(rs.next()) {
				//no. of tables
				int p= Integer.parseInt(people);
				int table=0;
				if(p%2==0)
				table = p/2;
				else
				table=(p/2)+1;
				if(rs.getInt(time)!=0) {
				 int n=rs.getInt(time)-table;
				 String que="select count(re_id) as no from adminreservationTable";
				 PreparedStatement or=connect.prepareStatement(que);
				 ResultSet rs2=or.executeQuery();
				 int order=0;
				 while(rs2.next()) {
					 order=rs2.getInt("no");
					 order++;
					 System.out.println(order);
				 } 
				 String update="update reservationSlot set "+time+"="+n+" where day='"+day+"'";
				 PreparedStatement psmt2=connect.prepareStatement(update);
				 int i= psmt2.executeUpdate();
				 String q="insert into adminreservationTable(re_id,name,day,slot,nooftable) values("+order+",?,?,?,?)";
				 PreparedStatement pre=connect.prepareStatement(q);
				 pre.setString(1,name);
				 pre.setString(2,day);
				 pre.setString(3,time);
				 pre.setString(4, people);
				 int j = pre.executeUpdate();
				 
				 return "We booked table for you please pay the adavanced on Reservation Id:- "+order;
				 
				}
				else
					return "sorry we are full at this slot Please change the slot ";
					
				}
			
			
			return null;
		}
		
		
		
	
		
		
}
