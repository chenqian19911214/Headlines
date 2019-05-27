package com.marksixinfo.ui.activity;

import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.PullGalleryOkEvent;
import com.marksixinfo.net.impl.GalleryImpl;
import com.marksixinfo.utils.EmojiFilter;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.widgets.CleanEditTextView;

import butterknife.BindView;

/**
 * 发布图解
 *
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 12:31
 * @Description:
 */
public class GalleryExplainReleaseActivity extends MarkSixActivity implements View.OnClickListener {

    @BindView(R.id.cetv_editTextView)
    CleanEditTextView refreshLayout;
    @BindView(R.id.cetv_editTextView_comment)
    EditText cetv_editTextView;
    TextView function;
    private String titleText,commentText,id,period;

    @Override
    public int getViewId() {
        return R.layout.activity_gallery_explain_release;
    }
    @Override
    public void afterViews() {
        period = getStringParam(StringConstants.PERIOD);
        id = getStringParam(StringConstants.ID);
        titleText = getStringParam("StringName");
        markSixTitle.init("发布图解","","发布",0,this);
        function =  markSixTitle.getTvFunction();
        refreshLayout.setText(titleText);
        noSend();
        setNickNameMaxLength(cetv_editTextView);
        setNickNameMaxLength(refreshLayout);
        cetv_editTextView.addTextChangedListener(new TextWatcher() { //内容
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                commentText = charSequence.toString();
                if (charSequence.length()>0){
                    if (TextUtils.isEmpty(titleText)){
                        noSend();
                    }else {
                        okSend();
                    }
                }else {
                    noSend();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        refreshLayout.addTextChangedListener(new TextWatcher() { //标题
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                titleText = charSequence.toString();
                if (charSequence.length()>0){
                    if (TextUtils.isEmpty(commentText)){
                        noSend();
                    }else {
                        okSend();
                    }
                }else {
                    noSend();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void okSend() {
        function.setEnabled(true);
        function.setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void noSend() {
        function.setEnabled(false);
        function.setTextColor(Color.parseColor("#FA8080"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_function://
                if (!TextUtils.isEmpty(titleText)&&!TextUtils.isEmpty(commentText)&&!TextUtils.isEmpty(id)&&!TextUtils.isEmpty(period)){
                    getSelectPicture(titleText,commentText,period,id);
                }else {
                    toast("网络异常");
                }
                break;
        }
    }

    /**
     * 限制表情输入
     */
    private void setNickNameMaxLength(EditText text) {
        if (text != null) {
            if (text != null) {
                text.setFilters(new InputFilter[]{ new EmojiFilter()});
            }
        }
    }

    private void getSelectPicture(String titleText,String commentText,String period,String id){

        new GalleryImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {
                //toast("发布成功");
                EventBusUtil.post(new PullGalleryOkEvent());
                finish();
            }

        }).postPullAnswer(titleText,commentText,id,period);
    }
}
