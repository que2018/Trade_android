package com.coin.trade.constant;

public class ADDR {
	public static String HOME = "http://bisail.com/api/";
	
	public static String LOGIN = HOME + "account/login";
	public static String LOGOUT = HOME + "account/logout";
	public static String REGISTER = HOME + "account/register";
	public static String REGISTER_SMS = HOME + "account/validation/send_code";
	public static String BALANCE = HOME + "account/account";

	public static String ABOUT = "http://bisail.com/info/about";

	public static String TRADE = "http://saminthebox.com/trade/trade.php";
	public static String TRADE_SUBMIT = "http://saminthebox.com/trade/trade_submit.php";
	public static String PRICE_USDT = "http://bisail.com/api/exchange/trade/get_list?code_product=BTC";
	public static String PRICE_BTC = "http://bisail.com/api/exchange/trade/get_list?code_product=BTC";
	public static String PRICE_ETH = "http://bisail.com/api/exchange/trade/get_list?code_product=ETH";
	public static String TRADE_LIST = "http://bisail.com/api/exchange/trade/get_list?code_product=ETH";
	public static String TRADE_HISTORY = "http://bisail.com/api/exchange/trade/get_list?code_product=ETH";
	public static String EXCHANGE_HISTORY = "http://bisail.com/api/exchange/trade/get_list?code_product=ETH";
}
