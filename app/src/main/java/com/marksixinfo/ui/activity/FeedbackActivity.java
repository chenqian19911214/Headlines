package com.marksixinfo.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.marksixinfo.R;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.easyemoji.KeyBoardUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述: 用户反馈
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/4/19 0019 16:09
 */
public class FeedbackActivity extends MarkSixWhiteActivity implements TextWatcher {


    @BindView(R.id.edit_content)
    EditText editContent;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.tv_confirm)
    Button tvConfirm;
    private String content;

    @Override
    public int getViewId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void afterViews() {
        markSixTitleWhite.init("用户反馈");
        loading.showContent();
        editContent.addTextChangedListener(this);
        KeyBoardUtils.setEditTextState(editContent);

        checkInput();
    }


    @OnClick(R.id.tv_confirm)
    public void onViewClicked() {
        feedback();
    }

    /**
     * 反馈
     */
    private void feedback() {
        new HeadlineImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {
                editContent.setText("");
                toast("感谢您的反馈");
                editContent.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 800);
//                new CommonDialog(FeedbackActivity.this).show("", "感谢您的反馈,我们会做的更好,感谢您的支持"
//                        , "", "确定", null, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        });
            }
        }).feedbackContent(content);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        content = editContent.getText().toString().trim();
        checkInput();
    }

    /**
     * 检查输入
     */
    private void checkInput() {
        CommonUtils.setCommClickable(tvConfirm, CommonUtils.StringNotNull(content));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
