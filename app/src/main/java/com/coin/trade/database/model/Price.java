package com.coin.trade.database.model;

public class Price {

	private String title;
	private String value;
	private String trend;
	private String trendSign;
	
	public String getTitle() {
		return title;
	}
	
	public String getValue() {
		return value;
	}
			
	public String getTrend() {
		return trend;
	}
	
	public String getTrendSign() {
		return trendSign;
	}
	
	public void setTitle(String title) {
		this.title = title;
	} 
	
	public void setValue(String value) {
		this.value = value;
	} 
	
	public void setTrend(String trend) {
		this.trend = trend;
	} 
	
	public void setTrendSign(String trendSign) {
		this.trendSign = trendSign;
	}
}

