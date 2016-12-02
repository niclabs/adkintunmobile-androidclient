package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments.ActiveMeasurementsSettingsActivity;

public class ConnectivityTestFragment extends ActiveMeasurementViewFragment {

    public ConnectivityTestFragment() {
        this.title = "Conectividad";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_measurements_connectivitytest, container, false);
    }

    // TODO: Implementar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_history_btn:
                return true;
            case R.id.menu_settings_btn:
                Intent myIntent = new Intent(getContext(), ActiveMeasurementsSettingsActivity.class);
                myIntent.putExtra(getString(R.string.settings_active_measurements_key), R.string.settings_connectivity_test_category_key);
                startActivity(myIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
