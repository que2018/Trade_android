package com.coin.trade.network;

import android.util.Log;

import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.coin.trade.constant.CONF;
import com.coin.trade.constant.STATS;

public class GetNetData {
    public static JSONObject getResult(String url) {
        JSONObject outdata = new JSONObject();
		HashMap map = new HashMap();			
			
		try {
			HttpGet httpGet = new HttpGet(url);	
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(httpParameters, CONF.HTTP_SOCKET_TIMEOUT);
			HttpConnectionParams.setConnectionTimeout(httpParameters, CONF.HTTP_CONN_TIMEOUT);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpResponse response = httpClient.execute(httpGet);
			int httpCode = response.getStatusLine().getStatusCode();
			
			if (httpCode == STATS.HTTP_OK) {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");

				Log.d("get_data", result);

				JSONObject data = new JSONObject(result);

				map.put("http_code", httpCode);
				map.put("data", data);
                outdata = new JSONObject(map);	
				
            } else {
            	map.put("http_code", httpCode);
                outdata = new JSONObject(map);	 					 
            }	
		} catch(Exception e){
			map.put("http_code", STATS.HTTP_NO_HOST);
            outdata = new JSONObject(map);	
			
			e.printStackTrace();	
		}
		
		return outdata;
	}
}
