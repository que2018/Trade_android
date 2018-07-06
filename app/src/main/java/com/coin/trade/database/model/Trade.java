package com.coin.trade.database.model;

public class Trade {

	private String valueBuy;
	private String priceBuy;
	private String valueSell;
	private String priceSell;
	
	public String getValueBuy() {
		return this.valueBuy;
	}
	
	public String getPriceBuy() {
		return this.priceBuy;
	}
	
	public String getValueSell() {
		return this.valueSell;
	}
	
	public String getPriceSell() {
		return this.priceSell;
	}
	
	public void setValueBuy(String valueBuy) {
		this.valueBuy = valueBuy;
	}
	
	public void setPriceBuy(String priceBuy) {
		this.priceBuy = priceBuy;
	}
	
	public void setValueSell(String valueSell) {
		this.valueSell = valueSell;
	}
	
	public void setPriceSell(String priceSell) {
		this.priceSell = priceSell;
	}
}

