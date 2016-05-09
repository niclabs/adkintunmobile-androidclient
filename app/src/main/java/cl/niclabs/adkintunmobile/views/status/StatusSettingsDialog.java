package cl.niclabs.adkintunmobile.views.status;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import cl.niclabs.adkintunmobile.R;

public class StatusSettingsDialog extends DialogFragment {

    private EditText mEditText;

    public StatusSettingsDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_status_settings_dialog, container, false);
        //getDialog().setTitle("HOLA!!");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String dataQuotaTotal = sharedPreferences.getString(getActivity().getString(R.string.settings_app_data_quota_total), "");
        int dataQuotaTotalValue = Integer.parseInt(dataQuotaTotal);

        mEditText = (EditText) v.findViewById(R.id.txt_your_name);
        return v;
    }

}
