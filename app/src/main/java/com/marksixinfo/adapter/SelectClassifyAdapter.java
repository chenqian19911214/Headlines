package com.marksixinfo.adapter;

import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.marksixinfo.R;
import com.marksixinfo.bean.SelectClassifyData;
import com.marksixinfo.interfaces.ISelectClassify;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.recyclerviewHelper.IItemTouchHelperAdapter;
import com.marksixinfo.widgets.recyclerviewHelper.IItemTouchHelperViewHolder;
import com.marksixinfo.widgets.recyclerviewHelper.OnStartDragListener;
import com.marksixinfo.widgets.SwipeMenuLayout;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 发布选择分类
 *
 * @Auther: Administrator
 * @Date: 2019/3/18 0018 14:53
 * @Description:
 */
public class SelectClassifyAdapter extends RecyclerView.Adapter<ViewHolder>
        implements IItemTouchHelperAdapter {


    private List<SelectClassifyData> list;
    private final OnStartDragListener mDragListener;
    private ISelectClassify mISelectClassify;
    private int notMovePosition;
    private boolean isChangeOrderSuccess;
    private boolean isExpand;
    private Context mContext;

    public SelectClassifyAdapter(Context mContext,List<SelectClassifyData> list, OnStartDragListener mDragListener, ISelectClassify mISelectClassify) {
        this.mContext = mContext;
        this.list = list;
        this.mDragListener = mDragListener;
        this.mISelectClassify = mISelectClassify;
        setNotMovePosition();
    }

    public void notifyUI(List<SelectClassifyData> list, boolean isExpand) {
        this.list = list;
        this.isExpand = isExpand;
        setNotMovePosition();
        notifyDataSetChanged();
    }


    //设置不能拖动排序的位置
    private void setNotMovePosition() {
        for (int i = 0; i < list.size(); i++) {
            SelectClassifyData selectClassifyData = list.get(i);
            if (selectClassifyData != null) {
                if (selectClassifyData.getIs_Edit() == 1) {
                    notMovePosition = i;
                    break;
                }
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(mContext, View.inflate(mContext, R.layout.item_select_classify, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SelectClassifyData classifyData = list.get(position);
        String name = classifyData.getName();
        holder.setText(R.id.tv_classify, CommonUtils.StringNotNull(name) ? name : "");
        SwipeMenuLayout swipeMenuLayout = holder.getView(R.id.swipe_layout);

        holder.setVisible(R.id.iv_change_order, true);

        boolean b = classifyData.getIs_Edit() == 1;
        if (b) {//是否可以编辑
            if (classifyData.isEditState()) {//编辑状态
                swipeMenuLayout.setSwipeEnable(true);
                if (classifyData.isExpand()) {//展开
                    swipeMenuLayout.smoothExpand();
                    holder.setVisible(R.id.iv_change_order, false);
                } else {
                    swipeMenuLayout.smoothClose();
                    holder.setVisible(R.id.iv_change_order, true);
                }
                swipeMenuLayout.setListener(new SucceedCallBackListener() {
                    @Override
                    public void succeedCallBack(@Nullable Object o) {
                        holder.setVisible(R.id.iv_change_order, !(Boolean) o);
                    }
                });
            } else {
                swipeMenuLayout.setSwipeEnable(false);
                swipeMenuLayout.smoothClose();
                holder.setVisible(R.id.iv_change_order, false);
                swipeMenuLayout.setListener(null);
            }
        } else {
            swipeMenuLayout.setSwipeEnable(false);
            swipeMenuLayout.smoothClose();
            holder.setVisible(R.id.iv_change_order, false);
            swipeMenuLayout.setListener(null);
        }

        //修改分类名称
        holder.setOnClickListener(R.id.tv_update_name, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mISelectClassify != null) {
                    swipeMenuLayout.quickClose();
                    mISelectClassify.closeAllOpenIndex(-1);
                    mISelectClassify.updateClassifyName(position);
                }
            }
        });

        //删除分类
        holder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mISelectClassify != null) {
                    swipeMenuLayout.quickClose();
                    mISelectClassify.closeAllOpenIndex(-1);
                    mISelectClassify.deleteClassify(position);
                }
            }
        });


        //添加分类
        holder.setOnClickListener(R.id.ll_add_classify, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mISelectClassify != null) {
                    if (!classifyData.isEditState()) {
                        mISelectClassify.clickClassify(classifyData);
                    } else {
                        mISelectClassify.closeAllOpenIndex(-1);
                    }
                }
            }
        });

        //编辑下拖动排序
        holder.getView(R.id.iv_change_order).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    if (!isExpand) {
                        mDragListener.onStartDrag(holder);
                    } else {
                        mISelectClassify.closeAllOpenIndex(-1);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ItemViewHolder extends ViewHolder implements IItemTouchHelperViewHolder {

        ItemViewHolder(Context context, View itemView) {
            super(context, itemView);
        }


        //开始拖动
        @Override
        public void onItemSelected() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.setTranslationZ(10);
            }
            isChangeOrderSuccess = false;
        }


        //拖动结束
        @Override
        public void onItemClear() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.setTranslationZ(0);
            }
            if (mISelectClassify != null && isChangeOrderSuccess) {
                mISelectClassify.changeOrderSuccess();
            }
        }
    }


    //拖动排序,修改数据
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (toPosition <= notMovePosition - 1) {
            return;
        }
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        isChangeOrderSuccess = true;
    }


    //不需要滑动删除
    @Override
    public void onItemDismiss(int position) {
    }
}
