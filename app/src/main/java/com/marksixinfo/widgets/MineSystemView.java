package com.marksixinfo.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.utils.CommonUtils;

/**
 * 我的页面系统操作等
 *
 * @Auther: Administrator
 * @Date: 2019/4/19 0019 14:11
 * @Description:
 */
public class MineSystemView extends LinearLayout {


    private TextView tvName;
    private TextView tvMessageCount;
    private View viewLine;
    private String name = "";
    private boolean isViewLine = true;


    public MineSystemView(Context context) {
        super(context);
        init(null, 0);
    }

    public MineSystemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);

    }

    public MineSystemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    private void init(AttributeSet attrs, int defStyle) {

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MineSystemView, defStyle, 0);

        if (a.hasValue(R.styleable.MineSystemView_name)) {
            name = a.getString(R.styleable.MineSystemView_name);
        }
        if (a.hasValue(R.styleable.MineSystemView_line_view)) {
            isViewLine = a.getBoolean(R.styleable.MineSystemView_line_view, isViewLine);
        }
        a.recycle();

        setViews();
    }

    private void setViews() {
        setOrientation(LinearLayout.VERTICAL);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_mine_system, null);
        addView(view);
        tvName = view.findViewById(R.id.tv_name);
        tvMessageCount = view.findViewById(R.id.tv_message_count);
        viewLine = view.findViewById(R.id.view_line);

        tvName.setText(CommonUtils.StringNotNull(name) ? name : "");
        viewLine.setVisibility(isViewLine ? VISIBLE : GONE);

    }

    public void setTvMessageCount(int count) {
        if (tvMessageCount != null) {
            if (count > 0) {
                tvMessageCount.setText(count > 99 ? "99+" : String.valueOf(count));
                tvMessageCount.setVisibility(VISIBLE);
            } else {
                tvMessageCount.setVisibility(INVISIBLE);
            }
        }
    }
}
