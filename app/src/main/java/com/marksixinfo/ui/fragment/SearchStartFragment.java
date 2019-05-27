package com.marksixinfo.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.marksixinfo.R;
import com.marksixinfo.adapter.SearchKeyWordAdapter;
import com.marksixinfo.base.CommonDialog;
import com.marksixinfo.base.MarkSixFragment;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.HistoryData;
import com.marksixinfo.bean.SearchHotKeyWord;
import com.marksixinfo.interfaces.OnItemClickListener;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.ui.activity.SearchActivity;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索开始页面
 *
 * @Auther: Administrator
 * @Date: 2019/3/16 0016 17:16
 * @Description:
 */
public class SearchStartFragment extends MarkSixFragment {


    @BindView(R.id.recyclerView_history)
    RecyclerView recyclerViewHistory;
    @BindView(R.id.recyclerView_hot)
    RecyclerView recyclerViewHot;
    @BindView(R.id.ll_search_history)
    LinearLayout llSearchHistory;


    ArrayList<String> list = new ArrayList<>();
    private SearchKeyWordAdapter adapter1;
    private SearchKeyWordAdapter adapter2;
    private CommonDialog commonDialog;

    @Override
    public int getViewId() {
        return R.layout.fragment_search_start;
    }

    @Override
    protected void afterViews() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            HistoryData historyData = bundle.getParcelable(SPUtil.SEARCH_HISTORY);
            if (historyData != null) {
                List<String> list = historyData.getList();
                if (CommonUtils.ListNotNull(list)) {
                    llSearchHistory.setVisibility(View.VISIBLE);

                    adapter1 = new SearchKeyWordAdapter(getActivity(), list, new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            SearchActivity activity = (SearchActivity) getActivity();
                            if (activity != null) {
                                activity.getEditTextView().setText(list.get(position));
                                activity.getEditTextView().setSelection(list.get(position).length());
                                activity.goToResult();
                            }
                        }
                    });
                }
            }
        }

        adapter2 = new SearchKeyWordAdapter(getContext(), list, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SearchActivity activity = (SearchActivity) getActivity();
                if (activity != null) {
                    activity.getEditTextView().setText(list.get(position));
                    activity.getEditTextView().setSelection(list.get(position).length());
                    activity.goToResult();
                }
            }
        });
        recyclerViewHistory.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewHistory.setAdapter(adapter1);

        recyclerViewHot.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewHot.setAdapter(adapter2);

        getHotSearch();
    }


    /**
     * 获取热搜词
     */
    private void getHotSearch() {
        new HeadlineImpl(new MarkSixNetCallBack<List<SearchHotKeyWord>>(this, SearchHotKeyWord.class) {
            @Override
            public void onSuccess(List<SearchHotKeyWord> response, int id) {
                if (CommonUtils.ListNotNull(response)) {
                    list.clear();
                    for (SearchHotKeyWord keyWord : response) {
                        if (keyWord != null) {
                            list.add(keyWord.getKeyword());
                        }
                    }
                }
                if (CommonUtils.ListNotNull(list) && adapter2 != null) {
                    SearchActivity activity = (SearchActivity) getActivity();
                    if (activity != null) {
                        activity.getEditTextView().setHint(list.get(0));
                    }
                    adapter2.notifyUI(list);
                }
            }

            @Override
            public void onError(String msg, String code) {
            }
        }.setNeedDialog(false).setNeedToast(false)).getSearchHot();
    }


    @OnClick(R.id.iv_clean_search_history)
    public void onViewClicked() {
        if (commonDialog == null) {
            commonDialog = new CommonDialog(this);
        }
        commonDialog.show(getString(R.string.clear_search_history), "", "取消", "确定", null, new
                View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SPUtil.removeSearchHistory(getContext());
                        if (llSearchHistory != null) {
                            llSearchHistory.setVisibility(View.GONE);
                        }
                        if (commonDialog != null && commonDialog.isShowing()) {
                            commonDialog.dismiss();
                        }
                    }
                });

    }
}
