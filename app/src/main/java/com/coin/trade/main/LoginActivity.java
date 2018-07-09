package com.coin.trade.main;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.coin.trade.constant.ADDR;
import com.coin.trade.constant.STATS;
import com.coin.trade.customview.LoadingButton;
import com.coin.trade.network.PostNetData;
import com.coin.trade.R;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;
    private TextView signUpText;
    private LoadingButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        signUpText = findViewById(R.id.signup);
        loginButton = findViewById(R.id.login);

        String loginText = getResources().getString(R.string.button_login);

        loginButton.setText(loginText);
        loginButton.setFontSize(18);

        loginButton.setProgressBarColor(0xFFFFFFFF);

        loginButton.addButtonLister(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                usernameText.setFocusable(false);
                passwordText.setFocusable(false);

                loginButton.startLoading();

                //hide keyboard
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);

                View currentView = getCurrentFocus();

                if(currentView == null) {
                    currentView = new View(LoginActivity.this);
                }

                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                LoginTask loginTask = new LoginTask(username, password);
                loginTask.execute();
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                }
            });
        }

    class LoginTask extends AsyncTask<Void, Void, Void> {

        private String username;
        private String password;
        private JSONObject outdata;

        LoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            outdata = PostNetData.getResult(ADDR.LOGIN, nameValuePairs);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            try {
                JSONObject data = (JSONObject)outdata.get("data");
                boolean success = data.getBoolean("success");

                if(success) {
                    data = (JSONObject)data.get("data");

                    String username = data.getString("username");
                    String firstName = data.getString("first_name");
                    String lastName = data.getString("last_name");

                    SharedPreferences.Editor editor = getSharedPreferences("auth", MODE_PRIVATE).edit();
                    editor.putBoolean("is_login",true);
                    editor.putString("username",username);
                    editor.putString("first_name", firstName);
                    editor.putString("last_name", lastName);

                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } else {
                    usernameText.setFocusableInTouchMode(true);
                    passwordText.setFocusableInTouchMode(true);

                    usernameText.setText("");
                    passwordText.setText("");

                    loginButton.stopLoading();

                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("username or password not correct");

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
