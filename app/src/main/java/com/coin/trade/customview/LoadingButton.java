package com.coin.trade.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.coin.trade.R;

public class LoadingButton extends RelativeLayout {
	
    public LoadingButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		View rootView = (ViewGroup)inflater.inflate(R.layout.loading_button, this);
	}
}

















