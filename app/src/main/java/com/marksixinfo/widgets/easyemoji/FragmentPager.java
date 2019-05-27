package com.marksixinfo.widgets.easyemoji;


import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by MuMu on 2016/11/2/0002.
 */

public class FragmentPager extends FragmentStatePagerAdapter {
    private final List<EmoJiFragment> fragments;

    public FragmentPager(FragmentManager fm, List<EmoJiFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
