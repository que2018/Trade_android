package com.coin.trade.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.coin.trade.adapter.ExchangeAdapter;
import com.coin.trade.adapter.TradeAdapter;
import com.coin.trade.constant.ADDR;
import com.coin.trade.constant.STATS;
import com.coin.trade.customview.LoadingButton;
import com.coin.trade.database.model.Exchange;
import com.coin.trade.database.model.Trade;
import com.coin.trade.network.GetNetData;
import com.coin.trade.network.NetClient;
import com.coin.trade.network.PostNetData;
import com.coin.trade.R;

public class OrderActivity extends AppCompatActivity {

	private Button tradeListButton;
    private Button tradeHistoryButton;
    private Button exchangehistoryButton;

    private ListView historyList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
		
		tradeListButton = findViewById(R.id.trade_list_btn);
        tradeHistoryButton = findViewById(R.id.trade_history_btn);
        exchangehistoryButton = findViewById(R.id.exchange_history_btn);

		historyList = findViewById(R.id.history_list);
        progressBar = findViewById(R.id.progressbar);
		
        TradeListTask tradeListTask = new TradeListTask();
        tradeListTask.execute();

        tradeListButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
				setHeightButton("trade_list_btn");
				
                TradeListTask tradeListTask = new TradeListTask();
				tradeListTask.execute();
            }
        });

        tradeHistoryButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
				setHeightButton("trade_history_btn");
				
				TradeListTask tradeListTask = new TradeListTask();
				tradeListTask.execute();
            }
        });

        exchangehistoryButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
				setHeightButton("exchange_history_btn");
				
                ExchangeHistoryTask exchangeHistoryTask = new ExchangeHistoryTask();
				exchangeHistoryTask.execute();
            }
        });
    }
	
	private void setHeightButton(String type) {
		if(type.equals("trade_list_btn")) {
			tradeListButton.setTextColor(Color.parseColor("#ffffff"));
			tradeHistoryButton.setTextColor(Color.parseColor("#000000"));
			exchangehistoryButton.setTextColor(Color.parseColor("#000000"));
			
			tradeListButton.setBackgroundDrawable(ContextCompat.getDrawable(OrderActivity.this, R.drawable.r1111_purple));
			tradeHistoryButton.setBackgroundDrawable(ContextCompat.getDrawable(OrderActivity.this, R.drawable.r1111_grey_light));
			exchangehistoryButton.setBackgroundDrawable(ContextCompat.getDrawable(OrderActivity.this, R.drawable.r1111_grey_light));
		}
		
		if(type.equals("trade_history_btn")) {
			tradeListButton.setTextColor(Color.parseColor("#000000"));
			tradeHistoryButton.setTextColor(Color.parseColor("#ffffff"));
			exchangehistoryButton.setTextColor(Color.parseColor("#000000"));
			
			tradeListButton.setBackgroundDrawable(ContextCompat.getDrawable(OrderActivity.this, R.drawable.r1111_grey_light));
			tradeHistoryButton.setBackgroundDrawable(ContextCompat.getDrawable(OrderActivity.this, R.drawable.r1111_purple));
			exchangehistoryButton.setBackgroundDrawable(ContextCompat.getDrawable(OrderActivity.this, R.drawable.r1111_grey_light));
		}
		
		if(type.equals("exchange_history_btn")) {
			tradeListButton.setTextColor(Color.parseColor("#000000"));
			tradeHistoryButton.setTextColor(Color.parseColor("#000000"));
			exchangehistoryButton.setTextColor(Color.parseColor("#ffffff"));
			
			tradeListButton.setBackgroundDrawable(ContextCompat.getDrawable(OrderActivity.this, R.drawable.r1111_grey_light));
			tradeHistoryButton.setBackgroundDrawable(ContextCompat.getDrawable(OrderActivity.this, R.drawable.r1111_grey_light));
			exchangehistoryButton.setBackgroundDrawable(ContextCompat.getDrawable(OrderActivity.this, R.drawable.r1111_purple));
		}
    }
	
	class TradeListTask extends AsyncTask<Void, Void, Void> {

        private JSONObject outdata;

        TradeListTask() {
            progressBar.setVisibility(View.VISIBLE);
            historyList.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            outdata = GetNetData.getResult(ADDR.TRADE_HISTORY);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            progressBar.setVisibility(View.INVISIBLE);
            historyList.setVisibility(View.VISIBLE);

            try {
                JSONObject data = (JSONObject)outdata.get("data");

                boolean success = data.getBoolean("success");

                if(success){
                    JSONArray tradesJson = data.getJSONArray("data");

                    ArrayList<Trade> trades = new ArrayList<Trade>();

                    for (int i = 0; i < tradesJson.length(); i++) {
                        JSONObject tradeListJson = (JSONObject)tradesJson.get(i);

                        //JSONObject pirceCurrency = (JSONObject)priceJson.get("price_currency");

                        //String code = pirceCurrency.getString("code");
                        //String value = pirceCurrency.getString("value");
                        //String trend = pirceCurrency.getString("trend").toString();
                        //String trendSign = pirceCurrency.getString("trend_sign").toString();

                        //Trade trade = new Trade();
                        //trade.setCode(code);
                        //trade.setValue(value);
                        //trade.setTrend(value);
                        //trade.setTrendSign("p");

                        //trades.add(trade);
                    }

                    TradeAdapter tradeAdapter = new TradeAdapter(OrderActivity.this, trades);
                    historyList.setAdapter(tradeAdapter);
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
	
	class ExchangeHistoryTask extends AsyncTask<Void, Void, Void> {

        private JSONObject outdata;

        ExchangeHistoryTask() {
            progressBar.setVisibility(View.VISIBLE);
            historyList.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            outdata = GetNetData.getResult(ADDR.TRADE_HISTORY);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            progressBar.setVisibility(View.INVISIBLE);
            historyList.setVisibility(View.VISIBLE);

            try {
                JSONObject data = (JSONObject)outdata.get("data");

                boolean success = data.getBoolean("success");

                if(success){
                    JSONArray exchangesJson = data.getJSONArray("data");

                    ArrayList<Exchange> exchanges = new ArrayList<Exchange>();

                    for (int i = 0; i < exchangesJson.length(); i++) {
                        JSONObject exchangeJson = (JSONObject)exchangesJson.get(i);

                        //JSONObject pirceCurrency = (JSONObject)priceJson.get("price_currency");

                        //String code = pirceCurrency.getString("code");
                        //String value = pirceCurrency.getString("value");
                        //String trend = pirceCurrency.getString("trend").toString();
                        //String trendSign = pirceCurrency.getString("trend_sign").toString();

                        //Trade trade = new Trade();
                        //trade.setCode(code);
                        //trade.setValue(value);
                        //trade.setTrend(value);
                        //trade.setTrendSign("p");

                        //trades.add(trade);
                    }

                    ExchangeAdapter exchangeAdapter = new ExchangeAdapter(OrderActivity.this, exchanges);
                    historyList.setAdapter(exchangeAdapter);
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
