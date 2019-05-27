package com.marksixinfo.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.bean.HistoryData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.ui.fragment.GallerySearchResultFragment;
import com.marksixinfo.ui.fragment.GallerySearchStartFragment;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.JSONUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.widgets.CleanEditTextView;
import com.marksixinfo.widgets.easyemoji.KeyBoardUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 图库搜索
 *
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 12:25
 * @Description:
 */
public class GallerySearchActivity extends MarkSixActivity
        implements TextView.OnEditorActionListener, View.OnClickListener {



    @BindView(R.id.cetv_editTextView)
    CleanEditTextView mEditTextView;

    int index = -1;
    String keyword = "";
    private GallerySearchResultFragment searchResultFragment;


    @Override
    public int getViewId() {
        return R.layout.activity_gallery_search;
    }


    @Override
    public void afterViews() {

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


    @OnClick(R.id.tv_cancel)
    public void onViewClicked() {
        if (CommonUtils.StringNotNull(keyword)) {
            mEditTextView.setText("");
        } else {
            finish();
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
            GallerySearchStartFragment searchStartFragment = new GallerySearchStartFragment();
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
                searchResultFragment.startData(keyword);
            }
            return;
        }
        saveHistory();
        mEditTextView.setCursorVisible(false);
        searchResultFragment = new GallerySearchResultFragment();
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
                    GallerySearchActivity.this.getCurrentFocus().getWindowToken(),
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
        SPUtil.setSearchGalleryHistory(getContext(), JSONUtils.toJson(new HistoryData(set)));
    }


    /**
     * 获取搜索记录
     *
     * @return
     */
    private List<String> getHistory() {
        List<String> set = new ArrayList<>();
        String value = SPUtil.getSearchGalleryHistory(getContext());
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

}
