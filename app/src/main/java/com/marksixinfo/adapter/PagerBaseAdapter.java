package com.marksixinfo.adapter;

import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.utils.CommonUtils;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 *  viewPage配合使用
 *
 * @auther: Administrator
 * @date: 2019/3/29 0029 17:51
 */
public class PagerBaseAdapter extends FragmentStatePagerAdapter {

    private List<PageBaseFragment> fragments;

    public PagerBaseAdapter(FragmentManager fm, List<PageBaseFragment> fragmentList) {
        super(fm);
        fragments = fragmentList;
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return CommonUtils.ListNotNull(fragments) ? fragments.get(position) : null;
    }
}
