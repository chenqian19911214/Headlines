package com.marksixinfo.widgets.recyclerviewHelper;


import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private IItemTouchHelperAdapter mAdapter;

    private boolean isDrag = false;
    private boolean isSwipe = false;

    public MyItemTouchHelperCallback(IItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public MyItemTouchHelperCallback(IItemTouchHelperAdapter mAdapter, boolean isDrag) {
        this.mAdapter = mAdapter;
        this.isDrag = isDrag;
    }

    public MyItemTouchHelperCallback(IItemTouchHelperAdapter mAdapter, boolean isDrag, boolean isSwipe) {
        this.mAdapter = mAdapter;
        this.isDrag = isDrag;
        this.isSwipe = isSwipe;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //上下拖拽
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //向右侧滑
        int swipeFlags = ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof IItemTouchHelperViewHolder) {
                IItemTouchHelperViewHolder itemTouchHelperViewHolder = (IItemTouchHelperViewHolder) viewHolder;
                itemTouchHelperViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof IItemTouchHelperViewHolder) {
            IItemTouchHelperViewHolder itemTouchHelperViewHolder =
                    (IItemTouchHelperViewHolder) viewHolder;
            itemTouchHelperViewHolder.onItemClear();
        }
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isSwipe;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isDrag;
    }
}
