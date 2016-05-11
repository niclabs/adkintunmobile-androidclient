package cl.niclabs.adkintunmobile.views.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;
import cl.niclabs.adkintunmobile.data.persistent.visualization.DailyConnectionModeSummary;
import cl.niclabs.adkintunmobile.data.persistent.visualization.DailyNetworkTypeSummary;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NetworkTypeSample;
import cl.niclabs.adkintunmobile.utils.information.Network;
import cl.niclabs.adkintunmobile.views.BaseToolbarFragment;
import cl.niclabs.adkintunmobile.views.applicationstraffic.ApplicationsTrafficListElement;
import cl.niclabs.adkintunmobile.views.connectiontype.networktype.DailyNetworkTypeInformation;

public class DashboardFragment extends BaseToolbarFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.title = getActivity().getString(R.string.app_name);
        this.context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_collapsable_toolbar, container, false);
        View localFragmentView = view.findViewById(R.id.main_fragment);
        inflater.inflate(R.layout.fragment_dashboard, (ViewGroup) localFragmentView, true);
        setupToolbar(view);

        updateStatusToolbar(view);
        updateMobileConsumption(view);
        updateConnectionMode(view);
        updateTopApps(view);

        return view;
    }


    public void updateStatusToolbar(View view){

        TextView tvSim, tvAntenna, tvSignal;

        tvSim = (TextView) view.findViewById(R.id.tv_sim);
        tvAntenna = (TextView) view.findViewById(R.id.tv_antenna);
        tvSignal = (TextView) view.findViewById(R.id.tv_signal);

        tvSim.setText(Network.getSimCarrier(context));
        tvAntenna.setText(Network.getConnectedCarrrier(context));
        tvSignal.setText(Network.getNetworkType(context));

        final ImageView ivBackdrop = (ImageView) view.findViewById(R.id.iv_backdrop_toolbar);
        Animation zoomIn = AnimationUtils.loadAnimation(this.context, R.anim.zoom_toolbar);
        zoomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation anim = AnimationUtils.loadAnimation(context, R.anim.zoom_toolbar);
                anim.setAnimationListener(this);
                ivBackdrop.setAnimation(anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivBackdrop.setAnimation(zoomIn);

    }

    public void updateTopApps(View view){
        ApplicationsTrafficListElement[] topApps = prueba();

        if(topApps[0] != null) {
            ((ImageView) view.findViewById(R.id.iv_app1)).setImageDrawable(topApps[0].getLogo());
            ((TextView) view.findViewById(R.id.tv_app1)).setText(topApps[0].getLabel());
        }

        if(topApps[1] != null) {
            ((ImageView) view.findViewById(R.id.iv_app2)).setImageDrawable(topApps[1].getLogo());
            ((TextView) view.findViewById(R.id.tv_app2)).setText(topApps[1].getLabel());
        }

        if(topApps[2] != null) {
            ((ImageView) view.findViewById(R.id.iv_app3)).setImageDrawable(topApps[2].getLogo());
            ((TextView) view.findViewById(R.id.tv_app3)).setText(topApps[2].getLabel());
        }

    }

    public void updateMobileConsumption(View view){
        prueba2();
        ((TextView) view.findViewById(R.id.tv_download_data)).setText(Network.formatBytes(this.rxMobile));
        ((TextView) view.findViewById(R.id.tv_upload_data)).setText(Network.formatBytes(this.txMobile));
    }

    public void updateConnectionMode(View view){
        ((TextView) view.findViewById(R.id.tv_primary_conn)).setText(DailyConnectionModeSummary.getPrimaryType(System.currentTimeMillis()));
    }

    public ApplicationsTrafficListElement[] prueba(){
        ApplicationsTrafficListElement[] ret = new ApplicationsTrafficListElement[3];

        // Obtener aplicaciones con actividad de hoy
        Date today = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long initTime = calendar.getTimeInMillis();

        String sqlStatements[] = new String[2];
        sqlStatements[0] = Long.toString(initTime);
        sqlStatements[1] = Integer.toString(ApplicationTraffic.MOBILE);

        Iterator<ApplicationTraffic> apps = ApplicationTraffic.findAsIterator(
                ApplicationTraffic.class,
                "timestamp >= ? and network_type = ?",
                sqlStatements,
                null,
                "rx_bytes DESC",
                "3");

        int i = 0;
        while (apps.hasNext()){
            ApplicationTraffic current = apps.next();

            ApplicationsTrafficListElement elem = new ApplicationsTrafficListElement(context, current);
            ret[i++] = elem;
            Log.d("OK", elem.toString());
        }

        return ret;

    }


    public long rxMobile, txMobile;

    public void prueba2(){

        this.rxMobile = this.txMobile = 0;

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

        while (iterator.hasNext()){
            ApplicationTraffic current = iterator.next();
            this.rxMobile += current.rxBytes;
            this.txMobile += current.txBytes;
        }

    }

    public void prueba3(){
        DailyNetworkTypeSummary a = DailyNetworkTypeSummary.getSummary(System.currentTimeMillis());
        StatisticInformation statistic = new DailyNetworkTypeInformation(context, System.currentTimeMillis(), System.currentTimeMillis());
        List<NetworkTypeSample> samples = NetworkTypeSample.listAll(NetworkTypeSample.class);
        int b = 0;
    }
}
