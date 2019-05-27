package com.marksixinfo.ui.activity;

import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.bean.LoginData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.CountDownButtonHelper;
import com.marksixinfo.widgets.CleanEditTextView;
import com.marksixinfo.widgets.easyemoji.KeyBoardUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.marksixinfo.constants.NumberConstants.AUTH_CODE_LENGTH;
import static com.marksixinfo.constants.NumberConstants.PASS_WORD_LENGTH;
import static com.marksixinfo.constants.NumberConstants.PHONE_LENGTH;

/**
 * 功能描述: 找回密码
 *
 * @auther: Administrator
 * @date: 2019/4/16 0016 17:26
 */
public class ForgetPassWordActivity extends MarkSixWhiteActivity implements TextWatcher, TextView.OnEditorActionListener {

    @BindView(R.id.edit_phone)
    CleanEditTextView editPhone;
    @BindView(R.id.edit_auth_code)
    CleanEditTextView editAuthCode;
    @BindView(R.id.tv_send_auth_code)
    Button tvSendAuthCode;
    @BindView(R.id.edit_password)
    CleanEditTextView editPassword;
    @BindView(R.id.edit_affirm_password)
    CleanEditTextView editAffirmPassword;
    @BindView(R.id.tv_affirm)
    Button tvAffirm;

    private CountDownButtonHelper countDownButtonHelper;
    private String mPhone;
    private String mAuthCode;
    private String mPassword;
    private String mPasswordAgain;

    @Override
    public int getViewId() {
        return R.layout.activity_forget_pass_word;
    }

    @Override
    public void afterViews() {

        markSixTitleWhite.init("登录密码找回");

        mPhone = getStringParam(StringConstants.ACCOUNT);

        countDownButtonHelper = new CountDownButtonHelper(tvSendAuthCode, "短信验证码");


        editPhone.addTextChangedListener(this);
        editAuthCode.addTextChangedListener(this);
        editPassword.addTextChangedListener(this);
        editAffirmPassword.addTextChangedListener(this);

        countDownButtonHelper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
            @Override
            public void finish() {
                if (CommonUtils.StringNotNull(mPhone) && mPhone.length() == PHONE_LENGTH) {
                    setButtonEnabled(true);
                } else {
                    setButtonEnabled(false);
                }
            }
        });

        editPassword.setOnEditorActionListener(this);
        editPhone.requestFocus();
        if (CommonUtils.StringNotNull(mPhone)) {
            editPhone.setText(mPhone);
            editPhone.setSelection(mPhone.length());
        }
        KeyBoardUtils.setEditTextState(editPhone);
        setButtonEnabled(false);
        checkInput();
    }


    /**
     * 设置是否可以点击发送
     *
     * @param enable
     */
    private void setButtonEnabled(boolean enable) {
        tvSendAuthCode.setEnabled(enable);
        Resources resources = getResources();
        tvSendAuthCode.setTextColor(enable ?
                resources.getColor(R.color.colorPrimary) : resources.getColor(R.color.grey_bcb));
    }


    @OnClick({R.id.tv_send_auth_code, R.id.tv_affirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_send_auth_code://发送验证码
                sendAuthCode();
                break;
            case R.id.tv_affirm://确定
                retrieve();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mPhone = editPhone.getText().toString().trim();
        mAuthCode = editAuthCode.getText().toString().trim();
        mPassword = editPassword.getText().toString().trim();
        mPasswordAgain = editAffirmPassword.getText().toString().trim();

        checkInput();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * 检查输入
     */
    private void checkInput() {
        if (CommonUtils.StringNotNull(mPhone) && mPhone.length() == PHONE_LENGTH && !countDownButtonHelper.isStart()) {
            setButtonEnabled(true);
        } else {
            setButtonEnabled(false);
        }

        if (CommonUtils.StringNotNull(mPasswordAgain, mAuthCode, mPhone, mPassword)
                && mAuthCode.length() == AUTH_CODE_LENGTH
                && mPhone.length() == PHONE_LENGTH
                && mPassword.length() >= PASS_WORD_LENGTH
                && mPasswordAgain.length() >= PASS_WORD_LENGTH) {
            CommonUtils.setCommClickable(tvAffirm, true);
        } else {
            CommonUtils.setCommClickable(tvAffirm, false);
        }
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        retrieve();
        return true;
    }


    /**
     * 找回密码
     */
    private void retrieve() {
        if (tvAffirm != null && !tvAffirm.isEnabled()) {
            return;
        }
        if (!mPassword.equals(mPasswordAgain)) {
            toast("两次密码输入不一致");
            return;
        }
        new HeadlineImpl(new MarkSixNetCallBack<LoginData>(this, LoginData.class) {
            @Override
            public void onSuccess(LoginData response, int id) {
                if (response != null) {
                    toast("密码重置成功");
                    finish();
                }
            }
        }).forgetPassWord(mPhone, mAuthCode, mPassword);
    }


    /**
     * 发送验证码
     */
    private void sendAuthCode() {
        if (false/*CommonUtils.isMobileNO(mPhone)*/) {
            toast("请输入正确的手机号");
            return;
        }
        new HeadlineImpl(new MarkSixNetCallBack(this, null) {
            @Override
            public void onSuccess(Object response, int id) {
                toast("验证码发送成功");
                countDownButtonHelper.start();
                setButtonEnabled(false);
            }
        }).sendAuthCode(mPhone);
    }

}
