package com.coin.trade.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.MODE_PRIVATE;

public class PostNetData2 {

	public static String sessionID ="";

   	public static JSONObject performPostCall(String requestURL, HashMap<String, String> postDataParams) {

		URL url;
		String response = "";
		JSONObject outdata = new JSONObject();

		try {
			url = new URL(requestURL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			//如果sessionID存在，即存在会话
			if(!sessionID.equals("")){
				Log.d("post net2 id not empty", sessionID);

				conn.setRequestProperty("cookie", sessionID);
			}

			conn.setReadTimeout(15000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();

			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));
			writer.write(getPostDataString(postDataParams));

			writer.flush();
			writer.close();
			
			os.close();

			int responseCode=conn.getResponseCode();

			if (responseCode == HttpsURLConnection.HTTP_OK) {
				//sesssion fetch start
				String cookieValue = conn.getHeaderField("set-cookie");
				if((cookieValue != null) && sessionID.equals("")){
					sessionID = cookieValue.substring(0, cookieValue.indexOf(";"));
				}

				Log.d("post net2", sessionID);
				//session fetch end

				String line;
				BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line=br.readLine()) != null) {
					response+=line;
				}

				Log.d("post response data", response);

				JSONObject data = new JSONObject(response);

				HashMap map = new HashMap();

				map.put("http_code", 200);
				map.put("data", data);
				outdata = new JSONObject(map);
			}
			else {
				response="";    

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return outdata;
	}

	private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for(Map.Entry<String, String> entry : params.entrySet()){
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		return result.toString();
	}
}
