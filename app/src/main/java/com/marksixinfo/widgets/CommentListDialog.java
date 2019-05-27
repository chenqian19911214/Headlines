package com.marksixinfo.widgets;

import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.base.DialogBase;
import com.marksixinfo.interfaces.ActivityIntentInterface;

/**
 * @Auther: Administrator
 * @Date: 2019/4/18 0018 11:56
 * @Description:
 */
public class CommentListDialog extends DialogBase {


    public CommentListDialog(ActivityIntentInterface context) {
        super(context);
    }

    @Override
    public void initView() {
        View v = View.inflate(getContext(), R.layout.activity_comment_list, null);
        setContentView(v);
    }
}
