package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;

public class MediaTestFragment extends ActiveMeasurementViewFragment {

    public MediaTestFragment() {
        this.title = "Media";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_measurements_mediatest, container, false);
    }

    // TODO: Implementar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_history_btn:
                return true;
            case R.id.menu_settings_btn:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String maxQuality = sharedPreferences.getString(getString(R.string.settings_video_test_max_quality_key), "None");
                if (maxQuality.equals("None"))
                    ((ActiveMeasurementsActivity) getActivity()).startMediaTest();
                else {
                    Intent myIntent = new Intent(getContext(), ActiveMeasurementsSettingsActivity.class);
                    myIntent.putExtra(getString(R.string.settings_active_measurements_key), R.string.settings_video_test_category_key);
                    startActivity(myIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
