package cl.niclabs.adkintunmobile.views.status;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import cl.niclabs.adkintunmobile.R;

public class StatusSettings extends PreferenceActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();
        PreferenceManager.setDefaultValues(StatusSettings.this, R.xml.status, false);
    }

    static public class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.status);

            /*
            // Get the custom preference
            Preference customPref = (Preference) findPreference("customPref");
            customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getBaseContext(), "The custom preference has been clicked",
                            Toast.LENGTH_LONG).show();
                    SharedPreferences customSharedPreference = getSharedPreferences(
                            "myCustomSharedPrefs", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = customSharedPreference.edit();
                    editor.putString("myCustomPref", "The preference has been clicked");
                    editor.commit();
                    return true;
                }

            });
            */
        }
    }

}
