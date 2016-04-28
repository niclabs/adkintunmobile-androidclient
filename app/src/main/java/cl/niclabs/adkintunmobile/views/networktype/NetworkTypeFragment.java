package cl.niclabs.adkintunmobile.views.networktype;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NetworkTypeSample;

public class NetworkTypeFragment extends Fragment {

    private String title;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iterator<NetworkTypeSample> iterator = NetworkTypeSample.findAll(NetworkTypeSample.class);
        while (iterator.hasNext()){
            NetworkTypeSample sample = iterator.next();
            Log.d("NetworkType", sample.getInitialTime()+ " "+ sample.getType());
        }
        this.title = getActivity().getString(R.string.view_network_type);
        this.context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(this.title);
         View view = inflater.inflate(R.layout.fragment_network_type, container, false);
        return view;
    }

}
