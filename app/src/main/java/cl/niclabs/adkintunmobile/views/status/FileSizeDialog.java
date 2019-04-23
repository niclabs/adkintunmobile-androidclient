package cl.niclabs.adkintunmobile.views.status;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import cl.niclabs.adkintunmobile.R;

public class FileSizeDialog extends DialogFragment {

    static public final String TAG = "AdkM:FileSizeDialog";

    private NumberPicker mNumberPicker;

    private DialogInterface.OnDismissListener onDismissListener;

    public FileSizeDialog() {
        // Required empty public constructor
    }

    public static FileSizeDialog newInstance(String title) {
        FileSizeDialog frag = new FileSizeDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                updateFileSize(mNumberPicker.getValue());
                String[] fileSizeOptions = getResources().getStringArray(R.array.speed_test_file_size);
                TextView fileSize = getActivity().findViewById(R.id.file_size);
                fileSize.append(fileSizeOptions[mNumberPicker.getValue()]);
                dismiss();
            }
        });
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.fragment_file_size_dialog, null);
        builder.setTitle("Tamaño de Archivo");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String value = sharedPreferences.getString(getActivity().getString(R.string.speedtest_file_size), "0");

        mNumberPicker = view.findViewById(R.id.np_file_size);

        String[] fileSizeOptions = getResources().getStringArray(R.array.speed_test_file_size);

        mNumberPicker.setMaxValue(fileSizeOptions.length - 1);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setDisplayedValues(fileSizeOptions);

        mNumberPicker.setWrapSelectorWheel(true);
        mNumberPicker.setValue(Integer.parseInt(value));

        builder.setView(view);

        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void updateFileSize(int mbFileSize){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getActivity().getString(R.string.speedtest_file_size), Integer.toString(mbFileSize));
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
}

