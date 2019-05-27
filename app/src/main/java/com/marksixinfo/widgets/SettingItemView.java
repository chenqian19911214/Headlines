package com.marksixinfo.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.utils.CommonUtils;

/**
 * 设置页条目View
 *
 * @Auther: Administrator
 * @Date: 2019/5/24 0024 22:37
 * @Description:
 */
public class SettingItemView extends LinearLayout {


    private TextView tvName;
    private View viewLine;
    private String name = "";
    private boolean isViewLine = true;
    private boolean arrow = true;
    private TextView tvValue;
    private ImageView ivArrow;
    private int color = 0xff333333;


    public SettingItemView(Context context) {
        super(context);
        init(null, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);

    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    private void init(AttributeSet attrs, int defStyle) {

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingItemView, defStyle, 0);

        if (a.hasValue(R.styleable.SettingItemView_setting_name)) {
            name = a.getString(R.styleable.SettingItemView_setting_name);
        }
        if (a.hasValue(R.styleable.SettingItemView_setting_arrow)) {
            arrow = a.getBoolean(R.styleable.SettingItemView_setting_arrow, arrow);
        }
        if (a.hasValue(R.styleable.SettingItemView_setting_color)) {
            color = a.getColor(R.styleable.SettingItemView_setting_color, 0xff333333);
        }
        if (a.hasValue(R.styleable.SettingItemView_setting_line_view)) {
            isViewLine = a.getBoolean(R.styleable.SettingItemView_setting_line_view, isViewLine);
        }

        a.recycle();

        setViews();
    }

    private void setViews() {
        setOrientation(LinearLayout.VERTICAL);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_setting_item, null);
        addView(view);
        tvName = view.findViewById(R.id.tv_name);
        tvValue = view.findViewById(R.id.tv_value);
        ivArrow = view.findViewById(R.id.iv_arrow);
        viewLine = view.findViewById(R.id.view_line);

        tvName.setText(CommonUtils.StringNotNull(name) ? name : "");


        if (arrow) {
            ivArrow.setVisibility(VISIBLE);
        } else {
            ivArrow.setVisibility(GONE);
        }

        tvValue.setTextColor(color);
        viewLine.setVisibility(isViewLine ? VISIBLE : GONE);

    }

    public void setValue(String value) {
        if (tvValue != null) {
            tvValue.setText(CommonUtils.StringNotNull(value) ? value : "");
        }
    }

    public String getValue() {
        String s = "";
        if (tvValue != null) {
            s = tvValue.getText().toString().trim();
        }
        return s;
    }
}
