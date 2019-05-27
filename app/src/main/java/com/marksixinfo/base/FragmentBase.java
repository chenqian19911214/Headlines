package com.marksixinfo.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 21:03
 * @Description:
 */
public abstract class FragmentBase extends Fragment {


    /**
     * layout ID
     *
     * @return
     */
    @LayoutRes
    public abstract int getViewId();

    protected Bundle bundle = new Bundle();
    /**
     * 是否是第一次加载
     */
    protected boolean isIntialized = false;

    public boolean isIntialized() {
        return isIntialized;
    }

    /**
     * 设置为true 则不会销毁view主要用于viewpager重用
     */
    protected boolean isCache = false;

    public void setCache(boolean isCache) {
        this.isCache = isCache;
    }

    protected View view;

    /**
     * 对Activity是否还存在的判断
     *
     * @return
     */
    protected boolean isUsable() {
        return getActivity() != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey("bundle") && savedInstanceState.getBundle("bundle") != null) {
            bundle = savedInstanceState.getBundle("bundle");
        }


        if (view != null && isCache) {
            return view;
        }
        this.view = this.onSuperCreateView(inflater, container, savedInstanceState);
        if (view == null) return null;
        superViews();
        ButterKnife.bind(this, view);
        ViewTreeObserver vx = view.getViewTreeObserver();
        vx.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!isIntialized) {
                    if (onLoadListener != null) {
                        onLoadListener.onSuperViews();
                    }
                    afterViews();
                    if (onLoadListener != null) {
                        onLoadListener.onAfterViews();
                    }
                    isIntialized = true;
                }
                return true;
            }
        });
        return view;
    }

    protected View onSuperCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = getViewId();
        if (layoutId > 0) {
            return inflater.inflate(getViewId(), container, false);
        } else {
            return null;
        }
    }

    public <T extends View> T findViewById(int id) {
        return view.findViewById(id);
    }

    /**
     * 页面控件加载完毕都不为空是调用，可以在这里写逻辑
     */
    protected abstract void afterViews();

    public void hide() {
        hide(this);
    }

    public void hide(Fragment fragment) {

        getFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
    }

    public void show(Fragment fragment) {

        getFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
    }

    public void show() {
        show(this);
    }

    public Fragment getByTag(String tag) {
        return getFragmentManager().findFragmentByTag(tag);
    }

    public onLoadListener onLoadListener;

    public void setOnLoadListener(onLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public interface onLoadListener {
        void onSuperViews();

        void onAfterViews();
    }

    public Bundle getSaveInstance() {
        return this.bundle;
    }

    public Object getSaveObject(String key, Object def) {
        if (this.bundle == null) {
            return def;
        }
        if (!this.bundle.containsKey(key))
            return def;
        return this.bundle.get(key);
    }

    public String getSaveString(String key, String def) {
        return (String) getSaveObject(key, def);
    }

    public String getSaveString(String key) {
        return (String) getSaveObject(key, null);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle("bundle", bundle);

        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        if (isCache) {
            if (view != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
    }

    /**
     * back 拦截 true表示不拦截
     *
     * @return
     */
    public boolean onBackPressed() {
        return true;
    }

    /**
     * 页面控件加载完毕前调用，可以在增加View
     */
    public void superViews() {

    }

    public Intent getIntent() {
        if (getActivity() != null) {
            return getActivity().getIntent();
        }
        return null;
    }

    public void replace(int id, Fragment fragment) {
        if (getChildFragmentManager() != null) {
            getChildFragmentManager().beginTransaction().replace(id, fragment).commitAllowingStateLoss();
        }
    }

    public void removeFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        if (getChildFragmentManager() != null) {
            getChildFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStringEvent(String event) {
    }
}
