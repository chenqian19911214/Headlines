package com.marksixinfo.adapter.roadbeadtreeview;

import com.marksixinfo.R;
import com.marksixinfo.adapter.ViewHolder;
import com.marksixinfo.bean.RoadBeadDetailData;
import com.marksixinfo.bean.RoadBeadTitleData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.treerecycler.viewholder.TreeItem;
import com.marksixinfo.widgets.treerecycler.viewholder.TreeParentItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 开奖路珠第几球
 *
 * @Auther: Administrator
 * @Date: 2019/5/15 0015 13:31
 * @Description:
 */
public class RoadBeadTitleItem extends TreeParentItem<RoadBeadTitleData> {


    public RoadBeadTitleItem(RoadBeadTitleData data, TreeParentItem parentItem,
                             ActivityIntentInterface context) {
        super(data, parentItem, context);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.item_lottery_road_bead_group;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RoadBeadTitleData data = getData();
        if (data != null) {
            String numberTitle = data.getNumberTitle();
            holder.setText(R.id.tv_ball_number, CommonUtils.StringNotNull(numberTitle) ? numberTitle : "");
        }
    }

    @Override
    protected List<TreeItem> initChildsList(RoadBeadTitleData data) {
        ArrayList<TreeItem> contentItems = new ArrayList<>();
        if (data != null) {
            List<RoadBeadDetailData> details = data.getDetails();
            if (CommonUtils.ListNotNull(details)) {
                for (int i = 0; i < details.size(); i++) {
                    RoadBeadDetailData detail = details.get(i);
                    if (detail != null) {
                        contentItems.add(new RoadBeadDetailItem(detail
                                , this, activity, i, i == details.size() - 1));
                    }
                }
            }
        }
        return contentItems;
    }

}
