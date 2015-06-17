/**
 *
 */
/**
 * 
 * Tool Usage Example:</br>
 * 
 * <pre> 
 * import java.sql.Connection;
 * import java.sql.DriverManager;
 * import java.sql.SQLException;
 * import java.util.Scanner;

 * import conditiontool.*;

 * public class Tool {
	
 * 	public static void main(String[] argv){
 * 		String conUrl = "jdbc:sqlserver://140.112.42.141\\SQLEXPRESS:1433;user=sa;password=bl618;";
 * 		Connection con = null;
 * 		try {
 * 			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
 * 			con = DriverManager.getConnection(conUrl);
 * 		} catch (ClassNotFoundException e) {
 * 			// TODO Auto-generated catch block
 * 			e.printStackTrace();
 * 		} catch (SQLException e) {
 * 			// TODO Auto-generated catch block
 * 			e.printStackTrace();
 * 		}
 * 		
 * 		
 * 		// Create Tool Object
 * 		TCList tcList = new TCList(con);
 * 		
 * 		// Show Project List in Database
 * 		tcList.printProject_List();
 * 		
 * 		// Input
 * 		Scanner scanner = new Scanner(System.in);
 * 		String projectcode = scanner.next();
 * 		
 * 		// Start to Execute Solving Test Case Condition
 * 		tcList.run(projectcode);
 * 	}
 * }
 * </pre>
 * 
 * @author Rice
 *
 */
package conditiontool;