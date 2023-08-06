package com.akistd.moneybucket.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class ChartCurrencyFormatter implements IValueFormatter, IAxisValueFormatter {

    private static String[] SUFFIX = new String[]{
            "", "K", "Tr", "T", "Nt"
    };
    private static final int MAX_LENGTH = 5;
    private DecimalFormat mFormat;
    private String mText = "";

    public ChartCurrencyFormatter() {
        mFormat = new DecimalFormat("###E00");
    }

    /**
     * Creates a formatter that appends a specified text to the result string
     *
     * @param appendix a text that will be appended
     */
    public ChartCurrencyFormatter(String appendix) {
        this();
        mText = appendix;
    }

    // IValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return makePretty(value) + mText;
    }

    // IAxisValueFormatter
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return makePretty(value) + mText;
    }

    /**
     * Set an appendix text to be added at the end of the formatted value.
     *
     * @param appendix
     */
    public void setAppendix(String appendix) {
        this.mText = appendix;
    }

    /**
     * Set custom suffix to be appended after the values.
     * Default suffix: ["", "k", "m", "b", "t"]
     *
     * @param suff new suffix
     */
    public void setSuffix(String[] suff) {
        SUFFIX = suff;
    }

    /**
     * Formats each number properly. Special thanks to Roman Gromov
     * (https://github.com/romangromov) for this piece of code.
     */
    private String makePretty(double number) {

        String r = mFormat.format(number);

        int numericValue1 = Character.getNumericValue(r.charAt(r.length() - 1));
        int numericValue2 = Character.getNumericValue(r.charAt(r.length() - 2));
        int combined = Integer.valueOf(numericValue2 + "" + numericValue1);

        r = r.replaceAll("E[0-9][0-9]", SUFFIX[combined / 3]);

        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length()-4) + r.substring(r.length() - 2);


        }

        r = r.replace(",Tr","Tr");
        r = r.replace(",K","K");
        r = r.replace(",T","T");
        r = r.replace(",Nt","Nt");

        if (r.contains("K") & r.length()>3){
            r = r.substring(0,3) + "K";
        }
        return r;
    }

    public int getDecimalDigits() {
        return 0;
    }

    public String getPrettiedValue(double v){
        return makePretty(v);
    }

}
