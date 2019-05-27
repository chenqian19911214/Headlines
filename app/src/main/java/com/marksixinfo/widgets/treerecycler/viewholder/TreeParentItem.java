package com.marksixinfo.widgets.treerecycler.viewholder;


import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.widgets.treerecycler.adpater.TreeRecyclerViewType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baozi on 2016/12/22.
 * //拥有子集
 * //子集可以是parent,也可以是child
 * //可以展开与折叠
 * //持有adapter,因为可以影响子集
 * <p/>
 * 修改后,适合进货单全部展开效果
 */

public abstract class TreeParentItem<D> extends TreeItem<D>
        implements ParentItem {
    protected TreeParentItem parentItem;
    /**
     * 持有的子item
     */
    protected List<TreeItem> childs;
    /**
     * 是否展开
     */
    protected boolean isExpand = true;
    /**
     * 能否展开
     */
    protected boolean isCanChangeExpand = true;

    public TreeParentItem(D data, TreeParentItem parentItem, ActivityIntentInterface context) {
        super(data, parentItem, context);
        childs = new ArrayList<>();
        List<TreeItem> treeItems = initChildsList(data);
        if (treeItems != null) {
            childs.addAll(treeItems);
        }
    }

    @Override
    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        if (isCanChangeExpand) {
            isExpand = expand;
        }
    }


    /**
     * 展开
     */
    @Override
    public void onExpand() {

    }

    /**
     * 折叠
     */
    @Override
    public void onCollapse() {

    }

    @Override
    public List<TreeItem> getChilds() {
        return childs;
    }

    public List<TreeItem> getChilds(TreeRecyclerViewType treeRecyclerViewType) {
        ArrayList<TreeItem> treeItems = new ArrayList<>();
        for (int i = 0; i < childs.size(); i++) {
            TreeItem treeItem = childs.get(i);//下级
            treeItems.add(treeItem);//直接add
            if (treeItem instanceof TreeParentItem && ((TreeParentItem) treeItem).isExpand()) {//判断是否还有下下级,并且处于expand的状态
                List list = ((TreeParentItem) treeItem).getChilds(treeRecyclerViewType);//调用下级的getAllChilds遍历,相当于递归遍历
                if (list != null && list.size() > 0) {
                    treeItems.addAll(list);
                }
                if (treeRecyclerViewType == TreeRecyclerViewType.SHOW_ALL) {
                    ((TreeParentItem) treeItem).setExpand(true);
                    ((TreeParentItem) treeItem).onExpand();
                }
                if (treeRecyclerViewType == TreeRecyclerViewType.SHOW_COLLAPSE_CHILDS) {
                    ((TreeParentItem) treeItem).setExpand(false);
                    ((TreeParentItem) treeItem).onCollapse();
                }
            }
        }
        return treeItems;
    }

    public void setChilds(List<TreeItem> childs) {
        this.childs = childs;
    }

    public void addChilds(List<TreeItem> childs) {
        childs.addAll(childs);
    }

    public void addChild(TreeItem treeItem) {
        childs.add(treeItem);
    }

    public void addChild(TreeItem treeItem, int position) {
        childs.add(treeItem);
    }

    public void removeChild(TreeItem treeItem) {
        childs.remove(treeItem);
    }

    public void removeChild(int position) {
        childs.remove(position);
    }

    public void cleanChild() {
        childs.clear();
    }

    public boolean isCanChangeExpand() {
        return isCanChangeExpand;
    }

    public void setCanChangeExpand(boolean canChangeExpand, boolean defualtExpand) {
        isCanChangeExpand = canChangeExpand;
        this.isExpand = defualtExpand;
    }

    /**
     * 初始化子数据
     *
     * @param data 父级数据
     * @return 得到处理好的子集
     */
    protected abstract List<TreeItem> initChildsList(D data);

    /**
     * 当子类发现变化时,父一级是否需要处理
     */
    public void onUpdate() {

    }

    /**
     * 获取当前item的父级
     *
     * @return
     */
    public TreeParentItem getParentItem() {
        return parentItem;
    }
}
