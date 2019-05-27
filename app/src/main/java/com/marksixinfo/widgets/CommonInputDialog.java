package com.marksixinfo.widgets;

import android.content.DialogInterface;
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
import com.marksixinfo.widgets.easyemoji.KeyBoardUtils;

/**
 * @Auther: Administrator
 * @Date: 2019/3/18 0018 17:43
 * @Description:
 */
public class CommonInputDialog extends DialogBase {

    private TextView textViewTitle;

//    private TextView textViewInputTitle;

    private EditText editTextInputContent;
    private TextView textViewOk;

    private TextView textViewCancel;

    private TextView textViewUnit;

    public CommonInputDialog(ActivityIntentInterface context) {
        super(context);
    }

    public CommonInputDialog(ActivityIntentInterface context, int theme) {
        super(context, theme);
    }

    protected CommonInputDialog(ActivityIntentInterface context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void initView() {
        setView(R.layout.dialog_common_input);
        textViewTitle = findViewById(R.id.title);
//        textViewInputTitle = findViewById(R.id.input_title);
        editTextInputContent = findViewById(R.id.edit_input_content);
        textViewOk = findViewById(R.id.tv_ok);
        textViewCancel = findViewById(R.id.tv_cancel);
        textViewUnit = findViewById(R.id.text_unit);
    }

    public String getInputContent() {
        return editTextInputContent.getText().toString();
    }

    public CommonInputDialog show(String title/*, String inputTitle*/, String inputHit, String leftStr, String rightStr, View.OnClickListener left, View.OnClickListener right) {
        super.show();
        textViewTitle.setText(title);
//        textViewInputTitle.setText(inputTitle);
        editTextInputContent.setHint(inputHit);

        textViewTitle.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
//        textViewInputTitle.setVisibility(TextUtils.isEmpty(inputTitle) ? View.GONE : View.VISIBLE);

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
        KeyBoardUtils.setEditTextState(editTextInputContent);
        return this;
    }

    public CommonInputDialog show(String title, String inputTitle, String inputHit, String leftStr, String rightStr, String unitText, View.OnClickListener left, View.OnClickListener right) {
        super.show();
        textViewTitle.setText(title);
//        textViewInputTitle.setText(inputTitle);
        editTextInputContent.setHint(inputHit);

        textViewCancel.setVisibility(TextUtils.isEmpty(leftStr) ? View.GONE : View.VISIBLE);
        textViewOk.setVisibility(TextUtils.isEmpty(rightStr) ? View.GONE : View.VISIBLE);

        this.textViewCancel.setText(leftStr);
        this.textViewOk.setText(rightStr);

        if (!TextUtils.isEmpty(unitText)) {
            textViewUnit.setVisibility(View.VISIBLE);
            textViewUnit.setText(unitText);
        } else {
            textViewUnit.setVisibility(View.GONE);
        }


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

    public void setTextViewInput(String s) {
        if (editTextInputContent != null) {
            editTextInputContent.setText(s);
            if (CommonUtils.StringNotNull(s)) {
                editTextInputContent.setSelection(s.length());
            }
        }
    }

    public EditText getEditText(){
        return editTextInputContent;
    }
}
