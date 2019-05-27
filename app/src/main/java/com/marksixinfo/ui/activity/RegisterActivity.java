package com.marksixinfo.ui.activity;

import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.bean.LoginData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.constants.UrlStaticConstants;
import com.marksixinfo.evenbus.LoginSuccessEvent;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.ActivityManager;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.CountDownButtonHelper;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.widgets.CleanEditTextView;
import com.marksixinfo.widgets.easyemoji.KeyBoardUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.marksixinfo.constants.NumberConstants.AUTH_CODE_LENGTH;
import static com.marksixinfo.constants.NumberConstants.PASS_WORD_LENGTH;
import static com.marksixinfo.constants.NumberConstants.PHONE_LENGTH;

/**
 * 功能描述: 注册
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/3/31 0031 18:14
 */
public class RegisterActivity extends MarkSixWhiteActivity implements TextWatcher, TextView.OnEditorActionListener {

    @BindView(R.id.edit_nickname)
    CleanEditTextView editNickname;
    @BindView(R.id.edit_phone)
    CleanEditTextView editPhone;
    @BindView(R.id.edit_auth_code)
    CleanEditTextView editAuthCode;
    @BindView(R.id.tv_send_auth_code)
    Button tvSendAuthCode;
    @BindView(R.id.edit_password)
    CleanEditTextView editPassword;
    @BindView(R.id.edit_invite_code)
    CleanEditTextView editInviteCode;
    @BindView(R.id.tv_register)
    Button tvRegister;
    @BindView(R.id.iv_check_protest)
    ImageView ivCheckProtest;
    private CountDownButtonHelper countDownButtonHelper;
    private String mNickname;
    private String mPhone;
    private String mAuthCode;
    private String mPassword;
    private String mInviteCode;

    @Override
    public int getViewId() {
        return R.layout.activity_register;
    }

    @Override
    public void afterViews() {

        markSixTitleWhite.init("注册");

        countDownButtonHelper = new CountDownButtonHelper(tvSendAuthCode, "短信验证码");

        editNickname.addTextChangedListener(this);
        editPhone.addTextChangedListener(this);
        editAuthCode.addTextChangedListener(this);
        editPassword.addTextChangedListener(this);
        editInviteCode.addTextChangedListener(this);

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
        editNickname.requestFocus();
        KeyBoardUtils.setEditTextState(editNickname);
        setButtonEnabled(false);
        ivCheckProtest.setSelected(true);
        checkInput();
    }

    @OnClick({R.id.tv_send_auth_code, R.id.tv_register, R.id.iv_check_protest, R.id.tv_protest_webView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_send_auth_code://发送验证码
                sendAuthCode();
                break;
            case R.id.tv_register: //注册
                register();
                break;
            case R.id.iv_check_protest://免责声明勾选
                ivCheckProtest.setSelected(!ivCheckProtest.isSelected());
                checkInput();
                break;
            case R.id.tv_protest_webView://免责声明查看
                startClass(R.string.WebViewActivity, IntentUtils.getHashObj(new String[]{
                        StringConstants.URL, UrlStaticConstants.REGISTER_DISCLAIMER}));
                break;

        }
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

    /**
     * 设置是否可以点击发送
     *
     * @param enable
     */
    private void setButtonEnabled(boolean enable) {
        tvSendAuthCode.setEnabled(enable);
        Resources resources = getResources();
        tvSendAuthCode.setTextColor(enable ?
                resources.getColor(R.color.colorPrimary) :  resources.getColor(R.color.grey_bcb));
    }


    /**
     * 注册
     */
    private void register() {
        if (tvRegister != null && !tvRegister.isEnabled()) {
            return;
        }
        new HeadlineImpl(new MarkSixNetCallBack<LoginData>(this, LoginData.class) {
            @Override
            public void onSuccess(LoginData response, int id) {
                if (response != null) {
                    response.setPhone(mPhone);
                    CommonUtils.setLoginSuccessData(getContext(), response);
                    toast("注册成功");
                    EventBusUtil.post(new LoginSuccessEvent());
                    ActivityManager.getActivityManager().popAllActivityExceptOne(MainActivity.class);
                    finish();
                }
            }
        }).register(mNickname, mPhone, mAuthCode, mPassword, mInviteCode);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }


    /***
     * 输入变化
     * @param charSequence
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mNickname = editNickname.getText().toString().trim();
        mPhone = editPhone.getText().toString().trim();
        mAuthCode = editAuthCode.getText().toString().trim();
        mPassword = editPassword.getText().toString().trim();
        mInviteCode = editInviteCode.getText().toString().trim();

        checkInput();
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

        if (CommonUtils.StringNotNull(mNickname, mAuthCode, mPhone, mPassword)
                && mAuthCode.length() == AUTH_CODE_LENGTH
                && mPhone.length() == PHONE_LENGTH
                && mPassword.length() >= PASS_WORD_LENGTH && ivCheckProtest.isSelected()) {
            CommonUtils.setCommClickable(tvRegister, true);
        } else {
            CommonUtils.setCommClickable(tvRegister, false);
        }
    }


    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownButtonHelper != null) {
            countDownButtonHelper.stopCountDownTimer();
        }
    }

    /**
     * 键盘动作
     *
     * @param textView
     * @param actionId
     * @param keyEvent
     * @return
     */
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_GO == actionId) {
            register();
        }
        return true;
    }
}
