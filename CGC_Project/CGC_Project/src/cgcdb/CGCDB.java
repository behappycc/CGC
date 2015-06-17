package cgcdb;

import java.sql.*;
import java.util.*;

public class CGCDB {
	Connection con = null;
	private Statement stmt;
	
	private ArrayList<String> PROJECT_CODE_LIST;
	private HashMap<String, Project> PROJECTS;
	private HashMap<String, String> REF_SPEC;
	private ArrayList<String> RELEASE_LIST;
	private HashMap<String, Integer> RELEASE;
	
	private ArrayList<ConditionError> ERROR_LIST;
	
	private String DBNAME;
	
	public CGCDB(Connection con, String DBNAME){
		//String conUrl = "jdbc:sqlserver://140.112.42.141\\SQLEXPRESS:1433;user=sa;password=bl618;";
		this.DBNAME = DBNAME;
		
		
		try{//註冊JODBC類
			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			//con = DriverManager.getConnection(conUrl);
			this.con = con;
			stmt = con.createStatement();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		ERROR_LIST = new ArrayList<ConditionError>();
		
		loadRef_Spec();
		loadRelease();
	}
	
	public void printProjectList(){
		for(Object key : PROJECTS.keySet()){
			System.out.println(key + ": " + PROJECTS.get(key));
		}
	}
	
	public Project getProject(String project_code){
		return PROJECTS.get(project_code);
	} 
	
	
	public void loadProject(String projectCode){
		PROJECT_CODE_LIST = new ArrayList<String>();
		PROJECTS = new HashMap<String, Project>();
		try {
			getProject2G(projectCode);
			getProject3G(projectCode);
			getProjectLTE(projectCode);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void loadProject(String projectCode, Protocol protocol){
		PROJECT_CODE_LIST = new ArrayList<String>();
		PROJECTS = new HashMap<String, Project>();

		try {
			if(protocol == Protocol._2G)
				getProject2G(projectCode);
			else if(protocol == Protocol._3G)
				getProject3G(projectCode);
			else if(protocol == Protocol._LTE)
				getProjectLTE(projectCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private void getProject2G(String projectCode) throws SQLException{
		// Load [PROJRUN] 2G
		String SQL = "SELECT ALL [PROJECT_CODE],"
				+ "[GCF_VERSION],"
				+ "[PTCRB_VERSION],"
				+ "[GSMCATEGORY],"
				+ "[GPRSCATEGORY],"
				+ "[EGPRSCATEGORY],"
				+ "[AMRCATEGORY],"
				+ "[SATKCATEGORY],"
				+ "[DTMCATEGORY]"
				+ "FROM [" + DBNAME + "].[dbo].[PROJRUN]"
				+ "WHERE [PROJECT_CODE] = '" + projectCode + "'";
		ResultSet rs = stmt.executeQuery(SQL);
		while( rs.next() ){
			Project project = new Project(rs.getString("PROJECT_CODE"));
			project.set2GInformation(
					rs.getString("GCF_VERSION"), 
					rs.getString("PTCRB_VERSION"), 
					rs.getString("GSMCATEGORY"), 
					rs.getString("GPRSCATEGORY"), 
					rs.getString("EGPRSCATEGORY"), 
					rs.getString("AMRCATEGORY"), 
					rs.getString("SATKCATEGORY"), 
					rs.getString("DTMCATEGORY"));
			PROJECT_CODE_LIST.add(rs.getString("PROJECT_CODE"));
			PROJECTS.put(project.getProject_Code(), project);
		}
		rs.close();
	}
	
	private void getProject3G(String projectCode) throws SQLException{
		
		String SQL = "SELECT ALL [PROJECT_CODE],"
				+ "[GCF_VERSION],"
				+ "[PTCRB_VERSION],"
				+ "[CATEGORY],"
				+ "[HSDPA_CATEGORY],"
				+ "[PRIMARY_SPEC],"
				+ "[AUDIO_CATEGORY]"
				+ "FROM [" + DBNAME + "].[dbo].[PROJRUN_3G]"
				+ "WHERE [PROJECT_CODE] = '" + projectCode + "'";
		
		ResultSet rs = stmt.executeQuery(SQL);
		while(rs.next()){
			
			System.out.println("project code: " + rs.getString("PROJECT_CODE"));
			
			Project project = new Project(rs.getString("PROJECT_CODE"));
			
			try{
				project.set3GInformation(
						rs.getString("CATEGORY"), 
						rs.getString("HSDPA_CATEGORY"), 
						rs.getString("PRIMARY_SPEC")
				);
			}catch(NullPointerException e){
				e.printStackTrace();
				
				System.out.println(rs.getString("CATEGORY"));
				System.out.println(rs.getString("HSDPA_CATEGORY"));
				System.out.println(rs.getString("PRIMARY_SPEC"));
			}
			PROJECT_CODE_LIST.add(rs.getString("PROJECT_CODE"));
			PROJECTS.put(project.getProject_Code(), project);
		}
		rs.close();
	}
	
	private void getProjectLTE(String projectCode) throws SQLException{
		// Load [PROJRUN_LTE]
		String SQL = "SELECT ALL [PROJECT_CODE],"
			+ "[GCF_VERSION],"
			+ "[PTCRB_VERSION],"
			+ "[CATEGORY],"
			+ "[HSDPA_CATEGORY],"
			+ "[PRIMARY_SPEC],"
			+ "[BAND]"
			+ "FROM [" + DBNAME + "].[dbo].[PROJRUN_LTE]"
			+ "WHERE [PROJECT_CODE] = '" + projectCode + "'";
		ResultSet rs = stmt.executeQuery(SQL);
		while(rs.next()){
			Project project = new Project(rs.getString("PROJECT_CODE"));
			project.setLTEInformation(
				rs.getString("GCF_VERSION"), 
				rs.getString("PTCRB_VERSION"), 
				rs.getString("CATEGORY"), 
				rs.getString("HSDPA_CATEGORY"), 
				rs.getString("PRIMARY_SPEC"), 
				rs.getString("BAND"));
			PROJECT_CODE_LIST.add(rs.getString("PROJECT_CODE"));
			PROJECTS.put(project.getProject_Code(), project);
		}
		rs.close();
	}
	
	public void loadPics(Project project){
		load2GPics(project);
		load3GPics(project);
		loadLTEPics(project);

	}
	
	public void loadPics(Project project, Protocol protocol){

		if(protocol == Protocol._2G)
			load2GPics(project);
		else if(protocol == Protocol._3G)
			load3GPics(project);
		else if(protocol == Protocol._LTE)
			loadLTEPics(project);
	}
	
	public void loadConditions(Project project){
		load2GCondtion(project);
		load3GCondtion(project);
		loadLTECondtion(project);
	}
	
	public void loadConditions(Project project, Protocol protocol){

		if(protocol == Protocol._2G)
			load2GCondtion(project);
		else if(protocol == Protocol._3G)
			load3GCondtion(project);
		else if(protocol == Protocol._LTE)
			loadLTECondtion(project);
	}
	
	private void loadRelease(){
		RELEASE_LIST = new ArrayList<String>();
		RELEASE = new HashMap<String, Integer>();
		String SQL = "SELECT ALL"
				+ "[c00],"
				+ "[rel]"
				+ "FROM [" + DBNAME + "].[dbo].[release]";
		
		try{
			ResultSet rs = stmt.executeQuery(SQL);
			while(rs.next()){
				RELEASE_LIST.add(rs.getString("rel"));
				if(Integer.parseInt(rs.getString("c00")) >= 7)
					RELEASE.put(rs.getString("rel"), new Integer(rs.getString("c00")));
			}
			rs.close();
		}catch(SQLException exception){
			exception.printStackTrace();
		}
	}
	
	private void loadRef_Spec(){
		REF_SPEC = new HashMap<String, String>();
		String SQL = "SELECT ALL [C00],"
				+ "[tech_type],"
				+ "[ref],"
				+ "[mapping_spec]"
				+ "FROM [" + DBNAME + "].[dbo].[ref_spec]";
		
		try{
			ResultSet rs = stmt.executeQuery(SQL);
			while(rs.next()){
				REF_SPEC.put(rs.getString("ref"), rs.getString("mapping_spec"));
			}
			rs.close();
		}catch(SQLException exception){
			exception.printStackTrace();
		}
	}
	
	public String getRefSpec(String ref){
		return REF_SPEC.get(ref);
	}
	
	private int load2GPics(Project project){
		HashMap<String, Pic> PICS_2G = new HashMap<String, Pic>();
		int picnumber = 0;
		try {
			String SQL = "SELECT ALL "
					+ "[PROJECT_CODE],"
					+ "[TABLE_ID],"
					+ "[ITEM_ID],"
					+ "[SUPPORT]"
					+ "FROM [" + DBNAME + "].[dbo].[PSPICS]"
					+ "WHERE [PROJECT_CODE] = '" + project.getProject_Code() + "'";
			
			ResultSet rs = stmt.executeQuery(SQL);
			
			while(rs.next()){
				
				Pic p = new Pic(
						rs.getString("PROJECT_CODE"),
						"",
						rs.getString("TABLE_ID"),
						rs.getString("ITEM_ID"),
						rs.getString("SUPPORT")
				);
				PICS_2G.put(p.getTable_ID() + "/" + p.getItem_ID(), p);
				picnumber++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		project.PICS.put("2G", PICS_2G);
		
		return picnumber;
	}
	
	private int load3GPics(Project project){
		HashMap<String, Pic> PICS_3G = new HashMap<String, Pic>();
		int picnumber = 0;
		String SQL = "SELECT ALL "
				+ "[PROJECT_CODE],"
				+ "[TABLE_SPEC],"
				+ "[TABLE_ID],"
				+ "[ITEM_ID],"
				+ "[SUPPORT]"
				+ "FROM [" + DBNAME + "].[dbo].[PICS_3G_DATA]"
				+ "WHERE [PROJECT_CODE] = '" + project.getProject_Code() + "'";
		
		try {
			ResultSet rs = stmt.executeQuery(SQL);
			
			while(rs.next()){
				Pic p = new Pic(
						rs.getString("PROJECT_CODE"),
						rs.getString("TABLE_SPEC"),
						rs.getString("TABLE_ID"),
						rs.getString("ITEM_ID"),
						rs.getString("SUPPORT")
				);
				if( (PICS_3G = project.PICS.get(p.getTable_spec())) == null ){
					PICS_3G = new HashMap<String, Pic>();
					project.PICS.put(p.getTable_spec(), PICS_3G);
				}
				PICS_3G.put(p.getTable_ID() + "/" + p.getItem_ID(), p);
				picnumber++;
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return picnumber;
	}

	private int loadLTEPics(Project project){
		HashMap<String, Pic> PICS_LTE = new HashMap<String, Pic>();
		
		int picnumber = 0;
		
		String SQL = "SELECT ALL "
				+ "[PROJECT_CODE],"
				+ "[TABLE_SPEC],"
				+ "[TABLE_ID],"
				+ "[ITEM_ID],"
				+ "[SUPPORT]"
				+ "FROM [" + DBNAME + "].[dbo].[PICS_LTE_DATA]"
				+ "WHERE [PROJECT_CODE] = '" + project.getProject_Code() + "'";
		
		try {
			ResultSet rs = stmt.executeQuery(SQL);
			// Print搜尋結果
			while(rs.next()){
				Pic p = new Pic(
						rs.getString("PROJECT_CODE"),
						rs.getString("TABLE_SPEC"),
						rs.getString("TABLE_ID"),
						rs.getString("ITEM_ID"),
						rs.getString("SUPPORT")
				);
				
				if( (PICS_LTE = project.PICS.get(p.getTable_spec())) == null ){;
					PICS_LTE = new HashMap<String, Pic>();
					project.PICS.put(p.getTable_spec(), PICS_LTE);
				}
				PICS_LTE.put(p.getTable_ID() + "/" + p.getItem_ID(), p);
				picnumber++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return picnumber;
	}
	
	private void load2GCondtion(Project project){
		HashMap<String, Condition> CONDITIONS_2G = new HashMap<String, Condition>();
		
		String SQL = "SELECT ALL "
				+ "[CONDITION_ID],"
				+ "[CONDITION_DESC]"
				+ "FROM [" + DBNAME + "].[dbo].[ETSITCCONDITION]";
		
		try {
			ResultSet rs = stmt.executeQuery(SQL);
			while(rs.next()){
				
				Condition con = new Condition(
						Protocol._2G,
						"2G",
						rs.getString("CONDITION_ID"),
						rs.getString("CONDITION_DESC")
				);
				// Add condition to condition list
				project.getCondition_List().add(con);
				// add condition to conditon hashmap
				CONDITIONS_2G.put(con.getCondition_ID(), con);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		project.CONDITIONS.put("2G", CONDITIONS_2G);
	}
	
	private void load3GCondtion(Project project){
		HashMap<String, Condition> CONDITIONS_3G = new HashMap<String, Condition>();
		
		String SQL = "SELECT ALL "
				+ "[TABLE_SPEC],"
				+ "[CONDITION_ID],"
				+ "[CONDITION_DESC]"
				+ "FROM [" + DBNAME + "].[dbo].[TESTCASE_COND_3G]";
		
		try {
			ResultSet rs = stmt.executeQuery(SQL);
			// Print搜尋結果
			while(rs.next()){
				Condition con = new Condition(
						Protocol._3G,
						rs.getString("TABLE_SPEC"),
						rs.getString("CONDITION_ID"),
						rs.getString("CONDITION_DESC")
				);
				project.CONDITION_LIST.add(con);
				if( (CONDITIONS_3G = project.CONDITIONS.get(con.getTable_Spec())) == null ){;
					CONDITIONS_3G = new HashMap<String, Condition>();
					project.CONDITIONS.put(con.getTable_Spec(), CONDITIONS_3G);
				}
				CONDITIONS_3G.put(con.getCondition_ID(), con);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadLTECondtion(Project project){
		HashMap<String, Condition> CONDITIONS_LTE = new HashMap<String, Condition>();
		
		String SQL = "SELECT ALL "
				+ "[TABLE_SPEC],"
				+ "[SPEC_VERSION],"
				+ "[CONDITION_ID],"
				+ "[CONDITION_DESC]"
				+ "FROM [" + DBNAME + "].[dbo].[TESTCASE_COND_LTE]";
		
		try {
			ResultSet rs = stmt.executeQuery(SQL);
			// Print搜尋結果
			while(rs.next()){
				Condition con = new Condition(
						Protocol._LTE,
						rs.getString("TABLE_SPEC"),
						rs.getString("CONDITION_ID"),
						rs.getString("CONDITION_DESC")
				);
				project.CONDITION_LIST.add(con);
				if( (CONDITIONS_LTE = project.CONDITIONS.get(con.getTable_Spec())) == null ){;
					CONDITIONS_LTE = new HashMap<String, Condition>();
					project.CONDITIONS.put(con.getTable_Spec(), CONDITIONS_LTE);
				}
				CONDITIONS_LTE.put(con.getCondition_ID(), con);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getTestcase(Project project){
		get2GTestcase(project);
		get3GTestcase(project);
		getLTETestcase(project);
	}
	
	public void getTestcase(Project project, Protocol protocol){
		if(protocol == Protocol._2G)
			get2GTestcase(project);
		else if(protocol == Protocol._3G)
			get3GTestcase(project);
		else if(protocol == Protocol._LTE)
			getLTETestcase(project);
	}
	
	
	private void get2GTestcase(Project project){
		String SQL;
		// 2G
		try {
			stmt.executeUpdate("TRUNCATE TABLE [" + DBNAME + "].[dbo].[TCLIST_2G];");
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO [" + DBNAME + "].[dbo].[TCLIST_2G] VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			// ---------- SIM 2G ----------		
			SQL = "SELECT ALL [IDENTIFY_EB]"
					+ ",[CLAUSE_ID]"
					+ ",[E1_DESC]"
					+ ",[RELEASE]"
					+ ",[REL96]"
					+ ",[REL97]"
					+ ",[REL98]"
					+ ",[REL99]"
					+ ",[RSTATUS]"
					+ "FROM [" + DBNAME + "].[dbo].[ETSIE1BODY]";
			ResultSet rs = stmt.executeQuery(SQL);
			while(rs.next()){
				String result = "N/A";
				
				
				// Get Condition ID from every release
				for(int i = 99; i>=96; i--){ // rel99 -> rel96
					String condition_id = "";
					condition_id = rs.getString("REL" + i);
					if(condition_id == null)
						break;
					simparser.EG1 parser = new simparser.EG1(condition_id, project, "2G");
					
					if(parser.getResult()){
						result = "A";
						break;
					}
				}
				
				// Check Redundancy
				String rstatus = rs.getString("RSTATUS");
				Condition condition = project.getCondition(rstatus, "2G");
				
				if(condition == null)
					rstatus = "N";
				else if(condition.getResult().getValue())
					rstatus = "Y";
				else
					rstatus = "N";
				
				uploadTestCaseResult(
						pstmt, 
						project.getProject_Code(), 
						"", 
						rs.getString("CLAUSE_ID"), 
						rs.getString("E1_DESC"),
						rs.getString("RELEASE"),
						result,
						rstatus,
						result
				);
			}
			// -----------------------------------------------
			// ------- TestCase 2G --------
			SQL = "SELECT ALL "
					+ "[IDENTIFY_VERSION],"
					+ "[CLAUSE_ID],"
					+ "[TITLE_NAME],"
					+ "[RELEASE_VERSION],"
					+ "[APPLICABALITY],"
					+ "[STATUS],"
					+ "[REDUENCYTC],"
					+ "[RSTATUS]"
					+ "FROM [" + DBNAME + "].[dbo].[ETSITC]";
			rs = stmt.executeQuery(SQL);
			while(rs.next()){
				String condition_id = rs.getString("STATUS");
				String redundancy = rs.getString("REDUENCYTC");
				String r_condition = rs.getString("RSTATUS");
				String result = "N/A";
				Condition condition = project.getCondition(r_condition, "2G");
				boolean r = false;
				boolean rc = true;
				if(condition == null){ // Check R_condition
					if(r_condition != null){
						if(r_condition.equalsIgnoreCase("N"))
							rc = false;
					}
				}else {
					rc = condition.getResult().getValue();
				}
				if(rc){ // Check Redundancy
					if(redundancy != null){
						if(redundancy.equalsIgnoreCase("Y"))
							r = true;
					}
				}
				if(condition_id != null){
					simparser.EG1 parser = new simparser.EG1(condition_id, project, "2G");
					if(parser.getResult()){
						
						result = "A";
					}
				}
				
				String fresult = "N/A";
				if(!r)
					fresult = result;
				
				uploadTestCaseResult(
					pstmt,
					project.getProject_Code(),
					"",
					rs.getString("CLAUSE_ID"),
					rs.getString("TITLE_NAME"),
					rs.getString("RELEASE_VERSION"),
					result,
					String.valueOf(r),
					fresult
				);
			}
			
			pstmt.executeBatch();
			rs.close();
		} catch (BatchUpdateException e){
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void get3GTestcase(Project project){
		String SQL ="";
		// --------- 3G ----------
		try {
			// ------ Prepare Update Batch ------
			stmt.executeUpdate("TRUNCATE TABLE [" + DBNAME + "].[dbo].[TCLIST_3G];");
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO [" + DBNAME + "].[dbo].[TCLIST_3G] VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			// ------- SIM 3G -------			
			SQL = "SELECT ALL [IDENTIFY_SIM_TC]"
				+ ",[TABLE_SPEC]"
				+ ",[CLAUSE_ID]"
				+ ",[TITLE]"
				+ ",[RELEASE]"
				+ ",[TEST_SEQ]"
				+ " ,[R99ME]"
				+ ",[R4ME]"
				+ ",[R5ME]"
				+ ",[R6ME]"
				+ ",[TERMINAL_PROFILE]"
				+ ",[R7ME]"
				+ ",[R8ME]"
				+ ",[R9ME]"
				+ ",[R10ME]"
				+ ",[R11ME]"
				+ ",[R12ME]"
				+ ",[R13ME]"
				+ ",[R14ME]"
				+ ",[R15ME]"
				+ ",[R16ME]"
				+ ",[R17ME]"
				+ ",[R18ME]"
				+ ",[R19ME]"
				+ ",[R20ME]"
				+ "FROM [" + DBNAME + "].[dbo].[TESTCASE_SIM_3G]";
			ResultSet rs = stmt.executeQuery(SQL);
			int REL = RELEASE.get(project.get3GInfo().getCategory()).intValue()-1;
			while(rs.next()){
				String result = "N/A";
				String table_spec = rs.getString("TABLE_SPEC");
				for(int i = REL; i>=6; i--){
					String condition_id = rs.getString("R" + RELEASE_LIST.get(i).substring(4) + "ME");
					if(condition_id == null)
						condition_id = "";
							
					simparser.EG1 parser = new simparser.EG1(condition_id, project, table_spec);
					if(parser.getResult()){
						result = "R";
						break;
					}
				}
				uploadTestCaseResult(
					pstmt, 
					project.getProject_Code(), 
					rs.getString("TABLE_SPEC"), 
					rs.getString("CLAUSE_ID"), 
					rs.getString("TITLE"),
					rs.getString("RELEASE"),
					result,
					"",
					result
				);
			}// while
			// -------------------------------------------------
			// ------- Testcase 3G -------
			SQL = "SELECT ALL "
				+ "[TABLE_SPEC],"
				+ "[CLAUSE_ID],"
				+ "[TITLE],"
				+ "[RELEASE],"
				+ "[APPLICABILITY]," // Reference to Condition_ID
				+ "[REDUNENCY],"
				+ "[R_CONDITION]"
				+ "FROM [" + DBNAME + "].[dbo].[TESTCASE_3G]";
			rs = stmt.executeQuery(SQL);
			while(rs.next()){
				String condition_id = rs.getString("APPLICABILITY");
				String table_spec = rs.getString("TABLE_SPEC");
				String redundancy = rs.getString("REDUNENCY");
				String r_condition = rs.getString("R_CONDITION");
				String result = "N/A";
				Condition condition = project.getCondition(r_condition, table_spec);
				boolean r = false;
				boolean rc = true;
				if(condition == null){ // Check R_condition
					if(r_condition != null){
						if(r_condition.equalsIgnoreCase("N"))
						rc = false;
					}
				}else {
					rc = condition.getResult().getValue();
				}
				if(rc){ // Check Redundancy
					if(redundancy != null){
						if(redundancy.equalsIgnoreCase("Y"))
							r = true;
					}
				}
				if(condition_id != null){
					simparser.EG1 parser = new simparser.EG1(condition_id, project, table_spec);
					if(parser.getResult()){
						result = "R";
					}
				}
				
				String fresult = "N/A";
				if(!r)
					fresult = result;
			
				uploadTestCaseResult(
						pstmt, 
						project.getProject_Code(), 
						rs.getString("TABLE_SPEC"), 
						rs.getString("CLAUSE_ID"), 
						rs.getString("TITLE"),
						rs.getString("RELEASE"),
						result,
						String.valueOf(r),
						fresult
				);
			}// while	
			pstmt.executeBatch();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void getLTETestcase(Project project){
		// LTE
		String SQL = "SELECT ALL "
			+ "[TABLE_SPEC],"
			+ "[CLAUSE_ID],"
			+ "[TITLE],"
			+ "[RELEASE],"
			+ "[APPLICABILITY]" // Reference to Condition_ID	
			+ "FROM [" + DBNAME + "].[dbo].[TESTCASE_LTE]";

		try {
			stmt.executeUpdate("TRUNCATE TABLE [" + DBNAME + "].[dbo].[TCLIST_LTE];");
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO [" + DBNAME + "].[dbo].[TCLIST_LTE] VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ResultSet rs = stmt.executeQuery(SQL);
			// Print搜尋結果
			while(rs.next()){
				String condition_id = rs.getString("APPLICABILITY");
				String table_spec = rs.getString("TABLE_SPEC");
				Condition condition = project.getCondition(condition_id, table_spec);
				String result = "null";
				if(condition == null){
					result = condition_id;
				}else if(condition.getResult().getValue()){
					result = "R";
				}else{
					result = "N/A";
				}
				
				uploadTestCaseResult(
						pstmt, 
						project.getProject_Code(), 
						rs.getString("TABLE_SPEC"), 
						rs.getString("CLAUSE_ID"), 
						rs.getString("TITLE"),
						rs.getString("RELEASE"),
						result,
						"",
						result
				);
			}// while
					
			pstmt.executeBatch();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}// try
	}
	
	private void uploadTestCaseResult(PreparedStatement pstmt, 
									  String project_code, 
									  String spec, 
									  String tc_id, 
									  String tc_desc,
									  String release,
									  String status_result,
									  String r_result,
									  String final_result){
		try {
			pstmt.setString(1, UUID.randomUUID().toString());
			pstmt.setString(2, project_code);
			pstmt.setString(3, spec);
			pstmt.setString(4, tc_id);
			pstmt.setString(5, tc_desc);
			pstmt.setString(6, release);
			pstmt.setString(7, status_result);
			pstmt.setString(8, r_result);
			pstmt.setString(9, final_result);
						
			pstmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void uploadError(){
		//System.out.println("Error: " + ERROR_LIST.size());
		
		try {
			stmt.executeUpdate("TRUNCATE TABLE [" + DBNAME + "].[dbo].[ERROR_LIST];");
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO [" + DBNAME + "].[dbo].[ERROR_LIST] VALUES( ?, ?, ?, ?, ?)");
			for(ConditionError conerr : ERROR_LIST){
				pstmt.setString(1, UUID.randomUUID().toString());
				pstmt.setString(2, conerr.getProject_code());
				pstmt.setString(3, conerr.getSpec());
				pstmt.setString(4, conerr.getCondition_id());
				pstmt.setString(5, conerr.getError_desc());
				pstmt.addBatch();
			}
			
			pstmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<ConditionError> getError_List(){
		return ERROR_LIST;
	}
}

