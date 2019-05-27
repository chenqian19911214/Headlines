package com.marksixinfo.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.SimpleAdapter3;
import com.marksixinfo.bean.CarSearchData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.GallerySearchItemClickListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.AutoFlowLayout;
import com.marksixinfo.widgets.ScaleImageView;
import com.marksixinfo.widgets.SpannableStringUtils;
import com.marksixinfo.widgets.glide.GlideUtil;

import java.util.List;

/**
 * 图库搜索结果列表实体
 *
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 20:47
 * @Description:
 */
public class GallerySearchAdapter extends SimpleAdapter3<CarSearchData.ListBean> {


    private final ViewGroup.MarginLayoutParams params;
    private GallerySearchItemClickListener listener;
    private String keyword;

    public GallerySearchAdapter(ActivityIntentInterface context, List<CarSearchData.ListBean> mData
    ,GallerySearchItemClickListener listener) {
        super(context, mData, R.layout.item_gallery_search);
        params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT
                , ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        int j = UIUtils.dip2px(context.getContext(), 10);
        int i = UIUtils.dip2px(context.getContext(), 12);
        params.setMargins(0, j, i, 0);
        this.listener = listener;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void convert(ViewHolder holder, CarSearchData.ListBean item, int position) {
        ScaleImageView imageView = holder.getView(R.id.iv_image);
        //  imageView.setInitSize(Integer.parseInt(item.getWidth()),Integer.parseInt(item.getHeight()));
        GlideUtil.loadImage(item.getPic(), imageView);
        SpannableString oktext = SpannableStringUtils.matcherSearchTitle(0xfffc5c66, item.getTitle(), keyword);
        holder.setText(R.id.tv_title, oktext);
        StringBuffer buffer = new StringBuffer();
        buffer.append("更新至第");
        String fastPeriod = item.getPeriod().get(0);
        String id = item.getId();
        buffer.append(fastPeriod);
        buffer.append("期");
        holder.setText(R.id.tv_update_time, buffer);

        AutoFlowLayout autoFlowLayout = holder.getView(R.id.group_flow_layout);
        if (CommonUtils.ListNotNull(autoFlowLayout.getAllTextView())) {
            autoFlowLayout.removeAllViews();
        }
        for (String str : item.getPeriod()) {
            autoFlowLayout.addView(getTextView(str));
        }

        autoFlowLayout.setOnTextViewClickListener(new AutoFlowLayout.OnTextViewClickListener() {
            @Override
            public void onGetText(TextView textView, int position) {
                if (listener != null) {
                    listener.periodsSelect(id, item.getPeriod().get(position));
                }
            }
        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.periodsSelect(id, fastPeriod);
                }
            }
        });
    }

    private TextView getTextView(String position) {
        TextView textView = new TextView(mContext.getContext());
        textView.setText("第" + CommonUtils.fixThree(position) + "期");
        UIUtils.setPadding(mContext.getContext(), textView, 14, 3, 14, 3);
        textView.setTextSize(14);
        textView.setTextColor(Color.parseColor("#666666"));
        textView.setBackgroundResource(R.drawable.shape_gallery_period);
        textView.setLayoutParams(params);
        return textView;
    }
}
