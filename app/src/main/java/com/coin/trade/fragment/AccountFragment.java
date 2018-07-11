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

import com.coin.trade.main.LogoutActivity;
import com.coin.trade.R;

public class AccountFragment extends Fragment {

    private TextView usernameText;
    private RelativeLayout logoutBlock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, null);

        usernameText = rootView.findViewById(R.id.username);
        logoutBlock = rootView.findViewById(R.id.logout_block);

        SharedPreferences shared = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
        String firstName = shared.getString("first_name","");
        String lastName = shared.getString("last_name", "");
        usernameText.setText(firstName + " " + lastName);

        logoutBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LogoutActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
