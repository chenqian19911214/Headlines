package com.marksixinfo.widgets.treerecycler.adpater;

import android.view.ViewGroup;

import com.marksixinfo.adapter.ViewHolder;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.widgets.treerecycler.viewholder.TreeItem;
import com.marksixinfo.widgets.treerecycler.viewholder.TreeItemManager;
import com.marksixinfo.widgets.treerecycler.viewholder.TreeParentItem;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TreeRecyclerViewAdapter<T extends TreeItem> extends RecyclerView.Adapter<ViewHolder>
        implements TreeItemManager {
    protected TreeRecyclerViewType type;
    /**
     * 上下文
     */
    protected ActivityIntentInterface mContext;
    /**
     * 存储原始的items;
     */
    private List<T> mDatas;//处理后的展示数据
    /**
     * 存储可见的items
     */
    protected List<T> mShowDatas;//处理后的展示数据

    /**
     * @param context 上下文
     * @param datas   条目数据
     */
    public TreeRecyclerViewAdapter(ActivityIntentInterface context, List<T> datas) {
        this(context, datas, TreeRecyclerViewType.SHOW_DEFUTAL);
    }

    /**
     * @param context 上下文
     * @param datas   条目数据
     */
    public TreeRecyclerViewAdapter(ActivityIntentInterface context, List<T> datas, TreeRecyclerViewType type) {
        this.type = type;
        mContext = context;
        mDatas = datas;
        datas = datas == null ? new ArrayList<T>() : datas;
        if (type == TreeRecyclerViewType.SHOW_ALL) {
            mShowDatas = new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                T t = datas.get(i);
                mShowDatas.add(t);
                if (t instanceof TreeParentItem) {
                    List allChilds = ((TreeParentItem) t).getChilds(type);
                    mShowDatas.addAll(allChilds);
                }
            }
        } else {
            mShowDatas = datas;
        }
    }

    public List<T> getmShowDatas() {
        return mShowDatas;
    }

    public void setmShowDatas(List<T> mShowDatas) {
        this.mShowDatas = mShowDatas;
    }

    /**
     * 相应ListView的点击事件 展开或关闭某节点
     *
     * @param position 触发的条目
     */
    private void expandOrCollapse(int position) {
        TreeItem treeItem = null;
        try {
            treeItem = mShowDatas.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (treeItem != null) {
            if (treeItem instanceof TreeParentItem && ((TreeParentItem) treeItem).isCanChangeExpand()) {
                TreeParentItem treeParentItem = (TreeParentItem) treeItem;
                boolean expand = treeParentItem.isExpand();
                List allChilds = treeParentItem.getChilds(type);
                if (expand) {
                    mShowDatas.removeAll(allChilds);
                    treeParentItem.onCollapse();
                    treeParentItem.setExpand(false);
                } else {
                    mShowDatas.addAll(position + 1, allChilds);
                    treeParentItem.onExpand();
                    treeParentItem.setExpand(true);
                }
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    TreeItem treeItem = mShowDatas.get(position);
                    if (treeItem.getSpanSize() == 0) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return treeItem.getSpanSize();
                }
            });
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.createViewHolder(mContext.getContext(), parent, viewType);
    }

    /**
     * position的回调接口
     */
    public interface OnPositionLitener {
        void onPosition(int position);
    }

    private OnPositionLitener OnPositionLitener;

    public void setOnPositionLitener(OnPositionLitener OnPositionLitener) {
        this.OnPositionLitener = OnPositionLitener;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TreeItem treeItem = mShowDatas.get(position);
        treeItem.setTreeItemManager(this);
        treeItem.onBindViewHolder(holder, position);
        if (OnPositionLitener != null) {
            OnPositionLitener.onPosition(holder.getLayoutPosition());
        }
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (type != TreeRecyclerViewType.SHOW_ALL) {
//                    expandOrCollapse(holder.getLayoutPosition());
//                }
//                treeItem.onClickChange(treeItem);
//            }
//        });

    }

    @Override
    public int getItemViewType(int position) {
        return mShowDatas.get(position).getLayoutId();
    }

    @Override
    public int getItemCount() {
        return mShowDatas == null ? 0 : mShowDatas.size();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> datas) {
        mDatas = datas;
        datas = datas == null ? new ArrayList<T>() : datas;
        if (type == TreeRecyclerViewType.SHOW_ALL) {
            mShowDatas = new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                T t = datas.get(i);
                mShowDatas.add(t);
                if (t instanceof TreeParentItem) {
                    List allChilds = ((TreeParentItem) t).getChilds(type);
                    mShowDatas.addAll(allChilds);
                }
            }
        }
        notifyDataSetChanged();
    }

}