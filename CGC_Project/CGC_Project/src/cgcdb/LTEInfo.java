package cgcdb;

public class LTEInfo {
	private String gcf_version;
	private String ptcrb_version;
	private String category;
	private String hsdpa_category;
	private String primary_spec;
	private String band;
	
	public LTEInfo(
			String gcf_version,
			String ptcrb_version,
			String category,
			String hsdpa_category,
			String primary_spec,
			String band){
		this.gcf_version = gcf_version;
		this.ptcrb_version = ptcrb_version;
		this.category = category;
		this.hsdpa_category = hsdpa_category;
		this.primary_spec = primary_spec;
		this.band = band;
	}
	
	public String getGCF_Version(){
		return gcf_version;
	}
	public String getPTCRB_Version(){
		return ptcrb_version;
	}
	public String getCategory(){
		return category;
	}
	public String getHSDPA_Category(){
		return hsdpa_category;
	}
	public String getPrimary_Spec(){
		return primary_spec;
	}
	public String getBand(){
		return band;
	}
}
