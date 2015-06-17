/**
 * Example:</br>
 * 
 */
package conditiontool;

import java.sql.Connection;
import cgcdb.*;
import javacccode.*;

public class TCList {
	
	private CGCDB db;
	
	private Project project;
	
	/**
	 * Instantiates a new TC list. Input a Connection to SQL Server to initialize a Database to further use.</br>
	 * --------------------------------------------------------</br>
	 * Ex:</br>
	 * <pre>
	 * String conUrl = "jdbc:sqlserver://123.12.12.12\\SQLEXPRESS:1433;user=;password=;";
	 * Connection con = null;
	 * try {
	 * 	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	 * 	con = DriverManager.getConnection(conUrl);
	 * } catch (ClassNotFoundException e) {
	 * 	e.printStackTrace();
	 * } catch (SQLException e) {
	 * 	e.printStackTrace();
	 * }
	 * 
	 * TCList tcList = new TCList(con, "NTUTC");
	 * </pre>
	 *
	 * @param con java.sql.Connection
	 * @param database_name the database_name
	 */
	public TCList(Connection con, String database_name){
		db = new CGCDB(con, database_name);
	}
	
	/**
	 * Execution the inputed Project</br>
	 * ----------------------------------</br>
	 * Ex:</br>
	 * <pre>tclist.run("LYW200");</pre>
	 *
	 * @param project_code String 
	 */
	public void run(String projectCode/*, String protocol*/){
		System.out.println("Project Code: " + projectCode/* + ", Protocol: " + protocol*/);
		
		db.loadProject(projectCode/*, Protocol.BySymbol(protocol)*/);
		project = db.getProject(projectCode);
		
		db.loadPics(project/*, Protocol.BySymbol(protocol)*/);
		db.loadConditions(project/*, Protocol.BySymbol(protocol)*/);
		//-----------Solve Conditions----------
		run(project);
		//-------------------------------------
		db.getTestcase(project/*, Protocol.BySymbol(protocol)*/);
		db.uploadError();
		System.out.println("Solving Conditon is Done.");
		// --------------------------------------
	}
	
	
	
	private void run(Project project){
		
	  	for(Condition con : project.getCondition_List()){
	  		ConditionSolver.solveCondition(db, project, con, db.getError_List());
	  	}
	}
}
