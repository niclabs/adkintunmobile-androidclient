package cl.niclabs.adkintunmobile.views.status;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class StatusFragment extends Fragment {

    private String title;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.title = getActivity().getString(R.string.view_status);
        this.context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(this.title);
        View view = inflater.inflate(R.layout.fragment_status, container, false);



        /*
        new AlertDialog.Builder(context)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
                */

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData(this.context);
    }

    public void updateData(Context context){

        ((TextView) getView().findViewById(R.id.tv_signal)).setText(Network.getNetworkType(context));
        ((TextView) getView().findViewById(R.id.tv_signal2)).setText(Network.getSpecificNetworkType(context));

        ((ImageView) getView().findViewById(R.id.iv_sim)).setImageResource(Network.getConnectedCarrrierIntRes(context));
        ((TextView) getView().findViewById(R.id.tv_sim)).setText(Network.getSimCarrier(context));
        ((ImageView) getView().findViewById(R.id.iv_red)).setImageResource(Network.getConnectedCarrrierIntRes(context));
        ((TextView) getView().findViewById(R.id.tv_antenna)).setText(Network.getConnectedCarrrier(context));



    }
}
