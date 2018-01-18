package com.zhiyang.media.bean;

public class Result {
	private String url;
	private Double rate;
	private String suggestion;
	private String label;
	private String scene;
	private String type;
	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Result(String url, Double rate, String suggestion, String label, String scene, String type) {
		super();
		this.url = url;
		this.rate = rate;
		this.suggestion = suggestion;
		this.label = label;
		this.scene = scene;
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Result [url=" + url + ", rate=" + rate + ", suggestion=" + suggestion + ", label=" + label + ", scene="
				+ scene + ", type=" + type + "]";
	}
	
}
