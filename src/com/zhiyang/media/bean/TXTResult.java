package com.zhiyang.media.bean;

public class TXTResult {
	private String txtcontent;
	private Double rate;
	private String suggestion;
	private String label;
	private String scene;
	public TXTResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TXTResult(String txtcontent, Double rate, String suggestion, String label, String scene) {
		super();
		this.txtcontent = txtcontent;
		this.rate = rate;
		this.suggestion = suggestion;
		this.label = label;
		this.scene = scene;
	}
	public String getTxtcontent() {
		return txtcontent;
	}
	public void setTxtcontent(String txtcontent) {
		this.txtcontent = txtcontent;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getScene() {
		return scene;
	}
	public void setScene(String scene) {
		this.scene = scene;
	}
	@Override
	public String toString() {
		return "TXTResult [txtcontent=" + txtcontent + ", rate=" + rate + ", suggestion=" + suggestion + ", label="
				+ label + ", scene=" + scene + "]";
	}
	
	
}
