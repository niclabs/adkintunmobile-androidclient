package cl.niclabs.adkintunmobile.views.status;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;
import cl.niclabs.adkintunmobile.utils.display.DisplayManager;
import cl.niclabs.adkintunmobile.utils.information.Network;
import cl.niclabs.adkintunmobile.views.BaseToolbarFragment;

public class StatusFragment extends BaseToolbarFragment {

    private final String TAG = "AdkM:StatusFragment";

    private String ret;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.title = getActivity().getString(R.string.view_connection_type);
        this.context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.toolbar_simple, container, false);
        View localFragmentView = view.findViewById(R.id.main_fragment);
        inflater.inflate(R.layout.fragment_status, (ViewGroup) localFragmentView, true);
        setupToolbar(view);


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

    }

}
