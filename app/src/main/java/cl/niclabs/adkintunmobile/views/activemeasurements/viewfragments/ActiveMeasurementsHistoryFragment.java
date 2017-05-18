package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.ConnectivityTestReport;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.MediaTestReport;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.SpeedTestReport;
import cl.niclabs.adkintunmobile.views.activemeasurements.ConnectivityTestReportDialog;
import cl.niclabs.adkintunmobile.views.activemeasurements.MediaTestReportDialog;
import cl.niclabs.adkintunmobile.views.activemeasurements.SpeedTestReportDialog;

public class ActiveMeasurementsHistoryFragment extends ListFragment implements AdapterView.OnItemClickListener {

    public ArrayList<String> reportsDatetimeList, reportTimestampList;
    public TextView emptyListview;

    public ActiveMeasurementsHistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_active_measurements, container, false);
        emptyListview = (TextView) view.findViewById(R.id.empty);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String measurementsType = getActivity().getIntent().getStringExtra(getString(R.string.settings_active_measurements_key));
        if (measurementsType.equals(getString(R.string.settings_speed_test_category_key))) {
            reportTimestampList = SpeedTestReport.getTimestampsAllReports();
            reportsDatetimeList = SpeedTestReport.getDatetimeAllReports();
        }else if (measurementsType.equals(getString(R.string.settings_video_test_category_key))) {
            reportTimestampList = MediaTestReport.getTimestampsAllReports();
            reportsDatetimeList = MediaTestReport.getDatetimeAllReports();
        }else {         //if (measurementsType.equals(getString(R.string.settings_connectivity_test_category_key)))
            reportTimestampList = ConnectivityTestReport.getTimestampsAllReports();
            reportsDatetimeList = ConnectivityTestReport.getDatetimeAllReports();
        }

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, reportsDatetimeList);
        setListAdapter(adapter);
        getListView().setEmptyView(emptyListview);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showDialog(parent.getItemAtPosition(position).toString(), position);
    }

    private void showDialog(String data, int position) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("reportsFragment");
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
        if (measurementsType.equals(getString(R.string.settings_connectivity_test_category_sites_key)))
            reportsFragment = new ConnectivityTestReportDialog();


        Bundle bundle = new Bundle();
        //bundle.putString("value", data);
        bundle.putString("value", reportTimestampList.get(position));
        bundle.putInt("position", 1);

        reportsFragment.setArguments(bundle);
        reportsFragment.show(ft, "reportsFragmento");
    }
}
