package com.marksixinfo.adapter.roadbeadtreeview;

import android.view.View;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.ViewHolder;
import com.marksixinfo.bean.RoadBeadRootData;
import com.marksixinfo.bean.RoadBeadTitleData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.ILotteryRoadBead;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.treerecycler.viewholder.TreeItem;
import com.marksixinfo.widgets.treerecycler.viewholder.TreeParentItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 开奖路珠最外层
 *
 * @Auther: Administrator
 * @Date: 2019/5/15 0015 13:12
 * @Description:
 */
public class RoadBeadRootItem extends TreeParentItem<RoadBeadRootData> {

    ILotteryRoadBead lotteryRoadBead;

    public RoadBeadRootItem(RoadBeadRootData data, ActivityIntentInterface context) {
        super(data, null, context);
        lotteryRoadBead = (ILotteryRoadBead) context;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.view_road_bead_head;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RoadBeadRootData data = getData();
        if (data != null) {
            String title = data.getTitle();
            LinkedHashMap<Integer, Boolean> checked = data.getChecked();
            holder.setText(R.id.tv_category_name, CommonUtils.StringNotNull(title) ? title : "");
            for (Map.Entry<Integer, Boolean> entry : checked.entrySet()) {
                if (entry != null) {
                    Integer key = entry.getKey();
                    Boolean value = entry.getValue();
                    TextView textView = setCheckView(holder, key, value);
                    if (textView != null) {
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lotteryRoadBead.checkSelect(key, !value);
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * 设置数据
     *
     * @param holder
     * @param position
     * @param isCheck
     */
    private TextView setCheckView(ViewHolder holder, int position, boolean isCheck) {
        TextView tvBall = null;
        switch (position) {
            case 0:
                tvBall = holder.getView(R.id.tv_ball_1);
                break;
            case 1:
                tvBall = holder.getView(R.id.tv_ball_2);
                break;
            case 2:
                tvBall = holder.getView(R.id.tv_ball_3);
                break;
            case 3:
                tvBall = holder.getView(R.id.tv_ball_4);
                break;
            case 4:
                tvBall = holder.getView(R.id.tv_ball_5);
                break;
            case 5:
                tvBall = holder.getView(R.id.tv_ball_6);
                break;
            case 6:
                tvBall = holder.getView(R.id.tv_ball_7);
                break;
        }
        if (tvBall != null) {
            tvBall.setSelected(isCheck);
        }
        return tvBall;
    }

    @Override
    protected List<TreeItem> initChildsList(RoadBeadRootData data) {
        ArrayList<TreeItem> contentItems = new ArrayList<>();
        if (data != null) {
            LinkedHashMap<Integer, RoadBeadTitleData> titles = data.getTitles();
            if (CommonUtils.MapNotNull(titles)) {
                for (Map.Entry<Integer, RoadBeadTitleData> entry : titles.entrySet()) {
                    if (entry != null) {
                        RoadBeadTitleData title = entry.getValue();
                        if (title != null) {
                            contentItems.add(new RoadBeadTitleItem(title, this, activity));
                        }
                    }
                }
            }
        }
        return contentItems;
    }
}