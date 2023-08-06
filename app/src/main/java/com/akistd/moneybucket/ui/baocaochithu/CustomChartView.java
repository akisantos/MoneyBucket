package com.akistd.moneybucket.ui.baocaochithu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.util.UtilConverter;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class CustomChartView extends MarkerView {
    private TextView tvContent;
    public CustomChartView (Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e != null) {
            tvContent.setText("" + UtilConverter.getInstance().vndCurrencyConverter(Double.valueOf(e.getY())));
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
