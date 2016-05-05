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

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;
import cl.niclabs.adkintunmobile.utils.information.Network;
import cl.niclabs.adkintunmobile.views.BaseToolbarFragment;
import cl.niclabs.adkintunmobile.views.applicationstraffic.ApplicationsTrafficListElement;

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

        updateStatusBanner(view);

        setTopApps(view);

        return view;
    }


    public void updateStatusBanner(View view){
        TextView tvSim,tvAntenna, tvSignal;
        tvSim = (TextView) view.findViewById(R.id.tv_sim);
        tvAntenna = (TextView) view.findViewById(R.id.tv_antenna);
        tvSignal = (TextView) view.findViewById(R.id.tv_signal);


        tvSim.setText(Network.getSimCarrier(context));
        tvAntenna.setText(Network.getConnectedCarrrier(context));
        tvSignal.setText(Network.getNetworkType(context));

        final ImageView ivBackdrop = (ImageView) view.findViewById(R.id.backdrop);
        Animation zoomin = AnimationUtils.loadAnimation(this.context, R.anim.zoom_toolbar);
        zoomin.setAnimationListener(new Animation.AnimationListener() {
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
        ivBackdrop.setAnimation(zoomin);

    }

    public void setTopApps(View view){
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
}
