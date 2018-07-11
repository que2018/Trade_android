package com.coin.trade.main;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.coin.trade.constant.ADDR;
import com.coin.trade.customview.LoadingButton;
import com.coin.trade.network.GetNetData;
import com.coin.trade.network.PostNetData;
import com.coin.trade.R;

public class LogoutActivity extends AppCompatActivity {

    private LoadingButton logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        logoutButton = findViewById(R.id.logout);

        String logoutText = getResources().getString(R.string.button_logout);

        logoutButton.setText(logoutText);
        logoutButton.setFontSize(18);

        logoutButton.setProgressBarColor(0xFFFFFFFF);

        logoutButton.addButtonLister(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutButton.startLoading();

                LogoutTask logoutTask = new LogoutTask();
                logoutTask.execute();
            }
        });
    }

    class LogoutTask extends AsyncTask<Void, Void, Void> {

        private JSONObject outdata;

        @Override
        protected Void doInBackground(Void... params) {
            outdata = GetNetData.getResult(ADDR.LOGOUT);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            try {
                JSONObject data = (JSONObject)outdata.get("data");
                boolean success = data.getBoolean("success");

                if(success) {
                    SharedPreferences.Editor editor = getSharedPreferences("auth", MODE_PRIVATE).edit();
                    editor.putBoolean("is_login",false);
                    editor.putString("username", "");
                    editor.putString("first_name", "");
                    editor.putString("last_name", "");

                    editor.apply();

                    Intent intent = new Intent(LogoutActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
