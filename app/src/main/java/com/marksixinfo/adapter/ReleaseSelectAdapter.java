package com.marksixinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.bean.ReleaseSelectData;
import com.marksixinfo.interfaces.OnItemClickListener;
import com.marksixinfo.utils.CommonUtils;

import java.util.List;

/**
 *  发布选择期数
 *
 * @Auther: Administrator
 * @Date: 2019/3/28 0028 13:43
 * @Description:
 */
public class ReleaseSelectAdapter extends BaseAdapter {


    private List<ReleaseSelectData> list;
    private OnItemClickListener listener;
    private Context context;

    public ReleaseSelectAdapter(Context context, List<ReleaseSelectData> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
        this.context = context;
    }

    public void notifyUI(List<ReleaseSelectData> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ReleaseSelectData getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_release_select_popu, null);
            holder = new ViewHolder(context, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ReleaseSelectData data = list.get(position);

        TextView period = holder.getView(R.id.tv_period_number_name);

        String name = data.getName();
        boolean check = data.isCheck();
        period.setText(CommonUtils.StringNotNull(name) ? name : "");
        if (check) {
            period.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            period.setTextColor(context.getResources().getColor(R.color.black_333));
        }

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(view, position);
                }
            }
        });

        return convertView;
    }
}
