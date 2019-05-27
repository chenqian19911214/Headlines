package com.marksixinfo.widgets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.DialogBase;
import com.marksixinfo.bean.EditionNameDate;
import com.marksixinfo.interfaces.ActivityIntentInterface;

import java.util.List;

/**
 * 版本更新弹窗
 *
 * @Auther: Administrator
 * @Date: 2019/5/18 0018 19:36
 * @Description:
 */
public class EditionDialog extends DialogBase implements View.OnClickListener {

    private TextView tv_ok,tv_cancel,tv_version_name;
    private ActivityIntentInterface context;
    private TextView update_message;
    private String dowUrl;
    public EditionDialog(ActivityIntentInterface context, EditionNameDate editionNameDate) {
        super(context, R.style.SignInSuccessDialog);
        this.context = context;
        if (update_message!=null&&editionNameDate!=null){
          List<String> massageList = editionNameDate.getNews();
          StringBuffer stringBuffer = new StringBuffer();
            for (String str : massageList) {
                stringBuffer.append(str+"\n");
            }
            update_message .setText(stringBuffer.toString());
            if (editionNameDate.getState().equals("2")) {
                tv_cancel.setVisibility(View.GONE);
            } else if(editionNameDate.getState().equals("1")) {
                findViewById(R.id.tv_ok).setVisibility(View.VISIBLE);
            }
            String version = editionNameDate.getVersion();
          /*  StringBuffer stringBuffer1 = new StringBuffer(version);
            stringBuffer1.insert(version.length()-1,".");*/
            dowUrl = editionNameDate.getUrl();
            tv_version_name.setText(version);
        }
    }


    @SuppressLint("WrongViewCast")
    @Override
    public void initView() {
        setView(R.layout.dialog_edition_update);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        tv_ok = findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(this);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        update_message =  findViewById(R.id.update_message);
        tv_version_name =  findViewById(R.id.tv_version_name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                if (!TextUtils.isEmpty(dowUrl)) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(dowUrl);
                    intent.setData(content_url);
                    context.getActivity().startActivity(intent);
                }

                break;

            case R.id.tv_cancel:
                if (isShowing()) {
                    dismiss();
                }
                break;
        }

    }
}
