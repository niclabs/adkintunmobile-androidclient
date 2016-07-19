package cl.niclabs.adkintunmobile.views.activeconnections;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.List;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.display.DisplayManager;
import cl.niclabs.adkintunmobile.utils.display.ShowCaseTutorial;
import cl.niclabs.adkintunmobile.utils.information.SystemSocket;
import cl.niclabs.adkintunmobile.utils.information.SystemSockets;

public class ActiveConnectionsActivity extends AppCompatActivity {

    private final String TAG = "AdkM:ActiveConnActivity";

    private String title;
    private Context context;
    private Toolbar toolbar;
    private RelativeLayout loadingPanel;

    private ArrayList<ActiveConnectionListElement> activeSockets;
    private ActiveConnectionListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_connections);

        setBaseActivityParams();
        setupToolbar();

        // Cargar datos disponibles
        DisplayManager.enableLoadingPanel(this.loadingPanel);
        (new Thread(){
            @Override
            public void run() {

                // Recuperar datos
                activeSockets = new ArrayList<ActiveConnectionListElement>();
                AddListElements(activeSockets, SystemSockets.getTCPSockets());
                AddListElements(activeSockets, SystemSockets.getUDPSockets());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initList();
                        DisplayManager.dismissLoadingPanel(loadingPanel, context);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        ListView listView = (ListView) findViewById(R.id.list);
        listAdapter = new ActiveConnectionListAdapter(this.context, activeSockets);
        listView.setAdapter(listAdapter);
        listView.setEmptyView(findViewById(R.id.empty));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.setEnabled(false);
                Log.d(TAG, ((ActiveConnectionListElement) parent.getItemAtPosition(position)).getLabel());
                ActiveConnectionMapBottomSheetDialogFragment bottomSheetDialogFragment = new ActiveConnectionMapBottomSheetDialogFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                bottomSheetDialogFragment.setActiveConnectionListElement((ActiveConnectionListElement) parent.getItemAtPosition(position));
                bottomSheetDialogFragment.setParentToEnable(parent);
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
                updateActiveConnections();
                Toast.makeText(getApplicationContext(), getString(R.string.view_active_connections_reload), Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void updateActiveConnections() {
        this.activeSockets.clear();
        AddListElements(activeSockets, SystemSockets.getTCPSockets());
        AddListElements(activeSockets, SystemSockets.getUDPSockets());
        this.listAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.active_connections, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_info_btn:
                showTutorial();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    private ShowcaseView showcaseView;
    private int helpCounter;

    private void showTutorial() {
        helpCounter = 0;
        final String[] tutorialTitle = getResources().getStringArray(R.array.tutorial_active_connections_title);
        final String[] tutorialBody = getResources().getStringArray(R.array.tutorial_active_connections_body);
        Target firstTarget = new ViewTarget(toolbar.getChildAt(0));
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpCounter++;
                Target mTarget;

                switch (helpCounter) {
                    case 1:
                        ListView mListView = (ListView)findViewById(R.id.list);
                        if (mListView.getChildAt(0) != null) {
                            mTarget = new ViewTarget(((ViewGroup) mListView.getChildAt(0)).getChildAt(0));
                            showcaseView.setButtonText(getString(R.string.tutorial_close));
                            break;
                        } else
                            helpCounter++;

                    default:
                        showcaseView.hide();
                        return;
                }
                showcaseView.setContentTitle(tutorialTitle[helpCounter]);
                showcaseView.setContentText(tutorialBody[helpCounter]);
                showcaseView.setShowcase(mTarget, true);
            }
        };

        showcaseView = ShowCaseTutorial.createViewTutorial(this, firstTarget, tutorialTitle, tutorialBody, onClickListener);
    }
}
