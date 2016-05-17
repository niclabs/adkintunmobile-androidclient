package cl.niclabs.adkintunmobile.views.notificationlog;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

import cl.niclabs.adkintunmobile.R;

public class NotificationLogListAdapter extends ArrayAdapter<NotificationLogListElement> {

    private class ViewHolder {
        TextView tvTitle, tvContent, tvDate;
    }

    public NotificationLogListAdapter(Context context, ArrayList<NotificationLogListElement> notifications){
        super(context, R.layout.item_notification_log, notifications);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NotificationLogListElement element = getItem(position);
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
        if(element.isMark()){
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
        this.sort(new Comparator<NotificationLogListElement>() {
            @Override
            public int compare(NotificationLogListElement lhs, NotificationLogListElement rhs) {
                long res = lhs.getTimestamp() - rhs.getTimestamp();
                if (res == 0)
                    return 0;
                else
                    return res > 0 ? -1 : 1;
            }
        });
        this.notifyDataSetChanged();
    }
}
