package cgcdb;

public class _2GInfo{
	private String gcf_version;
	private String ptcrb_version;
	private String gsm;
	private String gprs;
	private String egprs;
	private String amr;
	private String satk;
	private String dtm;
	
	public _2GInfo(
			String gcf_version,
			String ptcrb_version,
			String gsm,
			String gprs,
			String egprs,
			String amr,
			String satk,
			String dtm){
		this.gcf_version = gcf_version;
		this.ptcrb_version = ptcrb_version;
		this.gsm = gsm;
		this.gprs = gprs;
		this.egprs = egprs;
		this.amr = amr;
		this.satk = satk;
		this.dtm = dtm;
	}
	
	public String getGCF_Version(){
		return gcf_version;
	}
	public String getPTCRB_Version(){
		return ptcrb_version;
	}
	public String getGSM_Category(){
		return gsm;
	}
	public String getGPRS_Category(){
		return gprs;
	}
	public String getEGPRS_Category(){
		return egprs;
	}
	public String getAMR_Category(){
		return amr;
	}
	public String getSATK_Category(){
		return satk;
	}
	public String getDTM_Category(){
		return dtm;
	}
}
