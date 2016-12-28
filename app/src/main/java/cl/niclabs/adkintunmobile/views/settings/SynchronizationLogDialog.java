package cl.niclabs.adkintunmobile.views.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NewsNotification;

public class SynchronizationLogDialog extends DialogFragment{

    static public final String TAG = "AdkM:SynchronizationLogDialog";

    private ListView mListView;

    public SynchronizationLogDialog() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_synchronization_log_dialog, container, false);
        getDialog().setTitle("Log de sincronización");

        // Get visual elements
        this.mListView = (ListView)view.findViewById(R.id.list);

        String[] where = {NewsNotification.SYNC_LOG + ""};

        Iterator<NewsNotification> iterator = NewsNotification.findAsIterator(
                NewsNotification.class,
                "type = ?",
                where,
                null,
                "timestamp DESC",
                "100");
        ArrayList<NewsNotification> log = new ArrayList<NewsNotification>();

        while (iterator.hasNext()){
            log.add(iterator.next());
        }

        SynchronizationLogAdapter adapter = new SynchronizationLogAdapter(getContext(), log);

        mListView.setAdapter(adapter);

        return view;
    }

    static public void showDialog(FragmentManager fm){
        SynchronizationLogDialog dialog = new SynchronizationLogDialog();
        dialog.show(fm, TAG);
    }




    public class SynchronizationLogAdapter extends ArrayAdapter<NewsNotification>{

        private class ViewHolder{
            TextView date, status;
        }

        public SynchronizationLogAdapter(Context context, ArrayList<NewsNotification> log){
            super(context, R.layout.item_synchronization_log, log);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            NewsNotification notification = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder;
            if (convertView == null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_synchronization_log, parent, false);
                viewHolder.date = (TextView) convertView.findViewById(R.id.tv_date);
                viewHolder.status = (TextView) convertView.findViewById(R.id.tv_status);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // Populate the data into the template view using the data object
            viewHolder.date.setText(notification.title);
            viewHolder.status.setText(notification.content);
            // return
            return convertView;
        }
    }
}
