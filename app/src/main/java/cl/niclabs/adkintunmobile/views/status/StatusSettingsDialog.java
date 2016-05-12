package cl.niclabs.adkintunmobile.views.status;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class StatusSettingsDialog extends DialogFragment {

    private EditText mEditText;
    private NumberPicker mNumberPicker;
    private Button mButton;
    private int dataQuotaTotalValue;

    private DialogInterface.OnDismissListener onDismissListener;

    public StatusSettingsDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_status_settings_dialog, container, false);
        getDialog().setTitle(getActivity().getString(R.string.view_status_settings_dialog_title));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String value = sharedPreferences.getString(getActivity().getString(R.string.settings_app_data_quota_total_key), "0");
        dataQuotaTotalValue = Integer.parseInt(value);

        mNumberPicker = (NumberPicker) v.findViewById(R.id.np_data_quota);
        mButton = (Button) v.findViewById(R.id.bt_save_data_quota);


        String[] dataQuotaOptions = getResources().getStringArray(R.array.data_quotas);
        String[] formatedDataQuotaOptions = new String[dataQuotaOptions.length];

        for(int i =0 ; i<dataQuotaOptions.length; i++){
            formatedDataQuotaOptions[i] = Network.formatBytes(Long.parseLong(dataQuotaOptions[i]));
        }


        mNumberPicker.setMaxValue(formatedDataQuotaOptions.length - 1);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setDisplayedValues(formatedDataQuotaOptions);

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
        editor.putString(getActivity().getString(R.string.settings_app_data_quota_total_key), Integer.toString(mbDataPlan));
        editor.apply();
        Toast.makeText(getActivity(), getActivity().getString(R.string.settings_updated), Toast.LENGTH_SHORT).show();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    static public void showDialogPreference(FragmentManager fm, DialogInterface.OnDismissListener onDismissListener){
        StatusSettingsDialog editNameDialog = new StatusSettingsDialog();
        editNameDialog.setOnDismissListener(onDismissListener);
        editNameDialog.show(fm, "fragment_data_picker");
    }

}
