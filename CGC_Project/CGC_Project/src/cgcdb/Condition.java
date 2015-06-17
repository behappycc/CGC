package cgcdb;

public class Condition {
	private Protocol protocol;
	private String table_spec;
	private String condition_id;
	private String condition_desc;
	private Result result;
	private boolean done;
	
	public Condition(
			Protocol protocol,
			String table_spec,
			String condition_id,
			String condition_desc){
		
		this.protocol = protocol;
		this.table_spec = table_spec.trim();
		this.condition_id = condition_id.trim();
		this.condition_desc = condition_desc.trim();
		result = Result.VOID;	
		done = false;
	}
	
	public Protocol getProtocol(){
		return protocol;
	}
	
	public String getTable_Spec(){
		return table_spec;
	}
	
	public String getCondition_ID(){
		return condition_id;
	}
	
	public String getCondition_desc(){
		return condition_desc;
	}
	
	public void setResult(Result result){
		this.result = result;
	}
	public Result getResult(){
		return result;
	}
	
	public boolean isDone(){
		return done;
	}
	
	public void Done(){
		done = true;
	}
}
