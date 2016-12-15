package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsHistoryActivity;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments.ActiveMeasurementsSettingsActivity;

public class MediaTestFragment extends ActiveMeasurementViewFragment {

    public MediaTestFragment() {
        this.title = "Media";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_measurements_mediatest, container, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.qualities_table_layout);

        String[] qualitiesName = getResources().getStringArray(R.array.qualities_name);
        String[] qualitiesPixels = getResources().getStringArray(R.array.qualities_pixels);

        boolean qualityTiny = sharedPreferences.getBoolean(getString(R.string.settings_video_test_quality_tiny_key), false);
        boolean qualitySmall = sharedPreferences.getBoolean(getString(R.string.settings_video_test_quality_small_key), false);
        boolean qualityMedium = sharedPreferences.getBoolean(getString(R.string.settings_video_test_quality_medium_key), false);
        boolean qualityLarge = sharedPreferences.getBoolean(getString(R.string.settings_video_test_quality_large_key), false);
        boolean qualityHd720 = sharedPreferences.getBoolean(getString(R.string.settings_video_test_quality_hd720_key), false);

        boolean[] qualitiesToTest = {qualityTiny, qualitySmall, qualityMedium, qualityLarge, qualityHd720};

        for (int i = 0; i < qualitiesToTest.length; i++){
            if(qualitiesToTest[i]){
                addQualityToTable(i, qualitiesName[i], qualitiesPixels[i], tableLayout);
            }
        }

        return view;
    }

    private void addQualityToTable(int i, String qualityName, String qualityPixel, TableLayout tableLayout) {
        TableRow tableRow = new TableRow(getContext());

        TextView tvPos = new TextView(getContext());
        tvPos.setText(Integer.toString(i+1));

        TextView tvQuality = new TextView(getContext());
        tvQuality.setText(qualityName);
        tvQuality.setTextAppearance(getContext(), android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Body2);

        TextView tvPixels = new TextView(getContext());
        tvPixels.setText(qualityPixel);

        tableRow.addView(tvPos);
        tableRow.addView(tvQuality);
        tableRow.addView(tvPixels);

        tableLayout.addView(tableRow);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        switch (item.getItemId()){
            case R.id.menu_history_btn:
                myIntent = new Intent(getContext(), ActiveMeasurementsHistoryActivity.class);
                myIntent.putExtra(getString(R.string.settings_active_measurements_key), getString(R.string.settings_video_test_category_key));
                startActivity(myIntent);
                return true;
            case R.id.menu_settings_btn:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String maxQuality = sharedPreferences.getString(getString(R.string.settings_video_test_max_quality_key), "None");
                if (maxQuality.equals("None"))
                    ((ActiveMeasurementsActivity) getActivity()).startMediaTest();
                else {
                    myIntent = new Intent(getContext(), ActiveMeasurementsSettingsActivity.class);
                    myIntent.putExtra(getString(R.string.settings_active_measurements_key), R.string.settings_video_test_category_key);
                    startActivity(myIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
