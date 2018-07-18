package com.coin.trade.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coin.trade.main.AboutActivity;
import com.coin.trade.main.BalanceActivity;
import com.coin.trade.main.LoginActivity;
import com.coin.trade.main.LogoutActivity;
import com.coin.trade.main.OrderActivity;
import com.coin.trade.R;

public class AccountFragment extends Fragment {

    private TextView usernameText;
    private TextView loginHintText;

    private RelativeLayout balanceView;
    private RelativeLayout orderView;
    private RelativeLayout aboutView;
    private RelativeLayout logoutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, null);

        usernameText = rootView.findViewById(R.id.username);
        loginHintText = rootView.findViewById(R.id.login_hint);

        balanceView = rootView.findViewById(R.id.balance);
        orderView = rootView.findViewById(R.id.order);
        aboutView = rootView.findViewById(R.id.about);
        logoutView = rootView.findViewById(R.id.logout);

        SharedPreferences shared = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
		
		final boolean isLogin = shared.getBoolean("is_login",false);

		if(isLogin) {
			String firstName = shared.getString("first_name","");
			String lastName = shared.getString("last_name", "");
			usernameText.setText(firstName + " " + lastName);

            String welcomeText = getResources().getString(R.string.text_welcome);
            loginHintText.setText(welcomeText);
        }

        balanceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				if(isLogin) {
					Intent intent = new Intent(getActivity(), BalanceActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(), LoginActivity.class);
					startActivity(intent);
				}
            }
        });
		
		orderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				if(isLogin) {
					Intent intent = new Intent(getActivity(), OrderActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(), LoginActivity.class);
					startActivity(intent);
				}
            }
        });
		
		aboutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Intent intent = new Intent(getActivity(), AboutActivity.class);
				startActivity(intent);
            }
        });
		
		if(isLogin) {
			logoutView.setVisibility(View.VISIBLE);

            logoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LogoutActivity.class);
                    startActivity(intent);
                }
            });
		} else {
			logoutView.setVisibility(View.INVISIBLE);
		}
		
        return rootView;
    }
}


