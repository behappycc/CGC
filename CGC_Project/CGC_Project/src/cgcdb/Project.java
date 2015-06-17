package cgcdb;

import java.util.*;

public class Project {
	private String project_code;
	private _2GInfo _2G;
	private _3GInfo _3G;
	private LTEInfo LTE;
	
	protected ArrayList<Condition> CONDITION_LIST;
	protected HashMap<String, HashMap<String, Condition>> CONDITIONS;
	protected HashMap<String, HashMap<String, Pic>> PICS;// HashMap<"Table Spec", HashMap<"Item Id + Item Id", Pic>>
	
	public Project(String project_code){
		this.project_code = project_code;
		
		CONDITION_LIST = new ArrayList<Condition>();
		CONDITIONS = new HashMap<String, HashMap<String, Condition>>();
		PICS = new HashMap<String, HashMap<String, Pic>>();
	}
	
	public String getProject_Code(){
		return project_code;
	}
	
	public void set2GInformation(
			String gcf_version,
			String ptcrb_version,
			String gsm,
			String gprs,
			String egprs,
			String amr,
			String satk,
			String dtm
	){
		
		_2G = new _2GInfo(
				gcf_version,
				ptcrb_version,
				gsm,
				gprs,
				egprs,
				amr,
				satk,
				dtm
		);
	}
	public void set3GInformation(
			String category,
			String hsdpa_category,
			String primary_spec){
		
		_3G = new _3GInfo(
				category,
				hsdpa_category,
				primary_spec
		);
	}
	public void setLTEInformation(
			String gcf_version,
			String ptcrb_version,
			String category,
			String hsdpa_category,
			String primary_spec,
			String band){
		
		LTE = new LTEInfo(
				gcf_version,
				ptcrb_version,
				category,
				hsdpa_category,
				primary_spec,
				band
		);
	}
	
	public void setConditions(HashMap<String, HashMap<String, Condition>> CONDITIONS){
		this.CONDITIONS = CONDITIONS;
	}
	
	public void setCondition_List(ArrayList<Condition> CONDITION_LIST){
		this.CONDITION_LIST = CONDITION_LIST;
	}
	
	public void setPICS(HashMap<String, HashMap<String, Pic>> PICS){
		this.PICS = PICS;
	}
	
	public Pic getPic(String picname, String table_spec){
		HashMap<String, Pic> table;
		
		if( (table = PICS.get(table_spec)) == null)
			return null;
		return table.get(picname);
	}
	
	public ArrayList<Condition> getCondition_List(){
		return CONDITION_LIST;
	}
	
	public Condition getCondition(String condition_id, String table_spec){
		HashMap<String, Condition> table;
		if( (table = CONDITIONS.get(table_spec)) == null)
			return null;
		return table.get(condition_id);
	}
	
	public _2GInfo get2GInfo(){
		return _2G;
		
	}
	public _3GInfo get3GInfo(){
		return _3G;
		
	}
	public LTEInfo getLTEInfo(){
		return LTE;
	}
}
