package cl.niclabs.adkintunmobile.views.notificationlog;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

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

        /*
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll( Arrays.asList(planets) );
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.item_notification_log, R.id.tv_title, planetList);
        listAdapter.add( "Ceres" );
        listAdapter.add( "Pluto" );
        listAdapter.add( "Haumea" );
        listAdapter.add( "Makemake" );
        listAdapter.add("Eris");
        mListView.setAdapter(listAdapter );
        */

        ArrayList<NotificationListElement> dataArray = new ArrayList<NotificationListElement>();
        dataArray.add(new NotificationListElement("Notificación 1", "Esta es la primera de las notificaciones y es muy bacan", 1463430466L));
        dataArray.add(new NotificationListElement("Notificación 2", "Esta es la segunda y es la más vieja", 1463430416L));
        dataArray.add(new NotificationListElement("Notificación 3", "Esta es la tercera debe ser la más alta", 1463430469L));

        NotificationListAdapter mNotificationListAdapter = new NotificationListAdapter(this, dataArray);

        mListView.setAdapter(mNotificationListAdapter);
        mListView.setEmptyView(findViewById(R.id.empty));
        mNotificationListAdapter.sort();

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.colorPrimary,
                R.color.colorAccent,
                R.color.textColorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), "refreseado", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public class NotificationListElement{

        private String title;
        private String content;
        private long timestamp;
        private boolean isReaded;

        public NotificationListElement(String title, String content, long timestamp) {
            this.title = title;
            this.content = content;
            this.timestamp = timestamp;
            Random r = new Random();
            this.isReaded = r.nextBoolean();
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getDate(){
            return DisplayDateManager.getDateString(this.getTimestamp());
        }

        public long getTimestamp() {
            return timestamp;
        }

        public boolean isReaded() {
            return isReaded;
        }
    }

    public class NotificationListAdapter extends ArrayAdapter<NotificationListElement>{
        private class ViewHolder {
            TextView tvTitle, tvContent, tvDate;
        }

        public NotificationListAdapter(Context context, ArrayList<NotificationListElement> notifications){
            super(context, R.layout.item_notification_log, notifications);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            NotificationListElement element = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            if (convertView == null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_notification_log, parent, false);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Populate the data into the template view using the data object
            viewHolder.tvTitle.setText(element.getTitle());
            if(element.isReaded()){
                viewHolder.tvTitle.setTypeface(null, Typeface.BOLD);
            }
            viewHolder.tvContent.setText(element.getContent());
            viewHolder.tvDate.setText(element.getDate());

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public void sort(){
            this.sort(new Comparator<NotificationListElement>() {
                @Override
                public int compare(NotificationListElement lhs, NotificationListElement rhs) {
                    long res = lhs.getTimestamp() - rhs.getTimestamp();
                    if(res == 0)
                        return 0;
                    else
                        return res > 0 ? -1 : 1;
                }
            });
            this.notifyDataSetChanged();
        }
    }
}
