package com.coin.trade.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.coin.trade.R;

public class LoadingButton extends RelativeLayout {

	private Button ldLoginButton;
	private ProgressBar ldProgressBar;

	public LoadingButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		View rootView = inflater.inflate(R.layout.loading_button, this);

		ldLoginButton = rootView.findViewById(R.id.ld_login);
		ldProgressBar = rootView.findViewById(R.id.ld_progressbar);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

		ldProgressBar.setLayoutParams(params);
	}

	public void setText(String string) {
		ldLoginButton.setText(string);
	}

	public void setFontSize(int fontSize) {
		ldLoginButton.setTextSize(fontSize);
	}

	public void setLdProgressBarSize(int width, int height) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

		ldProgressBar.setLayoutParams(params);
	}

	public void setProgressBarColor(int color) {
		ldProgressBar.getIndeterminateDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY);
	}

	public void addButtonLister(OnClickListener buttonListener) {
		ldLoginButton.setOnClickListener(buttonListener);
	}

	public void startLoading() {
		ldLoginButton.setVisibility(View.INVISIBLE);
		ldProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopLoading() {
		ldLoginButton.setVisibility(View.VISIBLE);
		ldProgressBar.setVisibility(View.INVISIBLE);
	}
}

















