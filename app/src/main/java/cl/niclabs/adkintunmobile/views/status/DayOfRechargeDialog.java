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
import android.widget.NumberPicker;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;

public class DayOfRechargeDialog extends DialogFragment {

    static public final String TAG = "AdkM:DayOfRechargeDialog";

    private NumberPicker mNumberPicker;
    private Button confirmationButton;

    private DialogInterface.OnDismissListener onDismissListener;

    public DayOfRechargeDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_day_of_recharge_dialog, container, false);
        getDialog().setTitle(getActivity().getString(R.string.view_day_of_recharge_dialog_title));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String value = sharedPreferences.getString(getActivity().getString(R.string.settings_app_day_of_recharge_key), "0");
        int dayOfRechargeValue = Integer.parseInt(value);

        mNumberPicker = (NumberPicker) v.findViewById(R.id.np_day_of_recharge);
        confirmationButton = (Button) v.findViewById(R.id.bt_save_day_of_recharge);

        String[] dayOfRechargeOptions = new String[31];

        for(int i=0 ; i<dayOfRechargeOptions.length; i++){
            dayOfRechargeOptions[i] = Integer.toString(i+1);
        }

        mNumberPicker.setMaxValue(dayOfRechargeOptions.length - 1);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setDisplayedValues(dayOfRechargeOptions);

        mNumberPicker.setWrapSelectorWheel(true);
        mNumberPicker.setValue(dayOfRechargeValue);

        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDayOfRecharge(mNumberPicker.getValue());
                dismiss();
            }
        });

        return v;
    }

    public void updateDayOfRecharge(int dayOfRecharge){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getActivity().getString(R.string.settings_app_day_of_recharge_key), Integer.toString(dayOfRecharge));
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
        DayOfRechargeDialog editNameDialog = new DayOfRechargeDialog();
        editNameDialog.setOnDismissListener(onDismissListener);
        editNameDialog.show(fm, TAG);
    }
}
