package com.Resturant.MyResturant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;

public class MemberLogInSignUp {
	
	
	//==================================MembershipLogIn===========================================================
	
			@PostMapping("/FoodVibes/MemberLogIn")
			public String MemberLogin(HttpServletRequest req) throws ClassNotFoundException,SQLException{
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/resturantProject","root","Mysql@1225");
				String mail = req.getParameter("mailId");
				String pwd = req.getParameter("password");
				String logincheck="select * from resturantMember where customer_mailId ='"+mail+"'";
				PreparedStatement psmt= con.prepareStatement(logincheck);
				ResultSet rs = psmt.executeQuery();
				if(rs.next()) {
					if(rs.getString("customer_pwd").equals(pwd))
						return "Log In Successfully";
					else
						return "check the Password";
			
				}
				else
					return "check the mail id or Sign UP";		
			}
			
			
			
			//============================================MembershipSignUp================================================
			
			@PostMapping("/FoodVibes/MemberSignUp")
			public Map MemberSignUp(HttpServletRequest req) throws ClassNotFoundException, SQLException{
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection connectDB = DriverManager.getConnection("jdbc:mysql://Localhost:3306/resturantProject","root","Mysql@1225");
				String name=req.getParameter("name");
				String mail = req.getParameter("mailId");
				String pwd = req.getParameter("password");
				String sub = req.getParameter("MemberShipType");
				String contact = req.getParameter("Contact_no");
				String checkmail = "select * from resturantMember where customer_mailId ='"+mail+"'";
				PreparedStatement psmt= connectDB.prepareStatement(checkmail);
				HashMap<String,String> mp=new HashMap<String,String>();
				ResultSet rs = psmt.executeQuery();
				if(rs.next()) {
					mp.put(rs.getString("customer_name")+" Already have an account Please Log in","Thanks");
					
				}
				else {
					String c_id="select count(customer_id) as id from resturantMember";
					PreparedStatement smt2 = connectDB.prepareStatement(c_id);
					ResultSet rs2 = smt2.executeQuery();
					int customer_id=0;
					while(rs2.next()) {
						customer_id = rs2.getInt("id");
						customer_id++;
					}
					String insert= "insert into resturantMember(customer_id,customer_name,customer_mailId,contact,customer_pwd,subscription_type) values("+customer_id+",?,?,?,?,?)";
					PreparedStatement smt = connectDB.prepareStatement(insert);
					smt.setString(1, name);
					smt.setString(2, mail);
					smt.setString(3, contact);
					smt.setString(4, pwd);
					smt.setString(5, sub);
					int i = smt.executeUpdate();
					if(i>0)
						mp.put("Dear "+name+" Your customer Id is = "+customer_id," Pay the subscription Amount by this idand use our services, Thank you ");
					else
						mp.put(name, " Something went Wrong");
					
				}
				return mp;
				
			}
			
			
			
			
			
			

}
