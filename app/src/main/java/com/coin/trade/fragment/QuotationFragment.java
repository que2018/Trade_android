package com.coin.trade.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.coin.trade.adapter.PriceAdapter;
import com.coin.trade.constant.ADDR;
import com.coin.trade.database.model.Price;
import com.coin.trade.network.GetNetData;
import com.coin.trade.R;

public class QuotationFragment extends Fragment {

    private Button usdtButton;
    private Button btcButton;
    private Button ethButton;

    private ListView priceList;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quotation, null);

        priceList = rootView.findViewById(R.id.price_list);
        progressBar = rootView.findViewById(R.id.progressbar);

        usdtButton = rootView.findViewById(R.id.usdt);
        btcButton = rootView.findViewById(R.id.btc);
        ethButton = rootView.findViewById(R.id.eth);

        PriceTask priceTask = new PriceTask("usdt");
        priceTask.execute();

        usdtButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
				setHeightButton("usdt");
				
                PriceTask priceTask = new PriceTask("usdt");
                priceTask.execute();
            }
        });

        btcButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
				setHeightButton("btc");
				
                PriceTask priceTask = new PriceTask("btc");
                priceTask.execute();
            }
        });

        ethButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
				setHeightButton("eth");
				
                PriceTask priceTask = new PriceTask("eth");
                priceTask.execute();
            }
        });

        return rootView;
    }

    private void setHeightButton(String type) {
		if(type.equals("usdt")) {
			usdtButton.setTextColor(Color.parseColor("#ffffff"));
			btcButton.setTextColor(Color.parseColor("#000000"));
			ethButton.setTextColor(Color.parseColor("#000000"));
			
			usdtButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.r1111_purple));
			btcButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.r1111_grey_light));
			ethButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.r1111_grey_light));
			//layout.setBackground(ContextCompat.getDrawable(context, R.drawable.ready));
		}
		
		if(type.equals("btc")) {
			usdtButton.setTextColor(Color.parseColor("#000000"));
			btcButton.setTextColor(Color.parseColor("#ffffff"));
			ethButton.setTextColor(Color.parseColor("#000000"));
			
			usdtButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.r1111_grey_light));
			btcButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.r1111_purple));
			ethButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.r1111_grey_light));
		}
		
		if(type.equals("eth")) {
			usdtButton.setTextColor(Color.parseColor("#000000"));
			btcButton.setTextColor(Color.parseColor("#000000"));
			ethButton.setTextColor(Color.parseColor("#ffffff"));
			
			usdtButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.r1111_grey_light));
			btcButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.r1111_grey_light));
			ethButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.r1111_purple));
		}
    }

    class PriceTask extends AsyncTask<Void, Void, Void> {

        private String type;
        private JSONObject outdata;

        PriceTask(String type) {
            this.type = type;

            progressBar.setVisibility(View.VISIBLE);
            priceList.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(type.equals("usdt"))
                outdata = GetNetData.getResult(ADDR.PRICE_USDT);

            if(type.equals("btc"))
                outdata = GetNetData.getResult(ADDR.PRICE_BTC);

            if(type.equals("eth"))
                outdata = GetNetData.getResult(ADDR.PRICE_ETH);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            progressBar.setVisibility(View.INVISIBLE);
            priceList.setVisibility(View.VISIBLE);

            try {
                JSONObject data = (JSONObject)outdata.get("data");

                JSONArray pricesJson = data.getJSONArray("prices");

                ArrayList<Price> prices = new ArrayList<Price>();

                for(int i = 0; i < pricesJson.length(); i++) {
                    JSONObject priceJson = (JSONObject) pricesJson.get(i);
                    String title = priceJson.getString("title");
                    String value = priceJson.getString("value");
                    String trend = priceJson.getString("trend").toString();
                    String trendSign = priceJson.getString("trend_sign").toString();

                    Price price = new Price();
                    price.setTitle(title);
                    price.setValue(value);
                    price.setTrend(trend);
                    price.setTrendSign(trendSign);

                    prices.add(price);
                }

                PriceAdapter priceAdapter = new PriceAdapter(prices);
                priceList.setAdapter(priceAdapter);

            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
