package com.hyaline.avoidbrowser.ui.activities.history;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hyaline.avoidbrowser.ui.fragments.collection.CollectionsFragment;
import com.hyaline.avoidbrowser.ui.fragments.history.HistoryFragment;

import java.util.ArrayList;
import java.util.List;

public class HistoryPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public HistoryPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new HistoryFragment());
        fragments.add(new CollectionsFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "浏览历史";
            case 1:
                return "收藏/书签";
        }
        return super.getPageTitle(position);
    }
}
