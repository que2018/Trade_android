package com.coin.trade.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.coin.trade.main.LoginActivity;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import com.coin.trade.R;

public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

	private SliderLayout sliderShow;
	private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);

		sliderShow = rootView.findViewById(R.id.slider_show);
		loginButton = rootView.findViewById(R.id.login);

        //sliders
		ArrayList<Integer> slideList = new ArrayList<>();
		slideList.add(R.drawable.slide1);
		slideList.add(R.drawable.slide2);
		slideList.add(R.drawable.slide3);
		slideList.add(R.drawable.slide4);

		for(int i = 0; i < slideList.size(); i++) {
			DefaultSliderView sliderView = new DefaultSliderView(getActivity());
			sliderView.image(slideList.get(i));
            sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
				
           sliderShow.addSlider(sliderView);
		}
		
        sliderShow.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderShow.setCustomAnimation(new DescriptionAnimation());
        sliderShow.setDuration(4000);
        sliderShow.addOnPageChangeListener(this);

        //go to login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
    
    @Override
    public void onSliderClick(BaseSliderView slider) {}
    
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
    
    @Override
    public void onPageSelected(int position) {}
    
    @Override
    public void onPageScrollStateChanged(int state) {}
}
