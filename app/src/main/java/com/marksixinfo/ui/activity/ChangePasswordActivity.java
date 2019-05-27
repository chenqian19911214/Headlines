package com.marksixinfo.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.evenbus.TokenTimeOutEvent;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.ActivityManager;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.widgets.CleanEditTextView;
import com.marksixinfo.widgets.easyemoji.KeyBoardUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

import static com.marksixinfo.constants.NumberConstants.PASS_WORD_LENGTH;

/**
 * 功能描述: 修改密码
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/4/19 0019 16:10
 */
public class ChangePasswordActivity extends MarkSixWhiteActivity implements TextWatcher {

    @BindView(R.id.edit_old_password)
    CleanEditTextView editOldPassword;
    @BindView(R.id.edit_new_password)
    CleanEditTextView editNewPassword;
    @BindView(R.id.edit_affirm_password)
    CleanEditTextView editAffirmPassword;
    @BindView(R.id.tv_change)
    TextView tvChange;
    private String oldPassword;
    private String newPassword;
    private String affirmPassword;

    @Override
    public int getViewId() {
        return R.layout.activity_change_password;
    }

    @Override
    public void afterViews() {
        markSixTitleWhite.init("登录密码修改");

        editOldPassword.addTextChangedListener(this);
        editNewPassword.addTextChangedListener(this);
        editAffirmPassword.addTextChangedListener(this);

        checkInput();
        KeyBoardUtils.setEditTextState(editOldPassword);
    }

    @OnClick(R.id.tv_change)
    public void onViewClicked() {
        if (!newPassword.equals(affirmPassword)) {
            toast("新密码与确认密码不一致");
            return;
        }
        if (affirmPassword.equals(oldPassword)) {
            toast("新密码不能与旧密码相同");
            return;
        }
        setPassWord();
    }

    /**
     * 修改密码
     */
    private void setPassWord() {
        new HeadlineImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {
                ActivityManager.getActivityManager().popAllActivityExceptOne(MainActivity.class);
                EventBusUtil.post(new TokenTimeOutEvent(true, "修改成功,请重新登录"));
                finish();
            }
        }).changePassWord(oldPassword, newPassword);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * 检查输入
     */
    private void checkInput() {
        if (CommonUtils.StringNotNull(oldPassword, newPassword, affirmPassword)
                && oldPassword.length() >= PASS_WORD_LENGTH
                && newPassword.length() >= PASS_WORD_LENGTH
                && affirmPassword.length() >= PASS_WORD_LENGTH) {
            CommonUtils.setCommClickable(tvChange, true);
        } else {
            CommonUtils.setCommClickable(tvChange, false);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        oldPassword = editOldPassword.getText().toString().trim();
        newPassword = editNewPassword.getText().toString().trim();
        affirmPassword = editAffirmPassword.getText().toString().trim();

        checkInput();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
