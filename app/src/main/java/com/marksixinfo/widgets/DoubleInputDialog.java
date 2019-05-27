package com.marksixinfo.widgets;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.DialogBase;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.utils.CommonUtils;

/**
 * @Auther: Administrator
 * @Date: 2019/3/18 0018 17:43
 * @Description:
 */
public class DoubleInputDialog extends DialogBase {

    private TextView textViewTitle;


    private EditText editTextInputContent;
    private EditText editTextInputContent2;
    private TextView textViewOk;

    private TextView textViewCancel;


    public DoubleInputDialog(ActivityIntentInterface context) {
        super(context);
    }

    public DoubleInputDialog(ActivityIntentInterface context, int theme) {
        super(context, theme);
    }

    protected DoubleInputDialog(ActivityIntentInterface context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void initView() {
        setView(R.layout.dialog_double_input);
        textViewTitle = findViewById(R.id.title);
        editTextInputContent = findViewById(R.id.edit_input_content);
        editTextInputContent2 = findViewById(R.id.edit_input_content2);
        textViewOk = findViewById(R.id.tv_ok);
        textViewCancel = findViewById(R.id.tv_cancel);
    }

    public String getInputContent() {
        return editTextInputContent.getText().toString();
    }

    public String getInputContent2() {
        return editTextInputContent2.getText().toString();
    }

//
//    public DoubleInputDialog showPassWordDialog(View.OnClickListener listener) {
//        show("修改密码", "请输入原密码(6字以上)", "请输入新密码(6字以上)", "取消", "确定", null, listener);
//        return this;
//    }

    public DoubleInputDialog show(String title, String inputHit, String inputHit2, String leftStr,
                                  String rightStr, View.OnClickListener left, View.OnClickListener right) {
        super.show();
        textViewTitle.setText(title);
        editTextInputContent.setHint(inputHit);
        editTextInputContent2.setHint(inputHit2);

        textViewTitle.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);

        textViewCancel.setVisibility(TextUtils.isEmpty(leftStr) ? View.GONE : View.VISIBLE);
        textViewOk.setVisibility(TextUtils.isEmpty(rightStr) ? View.GONE : View.VISIBLE);

        this.textViewCancel.setText(leftStr);
        this.textViewOk.setText(rightStr);


        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        this.textViewCancel.setOnClickListener(left != null ? left : new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.textViewOk.setOnClickListener(right != null ? right : new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return this;
    }

    public DoubleInputDialog setTextViewInput(String s) {
        if (editTextInputContent != null && CommonUtils.StringNotNull(s)) {
            editTextInputContent.setText(s);
            editTextInputContent.setSelection(s.length());
        }
        return this;
    }

    public DoubleInputDialog setTextViewInput2(String s) {
        if (editTextInputContent2 != null && CommonUtils.StringNotNull(s)) {
            editTextInputContent2.setText(s);
            editTextInputContent2.setSelection(s.length());
        }
        return this;
    }
}
