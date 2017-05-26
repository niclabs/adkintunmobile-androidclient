package cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest;

import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.view.View;
import android.widget.Button;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.ConnectivityTestPreferenceFragment;

public class AddSitePreference extends Preference {

    private ConnectivityTestPreferenceFragment settingsFragment;

    public AddSitePreference(ConnectivityTestPreferenceFragment settingsFragment) {
        super(settingsFragment.getActivity());
        this.settingsFragment = settingsFragment;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        Button addSite = (Button) holder.findViewById(R.id.add_site_btn);
        addSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsFragment.addSitePreference();
            }
        });
    }
}