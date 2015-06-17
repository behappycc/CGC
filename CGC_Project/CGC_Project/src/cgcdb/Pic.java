package cgcdb;

public class Pic {
	String project_code;
	String table_spec;
	String table_id;
	String item_id;
	Boolean support;

	public Pic(String project_code,
			String table_spec,
			String table_id,
			String item_id,
			String support){
		
		this.project_code = project_code.trim();
		this.table_spec = table_spec.trim();
		this.table_id = table_id.trim();
		this.item_id = item_id.trim();
		
		if(support.contains("Y"))
			this.support = true;
		else
			this.support = false;
	}
	
	public String getTable_spec(){
		return table_spec;
	}
	
	public String getTable_ID(){
		return table_id;
	}
	
	public String getItem_ID(){
		return item_id;
	}
	
	public boolean getSupport(){
		return support;
	}
}
