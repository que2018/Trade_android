package com.coin.trade.fragment;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.coin.trade.adapter.TradeAdapter;
import com.coin.trade.constant.ADDR;
import com.coin.trade.constant.STATS;
import com.coin.trade.customview.LoadingButton;
import com.coin.trade.database.model.Trade;
import com.coin.trade.network.GetNetData;
import com.coin.trade.R;
import com.coin.trade.network.PostNetData;

public class TradeFragment extends Fragment {

    private ProgressBar progressBar;
    private ListView tradeList;
    private EditText priceEditText;
    private EditText amountEditText;
    private LoadingButton submitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trade, null);

        progressBar = rootView.findViewById(R.id.progressbar);
        tradeList = rootView.findViewById(R.id.trade_list);
        priceEditText = rootView.findViewById(R.id.price);
        amountEditText = rootView.findViewById(R.id.amount);
        submitButton = rootView.findViewById(R.id.submit);

        String submitText = getResources().getString(R.string.button_submit);

        submitButton.setText(submitText);
        submitButton.setFontSize(18);

        submitButton.setProgressBarColor(0xFFFFFFFF);

        submitButton.addButtonLister(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String price = priceEditText.getText().toString();
            String amount = amountEditText.getText().toString();

            priceEditText.setFocusable(false);
            amountEditText.setFocusable(false);

            submitButton.startLoading();

            SubmitTask submitTask = new SubmitTask(price, amount);
            submitTask.execute();
            }
        });

        TradeTask tradeTask = new TradeTask();
        tradeTask.execute();

        return rootView;
    }

    class TradeTask extends AsyncTask<Void, Void, Void> {

        private JSONObject outdata;

        @Override
        protected Void doInBackground(Void... params) {
            outdata = GetNetData.getResult(ADDR.TRADE);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            progressBar.setVisibility(View.INVISIBLE);
            tradeList.setVisibility(View.VISIBLE);

            try {
                JSONObject data = (JSONObject)outdata.get("data");

                JSONArray tradesJson = data.getJSONArray("trades");

                ArrayList<Trade> trades = new ArrayList<Trade>();

                for(int i = 0; i < tradesJson.length(); i++) {
                    JSONObject priceJson = (JSONObject) tradesJson.get(i);
                    String valueBuy = priceJson.getString("value_buy");
                    String priceBuy = priceJson.getString("price_buy");
                    String valueSell = priceJson.getString("value_sell");
                    String priceSell = priceJson.getString("price_sell");

                    Trade trade = new Trade();
                    trade.setValueBuy(valueBuy);
                    trade.setPriceBuy(priceBuy);
                    trade.setValueSell(valueSell);
                    trade.setPriceSell(priceSell);

                    trades.add(trade);
                }

                TradeAdapter tradeAdapter = new TradeAdapter(getActivity(), trades);
                tradeList.setAdapter(tradeAdapter);

                progressBar.setVisibility(View.INVISIBLE);
                tradeList.setVisibility(View.VISIBLE);

            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class SubmitTask extends AsyncTask<Void, Void, Void> {

        private String price;
        private String amount;
        private JSONObject outdata;

        SubmitTask(String price, String amount) {
            this.price = price;
            this.amount = amount;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("price", price));
            nameValuePairs.add(new BasicNameValuePair("amount", amount));
            outdata = PostNetData.getResult(ADDR.TRADE_SUBMIT, nameValuePairs);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            progressBar.setVisibility(View.INVISIBLE);
            tradeList.setVisibility(View.VISIBLE);

            try {
                JSONObject data = (JSONObject)outdata.get("data");
                int code = data.getInt("code");

                priceEditText.setFocusableInTouchMode(true);
                amountEditText.setFocusableInTouchMode(true);

                priceEditText.setText("");
                amountEditText.setText("");

                submitButton.stopLoading();

                if(code == STATS.TRADE_SUCCESS) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("trade is success");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    alertDialog.show();
                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("trade is fail");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    alertDialog.show();
                }

            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
