package com.coin.trade.adapter;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coin.trade.database.model.Price;
import com.coin.trade.R;

public class PriceAdapter extends BaseAdapter {
	
	private ArrayList<Price> prices;
	
	public PriceAdapter(ArrayList<Price> prices) {
		this.prices = prices;
	}
	
    @Override
    public int getCount() {
        return prices.size();
    } 

    @Override
    public Price getItem(int position) {	
        return prices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	    View priceView = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_item, parent, false);

        TextView titleText = priceView.findViewById(R.id.title);
        TextView valueText = priceView.findViewById(R.id.value);
        TextView trendText = priceView.findViewById(R.id.trend);

        Price price = prices.get(position);
        String title = price.getTitle();
        String value = price.getValue();
        String trend = price.getTrend();
        String trendSign = price.getTrendSign();

        titleText.setText(title);
        valueText.setText(value);
        trendText.setText(trend);

        if(trendSign.equals("p")) {
            trendText.setBackgroundColor(Color.parseColor("#50c382"));
        } else {
            trendText.setBackgroundColor(Color.parseColor("#ef7955"));
        }

        return priceView;
    }
} 


