package cl.niclabs.adkintunmobile.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import cl.niclabs.adkintunmobile.R;

public class BaseToolbarFragment extends Fragment{

    protected String title;
    protected Context context;
    protected Toolbar toolbar;
    protected RelativeLayout loadingPanel;

    protected void setupToolbar(View view) {
        // Setup toolbar as an actionbar
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(this.toolbar);

        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(this.title);
        setHasOptionsMenu(true);

        this.loadingPanel = (RelativeLayout) view.findViewById(R.id.loading_panel);
    }
}
