package cl.niclabs.adkintunmobile.views.status;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.niclabs.adkintunmobile.R;

public class StatusSettingsDialog extends DialogFragment {


    public StatusSettingsDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_status_settings_dialog, container, false);
        getDialog().setTitle("HOLA!!");
        return v;
    }

}
