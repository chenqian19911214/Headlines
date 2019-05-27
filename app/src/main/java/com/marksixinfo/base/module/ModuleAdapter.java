package com.marksixinfo.base.module;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.marksixinfo.interfaces.ActivityIntentInterface;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Auther: Administrator
 * @Date: 2019/3/24 0024 14:28
 * @Description:
 */
public class ModuleAdapter<T extends ModuleBaseMode> extends RecyclerView.Adapter
        implements ActivityIntentInterface {
    private static final String TAG = "CMSAdapter";
    HashMap<ITimerModuleViewHolder, ITimerModuleMode> tiTimerCMSViewHolderHashMap = new HashMap<>();


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void showDialog(String msg, boolean canCancle, boolean isListLoading) {

    }

    @Override
    public void closeDialog() {

    }

    @Override
    public void toast(String msg, int imgId) {

    }

    @Override
    public void toastSuccess(String msg) {

    }

    @Override
    public void toastInfo(String msg) {

    }

    @Override
    public void toastWarning(String msg) {

    }

    @Override
    public void toastError(String msg) {

    }

    @Override
    public void showCommonDialog(String title, String msg, String leftStr, String rightStr, View.OnClickListener left, View.OnClickListener right) {

    }

    @Override
    public void closeCommonDialog() {

    }

    @Override
    public void onBefore() {

    }

    @Override
    public FragmentActivity getActivity() {
        return null;
    }

    @Override
    public void beforeOnCreate() {

    }

    @Override
    public void goToLoginActivity() {

    }

    @Override
    public void startClass(String activityName) {

    }

    @Override
    public void startClass(int activityName) {

    }

    @Override
    public void startClass(String activityName, HashMap params) {

    }

    @Override
    public void startClass(int activityName, HashMap params) {

    }

    @Override
    public void startClassWithFlag(String activityName, HashMap params, int... flags) {

    }

    @Override
    public void startClassForResult(String activityName, HashMap params, int requestCode) {

    }

    @Override
    public HashMap getUrlParam() {
        return null;
    }

    @Override
    public Bundle getBundleParams() {
        return null;
    }

    @Override
    public void setResult(int resultCode, Bundle data) {

    }

    @Override
    public String getThisHost() {
        return null;
    }

    @Override
    public String getThisHostUrl() {
        return null;
    }

    @Override
    public String getLastHost() {
        return null;
    }

    @Override
    public String getLastHostUrl() {
        return null;
    }

    @Override
    public void startClassForResult(String activityName, HashMap params, int requestCode, boolean checkLogin, Bundle bundle, int... flags) {

    }

    @Override
    public void startClass(String activityName, HashMap params, boolean checkLogin, Bundle bundle, int... flags) {

    }

    @Override
    public boolean checkLogin(String activityName, HashMap params) {
        return false;
    }

    @Override
    public boolean checkLogin() {
        return false;
    }

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public void gotoMainActivity(int index) {

    }

}
