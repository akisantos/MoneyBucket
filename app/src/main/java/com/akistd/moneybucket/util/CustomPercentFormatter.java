package com.akistd.moneybucket.util;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class CustomPercentFormatter implements IValueFormatter {

    private ChartCurrencyFormatter mFormat;

    public CustomPercentFormatter() {
        mFormat = new ChartCurrencyFormatter();
    }

    public CustomPercentFormatter(ChartCurrencyFormatter format) {
        this.mFormat = format;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if (value == 0.0f)
            return "";
        return mFormat.getPrettiedValue(value);
    }
}