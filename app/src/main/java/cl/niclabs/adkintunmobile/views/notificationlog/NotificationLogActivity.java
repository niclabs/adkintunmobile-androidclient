package cl.niclabs.adkintunmobile.views.notificationlog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NewsNotification;

public class NotificationLogActivity extends AppCompatActivity {

    private final String TAG = "AdkM:AppTrafficActivity";

    private String title;
    private Context context;
    private Toolbar toolbar;
    private RelativeLayout loadingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_log);

        setBaseActivityParams();
        setupToolbar();
        setupListView();
    }

    public void setBaseActivityParams(){
        this.title = getString(R.string.view_notifications_log);
        this.context = this;
        this.loadingPanel = (RelativeLayout) findViewById(R.id.loading_panel);
    }

    public void setupToolbar(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(this.title);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void setupListView(){
        ListView mListView = (ListView) findViewById(R.id.list_notif);

        ArrayList<NotificationLogListElement> dataArray = new ArrayList<NotificationLogListElement>();
        //dataArray.add(new NotificationLogListElement("Notificación 1", "Esta es la primera de las notificaciones y es muy bacan, debería tener una cantidad de texto considerable para que se oculte automágicamente", 1463430466L));
        //dataArray.add(new NotificationLogListElement("Notificación 2", "Esta es la segunda y es la más vieja", 1463430416L));
        //dataArray.add(new NotificationLogListElement("Notificación 3", "Esta es la tercera debe ser la más alta, también tiene harto texto, debería poner un lorem impsum lara", 1463430469L));
        //dataArray.add(new NotificationLogListElement("Notificación 4", "Lejos l amás alta de toddasssssss texto, debería poner un lorem impsum lara", 1463498942358L));


        long valor = NewsNotification.count(NewsNotification.class, null, null);

        // Populate el data array
        for(NewsNotification n : NewsNotification.listAll(NewsNotification.class)){
            dataArray.add(new NotificationLogListElement(n));
        }


        NotificationLogListAdapter mNotificationLogListAdapter = new NotificationLogListAdapter(this, dataArray);

        mListView.setAdapter(mNotificationLogListAdapter);
        mListView.setEmptyView(findViewById(R.id.empty));
        mNotificationLogListAdapter.sort();

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
}
