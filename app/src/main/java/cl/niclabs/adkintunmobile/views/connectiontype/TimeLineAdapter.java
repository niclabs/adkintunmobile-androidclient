package cl.niclabs.adkintunmobile.views.connectiontype;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vipul.hp_hp.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.List;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionTypeSample;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NetworkTypeSample;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>{
    private List<ConnectionTypeSample> mFeedList;
    Context context;

    protected static class TimeLineViewHolder extends RecyclerView.ViewHolder{
        TextView initTime;
        TextView endTime;
        TimelineView timeLineMarker;

        public TimeLineViewHolder(View itemView) {
            super(itemView);
            initTime = (TextView) itemView.findViewById(R.id.tv_timeline_init_time);
            endTime = (TextView) itemView.findViewById(R.id.tv_timeline_end_time);
            timeLineMarker = (TimelineView) itemView.findViewById(R.id.time_marker);
        }
    }

    public TimeLineAdapter(Context context){
        this.context = context;
    }

    public void updateData(ArrayList<ConnectionTypeSample> samples) {
        mFeedList = samples;
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        return new TimeLineViewHolder(view);

    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        ConnectionTypeSample connectionTypeSample = mFeedList.get(position);
        holder.initTime.setText(DisplayDateManager.getDateString(connectionTypeSample.getInitialTime()));
        if (connectionTypeSample instanceof NetworkTypeSample){
            TypedArray icons = context.getResources().obtainTypedArray(R.array.network_type_legend_icons);

            holder.timeLineMarker.setMarker(icons.getDrawable(connectionTypeSample.getType()));
        }
        //TODO completar tipo
        //TODO completar endtime
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null ? mFeedList.size() : 0);
    }

}
