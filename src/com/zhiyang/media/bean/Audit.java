package com.zhiyang.media.bean;

public class Audit {
	private String auditlevel;
	private String auditresult;
	private String auditdescription;
	private String auditor = "机器";
	public Audit() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Audit(String auditlevel, String auditresult, String auditdescription, String auditor) {
		super();
		this.auditlevel = auditlevel;
		this.auditresult = auditresult;
		this.auditdescription = auditdescription;
		this.auditor = auditor;
	}
	public String getAuditlevel() {
		return auditlevel;
	}
	public void setAuditlevel(String auditlevel) {
		this.auditlevel = auditlevel;
	}
	public String getAuditresult() {
		return auditresult;
	}
	public void setAuditresult(String auditresult) {
		this.auditresult = auditresult;
	}
	public String getAuditdescription() {
		return auditdescription;
	}
	public void setAuditdescription(String auditdescription) {
		this.auditdescription = auditdescription;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	@Override
	public String toString() {
		return "Audit [auditlevel=" + auditlevel + ", auditresult=" + auditresult + ", auditdescription="
				+ auditdescription + ", auditor=" + auditor + "]";
	}
	public static void main(String[] args) {
		Audit a = new Audit();
		System.out.println(a);
	}
}
