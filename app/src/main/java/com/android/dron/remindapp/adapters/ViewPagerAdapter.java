package com.android.dron.remindapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.dron.remindapp.fragments.FragmentEvent;
import com.android.dron.remindapp.fragments.FragmentTask;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String[] nameTabs;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        nameTabs = new String[]{"Заметки", "События"};
    }

    @Override
    public Fragment getItem(int position) { //возвращает содержимое фрагментов выбраного таба
        switch (position) {
            case 0:
                return FragmentTask.getInstance();
            case 1:
                return new FragmentEvent();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nameTabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return nameTabs[position];
    }
}
