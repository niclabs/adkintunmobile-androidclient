package cl.niclabs.adkintunmobile.views.status;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import pl.pawelkleczkowski.customgauge.CustomGauge;

public class StatusFragment extends BaseToolbarFragment {

    private final String TAG = "AdkM:StatusFragment";

    private long rxDailyMobile, txDailyMobile;
    private long rxMonthlyMobile, txMonthlyMobile;
    private String rxDailyMobileData, txDailyMobileData;
    private String rxMonthlyMobileData, txMonthlyMobileData;
    private String currentMonth, currentDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.title = getActivity().getString(R.string.view_status);
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
                setCurrentDayMobileData();
                setCurrentMonthMobileData();

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

        long totalData = (this.rxDailyMobile +this.txDailyMobile) == 0 ? 1: (this.rxDailyMobile +this.txDailyMobile);
        ((CustomGauge) getView().findViewById(R.id.gauge1)).setValue((int)(100*this.rxDailyMobile /totalData));
        ((TextView) getView().findViewById(R.id.tvgauge1)).setText(this.rxDailyMobileData);
        ((CustomGauge) getView().findViewById(R.id.gauge2)).setValue((int) (100 * this.txDailyMobile / totalData));
        ((TextView) getView().findViewById(R.id.tvgauge2)).setText(this.txDailyMobileData);

        ((TextView) getView().findViewById(R.id.tv_daily_sample_period)).setText(this.currentDay);
        ((TextView) getView().findViewById(R.id.tv_monthly_sample_period)).setText(this.currentMonth);

        //Snackbar.make(getView(), this.ret, Snackbar.LENGTH_SHORT).show();


    }

    public void setCurrentDayMobileData(){
        Date today = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long[] dailyData = getTransferedData(ApplicationTraffic.MOBILE, calendar.getTimeInMillis());

        this.rxDailyMobile = dailyData[0];
        this.txDailyMobile = dailyData[1];
        this.rxDailyMobileData = Network.formatBytes(this.rxDailyMobile);
        this.txDailyMobileData = Network.formatBytes(this.txDailyMobile);

        /*
        this.currentDay = DisplayManager.dayNames[calendar.get(Calendar.DAY_OF_WEEK)];
        this.currentDay += " " + calendar.get(Calendar.DAY_OF_MONTH);
        this.currentDay += " de " + DisplayManager.monthNames[calendar.get(Calendar.MONTH)];
        */
    }

    public void setCurrentMonthMobileData(){
        Date today = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long[] dailyData = getTransferedData(ApplicationTraffic.MOBILE, calendar.getTimeInMillis());

        this.rxMonthlyMobile = dailyData[0];
        this.txMonthlyMobile = dailyData[1];
        this.rxMonthlyMobileData = Network.formatBytes(this.rxMonthlyMobile);
        this.txMonthlyMobileData = Network.formatBytes(this.txMonthlyMobile);
/*
        this.currentMonth = DisplayManager.monthNames[calendar.get(Calendar.MONTH)];
        */
    }



    /**
     *
     * @param appTrafficType Seleccionar ApplicationTraffic.MOBILE o ApplicationTraffic.WIFI
     * @param initialTimestamp Tiempo desde el cual recuperar datos
     * @return Arreglo con [DownloadedBytes, UploadedBytes]
     */
    public long[] getTransferedData(int appTrafficType, long initialTimestamp){

        long rxData = 0, txData = 0;

        Iterator<ApplicationTraffic> iterator = ApplicationTraffic.findAsIterator(
                ApplicationTraffic.class, "network_type = ? and timestamp >= ?",
                Integer.toString(appTrafficType),
                Long.toString(initialTimestamp));

        while (iterator.hasNext()){
            ApplicationTraffic current = iterator.next();
            rxData += current.rxBytes;
            txData += current.txBytes;
        }

        long[] ret = new long[2];
        ret[0] = rxData;
        ret[1] = txData;
        return ret;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.status, menu);
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
        FragmentManager fm = getActivity().getSupportFragmentManager();
        StatusSettingsDialog editNameDialog = new StatusSettingsDialog();
        editNameDialog.show(fm, "fragment_edit_name");
    }

}
