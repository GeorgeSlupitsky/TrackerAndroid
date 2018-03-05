package com.micro_gis.microgistracker.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.adapters.InfoObjectAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User9 on 02.03.2018.
 */

public class ContentObjectFragment extends Fragment {

    private InfoObjectFragment infoObjectFragment;
    private SensorsObjectFragment sensorsObjectFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_content_object, container, false);

        infoObjectFragment = new InfoObjectFragment();
        sensorsObjectFragment = new SensorsObjectFragment();

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) rootView.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(infoObjectFragment, getString(R.string.Info));
        adapter.addFragment(sensorsObjectFragment, getString(R.string.Sensors));
        adapter.addFragment(new Fragment(), getString(R.string.Events));
        adapter.addFragment(new Fragment(), getString(R.string.Control));
        adapter.addFragment(new Fragment(), getString(R.string.Trips));
        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
