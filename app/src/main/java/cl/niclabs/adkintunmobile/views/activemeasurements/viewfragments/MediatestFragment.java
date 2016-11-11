package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cl.niclabs.adkintunmobile.R;

public class MediatestFragment extends ActiveMeasurementViewFragment {

    public MediatestFragment() {
        this.title = "Media";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_measurements_mediatest, container, false);
    }

    // TODO: Implementar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_history_btn:
                return true;
            case R.id.menu_settings_btn:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
