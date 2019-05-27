package com.marksixinfo.widgets.backedittext;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.utils.CommonUtils;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

/**
 * @Auther: Administrator
 * @Date: 2019/3/28 0028 17:05
 * @Description:
 */
public class BackInputDialog extends Dialog implements TextWatcher {

    private BackEditText editText;
    private TextView send;
    private String s;
    private int commentPosition;
    private String id;
    public String hint = "优质评论将会被优先展示!";

    public BackInputDialog(@NonNull Context context, InputTextListener listener) {
        this(context, R.style.CustomDialog); //设置Style
        this.listener = listener;
    }


    protected BackInputDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(final Context context) {
        setCancelable(true);//是否可以取消 (也可以在调用处设置)
        setCanceledOnTouchOutside(true);//是否点击外部消失

        View view = View.inflate(context, R.layout.view_comment_edit, null);

        initView(view);
        initListener();

        setContentView(view);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window dialog_window = this.getWindow();
        dialog_window.setGravity(Gravity.BOTTOM);//设置显示的位置
        dialog_window.setAttributes(params);//设置显示的大小


    }

    private void initListener() {

        editText.addTextChangedListener(this);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);//使得点击 Dialog 中的EditText 可以弹出键盘
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);//总是显示键盘
                }
            }
        });
        //自定义的 EditText 监听 Back 键的点击( 在软键盘显示的情况下 )
        editText.setCallBack(new BackEditText.PressBackCallBack() {
            @Override
            public void callBack() {
                dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtils.StringNotNull(s)) {
                    return;
                }
                dismiss();
                if (listener != null) {
                    listener.onInputText(s);
                }
                editText.setText("");
            }
        });
    }

    private void initView(View view) {
        editText = view.findViewById(R.id.edit_comment);
        if (CommonUtils.StringNotNull(hint)) {
            editText.setHint(hint);
        }
        send = view.findViewById(R.id.tv_sub_comment);
    }

    public void show(String hint) {
        super.show();
        if (editText != null && CommonUtils.StringNotNull(hint)) {
            editText.setHint(hint);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        s = editText.getText().toString().trim();
        send.setSelected(CommonUtils.StringNotNull(s));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private InputTextListener listener;

    public interface InputTextListener {
        void onInputText(String text);
    }

    public InputTextListener getListener() {
        return listener;
    }

    public int getCommentPosition() {
        return commentPosition;
    }

    public BackInputDialog setCommentPosition(int commentPosition) {
        this.commentPosition = commentPosition;
        return this;
    }

    public String getId() {
        return id;
    }

    public BackInputDialog setId(String id) {
        this.id = id;
        return this;
    }

    public EditText getEditText() {
        return editText;
    }
}
