package com.marksixinfo.widgets.dockingexpandablelistview.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.marksixinfo.bean.MineCommentData;

/**
 * Created by qianxin on 2016/11/28.
 */
public interface IDockingAdapterDataSource {
    int getGroupCount();
    int getChildCount(int groupPosition);
    Object getGroup(int groupPosition);
    MineCommentData.ReplyBean getChild(int groupPosition, int childPosition);
    View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);
    View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);
}
