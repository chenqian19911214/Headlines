package com.marksixinfo.widgets.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 *  柱状图x
 *
 * @Auther: Administrator
 * @Date: 2019/5/13 0013 21:34
 * @Description:
 */
public class AxisValueFormatter implements IAxisValueFormatter {


    protected String[] mAreaNames;

    public String[] getAreaNames() {
        return mAreaNames;
    }

    public void setAreaNames(String[] mAreaNames) {
        this.mAreaNames = mAreaNames;
    }

    public AxisValueFormatter() {
    }


    public AxisValueFormatter(String[] mAreaNames) {
        this.mAreaNames = mAreaNames;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String s = "";
        try {
            if (mAreaNames.length > 0 && value < mAreaNames.length) {
                s = mAreaNames[(int) value];
                if (s == null) {
                    return "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
