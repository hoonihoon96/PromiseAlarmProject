package com.example.promisealarmfinal.activity;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;

import com.example.promisealarmfinal.R;
import com.example.promisealarmfinal.activity.BaseActivity;
import com.example.promisealarmfinal.adapter.TabPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class AlarmViewActivity extends BaseActivity {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_view);
        ButterKnife.bind(this);

        tabLayout.addTab(tabLayout.newTab().setText("알람 정보"));
        tabLayout.addTab(tabLayout.newTab().setText("알람 인원"));
        tabLayout.addTab(tabLayout.newTab().setText("알람 코멘트"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), getIntent().getIntExtra("id", 0));
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                log(tab.getPosition() + "");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
