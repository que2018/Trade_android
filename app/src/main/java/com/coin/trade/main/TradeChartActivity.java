package com.coin.trade.main;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import com.coin.trade.R;

public class TradeChartActivity extends AppCompatActivity {

    private TextView tvX, tvY;
    private CandleStickChart candleStickChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_trade_chart);

        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);

        candleStickChart = findViewById(R.id.chart);
        candleStickChart.setBackgroundColor(Color.WHITE);

        candleStickChart.getDescription().setEnabled(false);

        //if more than 60 entries are displayed in the chart, no values will be drawn
        candleStickChart.setMaxVisibleValueCount(60);

        //scaling can now only be done on x- and y-axis separately
        candleStickChart.setPinchZoom(false);

        candleStickChart.setDrawGridBackground(false);

        XAxis xAxis = candleStickChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = candleStickChart.getAxisLeft();  
		//leftAxis.setEnabled(false);
        leftAxis.setLabelCount(7, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        
        YAxis rightAxis = candleStickChart.getAxisRight();
        rightAxis.setEnabled(false);
		//rightAxis.setStartAtZero(false);

        int prog = 40;

		tvX.setText("" + prog);
        tvY.setText("" + prog);
        
        candleStickChart.resetTracking();

        ArrayList<CandleEntry> yVals1 = new ArrayList<CandleEntry>();

        for (int i = 0; i < prog; i++) {
            float mult = (1);
            float val = (float)(Math.random() * 40) + mult;
            
            float high = (float)(Math.random() * 9) + 8f;
            float low = (float)(Math.random() * 9) + 8f;
            
            float open = (float)(Math.random() * 6) + 1f;
            float close = (float)(Math.random() * 6) + 1f;

            boolean even = i % 2 == 0;

            yVals1.add(new CandleEntry(
                    i, val + high,
                    val - low,
                    even ? val + open : val - open,
                    even ? val - close : val + close,
                    getResources().getDrawable(R.drawable.star)
            ));
        }

        CandleDataSet set1 = new CandleDataSet(yVals1, "Data Set");

        set1.setDrawIcons(false);
        set1.setAxisDependency(AxisDependency.LEFT);
		//set1.setColor(Color.rgb(80, 80, 80));
        set1.setShadowColor(Color.DKGRAY);
        set1.setShadowWidth(0.7f);
        set1.setDecreasingColor(Color.rgb(22,177,135));
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(Color.rgb(241, 136, 104));
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(Color.BLUE);
        //set1.setHighlightLineWidth(1f);

        CandleData data = new CandleData(set1);
        
        candleStickChart.setData(data);
        candleStickChart.invalidate();
        
        candleStickChart.getLegend().setEnabled(false);
    }
}