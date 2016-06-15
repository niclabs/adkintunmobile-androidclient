package cl.niclabs.adkintunmobile.views.activeconnections;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.information.SystemSocket;
import cl.niclabs.adkintunmobile.utils.information.SystemSockets;

public class ActiveConnectionsActivity extends AppCompatActivity {

    private final String TAG = "AdkM:ActiveConnActivity";

    private String title;
    private Context context;
    private Toolbar toolbar;
    private RelativeLayout loadingPanel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_connections);

        setBaseActivityParams();
        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
    }

    private void AddListElements(List<ActiveConnectionListElement> list,
                                 List<SystemSocket> systemSocketList){
        for (SystemSocket socket : systemSocketList){
            ActiveConnectionListElement element =
                    new ActiveConnectionListElement(this.context, socket);

            if (element.isValidElement()){
                if (!list.contains(element)){
                    list.add(element);
                }else{
                    list.get(list.indexOf(element)).addConnection(socket);
                }
            }
        }
    }

    private void initList() {

        ArrayList<ActiveConnectionListElement> activeSockets = new ArrayList<ActiveConnectionListElement>();

        ArrayList<SystemSocket> tcpSockets = SystemSockets.getTCPSockets();
        ArrayList<SystemSocket> udpSockets = SystemSockets.getUDPSockets();

        AddListElements(activeSockets, tcpSockets);
        AddListElements(activeSockets, udpSockets);

        System.out.println(activeSockets);

        ListView listView = (ListView) findViewById(R.id.list);
        ActiveConnectionListAdapter listAdapter = new ActiveConnectionListAdapter(this.context, activeSockets);
        listView.setAdapter(listAdapter);
        listView.setEmptyView(findViewById(R.id.empty));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, ((ActiveConnectionListElement) parent.getItemAtPosition(position)).getLabel());
                ActiveConnectionMapBottomSheetDialogFragment bottomSheetDialogFragment = new ActiveConnectionMapBottomSheetDialogFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                bottomSheetDialogFragment.setActiveConnectionListElement((ActiveConnectionListElement) parent.getItemAtPosition(position));
            }
        });

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.colorPrimary,
                R.color.colorAccent,
                R.color.textColorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), "refrescando noticias", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
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
