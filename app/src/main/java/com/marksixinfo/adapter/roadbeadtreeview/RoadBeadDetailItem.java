package com.marksixinfo.adapter.roadbeadtreeview;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.EditText;

import com.marksixinfo.R;
import com.marksixinfo.adapter.ViewHolder;
import com.marksixinfo.bean.RoadBeadDetailData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.widgets.treerecycler.viewholder.TreeItem;
import com.marksixinfo.widgets.treerecycler.viewholder.TreeParentItem;

/**
 * 开奖路珠单双
 *
 * @Auther: Administrator
 * @Date: 2019/5/15 0015 13:36
 * @Description:
 */
public class RoadBeadDetailItem extends TreeItem<RoadBeadDetailData> {

    public RoadBeadDetailItem(RoadBeadDetailData data, TreeParentItem parentItem,
                              ActivityIntentInterface context, int myPosition, boolean isLast) {
        super(data, parentItem, context);
        this.myPosition = myPosition;
        this.isLast = isLast;
    }

    private int myPosition;
    private boolean isLast;

    @Override
    protected int initLayoutId() {
        return R.layout.item_lottery_road_bead_child;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RoadBeadDetailData data = getData();
        if (data != null) {
            int color = data.getColor();
            String string = data.getString();

            View view = holder.getView(R.id.ll_content);
            GradientDrawable myGrad = (GradientDrawable) view.getBackground();
            myGrad.setColor(myPosition % 2 == 0 ? 0xffffffff : 0xfff4f5f7);

            EditText textView = holder.getView(R.id.text_view);

            textView.setLetterSpacing(1.65f);

            textView.setTextColor(color);

            textView.setText(string);

            holder.setVisible(R.id.view_line, myPosition == getParentItem().getChilds().size() - 1 && isLast);
        }
    }
}
