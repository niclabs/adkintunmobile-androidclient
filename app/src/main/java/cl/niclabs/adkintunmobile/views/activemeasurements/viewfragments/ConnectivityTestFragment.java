package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsHistoryActivity;
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
        View view = inflater.inflate(R.layout.fragment_active_measurements_connectivitytest, container, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        int sitesCount = sharedPreferences.getInt(getString(R.string.settings_connectivity_sites_count_key), 0);

        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.sites_table_layout);

        for (int i=1; i<=sitesCount; i++){
            TableRow tableRow = new TableRow(getContext());
            TextView textView = new TextView(getContext());

            String siteTitle = sharedPreferences.getString(getString(R.string.settings_connectivity_test_site_) + i, "");
            textView.setText(siteTitle);
            tableRow.addView(textView);
            tableLayout.addView(tableRow);
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        switch (item.getItemId()){
            case R.id.menu_history_btn:
                myIntent = new Intent(getContext(), ActiveMeasurementsHistoryActivity.class);
                myIntent.putExtra(getString(R.string.settings_active_measurements_key), getString(R.string.settings_connectivity_test_category_key));
                startActivity(myIntent);
                return true;
            case R.id.menu_settings_btn:
                myIntent = new Intent(getContext(), ActiveMeasurementsSettingsActivity.class);
                myIntent.putExtra(getString(R.string.settings_active_measurements_key), R.string.settings_connectivity_test_category_key);
                startActivity(myIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
