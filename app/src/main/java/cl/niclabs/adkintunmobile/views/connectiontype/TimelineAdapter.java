package cl.niclabs.adkintunmobile.views.connectiontype;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vipul.hp_hp.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.List;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionModeSample;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionTypeSample;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NetworkTypeSample;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>{
    private List<ConnectionTypeSample> mFeedList;
    Context context;

    protected static class TimelineViewHolder extends RecyclerView.ViewHolder{
        TextView initTime;
        TextView endTime;
        TimelineView timelineMarker;

        public TimelineViewHolder(View itemView) {
            super(itemView);
            initTime = (TextView) itemView.findViewById(R.id.tv_timeline_init_time);
            endTime = (TextView) itemView.findViewById(R.id.tv_timeline_end_time);
            timelineMarker = (TimelineView) itemView.findViewById(R.id.time_marker);
        }
    }

    public TimelineAdapter(Context context){
        this.context = context;
    }

    public void updateData(ArrayList<ConnectionTypeSample> samples) {
        mFeedList = samples;
    }

    @Override
    public TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        return new TimelineViewHolder(view);

    }

    @Override
    public void onBindViewHolder(TimelineViewHolder holder, int position) {
        ConnectionTypeSample connectionTypeSample = mFeedList.get(position);
        holder.initTime.setText(DisplayDateManager.getDateString(connectionTypeSample.getInitialTime()));
        if (connectionTypeSample instanceof NetworkTypeSample){
            TypedArray icons = context.getResources().obtainTypedArray(R.array.network_type_legend_icons);
            holder.timelineMarker.setMarker(icons.getDrawable(connectionTypeSample.getType()));
        }
        else if (connectionTypeSample instanceof ConnectionModeSample){
            TypedArray icons = context.getResources().obtainTypedArray(R.array.connection_mode_legend_icons);
            holder.timelineMarker.setMarker(icons.getDrawable(connectionTypeSample.getType()));
        }
        //TODO completar endtime
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null ? mFeedList.size() : 0);
    }

}
