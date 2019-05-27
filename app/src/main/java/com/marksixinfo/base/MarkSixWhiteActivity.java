package com.marksixinfo.base;

import android.os.Bundle;

/**
 * 状态栏白色页面
 *
 * @Auther: Administrator
 * @Date: 2019/4/17 0017 13:13
 * @Description:
 */
public abstract class MarkSixWhiteActivity extends MarkSixActivity {


    /**
     * 白色title
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        isWhiteTitle = true;//白色标题
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置状态
     */
    @Override
    public void onResume() {
        super.onResume();
        setWhiteStatus();
    }
}
