package cl.niclabs.adkintunmobile.views.status;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import cl.niclabs.adkintunmobile.R;

public class StatusSettingsDialog extends DialogFragment {

    private EditText mEditText;
    private NumberPicker mNumberPicker;
    private Button mButton;
    private int dataQuotaTotalValue;

    public StatusSettingsDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_status_settings_dialog, container, false);
        getDialog().setTitle("HOLA!!");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        dataQuotaTotalValue = sharedPreferences.getInt(getActivity().getString(R.string.settings_app_data_quota_total_key), 0);
        Log.d("OK", dataQuotaTotalValue+" "+getActivity().getString(R.string.settings_app_data_quota_total_key));
        Log.d("OK", sharedPreferences.getAll().toString());

        //mEditText = (EditText) v.findViewById(R.id.txt_your_name);
        mNumberPicker = (NumberPicker) v.findViewById(R.id.txt_your_name);
        mButton = (Button) v.findViewById(R.id.bt_save_data_quota);


        mNumberPicker.setMaxValue(14);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setDisplayedValues(new String[]{"100", "200", "300", "400", "500", "600", "700", "800", "900", "1000", "1500", "2000", "3000", "4000", "5000"});

        mNumberPicker.setWrapSelectorWheel(true);
        mNumberPicker.setValue(dataQuotaTotalValue);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataQuota(mNumberPicker.getValue());
                dismiss();
            }
        });

        return v;
    }

    public void updateDataQuota(int mbDataPlan){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getActivity().getString(R.string.settings_app_data_quota_total_key), mbDataPlan);
        editor.apply();
    }

}
