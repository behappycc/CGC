package cgcdb;

public class _3GInfo {
	private String gcf_version;
	private String ptcrb_version;
	private String category;
	private String hsdpa_category;
	private String primary_spec;
	private String audio_category;
	
	public _3GInfo(
			String category,
			String hsdpa_category,
			String primary_spec){
		this.category = category;
		this.hsdpa_category = hsdpa_category;
		this.primary_spec = primary_spec;
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
	public String getAudio_Category(){
		return audio_category;
	}
}
