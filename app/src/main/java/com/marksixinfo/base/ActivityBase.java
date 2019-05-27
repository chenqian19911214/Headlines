package com.marksixinfo.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.NavigationBarUtil;
import com.marksixinfo.utils.SPUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import butterknife.ButterKnife;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 20:42
 * @Description:
 */
public abstract class ActivityBase extends FragmentActivity {

    final String TAG = "ActivityBase";
    protected Bundle bundle = new Bundle();

    public ActivityBase() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        if (savedInstanceState != null && savedInstanceState.containsKey("bundle") &&
                savedInstanceState.getBundle("bundle") != null) {
            this.bundle = savedInstanceState.getBundle("bundle");
        }
        if(NavigationBarUtil.hasNavigationBar(this)){
            NavigationBarUtil.initActivity(this);
        }
        this.setContentView(this.getViewId());
        this.superViews();
        ButterKnife.bind(this);
        if (this.isRenderingView()) {
            this.afterViews();
        }

    }

    public abstract int getViewId();

    public abstract void afterViews();

    public void superViews() {
    }

    public void replace(int id, Fragment fragment) {
        if (!this.isFinishing()) {
            this.getSupportFragmentManager().beginTransaction().replace(id, fragment).commitAllowingStateLoss();
        }
    }

    public void removeFragment(Fragment fragment) {
        if (!this.isFinishing()) {
            if (fragment != null) {
                this.getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
            }
        }
    }

    public void onResume() {
        super.onResume();
        NavigationBarUtil.usableHeightView = 0;
        findViewById(android.R.id.content).requestLayout();
    }

    public Bundle getSaveInstance() {
        return this.bundle;
    }

    public Object getSaveObject(String key, Object def) {
        if (this.bundle == null) {
            return def;
        } else {
            return !this.bundle.containsKey(key) ? def : this.bundle.get(key);
        }
    }

    public String getSaveString(String key, String def) {
        return (String) this.getSaveObject(key, def);
    }

    public String getSaveString(String key) {
        return (String) this.getSaveObject(key, (Object) null);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("bundle", this.bundle);
    }

    public void onPause() {
        super.onPause();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            View view = this.getCurrentFocus();
            if (this.isHideInput(view, ev)) {
                this.HideSoftInput(view.getWindowToken());
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && v instanceof EditText) {
            int[] l = new int[]{0, 0};
            v.getLocationInWindow(l);
            int left = l[0];
            int top = l[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return ev.getX() <= (float) left || ev.getX() >= (float) right || ev.getY() <= (float) top || ev.getY() >= (float) bottom;
        } else {
            return false;
        }
    }

    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, 2);
        }

    }

    protected boolean isRenderingView() {
        return true;
    }

    public String getToken() {
        return SPUtil.getToken(getApplicationContext());
    }


    public boolean isLogin() {
        return CommonUtils.StringNotNull(getToken());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStringEvent(String event) {
    }
}

