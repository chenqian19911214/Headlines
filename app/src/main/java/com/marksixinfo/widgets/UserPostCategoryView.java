package com.marksixinfo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.utils.CommonUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * 他人详情页一级分类
 *
 * @Auther: Administrator
 * @Date: 2019/4/9 0009 14:02
 * @Description:
 */
public class UserPostCategoryView extends LinearLayout implements View.OnClickListener {


    String[] string = {"头条", "论坛", "图库"};
    Map<Integer, TextView> views = new TreeMap();
    int selectColor = 0xffffffff;
    int normalColor = 0xff999999;
    int selectColorBg = 0xfffc5c66;

    OnSelectedListener listener;

    public void setListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnSelectedListener {
        void onSelected(int index);
    }

    public UserPostCategoryView(Context context) {
        super(context);
        init();
    }

    public UserPostCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserPostCategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        for (int i = 0; i < string.length; i++) {
            addView(getTextView(i), new LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
        }
    }


    private TextView getTextView(int index) {
        TextView textView = (TextView) View.inflate(getContext()
                , R.layout.view_user_post_tab, null);
        textView.setText(string[index]);
        views.put(index, textView);
        textView.setOnClickListener(this);
        return textView;

    }

    @Override
    public void onClick(View view) {
        if (view.isSelected()) {
            return;
        }
        if (CommonUtils.MapNotNull(views)) {
            int selectIndex = getSelectIndex(view);
            setIndex(selectIndex);
        }
    }


    /**
     * 设置选择index
     *
     * @param index
     */
    public void setIndex(int index) {
        if (CommonUtils.MapNotNull(views)) {
            for (int i = 0; i < views.size(); i++) {
                TextView view = views.get(i);
                updateSelected(getSelectIndex(view) == index, view);
            }
        }
        if (listener != null) {
            listener.onSelected(index);
        }
    }


    /**
     * 更新选中状态
     *
     * @param isSelected
     * @param item
     */
    void updateSelected(boolean isSelected, TextView item) {
        if (item != null) {
            item.setSelected(isSelected);
            item.setTextColor(isSelected ? selectColor : normalColor);
            item.setBackgroundColor(isSelected ? selectColorBg : selectColor);
        }
    }


    /**
     * 点击位置
     *
     * @param v
     * @return
     */
    public int getSelectIndex(View v) {
        for (Map.Entry<Integer, TextView> entry : views.entrySet()) {
            int key = entry.getKey();
            View value = entry.getValue();
            if (value.equals(v)) {
                return key;
            }
        }
        return 0;
    }
}
