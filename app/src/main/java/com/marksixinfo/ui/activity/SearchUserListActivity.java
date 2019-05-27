package com.marksixinfo.ui.activity;

import com.google.gson.reflect.TypeToken;
import com.marksixinfo.R;
import com.marksixinfo.adapter.MineConcernAndFansAdapter;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.bean.MineConcernData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.interfaces.IMineConcernInterface;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.JSONUtils;
import com.marksixinfo.widgets.LoadingLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 功能描述: 头条搜索查看更多用户
 *
 * @auther: Administrator
 * @date: 2019/5/24 0024 19:30
 */
public class SearchUserListActivity extends MarkSixActivity implements IMineConcernInterface {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;


    private List<MineConcernData> list = new ArrayList<>();
    private MineConcernAndFansAdapter adapter;


    @Override
    public int getViewId() {
        return R.layout.activity_search_user_list;
    }

    @Override
    public void afterViews() {

        markSixTitle.init("相关用户");

        adapter = new MineConcernAndFansAdapter(getContext(), list, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        setData();
    }


    private void setData() {
        String s = getStringParam(StringConstants.DATA_LIST);
        if (CommonUtils.StringNotNull(s)) {
            if (CommonUtils.StringNotNull(s)) {
                ArrayList<MineConcernData> data = null;
                try {
                    data = JSONUtils.fromJson(s, new TypeToken<ArrayList<MineConcernData>>() {
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (CommonUtils.ListNotNull(data)) {
                    list.addAll(data);
                }
            }
        }
        if (adapter != null) {
            adapter.notifyUI(list);
        }
        if (CommonUtils.ListNotNull(list)) {
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }

    /**
     * 关注接口
     *
     * @param position
     */
    private void concern(int position) {
        if (CommonUtils.ListNotNull(list)) {
            MineConcernData listBean = list.get(position);
            if (listBean != null) {
                int status = listBean.getLook_status();
                listBean.setLook_status(-1);//接口中
                String userId = listBean.getMember_Id();
                new HeadlineImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                    @Override
                    public void onSuccess(LikeAndFavoriteData response, int id) {
                        if (response != null) {
                            refreshEventData(userId, response.getType());
                        }
                    }

                    @Override
                    public void onError(String msg, String code) {
                        super.onError(msg, code);
                        listBean.setLook_status(status);
                        if (adapter != null) {
                            adapter.notifyUI(list);
                        }
                    }
                }.setNeedDialog(false)).concernUser(userId);
            }
        }
    }

    /**
     * 接口刷新实体
     */
    private void refreshEventData(String userId, int is_look) {
        EventBusUtil.post(new CommListDataEvent(CommonUtils.getMainHomeData(userId, is_look)));
    }


    /**
     * 帖子数据变更
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommListDataEvent event) {
        if (event != null && event.getData() != null) {
            MainHomeData data = event.getData();
            if (CommonUtils.ListNotNull(list)) {
                CommonUtils.mineConcernDataChange(data, list);
                if (adapter != null) {
                    adapter.notifyUI(list);
                }
            }
        }
    }

    /**
     * 点击取消/关注
     *
     * @param position
     */
    @Override
    public void deleteConcern(int position) {
        concern(position);
    }

    /**
     * 查看用户详情
     *
     * @param id
     */
    @Override
    public void checkUser(String id) {
        goToUserCenterActivity(id, 0);
    }

}
