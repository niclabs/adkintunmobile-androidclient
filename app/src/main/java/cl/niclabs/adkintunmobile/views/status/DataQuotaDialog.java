package cl.niclabs.adkintunmobile.views.status;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class DataQuotaDialog extends DialogFragment {

    static public final String TAG = "AdkM:DataQuotaDialog";

    private NumberPicker mNumberPicker;

    private DialogInterface.OnDismissListener onDismissListener;

    public DataQuotaDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                updateDataQuota(mNumberPicker.getValue());
                dismiss();
            }
        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.fragment_data_quota_dialog, null);
        builder.setTitle(getActivity().getString(R.string.view_data_quota_dialog_title));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String value = sharedPreferences.getString(getActivity().getString(R.string.settings_app_data_quota_total_key), "0");
        int dataQuotaTotalValue = Integer.parseInt(value);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.np_data_quota);

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

        builder.setView(view);
        AlertDialog dialog = builder.create();

        return dialog;
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
        DataQuotaDialog editNameDialog = new DataQuotaDialog();
        editNameDialog.setOnDismissListener(onDismissListener);
        editNameDialog.show(fm, TAG);
    }

}
