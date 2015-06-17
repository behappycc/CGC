package cgcdb;

public class ConditionError {
	String project_code;
	String spec;
	String condition_id;
	String error_desc;
	
	public ConditionError(String project_code, String spec, String condition_id, String error_desc){
		this.project_code = project_code;
		this.spec = spec;
		this.condition_id = condition_id;
		this.error_desc = error_desc;
	}
	
	public String getCondition_id() {
		return condition_id;
	}
	
	public String getSpec() {
		return spec;
	}
	public String getProject_code() {
		return project_code;
	}
	public String getError_desc() {
		return error_desc;
	}
}
