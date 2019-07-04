package com.example.promisealarmfinal.adapter;

import android.os.Bundle;

import com.example.promisealarmfinal.fragment.AlarmCommentFragment;
import com.example.promisealarmfinal.fragment.AlarmInfoFragment;
import com.example.promisealarmfinal.fragment.AlarmUserFragment;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private AlarmInfoFragment tabFragmentOne = new AlarmInfoFragment();
    private AlarmUserFragment tabFragmentTwo = new AlarmUserFragment();
    private AlarmCommentFragment tabFragmentThree = new AlarmCommentFragment();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private int alarmId;

    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount, int alarmId) {
        super(fm);
        this.tabCount = tabCount;
        this.alarmId = alarmId;

        fragments.add(tabFragmentOne);
        fragments.add(tabFragmentTwo);
        fragments.add(tabFragmentThree);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", alarmId);
        fragments.get(position).setArguments(bundle);

        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
