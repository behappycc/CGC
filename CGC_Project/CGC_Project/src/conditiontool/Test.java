package conditiontool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Stack;
import java.util.ArrayList;

public class Test {

	
  	public static void main(String[] argv){
  		test1();
  	}
  	
  	private static void test1(){
  		String conUrl = "jdbc:sqlserver://140.112.42.144\\SQLEXPRESS:1433;user=sa;password=bl618;";
  		Connection con = null;
  		try {
  			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
  			con = DriverManager.getConnection(conUrl);
  		} catch (ClassNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (SQLException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		
  		// Create Tool Object
  		TCList tcList = new TCList(con, "NTUTC");
  		
  		// Start to Execute Solving Test Case Condition
  		tcList.run("LHIF00");
  		//tcList.run("LYW100", "3G");
  	}
  	
  	private static void test2(){
  		ArrayList list = new ArrayList();
  		list.add("String");
  		list.add(true);
  		
  		System.out.println("remove: " + list.remove(0));
  		System.out.println("remove: " + list.remove(0));
  	}
  	
}
