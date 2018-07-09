package com.coin.trade.network;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.coin.trade.constant.STATS;

public class PostNetData {
    public static JSONObject getResult(String url, ArrayList<BasicNameValuePair> postdata) {
        JSONObject outdata = new JSONObject();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
		
        try {
            httppost.setEntity(new UrlEncodedFormEntity(postdata));
            HttpResponse response = httpclient.execute(httppost);
            			
			int httpCode = response.getStatusLine().getStatusCode();	
            HashMap map = new HashMap();	
						
			if (httpCode == STATS.HTTP_OK) {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
				JSONObject data = new JSONObject(result);

				Log.d("postnetdata", result);
				
				map.put("http_code", httpCode);
				map.put("data", data);
                outdata = new JSONObject(map);	
			
			} else {
            	map.put("http_code", httpCode);
                outdata = new JSONObject(map);	 			 
            } 
        } catch (ClientProtocolException e) {
			e.printStackTrace();	
        } catch (IOException e) {
			e.printStackTrace();	
        } catch(JSONException e) {
			e.printStackTrace();	
		}
        
        return outdata;
    }
}
