package cl.niclabs.adkintunmobile.views.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.information.Network;
import cl.niclabs.adkintunmobile.views.BaseToolbarFragment;

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
    }
}
