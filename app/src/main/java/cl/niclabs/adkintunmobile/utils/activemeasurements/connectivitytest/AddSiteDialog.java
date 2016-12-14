package cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments.ActiveMeasurementsSettingsFragment;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments.ConnectivityTestSettingsFragment;

public class AddSiteDialog extends DialogFragment {

    private EditText editText;
    private PreferenceFragment preferenceFragment;

    public AddSiteDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Nuevo sitio de prueba");
        builder.setPositiveButton("Aceptar", null);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_site_dialog, null);

        // Get visual elements
        editText = (EditText) view.findViewById(R.id.site_edit_text);

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
                        //url validation
                        if(!Patterns.WEB_URL.matcher(editText.getText()).matches()){
                            Toast.makeText(getContext(), getString(R.string.view_active_measurements_connectivitytest_url_error_message), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        int sitesCount = sharedPreferences.getInt(getString(R.string.settings_connectivity_sites_count_key), 0);
                        sitesCount ++;
                        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceFragment.findPreference(getString(R.string.settings_connectivity_test_category_key));
                        Preference preference = new Preference(getContext());
                        preference.setSummary(editText.getText());
                        preference.setKey(getString(R.string.settings_connectivity_test_site_) + sitesCount);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(getActivity().getString(R.string.settings_connectivity_sites_count_key), sitesCount);
                        editor.putString(preference.getKey(), editText.getText().toString());
                        Log.d("EDITOR", getActivity().getString(R.string.settings_connectivity_sites_count_key)+ " " + sitesCount);
                        Log.d("EDITOR", preference.getKey()+ " " + editText.getText());

                        editor.apply();

                        preferenceCategory.addPreference(preference);
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
}
