package cl.niclabs.adkintunmobile.views.status;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Iterator;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class StatusFragment extends Fragment {

    private final String TAG = "AdkM:StatusFragment";

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

        getCurrentMonthDownloadedData();

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
        ((ImageView) getView().findViewById(R.id.iv_antenna)).setImageResource(Network.getConnectedCarrrierIntRes(context));
        ((TextView) getView().findViewById(R.id.tv_antenna)).setText(Network.getConnectedCarrrier(context));

    }

    public void getCurrentMonthDownloadedData(){



        Iterator<ApplicationTraffic> iterator = ApplicationTraffic.findAsIterator(
                ApplicationTraffic.class, "network_type = ?",
                Integer.toString(ApplicationTraffic.MOBILE));

        Long rx = 0L, tx = 0L;

        while (iterator.hasNext()){
            ApplicationTraffic current = iterator.next();
            rx += current.rxBytes;
            tx += current.txBytes;
        }

        Log.d(TAG, "rx: "+ Network.formatBytes(rx));
        Log.d(TAG, "tx: "+ Network.formatBytes(tx));
    }

}
