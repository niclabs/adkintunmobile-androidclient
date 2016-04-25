package cl.niclabs.adkintunmobile.views.applicationstraffic;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;


public class ApplicationsTrafficListFragment extends Fragment {

    private final String TAG = "AdkM:AppTrafficListFragmentMobile";

    private Context context;
    private String title;
    private ArrayList<ApplicationsTrafficListElement> dataArray;
    private ApplicationsTrafficListAdapter listAdapter;

    public ApplicationsTrafficListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applications_traffic_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list_view_traffic);
        this.listAdapter = new ApplicationsTrafficListAdapter(this.context, this.dataArray);
        listView.setAdapter(listAdapter);
        listView.setEmptyView(view.findViewById(R.id.empty));
        this.listAdapter.sort();

        return view;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDataArray(ArrayList<ApplicationsTrafficListElement> dataArray) {
        this.dataArray = dataArray;
    }

    public void updateData(ArrayList<ApplicationsTrafficListElement> dataArray){
        this.listAdapter.clear();
        this.listAdapter.addAll(dataArray);
        this.listAdapter.sort();
    }
}
