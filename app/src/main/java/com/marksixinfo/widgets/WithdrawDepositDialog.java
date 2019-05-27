package com.marksixinfo.widgets;

import android.content.Intent;
import android.net.Uri;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.DialogBase;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EmojiFilter;
import com.marksixinfo.utils.TextChangedListener;
import com.marksixinfo.widgets.easyemoji.KeyBoardUtils;

/**
 * 提现兑换
 *
 * @Auther: Administrator
 * @Date: 2019/3/18 0018 17:43
 * @Description:
 */
public class WithdrawDepositDialog extends DialogBase {


    private TextView tvPlatformName;
    private EditText editInputNumber;
    private EditText editInputAccount;
    private TextView tvCancel;
    private TextView tvOk;
    private LinearLayout mLlRealName;
    private EditText editInputName;
    private View viewLine;
    private View tvPlatformClick;


    public WithdrawDepositDialog(ActivityIntentInterface context) {
        super(context);
    }

    public WithdrawDepositDialog(ActivityIntentInterface context, int theme) {
        super(context, theme);
    }

    protected WithdrawDepositDialog(ActivityIntentInterface context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    public void initView() {
        setView(R.layout.dialog_withdraw_deposit_input);
        tvPlatformName = findViewById(R.id.tv_platform_name);
        editInputNumber = findViewById(R.id.edit_input_number);
        editInputAccount = findViewById(R.id.edit_input_account);
        mLlRealName = findViewById(R.id.ll_real_name);
        editInputName = findViewById(R.id.edit_input_name);
        viewLine = findViewById(R.id.view_line);
        tvPlatformClick = findViewById(R.id.tv_platform_click);
        tvPlatformClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUrl();
            }
        });
        //不能输入中文
        TextChangedListener.StringWatcher(editInputAccount);
        editInputAccount.setFilters(new InputFilter[]{new EmojiFilter()});
        tvCancel = findViewById(R.id.tv_cancel);
        tvOk = findViewById(R.id.tv_ok);


    }

    public String getEditInputName() {
        return editInputName.getText().toString().trim();
    }

    public String getInputNumber() {
        return editInputNumber.getText().toString().trim();
    }

    public String getInputAccount() {
        return editInputAccount.getText().toString().trim();
    }

    public EditText getEditInputNumber() {
        return editInputNumber;
    }

    public void setInputNumber(String text) {
        if (editInputNumber != null && CommonUtils.StringNotNull(text)) {
            editInputNumber.setText(text);
            editInputNumber.requestFocus();
            editInputNumber.setSelection(text.length());
        }
    }


    public WithdrawDepositDialog show(int type, String name, String leftStr, String rightStr, View.OnClickListener left, View.OnClickListener right) {
        super.show();

        if (type != 0) {//0,网站平台  1,银行账户   2,支付宝
            mLlRealName.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.VISIBLE);
        } else {
            mLlRealName.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        }

        if (CommonUtils.StringNotNull(name) && name.contains("优7")) {
            tvPlatformClick.setVisibility(View.VISIBLE);
        } else {
            tvPlatformClick.setVisibility(View.GONE);
        }

        tvPlatformName.setVisibility(TextUtils.isEmpty(name) ? View.GONE : View.VISIBLE);
        tvPlatformName.setText(TextUtils.isEmpty(name) ? "" : name);

        tvCancel.setVisibility(TextUtils.isEmpty(leftStr) ? View.GONE : View.VISIBLE);
        tvOk.setVisibility(TextUtils.isEmpty(rightStr) ? View.GONE : View.VISIBLE);

        this.tvCancel.setText(leftStr);
        this.tvOk.setText(rightStr);

        editInputNumber.setText("");
        editInputAccount.setText("");
        editInputName.setText("");


        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        this.tvCancel.setOnClickListener(left != null ? left : new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.tvOk.setOnClickListener(right != null ? right : new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (mLlRealName.getVisibility() == View.VISIBLE) {
            KeyBoardUtils.setEditTextState(editInputName);
        } else {
            KeyBoardUtils.setEditTextState(editInputNumber);
        }
        return this;
    }

    /**
     * 跳转优7
     */
    private void startUrl() {
        Uri uri = Uri.parse("https://www.u7996.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        getCtrl().getActivity().startActivity(intent);
        dismiss();
    }
}
