package com.marksixinfo.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.DialogBase;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.utils.CommonUtils;

/**
 * @Auther: Administrator
 * @Date: 2019/3/18 0018 17:23
 * @Description:
 */
public class CommonEditDialog extends DialogBase {

    EditText edit;
    TextView ok;

    public CommonEditDialog(ActivityIntentInterface context) {
        super(context);
    }

    public CommonEditDialog(ActivityIntentInterface context, int theme) {
        super(context, theme);
    }

    protected CommonEditDialog(ActivityIntentInterface context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void initView() {
        setView(R.layout.dialog_common_edit);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        edit = findViewById(R.id.edit);
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditEndListener != null) {
                    onEditEndListener.editEnd(getNum());
                }
                if (onEditEndListener2 != null) {
                    onEditEndListener2.editEnd(edit.getText().toString());
                }
                dismiss();
            }
        });
    }

//    protected long getNum() {
//        if (edit != null) {
//            try {
//                return Long.parseLong(String.valueOf(edit.getText()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//        return 0;
//    }

    protected String getNum() {
        return edit.getText().toString();
    }

    @Deprecated
    public void show() {
        super.show();
    }

    public void show(long num) {
        show();
        if (edit != null) {
            edit.setText(String.valueOf(num));
            edit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (edit != null) {

                        edit.requestFocus();
                        InputMethodManager inputManager =
                                (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(edit, 0);
                        try {
                            edit.setSelection(edit.length() > 0 ? edit.length() : 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }, 500);

        }

    }

    public CommonEditDialog show(String s) {
        show();
        if (edit != null) {
            edit.setText(s);
            edit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (edit != null) {

                        edit.requestFocus();
                        InputMethodManager inputManager =
                                (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(edit, 0);
                        try {
                            edit.setSelection(edit.length() > 0 ? edit.length() : 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }, 500);

        }

        return this;
    }

    @Override
    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int getWindowManagerWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    public void setOnEditEndListener(OnEditEndListener onEditEndListener) {
        this.onEditEndListener = onEditEndListener;
    }

    OnEditEndListener onEditEndListener;

    public interface OnEditEndListener {
        void editEnd(String s);
    }

    public void setOnEditEndListener2(OnEditEndListener2 onEditEndListener2) {
        this.onEditEndListener2 = onEditEndListener2;
    }

    OnEditEndListener2 onEditEndListener2;

    public interface OnEditEndListener2 {
        void editEnd(String s);
    }

    public EditText getEdit() {
        return edit;
    }

    public CommonEditDialog setHint(String hint) {
        getEdit().setHint(CommonUtils.StringNotNull(hint) ? hint : "");
        return this;
    }

    public void setMaxInputLength(int max) {
        edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
    }
}

