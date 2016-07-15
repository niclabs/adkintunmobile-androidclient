package cl.niclabs.adkintunmobile.views.applicationstraffic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ApplicationsTrafficViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<ApplicationsTrafficListFragment> mFragmentList = new ArrayList<>();

    public ApplicationsTrafficViewPagerAdapter(FragmentManager manager) {
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

    public void addFragment(ApplicationsTrafficListFragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentList.get(position).getTitle();
    }
}