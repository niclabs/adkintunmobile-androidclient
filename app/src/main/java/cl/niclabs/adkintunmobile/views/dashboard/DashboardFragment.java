package cl.niclabs.adkintunmobile.views.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.information.Network;
import cl.niclabs.adkintunmobile.views.BaseToolbarFragment;
import pl.pawelkleczkowski.customgauge.CustomGauge;

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
}
