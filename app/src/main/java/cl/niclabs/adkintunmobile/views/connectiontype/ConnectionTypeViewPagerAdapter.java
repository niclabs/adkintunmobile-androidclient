package cl.niclabs.adkintunmobile.views.connectiontype;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ConnectionTypeViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<ConnectionTypeViewFragment> mFragmentList = new ArrayList<>();

    public ConnectionTypeViewPagerAdapter(FragmentManager fm) {
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

    public void addFragment(ConnectionTypeViewFragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentList.get(position).getTitle();
    }
}
