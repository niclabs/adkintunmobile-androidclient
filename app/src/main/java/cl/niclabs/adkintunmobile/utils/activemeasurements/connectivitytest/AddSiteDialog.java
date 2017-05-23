package cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments.ConnectivityTestSettingsFragment;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments.SitePreference;
import cz.msebera.android.httpclient.Header;

public class AddSiteDialog extends DialogFragment {

    private EditText editText;
    private CheckBox checkBox;
    private ArrayList<String> recommendedSites = new ArrayList<>();
    int sitesCount;
    SharedPreferences.Editor editor;
    String testSiteKey;
    String sitesCountKey;
    private ConnectivityTestSettingsFragment preferenceFragment;

    public AddSiteDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sitesCount = sharedPreferences.getInt(getString(R.string.settings_connectivity_sites_count_key), 0);
        editor = sharedPreferences.edit();
        testSiteKey = getString(R.string.settings_connectivity_test_site_);
        sitesCountKey = getString(R.string.settings_connectivity_sites_count_key);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Aceptar", null);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_site_dialog, null);

        // Get visual elements
        editText = (EditText) view.findViewById(R.id.site_edit_text);
        checkBox = (CheckBox) view.findViewById(R.id.check_box);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    editText.setEnabled(false);
                else
                    editText.setEnabled(true);
            }
        });
        setCancelable(false);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (checkBox.isChecked()){
                            getRecommendedSites();
                        }
                        else {
                            //url validation
                            if (!Patterns.WEB_URL.matcher(editText.getText()).matches()) {
                                Toast.makeText(getContext(), getString(R.string.view_active_measurements_connectivitytest_url_error_message), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            int sitesCount = sharedPreferences.getInt(getString(R.string.settings_connectivity_sites_count_key), 0);
                            sitesCount++;
                            PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceFragment.findPreference(getString(R.string.settings_connectivity_test_category_sites_key));
                            SitePreference preference = new SitePreference(preferenceFragment);
                            preference.setSummary(editText.getText());
                            preference.setWidgetLayoutResource(R.layout.layout_connectivity_test_delete);
                            preference.setKey(getString(R.string.settings_connectivity_test_site_) + sitesCount);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(getActivity().getString(R.string.settings_connectivity_sites_count_key), sitesCount);
                            editor.putString(preference.getKey(), editText.getText().toString());

                            editor.apply();

                            preferenceCategory.addPreference(preference);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    public void setPreferenceFragment(ConnectivityTestSettingsFragment connectivityTestSettingsFragment) {
        preferenceFragment = connectivityTestSettingsFragment;
    }

    private void getRecommendedSites() {
        String host = getContext().getString(R.string.speed_test_server_host);
        String port = getContext().getString(R.string.speed_test_server_port);
        final String url = host + ":" + port + "/recommended_sites/";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(1000);

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
                preferenceFragment.makeNoConnectionToast();
            }
            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
        });
    }

    public void setChecked(boolean checked){
        checkBox.setChecked(checked);
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
        preferenceFragment.refreshView();
    }
}
