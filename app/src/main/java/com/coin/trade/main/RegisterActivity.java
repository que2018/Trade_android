package com.coin.trade.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.coin.trade.constant.ADDR;
import com.coin.trade.constant.STATS;
import com.coin.trade.network.PostNetData;
import com.coin.trade.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameText;
    private EditText lastNameText;
	private EditText emailText;
	private EditText phoneNumberText;
	private EditText smsText;
	private EditText passwordText;	
	private EditText passwordConfirmText;
	private EditText referenceNumberText;	
    private Button registerButton;
    private Button smsSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

		firstNameText = findViewById(R.id.firstname);
		lastNameText = findViewById(R.id.lastname);
		emailText = findViewById(R.id.email);
		phoneNumberText = findViewById(R.id.phone);
		smsText = findViewById(R.id.sms);
		passwordText = findViewById(R.id.password);
		passwordConfirmText = findViewById(R.id.password_confirm);
		referenceNumberText = findViewById(R.id.reference_number);
        registerButton = findViewById(R.id.register);
        smsSendButton = findViewById(R.id.sms_send);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
			String firstname = firstNameText.getText().toString();
			String lastname = lastNameText.getText().toString();
			String email = emailText.getText().toString();
			String phoneNumber = phoneNumberText.getText().toString();
			String sms = smsText.getText().toString();
			String password = passwordText.getText().toString();
			String passwordConfirm = passwordConfirmText.getText().toString();
			String referenceNumber = referenceNumberText.getText().toString();

			HashMap<String, String> parameters = new HashMap<String, String>();

			parameters.put("firstname", firstname);
			parameters.put("lastname", lastname);
			parameters.put("email", email);
			parameters.put("phone_number", phoneNumber);
			parameters.put("sms", sms);
			parameters.put("password", password);
			parameters.put("password_confirm", passwordConfirm);
			parameters.put("reference_number", referenceNumber);

			firstNameText.setFocusable(false);
			lastNameText.setFocusable(false);
			emailText.setFocusable(false);
			phoneNumberText.setFocusable(false);
			smsText.setFocusable(false);
			passwordText.setFocusable(false);
			passwordConfirmText.setFocusable(false);
			referenceNumberText.setFocusable(false);

			RegisterTask registerTask = new RegisterTask(parameters);
			registerTask.execute();
            }
        });

        smsSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String phoneNumber = phoneNumberText.getText().toString();

            SMSTask smsTask = new SMSTask(phoneNumber);
            smsTask.execute();
            }
        });
    }

    class RegisterTask extends AsyncTask<Void, Void, Void> {
		
        private String firstname;
        private String lastname;
		private String email;
		private String phoneNumber;
		private String sms;
		private String password;
		private String passwordConfirm;
		private String referenceNumber;
		
        private JSONObject outdata;

        RegisterTask(HashMap parameters) {
            this.firstname = (String)parameters.get("firstname");
			this.lastname = (String)parameters.get("lastname");
            this.email = (String)parameters.get("email");
            this.phoneNumber = (String)parameters.get("phone_number");
            this.sms = (String)parameters.get("sms");
            this.password = (String)parameters.get("password");
            this.passwordConfirm = (String)parameters.get("password_confirm");
            this.referenceNumber = (String)parameters.get("reference_number");
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("firstname", firstname));
            nameValuePairs.add(new BasicNameValuePair("lastname", lastname));
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("phone_number", phoneNumber));
			nameValuePairs.add(new BasicNameValuePair("sms", sms));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("password_confirm", passwordConfirm));
			nameValuePairs.add(new BasicNameValuePair("reference_number", referenceNumber));
			
            outdata = PostNetData.getResult(ADDR.REGISTER, nameValuePairs);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            try {
                JSONObject data = (JSONObject)outdata.get("data");
                int code = data.getInt("code");

                if(code == STATS.REGISTER_SUCCESS) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                } else {
					firstNameText.setFocusableInTouchMode(true);
					lastNameText.setFocusableInTouchMode(true);
					emailText.setFocusableInTouchMode(true);
					phoneNumberText.setFocusableInTouchMode(true);
					smsText.setFocusableInTouchMode(true);
					passwordText.setFocusableInTouchMode(true);
					passwordConfirmText.setFocusableInTouchMode(true);
					referenceNumberText.setFocusableInTouchMode(true);
					
                    firstNameText.setText("");
                    lastNameText.setText("");
					emailText.setText("");
                    phoneNumberText.setText("");
					smsText.setText("");
                    passwordText.setText("");
					passwordConfirmText.setText("");
                    referenceNumberText.setText("");

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

        private String phoneNumber;
        private JSONObject outdata;

        SMSTask(String phoneNumber) {
          this.phoneNumber = phoneNumber;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("phone_number", phoneNumber));

            outdata = PostNetData.getResult(ADDR.REGISTER_SMS, nameValuePairs);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            try {
                JSONObject data = (JSONObject)outdata.get("data");
                int code = data.getInt("code");


            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
