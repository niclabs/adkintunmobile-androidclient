package cl.niclabs.adkintunmobile.views.status;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionModeSample;
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
    private long monthlyDataQuota;
    private String rxDailyMobileData, txDailyMobileData;
    private String rxMonthlyMobileData, txMonthlyMobileData;
    private String currentMonth, currentDay, nextMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        setBaseActivityParams();
        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();

        DisplayManager.enableLoadingPanel(loadingPanel);
        (new Thread(){
            @Override
            public void run() {
                setCurrentDayMobileData();
                setCurrentMonthMobileData();
                setMonthlyDataQuota();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DisplayManager.dismissLoadingPanel(loadingPanel, context);
                        updateActivityView(context);
                    }
                });

            }
        }).start();




        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();

    }

    public void setBaseActivityParams(){
        this.title = getString(R.string.view_status);
        this.context = this;
        this.loadingPanel = (RelativeLayout) findViewById(R.id.loading_panel);
        ShimmerFrameLayout container =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        container.startShimmerAnimation();
    }

    public void setupToolbar(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
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
            case R.id.menu_settings_btn:
                showDialogMobileQuotaPref();
                break;
            case R.id.menu_info_btn:
                showTutorial();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogMobileQuotaPref() {
        final FragmentManager fm = getSupportFragmentManager();
        DataQuotaDialog.showDialogPreference(fm, new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setMonthlyDataQuota();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateActivityView(context);
                    }
                });
                DayOfRechargeDialog.showDialogPreference(fm, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        setCurrentMonthMobileData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateActivityView(context);
                            }
                        });
                    }
                });
            }
        });
    }

    public void showDialogMobileQuotaPref(View view){
        showDialogMobileQuotaPref();
    }


    public void updateActivityView(Context context){

        // Completando "Tu Internet"
        if(Network.getActiveNetwork(context) == ConnectionModeSample.MOBILE){
            ((TextView) findViewById(R.id.tv_internet_interface)).setText(R.string.view_connection_mode_mobile);
            ((ImageView)findViewById(R.id.iv_internet_type)).setImageResource(R.drawable.ic_02_mobile);
        }else if(Network.getActiveNetwork(context) == ConnectionModeSample.WIFI){
            ((TextView) findViewById(R.id.tv_internet_interface)).setText(R.string.view_connection_mode_wifi);
            ((ImageView)findViewById(R.id.iv_internet_type)).setImageResource(R.drawable.ic_01_wifi);
        }else {
            ((TextView) findViewById(R.id.tv_internet_interface)).setText(R.string.view_connection_mode_disconnected);
            ((ImageView)findViewById(R.id.iv_internet_type)).setImageResource(R.drawable.ic_03_nowifi);
        }
        ((TextView) findViewById(R.id.tv_internet_network)).setText(Network.getConnectionDetails(context)[0]);
        ((TextView) findViewById(R.id.tv_internet_state)).setText(Network.getConnectionDetails(context)[1]);

        // Completando "Tu SIM"
        ((ImageView)findViewById(R.id.iv_sim)).setImageResource(Network.getSIMIntRes(context));
        ((TextView)findViewById(R.id.tv_sim)).setText(Network.getSimCarrier(context));

        // Completando "Tu Red"
        ((ImageView)findViewById(R.id.iv_antenna)).setImageResource(Network.getConnectedCarrierIntRes(context));
        ((TextView)findViewById(R.id.tv_antenna)).setText(Network.getConnectedCarrrier(context));

        // Completando Tu consumo diario"
        long totalDailyData = (this.rxDailyMobile +this.txDailyMobile) == 0 ? 1: (this.rxDailyMobile +this.txDailyMobile);
        ((CustomGauge)findViewById(R.id.gauge_daily_rx)).setValue((int) (100 * this.rxDailyMobile / totalDailyData));
        ((TextView)findViewById(R.id.tv_gauge_daily_rx)).setText(this.rxDailyMobileData);
        ((CustomGauge)findViewById(R.id.gauge_daily_tx)).setValue((int) (100 * this.txDailyMobile / totalDailyData));
        ((TextView)findViewById(R.id.tv_gauge_daily_tx)).setText(this.txDailyMobileData);
        ((TextView)findViewById(R.id.tv_daily_sample_period)).setText(String.format(getString(R.string.view_status_sample_period), this.currentDay));

        // Completando "Tu consumo mensual"
        long totalMonthlyData = this.rxMonthlyMobile + this.txMonthlyMobile;
        ((CustomGauge)findViewById(R.id.gauge_monthly_rx)).setValue((int) (100 * this.rxMonthlyMobile / (totalMonthlyData != 0 ? totalMonthlyData : 1)));
        ((TextView)findViewById(R.id.tv_gauge_monthly_rx)).setText(this.rxMonthlyMobileData);
        ((CustomGauge)findViewById(R.id.gauge_monthly_tx)).setValue((int) (100 * this.txMonthlyMobile / (totalMonthlyData != 0 ? totalMonthlyData : 1)));
        ((TextView)findViewById(R.id.tv_gauge_monthly_tx)).setText(this.txMonthlyMobileData);

        int monthlyQuotaPercentage = (int) (100 * totalMonthlyData /this.monthlyDataQuota);
        ((ProgressBar)findViewById(R.id.pb_mobile_data_consumption)).setProgress(monthlyQuotaPercentage);
        ((TextView)findViewById(R.id.tv_data_quota_percentage)).setText(monthlyQuotaPercentage + "%");

        ((TextView)findViewById(R.id.tv_used_data_quota)).setText(Network.formatBytes(totalMonthlyData));
        ((TextView)findViewById(R.id.tv_available_data_quota)).setText(Network.formatBytes(this.monthlyDataQuota));

        ((TextView)findViewById(R.id.tv_monthly_sample_period)).setText(String.format(getString(R.string.view_status_monthly_sample_period), this.currentMonth, this.nextMonth));


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

        this.currentDay = getResources().getStringArray(R.array.day_of_week)[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        this.currentDay += " " + calendar.get(Calendar.DAY_OF_MONTH);
        this.currentDay += " de " + getResources().getStringArray(R.array.month_of_year)[calendar.get(Calendar.MONTH)];

        Log.d(TAG, "Daily: rx:"+this.rxDailyMobile+" tx:"+this.txDailyMobile);
        Log.d(TAG, "Daily: rx:"+this.rxDailyMobileData+" tx:"+this.txDailyMobileData);
    }

    public void setCurrentMonthMobileData(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = sharedPreferences.getString(getString(R.string.settings_app_day_of_recharge_key), "0");
        int dayOfRecharge = Integer.parseInt(value) + 1;

        Date today = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        if (dayOfRecharge > calendar.get(Calendar.DAY_OF_MONTH)) {
            int month = calendar.get(Calendar.MONTH) - 1;
            if (month < 0)
                month = Calendar.DECEMBER;
            calendar.set(Calendar.MONTH, month);
        }
        if (dayOfRecharge > calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            dayOfRecharge = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfRecharge);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long[] dailyData = getTransferedData(ApplicationTraffic.MOBILE, calendar.getTimeInMillis());

        this.rxMonthlyMobile = dailyData[0];
        this.txMonthlyMobile = dailyData[1];
        this.rxMonthlyMobileData = Network.formatBytes(this.rxMonthlyMobile);
        this.txMonthlyMobileData = Network.formatBytes(this.txMonthlyMobile);

        this.currentMonth = dayOfRecharge + " ";
        this.currentMonth += getResources().getStringArray(R.array.month_of_year)[calendar.get(Calendar.MONTH)];
        this.currentMonth += " " + calendar.get(Calendar.YEAR);

        calendar.add(Calendar.MONTH, 1);
        this.nextMonth = dayOfRecharge + " ";
        this.nextMonth += getResources().getStringArray(R.array.month_of_year)[calendar.get(Calendar.MONTH)];
        this.nextMonth += " " + calendar.get(Calendar.YEAR);

        Log.d(TAG, "Monthly: rx:"+this.rxMonthlyMobile+" tx:"+this.txMonthlyMobile);
        Log.d(TAG, "Monthly: rx:"+this.rxMonthlyMobileData+" tx:"+this.txMonthlyMobileData);
    }

    public void setMonthlyDataQuota(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int optionSelected = Integer.parseInt(sharedPreferences.getString(getString(R.string.settings_app_data_quota_total_key), "0"));
        this.monthlyDataQuota = Long.parseLong(getResources().getStringArray(R.array.data_quotas)[optionSelected]);
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


    private int helpCounter;
    private ShowcaseView showcaseView;

    private void showTutorial() {
        helpCounter = 0;
        final String[] tutorialTitle = getResources().getStringArray(R.array.tutorial_status_title);
        final String[] tutorialBody = getResources().getStringArray(R.array.tutorial_status_body);
        final NestedScrollView mScrollView = (NestedScrollView) findViewById(R.id.sv_activity_status);

        showcaseView = new ShowcaseView.Builder(this)
                .setTarget(new ViewTarget(toolbar.getChildAt(0)))
                .setContentTitle(tutorialTitle[helpCounter])
                .setContentText(tutorialBody[helpCounter])
                .setStyle(R.style.CustomShowcaseTheme)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helpCounter++;
                        showcaseView.setContentTitle("");
                        showcaseView.setContentText("");
                        Target mTarget = Target.NONE;

                        switch (helpCounter) {
                            case 1:
                                mScrollView.scrollTo(0, 0);
                                mTarget = new ViewTarget(findViewById(R.id.tv_internet_interface));
                                break;

                            case 2:
                                mTarget = new ViewTarget(findViewById(R.id.iv_sim));
                                showcaseView.forceTextPosition(ShowcaseView.BELOW_SHOWCASE);
                                break;

                            case 3:
                                mTarget = new ViewTarget(findViewById(R.id.iv_antenna));
                                break;

                            case 4:
                                mScrollView.scrollTo(0, mScrollView.getBottom());
                                mTarget = new ViewTarget(findViewById(R.id.gauge_daily_rx));
                                break;

                            case 5:
                                mTarget = new ViewTarget(findViewById(R.id.pb_mobile_data_consumption));
                                showcaseView.forceTextPosition(ShowcaseView.ABOVE_SHOWCASE);
                                showcaseView.setButtonText(getString(R.string.tutorial_close));
                                break;

                            default:
                                showcaseView.hide();
                                return;
                        }
                        showcaseView.setShowcase(mTarget, true);
                        showcaseView.setContentTitle(tutorialTitle[helpCounter]);
                        showcaseView.setContentText(tutorialBody[helpCounter]);
                    }
                })
                .withNewStyleShowcase()
                .build();
        showcaseView.setButtonText(getString(R.string.tutorial_next));
        showcaseView.setHideOnTouchOutside(true);
    }
}
