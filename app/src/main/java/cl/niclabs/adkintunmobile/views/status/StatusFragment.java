package cl.niclabs.adkintunmobile.views.status;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;
import cl.niclabs.adkintunmobile.utils.display.DisplayManager;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class StatusFragment extends Fragment {

    private final String TAG = "AdkM:StatusFragment";

    private String title;
    private Context context;
    private RelativeLayout loadingPanel;
    private String ret;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.title = getActivity().getString(R.string.view_status);
        this.context = getActivity();

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(this.title);
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        this.loadingPanel = (RelativeLayout) view.findViewById(R.id.loading_panel);

        (new Thread(){
            @Override
            public void run() {
                getCurrentDayTransferedData();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DisplayManager.dismissLoadingPanel(loadingPanel, context);
                        updateData(context);
                    }
                });

            }
        }).start();

        return view;
    }

    public void updateData(Context context){

        ((TextView) getView().findViewById(R.id.tv_signal)).setText(Network.getNetworkType(context));
        ((TextView) getView().findViewById(R.id.tv_signal2)).setText(Network.getSpecificNetworkType(context));

        ((ImageView) getView().findViewById(R.id.iv_sim)).setImageResource(Network.getSIMIntRes(context));
        ((TextView) getView().findViewById(R.id.tv_sim)).setText(Network.getSimCarrier(context));
        ((ImageView) getView().findViewById(R.id.iv_antenna)).setImageResource(Network.getConnectedCarrierIntRes(context));
        ((TextView) getView().findViewById(R.id.tv_antenna)).setText(Network.getConnectedCarrrier(context));

        Snackbar.make(getView(), this.ret, Snackbar.LENGTH_SHORT)
                .show();

    }

    public void getCurrentDayTransferedData(){

        Date today = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Iterator<ApplicationTraffic> iterator = ApplicationTraffic.findAsIterator(
                ApplicationTraffic.class, "network_type = ? and timestamp >= ?",
                Integer.toString(ApplicationTraffic.MOBILE),
                Long.toString(calendar.getTimeInMillis()));

        Long rx = 0L, tx = 0L;

        while (iterator.hasNext()){
            ApplicationTraffic current = iterator.next();
            rx += current.rxBytes;
            tx += current.txBytes;
        }

        Log.d(TAG, "rx: " + Network.formatBytes(rx));
        Log.d(TAG, "tx: " + Network.formatBytes(tx));

        this.ret = Network.formatBytes(rx) + "   " + Network.formatBytes(tx);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.application_traffic, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_date_picker_btn:
                showDialogPref();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogPref() {
        FragmentManager fm = getFragmentManager();
        StatusSettingsDialog dialogFragment = new StatusSettingsDialog ();
        dialogFragment.show(fm, "Sample Fragment");
    }

}
