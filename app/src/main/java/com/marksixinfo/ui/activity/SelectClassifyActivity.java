package com.marksixinfo.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.SelectClassifyAdapter;
import com.marksixinfo.base.CommonDialog;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.SelectClassifyData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.SelectClassifyEvent;
import com.marksixinfo.interfaces.ISelectClassify;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.widgets.CommonInputDialog;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.recyclerviewHelper.MyItemTouchHelperCallback;
import com.marksixinfo.widgets.recyclerviewHelper.OnStartDragListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述: 选择分类
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/3/31 0031 18:20
 */
public class SelectClassifyActivity extends MarkSixActivity implements
        OnStartDragListener, TextWatcher, ISelectClassify, View.OnClickListener {

    TextView tvEditState;
    TextView tvEditFinish;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ed_add_classify)
    EditText edAddClassify;
    @BindView(R.id.ll_add_classify)
    LinearLayout llAddClassify;
    @BindView(R.id.tv_save_classify)
    TextView tvSaveClassify;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    private SelectClassifyAdapter adapter;
    private boolean isEditState;
    private String name;
    private CommonInputDialog commonInputDialog;
    private String sortPosition = "";
    private boolean isSortPosition = false;
    private boolean isEdited;//是否编辑过
    private CommonDialog commonDialog;
    private boolean isExpand;

    @Override
    public int getViewId() {
        return R.layout.activity_select_classify;
    }

    List<SelectClassifyData> list = new ArrayList<>();

    private ItemTouchHelper mItemTouchHelper;


    @Override
    public void afterViews() {

        markSixTitle.init("分类编辑", "", "编辑", 0, this);
        tvEditState = markSixTitle.getTvTitle();
        tvEditState.setVisibility(View.GONE);
        tvEditFinish = markSixTitle.getTvFunction();
        tvEditFinish.setVisibility(View.INVISIBLE);

        mLoadingLayout.setRetryListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new SelectClassifyAdapter(this, list, this, this);
        recyclerView.setAdapter(adapter);
        mItemTouchHelper = new ItemTouchHelper(new MyItemTouchHelperCallback(adapter));
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        edAddClassify.addTextChangedListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isExpand) {
                    isExpand = false;
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            closeAllOpenIndex(-1);
                        }
                    }, 100);
                }
            }
        });
        setClassify(false);
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        //通知ItemTouchHelper开始拖拽
        mItemTouchHelper.startDrag(viewHolder);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_function://编辑/完成编辑
                isEditState = !isEditState;
                if (!isEditState && isSortPosition) {
                    sortClassify();
                }
                if (isEditState) {
                    isEdited = true;
                }
                notifyUI();
                break;
            case R.id.retry_button://网络错误
                setClassify(true);
                break;
        }
    }

    @OnClick(R.id.tv_save_classify)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_save_classify://添加分类
                String s = edAddClassify.getText().toString().trim();
                if (!CommonUtils.StringNotNull(s)) {
                    return;
                }
                if (noneRepeatClassify(s)) {
                    addClassify(s);
                }
                break;
        }
    }


    /**
     * 重复分类不能添加
     *
     * @param s
     */
    private boolean noneRepeatClassify(String s) {
        if (CommonUtils.ListNotNull(list)) {
            for (SelectClassifyData data : list) {
                if (data != null) {
                    if (s.equals(data.getName())) {
                        toast("分类已存在");
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * 刷新
     */
    private void setClassify(boolean isInit) {
        new HeadlineImpl(new MarkSixNetCallBack<List<SelectClassifyData>>(this, SelectClassifyData.class) {
            @Override
            public void onSuccess(List<SelectClassifyData> response, int id) {
                setData(response, isInit);
            }
            @Override
            public void onError(String msg, String code) {
                if (!CommonUtils.ListNotNull(list)) {
                    mLoadingLayout.showError();
                } else {
                    super.onError(msg, code);
                }
            }
        }.setNeedDialog(isInit)).setSelectClassify();
    }

    /**
     * 设置数据
     *
     * @param response
     * @param isInit
     */
    private void setData(List<SelectClassifyData> response, boolean isInit) {
        list.clear();
        if (CommonUtils.ListNotNull(response)) {
            list.addAll(response);
        }
        list.add(0, CommonUtils.getDefault());
        notifyClassifyData();
        if (CommonUtils.ListNotNull(list)) {
            tvEditFinish.setVisibility(View.VISIBLE);
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }


    /**
     * 修改名称
     *
     * @param name
     */
    private void changeName(String name, String id) {
        new HeadlineImpl(new MarkSixNetCallBack<List<SelectClassifyData>>(this, SelectClassifyData.class) {
            @Override
            public void onSuccess(List<SelectClassifyData> response, int id) {
                setClassify(false);
                toast("修改成功");
            }

        }).changeSelectClassify(name, id);
    }


    /**
     * 添加分类
     *
     * @param s 分类名称
     */
    private void addClassify(String s) {
        new HeadlineImpl(new MarkSixNetCallBack<List<SelectClassifyData>>(this, SelectClassifyData.class) {
            @Override
            public void onSuccess(List<SelectClassifyData> response, int id) {
                setClassify(false);
                toast("添加成功");
                if (edAddClassify != null) {
                    edAddClassify.setText("");
                }
            }
        }).addSelectClassify(s);
    }

    /**
     * 分类排序
     */
    private void sortClassify() {
        new HeadlineImpl(new MarkSixNetCallBack<List<SelectClassifyData>>(this, SelectClassifyData.class) {
            @Override
            public void onSuccess(List<SelectClassifyData> response, int id) {
                setClassify(false);
                toast("更换顺序成功");
            }
        }).sortSelectClassify(sortPosition);
    }


    /**
     * 删除分类
     *
     * @param id
     */
    private void deleteClassify(String id) {
        new HeadlineImpl(new MarkSixNetCallBack(this, null) {
            @Override
            public void onSuccess(Object response, int id) {
                setClassify(false);
                toast("删除成功");
            }
        }).deleteSelectClassify(id);
    }


    /**
     * 设置初始值
     */
    private void notifyClassifyData() {
        isEditState = false;
        isSortPosition = false;
        sortPosition = getSortPosition();
        notifyUI();
        EventBusUtil.post(new SelectClassifyEvent(list));
    }


    /**
     * 刷新数据
     */
    private void notifyUI() {
        if (CommonUtils.ListNotNull(list)) {
            for (SelectClassifyData data : list) {
                if (data != null) {
                    data.setEditState(isEditState);
                    data.setExpand(false);
                    isExpand = false;
                }
            }
        }
        if (adapter != null) {
            adapter.notifyUI(list, isExpand);
        }
        tvEditFinish.setText(isEditState ? "完成" : "编辑");
        llAddClassify.setVisibility(isEditState ? View.VISIBLE : View.GONE);
        tvEditState.setVisibility(isEditState ? View.VISIBLE : View.GONE);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    /**
     * 动态改变添加按钮
     *
     * @param charSequence
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String s = charSequence.toString().trim();
        if (CommonUtils.StringNotNull(s)) {
            if (s.length() > NumberConstants.CLASSIFY_NAME_LENGTH) {
                toast("分类名称不能超过" + NumberConstants.CLASSIFY_NAME_LENGTH + "个字符");
                return;
            }
            tvSaveClassify.setBackgroundColor(0xff42c056);
        } else {
            tvSaveClassify.setBackgroundColor(0xffdddddd);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * 关闭/打开侧滑
     *
     * @param position
     */
    @Override
    public void closeAllOpenIndex(int position) {
        if (CommonUtils.ListNotNull(list)) {
            for (int i = 0; i < list.size(); i++) {
                SelectClassifyData data = list.get(i);
                if (data != null) {
                    if (position == -1) {
                        isExpand = false;
                        data.setExpand(false);
                    } else {
                        isExpand = true;
                        data.setExpand(i == position);
                    }
                }
            }
            adapter.notifyUI(list, isExpand);
        }
    }

    /**
     * 点击
     *
     * @param classify
     */
    @Override
    public void clickClassify(SelectClassifyData classify) {
        if (classify != null) {
            Intent intent = new Intent(this, SelectClassifyActivity.class);
            intent.putExtra(StringConstants.RELEASE_CLASSIFY_SELECT, classify.getId());
            intent.putExtra(StringConstants.INTENT_PARAMS_BOOLEAN, isEdited);
            setResult(RESULT_OK, intent);
            super.finish();
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(StringConstants.INTENT_PARAMS_BOOLEAN, isEdited);
        intent.putExtra(StringConstants.RELEASE_CLASSIFY_SELECT, "");
        setResult(RESULT_OK, intent);
        super.finish();
    }

    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void deleteClassify(int position) {
        if (commonDialog == null) {
            commonDialog = new CommonDialog(this);
        }
        commonDialog.show("是否删除帖子分类？", "", "取消", "确定", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commonDialog != null && commonDialog.isShowing()) {
                    commonDialog.dismiss();
                }
                SelectClassifyData selectClassifyData = getClassifyDataByIndex(position);
                if (selectClassifyData != null) {
                    deleteClassify(selectClassifyData.getId());
                }
            }
        });
    }

    /**
     * 拖动排序完成
     */
    @Override
    public void changeOrderSuccess() {
        String sortPosition = getSortPosition();
        if (!this.sortPosition.equals(sortPosition)) {
            this.sortPosition = sortPosition;
            isSortPosition = true;
        }
    }


    /**
     * 获取排序id
     *
     * @return
     */
    private String getSortPosition() {
        if (CommonUtils.ListNotNull(list)) {
            ArrayList<String> array = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                SelectClassifyData data = list.get(i);
                String id = data.getId();
                if (!"0".equals(id)) {
                    array.add(id);
                }
            }
            return array.toString();
        }
        return "";
    }


    /**
     * 修改分类名称
     *
     * @param position
     */
    @Override
    public void updateClassifyName(int position) {
        SelectClassifyData selectClassifyData = getClassifyDataByIndex(position);
        if (selectClassifyData != null) {
            name = selectClassifyData.getName();
            if (commonInputDialog == null) {
                commonInputDialog = new CommonInputDialog(this);
            }
            commonInputDialog.show("修改分类名称", "请输入分类名称(10字以内)", "取消", "确认", null, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = commonInputDialog.getInputContent().trim();
                    if (commonInputDialog != null && commonInputDialog.isShowing()) {
                        commonInputDialog.dismiss();
                    }
                    if (!s.equals(name)) {
                        changeName(s, selectClassifyData.getId());
                    }
                }
            }).setTextViewInput(name);
        }
    }

    /**
     * 根据position获取实体
     *
     * @param index
     * @return
     */
    private SelectClassifyData getClassifyDataByIndex(int index) {
        if (CommonUtils.ListNotNull(list)) {
            SelectClassifyData selectClassifyData = null;
            try {
                selectClassifyData = list.get(index);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (selectClassifyData != null) {
                return selectClassifyData;
            }
        }
        return null;
    }
}