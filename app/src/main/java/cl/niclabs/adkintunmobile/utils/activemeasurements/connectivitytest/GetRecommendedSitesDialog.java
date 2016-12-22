package cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.ConnectivityTestFragment;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments.ActiveMeasurementsSettingsActivity;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments.ConnectivityTestSettingsFragment;
import cz.msebera.android.httpclient.Header;

public class GetRecommendedSitesDialog extends DialogFragment {

    private ArrayList<String> recommendedSites = new ArrayList<>();
    int sitesCount;
    SharedPreferences.Editor editor;
    String testSiteKey;
    String sitesCountKey;
    FragmentActivity parent;

    public GetRecommendedSitesDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        parent = getActivity();

        sitesCount = sharedPreferences.getInt(getString(R.string.settings_connectivity_sites_count_key), 0);
        editor = sharedPreferences.edit();
        testSiteKey = getString(R.string.settings_connectivity_test_site_);
        sitesCountKey = getString(R.string.settings_connectivity_sites_count_key);

        builder.setTitle("Sitios Recomendados").setMessage("Descargar sitios recomendados (Esto eliminará los sitios ingresados)");
        builder.setPositiveButton(android.R.string.yes, null);
        builder.setNegativeButton(android.R.string.cancel, null);
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getRecommendedSites();
                        dialog.dismiss();
                    }
                });
            }
        });
        return dialog;
    }

    private void getRecommendedSites() {String host = getContext().getString(R.string.speed_test_server_host);
        String port = getContext().getString(R.string.speed_test_server_port);
        final String url = host + ":" + port + "/recommendedSites/";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(getContext(), url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("data");

                    for (int i=0; i<data.length(); i++){
                        String site = data.getString(i);
                        recommendedSites.add(site);
                    }
                    setRecommendedSites();

                } catch (JSONException e) {
                    Log.d("JSON", "API JSONException");
                    e.printStackTrace();
                }
            };
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
        });
    }

    private void setRecommendedSites() {
        for (int i=1; i<=sitesCount; i++){
            editor.remove(testSiteKey + i);
        }

        for (int i=0; i<recommendedSites.size(); i++){
            editor.putString(testSiteKey + (i+1), recommendedSites.get(i));
        }
        editor.putInt(sitesCountKey, recommendedSites.size());
        editor.apply();
        if (parent instanceof ActiveMeasurementsActivity)
            ((ConnectivityTestFragment) ((ActiveMeasurementsActivity) parent).getViewPagerItem(2)).refreshView();
        else if (parent instanceof ActiveMeasurementsSettingsActivity)
            ((ConnectivityTestSettingsFragment) parent.getFragmentManager().findFragmentById(R.id.main_fragment)).refreshView();
        Log.d("ASDASD", "AS"+(getParentFragment() == null));
    }
}
