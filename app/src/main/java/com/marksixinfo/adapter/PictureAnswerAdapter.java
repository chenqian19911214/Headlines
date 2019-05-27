package com.marksixinfo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.SimpleAdapter;
import com.marksixinfo.bean.PictreCommentData;
import com.marksixinfo.bean.PictureAnswerData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.IDetailCommentInterface;
import com.marksixinfo.interfaces.NoDoubleClickListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.GoodView;
import com.marksixinfo.widgets.glide.GlideUtil;

import java.util.List;

/**
 * 图库 图解 adapter
 * @Description:
 */
public class PictureAnswerAdapter extends SimpleAdapter<PictureAnswerData> {
    private List<PictureAnswerData> list;
    private Context context;

    private int listSize;
    public PictureAnswerAdapter(Context context, ActivityIntentInterface contextIntentInterface, List<PictureAnswerData> mData) {
        super(contextIntentInterface, mData, R.layout.item_detail_answer);
        this.list = mData;
        this.context = context;
        listSize = mData.size();
    }

    public void notifyUI(List<PictureAnswerData> list) {
        this.list = list;
       // this.listSize = list.size();
        notifyDataSetChanged();
    }

    public void setListSize(int listSize){
        this.listSize = listSize;
    }

    public int getListSize(){
        return listSize;
    }
    @Override
    public int getCount() {
        return listSize;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_detail_answer, null);
            holder = new ViewHolder(viewGroup.getContext(), convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PictureAnswerData detailCommentData = list.get(position);
        holder.setText(R.id.tv_answer_item_name, detailCommentData.getNickname());
        holder.setText(R.id.tv_answer_item_comment, ":  "+ detailCommentData.getContent());
        return convertView;
    }

    @Override
    public void convert(ViewHolder holder, PictureAnswerData item) {
    }
}
