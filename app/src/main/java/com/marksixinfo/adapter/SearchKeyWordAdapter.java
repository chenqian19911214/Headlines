package com.marksixinfo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.marksixinfo.R;
import com.marksixinfo.interfaces.OnItemClickListener;
import com.marksixinfo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 搜索历史,热词搜索通用
 *
 * @Auther: Administrator
 * @Date: 2019/3/16 0016 17:39
 * @Description:
 */
public class SearchKeyWordAdapter extends RecyclerView.Adapter<ViewHolder> {


    private List<String> list;
    private OnItemClickListener itemClickListener;
    private Context mContext;


    public SearchKeyWordAdapter(Context mContext, List<String> list, OnItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    public void notifyUI(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.createViewHolder(mContext, parent, R.layout.view_search_item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.setText(R.id.tv_keyword, CommonUtils.StringNotNull(list.get(position)) ? list.get(position) : "");

        holder.setVisible(R.id.view_line, position % 2 != 0);

        //条目点击搜索
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}