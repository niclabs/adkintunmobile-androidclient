package cl.niclabs.adkintunmobile.views.activemeasurements;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.ActiveMeasurementViewFragment;

public class ActiveMeasurementsViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<ActiveMeasurementViewFragment> mFragmentList = new ArrayList<>();

    public ActiveMeasurementsViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(ActiveMeasurementViewFragment fragment) {
        mFragmentList.add(fragment);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentList.get(position).getTitle();
    }

}
