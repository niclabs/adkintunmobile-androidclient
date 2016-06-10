package cl.niclabs.adkintunmobile.views.activeconnections;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.information.SystemSocket;
import cl.niclabs.adkintunmobile.utils.information.SystemSockets;

public class ActiveConnections extends AppCompatActivity {

    private final String TAG = "AdkM:ActiveConnections";

    private String title;
    private Context context;
    private Toolbar toolbar;
    private RelativeLayout loadingPanel;

    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_connections);

        setBaseActivityParams();
        setupToolbar();

        initList();

    }

    private void initList() {
        ArrayList<SystemSocket> sockets = SystemSockets.getTCPSockets();
        ArrayList<ActiveConnectionListElement> socketsFinal = new ArrayList<ActiveConnectionListElement>();

        for (SystemSocket s : sockets){
            ActiveConnectionListElement element = new ActiveConnectionListElement(this.context, s);

            if (element.isValidElement()){
                if (!socketsFinal.contains(element)){
                    socketsFinal.add(element);
                }else{
                    socketsFinal.get(socketsFinal.indexOf(element)).addIpAddr(element.getIpAddr());
                    socketsFinal.get(socketsFinal.indexOf(element)).addPortAddr(element.getPortAddr());
                }
            }
        }

        ListView listView = (ListView) findViewById(R.id.list);
        ActiveConnectionListAdapter listAdapter = new ActiveConnectionListAdapter(this.context, socketsFinal);
        listView.setAdapter(listAdapter);
        listView.setEmptyView(findViewById(R.id.empty));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("CACA", ((ActiveConnectionListElement)parent.getItemAtPosition(position)).getLabel());
                BottomSheetDialogFragment bottomSheetDialogFragment = new ActiveConnectionMapBottomSheetDialogFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

    }

    public void setupToolbar(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(this.title);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void setBaseActivityParams(){
        this.title = getString(R.string.view_active_connections);
        this.context = this;
        this.loadingPanel = (RelativeLayout) findViewById(R.id.loading_panel);
        ShimmerFrameLayout container =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        container.startShimmerAnimation();
    }
}
