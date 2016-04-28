package cl.niclabs.adkintunmobile.views.applicationstraffic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.information.Network;


/**
 * Basado en https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */
public class ApplicationsTrafficListAdapter extends ArrayAdapter<ApplicationsTrafficListElement> {

    private static class ViewHolder {
        TextView tvAppName;
        ImageView ivLogo;
        TextView tvRxBytes, tvTxBytes;
        ProgressBar pbRxBytes, pbTxBytes;
    }

    public ApplicationsTrafficListAdapter(Context context, ArrayList<ApplicationsTrafficListElement> apps) {
        super(context, R.layout.item_application_traffic, apps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ApplicationsTrafficListElement element = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_application_traffic, parent, false);
            viewHolder.tvAppName = (TextView) convertView.findViewById(R.id.tv_appname);
            viewHolder.ivLogo = (ImageView) convertView.findViewById(R.id.iv_applogo);
            viewHolder.tvRxBytes = (TextView) convertView.findViewById(R.id.tv_rxbytes);
            viewHolder.tvTxBytes = (TextView) convertView.findViewById(R.id.tv_txbytes);
            viewHolder.pbRxBytes = (ProgressBar) convertView.findViewById(R.id.pb_rx);
            viewHolder.pbTxBytes = (ProgressBar) convertView.findViewById(R.id.pb_tx);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.tvAppName.setText(element.getLabel());
        viewHolder.ivLogo.setImageDrawable(element.getLogo());
        viewHolder.tvRxBytes.setText(Network.formatBytes(element.getRxBytes()));
        viewHolder.tvTxBytes.setText(Network.formatBytes(element.getTxBytes()));

        int txPercentage = Math.round( (element.getTxBytes() * 100) / countTotalData());
        int rxPercentage = Math.round( (element.getRxBytes() * 100) / countTotalData());

        viewHolder.pbTxBytes.setProgress(txPercentage);
        viewHolder.pbRxBytes.setProgress(rxPercentage);

        // Return the completed view to render on screen
        return convertView;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void sort(){
        this.sort(new Comparator<ApplicationsTrafficListElement>() {
            @Override
            public int compare(ApplicationsTrafficListElement lhs, ApplicationsTrafficListElement rhs) {
                return rhs.getRxBytes().compareTo(lhs.getRxBytes());
            }
        });
        this.notifyDataSetChanged();
    }

    private Long countTotalData(){
        Long total = 1L;
        Long totalDownLoad = 0L;
        Long totalUpload = 0L;
        for (int i = 0; i < getCount(); i++){
            ApplicationsTrafficListElement element = getItem(i);
            totalDownLoad += element.getRxBytes();
            totalUpload += element.getTxBytes();
        }
        total = Math.max(total, Math.max(totalDownLoad, totalUpload));
        return total;
    }

}
