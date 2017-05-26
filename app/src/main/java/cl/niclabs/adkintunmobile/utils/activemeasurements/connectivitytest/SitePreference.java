package cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest;

import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.view.View;
import android.widget.ImageView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.ConnectivityTestPreferenceFragment;

public class SitePreference extends Preference {

    private ConnectivityTestPreferenceFragment settingsFragment;

    public SitePreference(ConnectivityTestPreferenceFragment settingsFragment) {
        super(settingsFragment.getActivity());
        this.settingsFragment = settingsFragment;
        setWidgetLayoutResource(R.layout.layout_connectivity_test_delete);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        ImageView deleteImage = (ImageView) holder.findViewById(R.id.delete_image);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsFragment.deleteSitePreference(getKey());
            }
        });
    }
}