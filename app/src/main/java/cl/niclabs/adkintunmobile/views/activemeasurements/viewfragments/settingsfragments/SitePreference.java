package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments;

import android.content.Context;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import cl.niclabs.adkintunmobile.R;

public class SitePreference extends Preference {

    private ConnectivityTestSettingsFragment settingsFragment;

    public SitePreference(ConnectivityTestSettingsFragment settingsFragment) {
        super(settingsFragment.getActivity());
        this.settingsFragment = settingsFragment;
        setWidgetLayoutResource(R.layout.layout_connectivity_test_delete);
    }

    // constructors

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