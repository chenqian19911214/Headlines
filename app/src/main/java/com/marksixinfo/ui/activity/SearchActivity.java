package com.marksixinfo.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.bean.HistoryData;
import com.marksixinfo.bean.ReleaseSelectData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.ui.fragment.SearchResultFragment;
import com.marksixinfo.ui.fragment.SearchStartFragment;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.JSONUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.AutoFlowLayout;
import com.marksixinfo.widgets.CleanEditTextView;
import com.marksixinfo.widgets.easyemoji.KeyBoardUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述: 搜索页面
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/3/31 0031 18:15
 */
public class SearchActivity extends MarkSixActivity implements TextView.OnEditorActionListener,
        View.OnClickListener, AutoFlowLayout.OnTextViewClickListener {


    @BindView(R.id.cetv_editTextView)
    CleanEditTextView mEditTextView;
    @BindView(R.id.fl_select_period)
    FrameLayout flSelectPeriod;
    @BindView(R.id.tv_select_period)
    TextView tvSelectPeriod;
    @BindView(R.id.AutoFlowLayout)
    AutoFlowLayout autoFlowLayout;


    int index = -1;
    String keyword = "";
    private SearchResultFragment searchResultFragment;
    private List<ReleaseSelectData> period = new ArrayList<>();
    private ViewGroup.MarginLayoutParams params;

    @Override
    public int getViewId() {
        return R.layout.activity_search;
    }

    @Override
    public void afterViews() {

        params = new ViewGroup.MarginLayoutParams(UIUtils.dip2px(getContext(), 75)
                , UIUtils.dip2px(getContext(), 45));
        int margins15 = UIUtils.dip2px(getContext(), 15);
        params.setMargins(margins15, margins15, 0, 0);

        mEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handleKeyWord(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        mEditTextView.setOnEditorActionListener(this);
        mEditTextView.setOnClickListener(this);

        goToStart();
        getAllPeriod();

    }


    /**
     * 动态处理输入
     *
     * @param string
     */
    private void handleKeyWord(String string) {
        keyword = string;
        if (!CommonUtils.StringNotNull(keyword)) {
            goToStart();
        }
    }


    /**
     * 去搜索起始页面
     */
    public void goToStart() {
        if (index == 0) {
            return;
        } else {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            List<String> history = getHistory();
            SearchStartFragment searchStartFragment = new SearchStartFragment();
            if (history != null && history.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(SPUtil.SEARCH_HISTORY, new HistoryData(history));
                searchStartFragment.setArguments(bundle);
            }
            if (index == -1) {
                supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_right_in,
                                R.anim.slide_left_out,
                                R.anim.slide_left_in,
                                R.anim.slide_right_out
                        ).replace(R.id.fl_content, searchStartFragment)
                        .commitAllowingStateLoss();
            } else {
                supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_left_in,
                                R.anim.slide_right_out,
                                R.anim.slide_right_in,
                                R.anim.slide_left_out
                        ).replace(R.id.fl_content, searchStartFragment)
                        .commitAllowingStateLoss();
            }

            index = 0;
            KeyBoardUtils.setEditTextState(mEditTextView);
        }
    }


    /**
     * 去搜索结果页面
     */
    public void goToResult() {
        if (index != 1) {
            index = 1;
        } else {
            if (searchResultFragment != null) {
                searchResultFragment.startData(keyword, false);
            }
            return;
        }
        saveHistory();
        mEditTextView.setCursorVisible(false);
        searchResultFragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString(StringConstants.SEARCH_KEYWORD, keyword);
        searchResultFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right_in,
                        R.anim.slide_left_out,
                        R.anim.slide_left_in,
                        R.anim.slide_right_out
                ).replace(R.id.fl_content, searchResultFragment)
                .commitAllowingStateLoss();
    }


    /**
     * 软键盘搜索动作
     *
     * @param textView
     * @param i
     * @param keyEvent
     * @return
     */
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            // 当按了搜索之后关闭软键盘
            ((InputMethodManager) mEditTextView.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    SearchActivity.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            if (!CommonUtils.StringNotNull(keyword)) {
                //未输入直接搜索时,搜索第一个热搜词
                String hint = String.valueOf(mEditTextView.getHint());
                if (CommonUtils.StringNotNull(hint)) {
                    keyword = hint;
                    mEditTextView.setText(keyword);
                    mEditTextView.setSelection(keyword.length());
                }
            }
            goToResult();
            return true;
        }
        return false;
    }

    public CleanEditTextView getEditTextView() {
        return mEditTextView;
    }


    /**
     * 保存搜索历史
     */
    private void saveHistory() {
        List<String> history = getHistory();
        if (CommonUtils.StringNotNull(keyword)) {
            if (CommonUtils.ListNotNull(history)) {
                if (history.contains(keyword)) {
                    history.remove(keyword);
                }
                history.add(0, keyword);
                if (history.size() > NumberConstants.SEARCH_HISTORY_SIZE) {
                    history = history.subList(0, NumberConstants.SEARCH_HISTORY_SIZE);
                }
                saveHistory(history);
            } else {
                List<String> list = new ArrayList<>();
                list.add(keyword);
                saveHistory(list);
            }
        }
    }

    /**
     * 保存搜索历史
     */
    private void saveHistory(List<String> set) {
        SPUtil.setSearchHistory(getContext(), JSONUtils.toJson(new HistoryData(set)));
    }


    /**
     * 获取搜索记录
     *
     * @return
     */
    private List<String> getHistory() {
        List<String> set = new ArrayList<>();
        String value = SPUtil.getSearchHistory(getContext());
        if (CommonUtils.StringNotNull(value)) {
            HistoryData historyData = null;
            try {
                historyData = JSONUtils.fromJson(value, HistoryData.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (historyData != null) {
                List<String> list = historyData.getList();
                if (list != null) {
                    return list;
                }
            }
        }
        return set;
    }


    /**
     * 监听返回键,搜索结果页时,返回搜索起始页
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (index == 1 && mEditTextView != null) {
                mEditTextView.setText("");
            } else {
                finish();
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        mEditTextView.setCursorVisible(true);
    }


    @OnClick({R.id.rl_select_period, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_select_period:
//                toast("选择期数");
                selectPeriod();
                break;
            case R.id.tv_cancel:
                if (CommonUtils.StringNotNull(keyword)) {
                    mEditTextView.setText("");
                } else {
                    finish();
                }
                break;
        }
    }

    private void getAllPeriod() {
        period.clear();
        period.add(new ReleaseSelectData("0", "全部", true));
        for (int i = 20; i > 1; i--) {
            ReleaseSelectData releaseSelectData = new ReleaseSelectData();
            releaseSelectData.setName(CommonUtils.fixThree(i + "") + "期");
            releaseSelectData.setValue(i + "");
            period.add(releaseSelectData);
        }
    }


    /**
     * 设置期数选择
     */
    private void selectPeriod() {
        if (flSelectPeriod.getVisibility() == View.VISIBLE) {
            flSelectPeriod.setVisibility(View.GONE);
        } else {
            if (!CommonUtils.ListNotNull(autoFlowLayout.getAllTextView()) &&
                    CommonUtils.ListNotNull(period)) {
                for (int i = 0; i < period.size(); i++) {
                    ReleaseSelectData data = period.get(i);
                    if (data != null) {
                        TextView textView = getPeriodTextView(data.getName());
                        if (i == 0) {
                            textView.setTextColor(0xfffc5c66);
                        }
                        autoFlowLayout.addView(textView);
                    }
                }
                autoFlowLayout.setOnTextViewClickListener(this);
            }

            flSelectPeriod.setVisibility(View.VISIBLE);

            flSelectPeriod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flSelectPeriod.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 获取期数弹框TextView
     *
     * @param s
     * @return
     */
    private TextView getPeriodTextView(String s) {
        TextView textView = new TextView(getContext());
        textView.setText(s);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(15);
        textView.setTextColor(0xff333333);
        textView.setBackgroundResource(R.drawable.shape_release_select);
        textView.setLayoutParams(params);
        return textView;
    }


    @Override
    public void onGetText(TextView textView, int position) {
        flSelectPeriod.setVisibility(View.GONE);
        List<TextView> allTextView = autoFlowLayout.getAllTextView();
        if (CommonUtils.ListNotNull(allTextView)) {
            for (TextView v : allTextView) {
                if (v != null) {
                    v.setTextColor(0xff333333);
                }
            }
            textView.setTextColor(0xfffc5c66);
            String value = period.get(position).getValue();
            String name = period.get(position).getName();
            tvSelectPeriod.setText(name);
        }
    }
}
