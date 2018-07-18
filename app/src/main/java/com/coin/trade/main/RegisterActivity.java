package com.coin.trade.main;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.coin.trade.constant.ADDR;
import com.coin.trade.customview.LoadingButton;
import com.coin.trade.network.NetClient;
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

                RegisterTask registerTask = new RegisterTask(parameters);
                registerTask.execute();
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

				SMSTask smsTask = new SMSTask(countryCode + "" + phone);
                smsTask.execute();
            }
        });
    }

    class RegisterTask extends AsyncTask<Void, Void, Void> {

        private HashMap<String, String> parameters;
        private JSONObject outdata;

        RegisterTask(HashMap parameters) {
            this.parameters = parameters;
        }

        @Override
        protected Void doInBackground(Void... params) {
            NetClient netClient = NetClient.getInstance();
            outdata = netClient.postRequest(ADDR.REGISTER, parameters, null, null);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            try {
                boolean success = outdata.getBoolean("success");

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

                    registerButton.stopLoading();

                    JSONObject formErrorJson = (JSONObject)outdata.get("form_error");
                    String agreeError = formErrorJson.getString("agree");
                    String smsError = formErrorJson.getString("code[sms]");
                    String emailError = formErrorJson.getString("email");
                    String firstNameError = formErrorJson.getString("first_name");
                    String lastNameError = formErrorJson.getString("last_name");
                    String passwordError = formErrorJson.getString("password");
                    String passwordConfirmError = formErrorJson.getString("password_confirm");
                    String phoneError = formErrorJson.getString("phone");
                    String referralsError = formErrorJson.getString("referrals");

                    String message = "";

                    if(!agreeError.isEmpty()) {
                        String agreeErrorText = getResources().getString(R.string.error_agree);
                        message += agreeErrorText + "\n";
                    }

                    if(!smsError.isEmpty()) {
                        String agreeErrorText = getResources().getString(R.string.error_sms);
                        message += agreeErrorText + "\n";
                    }

                    if(!emailError.isEmpty()) {
                        String agreeErrorText = getResources().getString(R.string.error_email);
                        message += agreeErrorText + "\n";
                    }

                    if(!firstNameError.isEmpty()) {
                        String agreeErrorText = getResources().getString(R.string.error_first_name);
                        message += agreeErrorText + "\n";
                    }

                    if(!lastNameError.isEmpty()) {
                        String agreeErrorText = getResources().getString(R.string.error_last_name);
                        message += agreeErrorText + "\n";
                    }

                    if(!passwordError.isEmpty()) {
                        String agreeErrorText = getResources().getString(R.string.error_password);
                        message += agreeErrorText + "\n";
                    }

                    if(!passwordConfirmError.isEmpty()) {
                        String agreeErrorText = getResources().getString(R.string.error_password_confirm);
                        message += agreeErrorText + "\n";
                    }

                    if(!phoneError.isEmpty()) {
                        String agreeErrorText = getResources().getString(R.string.error_phone);
                        message += agreeErrorText + "\n";
                    }

                    if(!referralsError.isEmpty()) {
                        String agreeErrorText = getResources().getString(R.string.error_referrals);
                        message += agreeErrorText + "\n";
                    }

                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage(message);

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

            NetClient netClient = NetClient.getInstance();
			outdata = netClient.postRequest(ADDR.REGISTER_SMS, parameters, null, null);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
           try {
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
            } 
        }
    }
}
