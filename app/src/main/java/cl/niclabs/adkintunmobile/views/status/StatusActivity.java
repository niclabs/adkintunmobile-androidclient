package cl.niclabs.adkintunmobile.views.status;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import pl.pawelkleczkowski.customgauge.CustomGauge;

public class StatusActivity extends AppCompatActivity {

    private final String TAG = "AdkM:StatusActivity";

    protected String title;
    protected Context context;
    protected Toolbar toolbar;
    protected RelativeLayout loadingPanel;

    private long rxDailyMobile, txDailyMobile;
    private long rxMonthlyMobile, txMonthlyMobile;
    private String rxDailyMobileData, txDailyMobileData;
    private String rxMonthlyMobileData, txMonthlyMobileData;
    private String currentMonth, currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        setBaseActivityParams();
        setupToolbar();


        (new Thread(){
            @Override
            public void run() {
                setCurrentDayMobileData();
                setCurrentMonthMobileData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DisplayManager.dismissLoadingPanel(loadingPanel, context);
                        updateActivityView(context);
                    }
                });

            }
        }).start();

    }

    public void setBaseActivityParams(){
        this.title = getString(R.string.view_status);
        this.context = this;
        this.loadingPanel = (RelativeLayout) findViewById(R.id.loading_panel);
    }

    public void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(this.title);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.status, menu);
        return super.onCreateOptionsMenu(menu);
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
        FragmentManager fm = getSupportFragmentManager();
        StatusSettingsDialog editNameDialog = new StatusSettingsDialog();
        editNameDialog.show(fm, "fragment_data_picker");
    }


    public void updateActivityView(Context context){

        ((TextView)findViewById(R.id.tv_signal)).setText(Network.getNetworkType(context));
        ((TextView)findViewById(R.id.tv_signal2)).setText(Network.getSpecificNetworkType(context));

        ((ImageView)findViewById(R.id.iv_sim)).setImageResource(Network.getSIMIntRes(context));
        ((TextView)findViewById(R.id.tv_sim)).setText(Network.getSimCarrier(context));
        ((ImageView)findViewById(R.id.iv_antenna)).setImageResource(Network.getConnectedCarrierIntRes(context));
        ((TextView)findViewById(R.id.tv_antenna)).setText(Network.getConnectedCarrrier(context));

        long totalData = (this.rxDailyMobile +this.txDailyMobile) == 0 ? 1: (this.rxDailyMobile +this.txDailyMobile);
        ((CustomGauge)findViewById(R.id.gauge1)).setValue((int)(100*this.rxDailyMobile /totalData));
        ((TextView)findViewById(R.id.tvgauge1)).setText(this.rxDailyMobileData);
        ((CustomGauge)findViewById(R.id.gauge2)).setValue((int) (100 * this.txDailyMobile / totalData));
        ((TextView)findViewById(R.id.tvgauge2)).setText(this.txDailyMobileData);

        ((TextView)findViewById(R.id.tv_daily_sample_period)).setText(this.currentDay);
        ((TextView)findViewById(R.id.tv_monthly_sample_period)).setText(this.currentMonth);

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

        this.currentDay = getResources().getStringArray(R.array.day_of_week)[calendar.get(Calendar.DAY_OF_WEEK)];
        this.currentDay += " " + calendar.get(Calendar.DAY_OF_MONTH);
        this.currentDay += " de " + getResources().getStringArray(R.array.month_of_year)[calendar.get(Calendar.MONTH)];
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

        this.currentMonth = getResources().getStringArray(R.array.month_of_year)[calendar.get(Calendar.MONTH)];
        this.currentMonth += " " + calendar.get(Calendar.YEAR);
    }

    /**
     *
     * @param appTrafficType Seleccionar ApplicationTraffic.MOBILE o ApplicationTraffic.WIFI
     * @param initialTimestamp Tiempo desde el cual recuperar datos
     * @return Arreglo con [DownloadedBytes, UploadedBytes]
     */
    //TODO: Move to ApplicationTraffic.java
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
}
