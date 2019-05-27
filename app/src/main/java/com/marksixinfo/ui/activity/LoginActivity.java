package com.marksixinfo.ui.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.LoginSuccessEvent;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.widgets.CleanEditTextView;
import com.marksixinfo.widgets.WaveDrawable;
import com.marksixinfo.widgets.easyemoji.KeyBoardUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

import static com.marksixinfo.constants.NumberConstants.PASS_WORD_LENGTH;
import static com.marksixinfo.constants.NumberConstants.PHONE_LENGTH;

/**
 * 功能描述: 登录
 *
 * @auther: Administrator
 * @date: 2019/4/16 0016 12:10
 */
public class LoginActivity extends MarkSixWhiteActivity implements TextWatcher, TextView.OnEditorActionListener {

    @BindView(R.id.edit_phone)
    CleanEditTextView editPhone;
    @BindView(R.id.edit_password)
    CleanEditTextView editPassword;
    @BindView(R.id.iv_change_status)
    ImageView ivChangeStatus;
    @BindView(R.id.iv_logo_view)
    ImageView ivLogoView;
    @BindView(R.id.tv_login)
    Button tvLogin;

    private String mPhone = "";
    private String mPassword = "";
    private String callbackUrl = "";
    private boolean isChangeStatus = true;
    private WaveDrawable chromeWave;

    @Override
    public int getViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void afterViews() {

        markSixTitleWhite.init("用户登录");

        initParams();
        editPhone.addTextChangedListener(this);
        editPassword.addTextChangedListener(this);
        editPassword.setOnEditorActionListener(this);
        String userAccount = SPUtil.getUserAccount(getContext());
        if (CommonUtils.StringNotNull(userAccount)) {
            editPhone.setText(userAccount);
            editPhone.setSelection(userAccount.length());
        }

        editPassword.clearFocus();
        editPhone.requestFocus();
        KeyBoardUtils.setEditTextState(editPhone);
        checkInput();
//        startAnim();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initParams();
    }


    /**
     * 登录
     */
    private void goToLogin() {
        if (CommonUtils.StringNotNull(mPhone)) {
//            if (!CommonUtils.isMobileNO(phone)) {
//                toast("请输入正确手机号");
//                return;
//            }
        } else {
            toast("请输入手机号");
            return;
        }
        if (CommonUtils.StringNotNull(mPassword)) {
            if (mPassword.length() < NumberConstants.PASS_WORD_LENGTH) {
                toast("请输入正确的密码");
                return;
            }
            goToLogin(mPhone, mPassword);
        } else {
            toast("请输入密码");
        }
    }


    /**
     * 登录接口
     *
     * @param phone
     * @param password
     */
    private void goToLogin(String phone, String password) {
        if (tvLogin != null && !tvLogin.isEnabled()) {
            return;
        }
        new HeadlineImpl(new MarkSixNetCallBack<LoginData>(this, LoginData.class) {
            @Override
            public void onSuccess(LoginData response, int id) {
                if (response != null) {
                    response.setPhone(mPhone);
                    CommonUtils.setLoginSuccessData(getContext(), response);
                    toast("登录成功");
                    EventBusUtil.post(new LoginSuccessEvent());
                    CommonUtils.setCommClickable(tvLogin, false);
                    handleCallback();
                    MobclickAgent.onProfileSignIn(response.getId());
                }

            }
        }).login(phone, password);
    }


    @OnClick({R.id.iv_change_status, R.id.tv_register, R.id.tv_forget_password, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_change_status:
                isChangeStatus = !isChangeStatus;
                if (isChangeStatus) {
                    ivChangeStatus.setImageResource(R.drawable.icon_password_visible);
                    //从密码可见模式变为密码不可见模式
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    ivChangeStatus.setImageResource(R.drawable.icon_password_gone);
                    //从密码不可见模式变为密码可见模式
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                editPassword.setSelection(mPassword.length());
                break;
            case R.id.tv_register:
                startClass(R.string.RegisterActivity);
                break;
            case R.id.tv_forget_password:
                startClass(R.string.ForgetPassWordActivity, IntentUtils.getHashObj(new String[]{
                        StringConstants.ACCOUNT, mPhone}));
                break;
            case R.id.tv_login:
                goToLogin();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mPhone = editPhone.getText().toString().trim();
        mPassword = editPassword.getText().toString().trim();

        checkInput();
    }

    /**
     * 检查输入
     */
    private void checkInput() {
        if (CommonUtils.StringNotNull(mPhone, mPassword)
                && mPhone.length() == PHONE_LENGTH
                && mPassword.length() >= PASS_WORD_LENGTH) {
            CommonUtils.setCommClickable(tvLogin, true);
        } else {
            CommonUtils.setCommClickable(tvLogin, false);
        }
        ivChangeStatus.setVisibility(CommonUtils.StringNotNull(mPassword) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * 处理被拦截的页面跳转
     */
    private void handleCallback() {
        if (CommonUtils.StringNotNull(callbackUrl)) {
            startClass(callbackUrl, getUrlParam());
        }
        super.finish();
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_GO == actionId) {
            goToLogin();
        }
        return true;
    }

    private void initParams() {
        HashMap<String, String> hashMap = getUrlParam();
        if (hashMap == null) {
            return;
        }
        if (hashMap.containsKey(StringConstants.CALLBACK)) {
            callbackUrl = hashMap.get(StringConstants.CALLBACK);
        } else {
            callbackUrl = getIntent().getStringExtra(StringConstants.CALLBACK);
        }
    }

    /**
     * logo波浪效果
     */
    private void startAnim() {
        chromeWave = new WaveDrawable(this, R.drawable.icon_login_logo);
        ivLogoView.setImageDrawable(chromeWave);
        chromeWave.setWaveSpeed(4);
        chromeWave.setWaveLength(300);
        chromeWave.setWaveAmplitude(25);

        ValueAnimator animator = ValueAnimator.ofInt(5000, 10000);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(7000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                chromeWave.setLevel(animatedValue);
            }
        });
        animator.start();
    }

}
