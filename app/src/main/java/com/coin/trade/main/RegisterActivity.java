package com.coin.trade.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.coin.trade.constant.ADDR;
import com.coin.trade.constant.STATS;
import com.coin.trade.customview.LoadingButton;
import com.coin.trade.network.NetClient;
import com.coin.trade.network.PostNetData;
import com.coin.trade.R;

public class RegisterActivity extends AppCompatActivity {

    private String countryCode = "1";

    private EditText firstNameText;
    private EditText lastNameText;
	private EditText emailText;
	private EditText phoneText;
	private EditText smsText;
	private EditText passwordText;	
	private EditText passwordConfirmText;
	private EditText referralsText;
	private Spinner coutnryCodeSpinner;
	private CheckBox agreeCheckBox;
    private LoadingButton registerButton;
    private LoadingButton smsSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

		firstNameText = findViewById(R.id.firstname);
		lastNameText = findViewById(R.id.lastname);
		emailText = findViewById(R.id.email);
		phoneText = findViewById(R.id.phone);
		smsText = findViewById(R.id.sms);
		passwordText = findViewById(R.id.password);
		passwordConfirmText = findViewById(R.id.password_confirm);
        referralsText = findViewById(R.id.referrals);
        registerButton = findViewById(R.id.register);
        smsSendButton = findViewById(R.id.sms_send);
        coutnryCodeSpinner = findViewById(R.id.country_code);
        agreeCheckBox = findViewById(R.id.agree);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coutnryCodeSpinner.setAdapter(adapter);

        coutnryCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                if(pos == 1)
                    countryCode = "1";

                if(pos == 2)
                    countryCode = "86";
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        String registerText = getResources().getString(R.string.button_register);
        String smsSendText = getResources().getString(R.string.button_sms_send);

        registerButton.setText(registerText);
        registerButton.setFontSize(18);
        registerButton.setProgressBarColor(0xFFFFFFFF);

        smsSendButton.setText(smsSendText);
        smsSendButton.setFontSize(14);
        smsSendButton.setProgressBarColor(0xFFFFFFFF);
        smsSendButton.setLdProgressBarSize(60, 60);

        registerButton.addButtonLister(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = firstNameText.getText().toString();
                String lastname = lastNameText.getText().toString();
                String email = emailText.getText().toString();
                String phone = phoneText.getText().toString();
                String sms = smsText.getText().toString();
                String password = passwordText.getText().toString();
                String passwordConfirm = passwordConfirmText.getText().toString();
                String referrals = referralsText.getText().toString();

                String agree = "0";

                if(agreeCheckBox.isChecked()) {
                    agree = "1";
                }

                HashMap<String, String> parameters = new HashMap<String, String>();

                parameters.put("first_name", firstname);
                parameters.put("last_name", lastname);
                parameters.put("email", email);
                parameters.put("country_code", countryCode);
                parameters.put("phone", phone);
                parameters.put("code[sms]", sms);
                parameters.put("password", password);
                parameters.put("password_confirm", passwordConfirm);
                parameters.put("referrals", referrals);
                parameters.put("agree", agree);

                firstNameText.setFocusable(false);
                lastNameText.setFocusable(false);
                emailText.setFocusable(false);
                phoneText.setFocusable(false);
                smsText.setFocusable(false);
                passwordText.setFocusable(false);
                passwordConfirmText.setFocusable(false);
                referralsText.setFocusable(false);

                registerButton.startLoading();

                //RegisterTask registerTask = new RegisterTask(parameters);
                //registerTask.execute();

                NetClient.getInstance().request(NetClient.Method.POST, ADDR.REGISTER, parameters, new NetClient.Response() {
                    @Override
                    public void responseJSON(JSONObject jsonData) {
                        if ( jsonData == null ) {

                            Log.d("net data", "it is null");

                            return;
                        }

                        try {
                            boolean success = jsonData.getBoolean("success");

                            if(success) {
                                Log.d("net data", "it is a success");
                                smsSendButton.stopLoading();
                            } else {
                                String code = jsonData.getString("code");
                                Log.d("error code", code);
                                smsSendButton.stopLoading();
                            }

                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        smsSendButton.addButtonLister(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phoneText.getText().toString();

                //hide keyboard
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);

                View currentView = getCurrentFocus();

                if(currentView == null) {
                    currentView = new View(RegisterActivity.this);
                }

                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                smsSendButton.startLoading();

                HashMap<String, String> parameters = new HashMap<String, String>();

                parameters.put("type", "sms");
                parameters.put("register", "register");
                parameters.put("phone", countryCode + "" + phone);

                NetClient.getInstance().request(NetClient.Method.POST, ADDR.REGISTER_SMS, parameters, new NetClient.Response() {
                    @Override
                    public void responseJSON(JSONObject jsonData) {
                        if ( jsonData == null ) {

                            Log.d("net data", "it is null");

                            return;
                        }

                        try {
                            boolean success = jsonData.getBoolean("success");

                            if(success) {
                                Log.d("net data", "it is a success");
                                smsSendButton.stopLoading();
                            }

                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    class RegisterTask extends AsyncTask<Void, Void, Void> {

        private String firstname;
        private String lastname;
		private String email;
		private String countryCode;
		private String phone;
		private String sms;
		private String password;
		private String passwordConfirm;
		private String referrals;
        private String agree;

        private JSONObject outdata;

        RegisterTask(HashMap parameters) {
            this.firstname = (String)parameters.get("firstname");
			this.lastname = (String)parameters.get("lastname");
            this.email = (String)parameters.get("email");
            this.countryCode = (String)parameters.get("country_code");
            this.phone = (String)parameters.get("phone");
            this.sms = (String)parameters.get("sms");
            this.password = (String)parameters.get("password");
            this.passwordConfirm = (String)parameters.get("password_confirm");
            this.referrals = (String)parameters.get("referrals");
            this.agree = (String)parameters.get("agree");
        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.d("reigister ....", firstname);
            Log.d("reigister ....", lastname);
            Log.d("reigister ....", email);
            Log.d("reigister ....", countryCode);
            Log.d("reigister ....", phone);
            Log.d("reigister ....", sms);
            Log.d("reigister ....", password);
            Log.d("reigister ....", passwordConfirm);
            Log.d("reigister ....", referrals);

            ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("first_name", firstname));
            nameValuePairs.add(new BasicNameValuePair("last_name", lastname));
			nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("country_code", countryCode));
            nameValuePairs.add(new BasicNameValuePair("phone", phone));
			nameValuePairs.add(new BasicNameValuePair("code[sms]", sms));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("password_confirm", passwordConfirm));
			nameValuePairs.add(new BasicNameValuePair("referrals", referrals));
            nameValuePairs.add(new BasicNameValuePair("agree", agree));

            outdata = PostNetData.getResult(ADDR.REGISTER, nameValuePairs);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            try {
                JSONObject data = (JSONObject)outdata.get("data");
                boolean success = data.getBoolean("success");

                if(success) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                } else {
					firstNameText.setFocusableInTouchMode(true);
					lastNameText.setFocusableInTouchMode(true);
					emailText.setFocusableInTouchMode(true);
					phoneText.setFocusableInTouchMode(true);
					smsText.setFocusableInTouchMode(true);
					passwordText.setFocusableInTouchMode(true);
					passwordConfirmText.setFocusableInTouchMode(true);
                    referralsText.setFocusableInTouchMode(true);
					
                    firstNameText.setText("");
                    lastNameText.setText("");
					emailText.setText("");
                    phoneText.setText("");
					smsText.setText("");
                    passwordText.setText("");
					passwordConfirmText.setText("");
                    referralsText.setText("");

                    registerButton.stopLoading();

                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("some information are not correct");

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

    class SMSTask extends AsyncTask<Void, Void, Void> {

        private String phone;
        private JSONObject outdata;

        SMSTask(String phone) {
          this.phone = phone;
        }

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> parameters = new HashMap<String, String>();

            parameters.put("type", "sms");
            parameters.put("register", "register");
            parameters.put("phone", phone);

            NetClient.getInstance().request(NetClient.Method.POST, ADDR.REGISTER_SMS, parameters, new NetClient.Response() {
                @Override
                public void responseJSON(JSONObject jsonData) {
                    if ( jsonData == null ) {

                        Log.d("net data", "it is null");

                        return;
                    }

                    try {
                        outdata = jsonData;
                        boolean success = jsonData.getBoolean("success");

                        if(success)
                            Log.d("net data", "it is a success");

                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
           /* try {
                JSONObject data = (JSONObject)outdata.get("data");
                boolean success = outdata.getBoolean("success");

                if(success) {
                    String smsSent = getResources().getString(R.string.text_sms_sent);
                    Toast.makeText(RegisterActivity.this, smsSent, Toast.LENGTH_LONG).show();
                } else {
                    String msg = outdata.getString("msg");
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                }

                smsSendButton.stopLoading();

            } catch(JSONException e) {
                e.printStackTrace();
            } */
        }
    }
}
