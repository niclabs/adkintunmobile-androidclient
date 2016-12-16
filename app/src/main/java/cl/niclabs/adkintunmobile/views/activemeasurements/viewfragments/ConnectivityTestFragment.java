package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;

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
            String siteTitle = sharedPreferences.getString(getString(R.string.settings_connectivity_test_site_) + i, "");
            addSiteToTable(tableLayout, i, siteTitle);
        }
        return view;
    }

    private void addSiteToTable(TableLayout tableLayout, int i, String siteTitle) {
        TableRow tableRow = new TableRow(getContext());
        TextView tvSite = new TextView(getContext());
        TextView tvPos = new TextView(getContext());

        tvPos.setText(Integer.toString(i));
        tvPos.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        tvPos.setGravity(Gravity.CENTER);

        tvSite.setText(siteTitle);
        tvSite.setTextAppearance(getContext(), android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Body2);
        tvSite.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 4));

        tableRow.addView(tvPos);
        tableRow.addView(tvSite);
        tableLayout.addView(tableRow);
    }
}
