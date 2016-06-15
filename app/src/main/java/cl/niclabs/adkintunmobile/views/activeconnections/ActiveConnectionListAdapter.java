package cl.niclabs.adkintunmobile.views.activeconnections;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;

public class ActiveConnectionListAdapter extends ArrayAdapter<ActiveConnectionListElement>{

    private static class ViewHolder {
        TextView tvAppName;
        ImageView ivLogo;
        TextView tvConnTotal;
    }

    public ActiveConnectionListAdapter(Context context, ArrayList<ActiveConnectionListElement> apps) {
        super(context, R.layout.item_active_connection, apps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ActiveConnectionListElement element = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_active_connection, parent, false);
            viewHolder.tvAppName = (TextView) convertView.findViewById(R.id.tv_appname);
            viewHolder.ivLogo = (ImageView) convertView.findViewById(R.id.iv_applogo);
            viewHolder.tvConnTotal = (TextView) convertView.findViewById(R.id.tv_conn_total);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.tvAppName.setText(element.getLabel());
        viewHolder.ivLogo.setImageDrawable(element.getLogo());
        viewHolder.tvConnTotal.setText(element.getTotalActiveConnections() + "");

        // Return the completed view to render on screen
        return convertView;
    }


}
