package com.coin.trade.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coin.trade.database.model.Price;
import com.coin.trade.database.model.Trade;
import com.coin.trade.main.TradeChartActivity;
import com.coin.trade.R;

public class TradeAdapter extends BaseAdapter {

    private Context context;
	private ArrayList<Trade> trades;
	
	public TradeAdapter(Context context, ArrayList<Trade> trades) {
		this.context = context;
	    this.trades = trades;
	}
	
    @Override
    public int getCount() {
        return trades.size();
    } 

    @Override
    public Trade getItem(int position) {
        return trades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	    View priceView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trade_item, parent, false);

        TextView buyIndex = priceView.findViewById(R.id.buy_index);
        TextView sellIndex = priceView.findViewById(R.id.sell_index);

        TextView valueBuyText = priceView.findViewById(R.id.value_buy);
        TextView priceBuyText = priceView.findViewById(R.id.price_buy);
        TextView valueSellText = priceView.findViewById(R.id.value_sell);
        TextView priceSellText = priceView.findViewById(R.id.price_sell);

        Trade trade = trades.get(position);

        String index = Integer.toString(position + 1);

        String valueBuy = trade.getValueBuy();
        String priceBuy = trade.getPriceBuy();
        String valueSell = trade.getValueSell();
        String priceSell = trade.getPriceSell();

        buyIndex.setText(index);
        sellIndex.setText(index);

        valueBuyText.setText(valueBuy);
        priceBuyText.setText(priceBuy);
        valueSellText.setText(valueSell);
        priceSellText.setText(priceSell);

        priceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TradeChartActivity.class);
                context.startActivity(intent);
            }
        });

        return priceView;
    }
} 


