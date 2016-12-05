package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.ConnectivityTestReportDialog;
import cl.niclabs.adkintunmobile.views.activemeasurements.MediaTestReportDialog;
import cl.niclabs.adkintunmobile.views.activemeasurements.SpeedTestReportDialog;

public class HistoryActiveMeasurementsFragment extends ListFragment implements AdapterView.OnItemClickListener {


    public HistoryActiveMeasurementsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_active_measurements, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter adapter;

        String measurementsType = getActivity().getIntent().getStringExtra(getString(R.string.settings_active_measurements_key));
        if (measurementsType.equals(getString(R.string.settings_speed_test_category_key)))
            adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.sync_sampling_compression_type, android.R.layout.simple_list_item_1);
        else if (measurementsType.equals(getString(R.string.settings_video_test_category_key)))
            adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.mobile_operators, android.R.layout.simple_list_item_1);
        //if (measurementsType.equals(getString(R.string.settings_connectivity_test_category_key)))
        else
            adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.data_quotas, android.R.layout.simple_list_item_1);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showDialog(position);
    }

    private void showDialog(int position) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("TEST");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment reportsFragment = new DialogFragment();
        String measurementsType = getActivity().getIntent().getStringExtra(getString(R.string.settings_active_measurements_key));
        if (measurementsType.equals(getString(R.string.settings_speed_test_category_key)))
            reportsFragment = new SpeedTestReportDialog();
        if (measurementsType.equals(getString(R.string.settings_video_test_category_key)))
            reportsFragment = new MediaTestReportDialog();
        if (measurementsType.equals(getString(R.string.settings_connectivity_test_category_key)))
            reportsFragment = new ConnectivityTestReportDialog();


        Bundle bundle = new Bundle();
        bundle.putString("value", "una fecha");
        bundle.putInt("position", position);

        reportsFragment.setArguments(bundle);
        reportsFragment.show(ft, "TEST");
    }
}
