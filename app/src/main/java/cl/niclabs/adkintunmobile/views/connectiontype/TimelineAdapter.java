package cl.niclabs.adkintunmobile.views.connectiontype;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vipul.hp_hp.timelineview.TimelineView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionModeSample;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionTypeSample;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NetworkTypeSample;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>{
    private long queriedTimestamp;
    private List<ConnectionTypeSample> mFeedList;
    Context context;

    protected static class TimelineViewHolder extends RecyclerView.ViewHolder{
        TextView initTime;
        TextView duration;
        TimelineView timelineMarker;

        public TimelineViewHolder(View itemView) {
            super(itemView);
            initTime = (TextView) itemView.findViewById(R.id.tv_timeline_init_time);
            duration = (TextView) itemView.findViewById(R.id.tv_timeline_duration);
            timelineMarker = (TimelineView) itemView.findViewById(R.id.time_marker);


        }
    }

    public TimelineAdapter(Context context){
        this.context = context;
    }

    public void updateData(DailyConnectionTypeInformation statistic) {
        queriedTimestamp = statistic.initialTime;
        mFeedList = statistic.getSamples();
        notifyDataSetChanged();
    }

    @Override
    public TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        return new TimelineViewHolder(view);

    }

    @Override
    public void onBindViewHolder(TimelineViewHolder holder, int position) {
        Typeface tf1 = Typeface.createFromAsset(context.getAssets(),
                context.getString(R.string.font_text_view));

        ConnectionTypeSample connectionTypeSample = mFeedList.get(position);

        holder.initTime.setText(DisplayDateManager.getDateString(connectionTypeSample.getInitialTime()));
        holder.initTime.setTypeface(tf1);

        String durationText = "";
        long millis;

        if (position + 1 < mFeedList.size()){
            ConnectionTypeSample connectionNextTypeSample = mFeedList.get(position+1);
            millis = connectionNextTypeSample.getInitialTime() - connectionTypeSample.getInitialTime();
        }else{
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(queriedTimestamp);
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);

            // Este es el último registro y debe completar:

            // hasta:
            //  -El tiempo actual si se consultó el día de hoy
            //  -El final del día , si se consultó un día previo

            if (mCalendar.getTimeInMillis() > System.currentTimeMillis()){
                // se consultó el día de hoy
                millis = System.currentTimeMillis();

            } else {
                // se consultó un día previo
                millis = mCalendar.getTimeInMillis();
            }

            // desde:
            //  -las 0 Horas, si dicho registro es de un día previo al consultado
            //  -el getInitialTime() del registro, si es un registro del día consultado
            mCalendar.add(Calendar.DAY_OF_MONTH, -1);
            if (connectionTypeSample.getInitialTime() < mCalendar.getTimeInMillis()){
                millis -= mCalendar.getTimeInMillis();
            } else {
                millis -= connectionTypeSample.getInitialTime();
            }

        }

        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 25;

        if (hour > 0)
            durationText += String.format(" %d Hr.", hour);
        if (minute > 0)
            durationText += String.format(" %d Min.", minute);
        if (second > 0)
            durationText += String.format(" %d Seg.", second);
        if ( (hour+minute+second) == 0)
            durationText += "< 1 Seg.";

        holder.duration.setText(durationText);

        if (connectionTypeSample instanceof NetworkTypeSample){
            TypedArray icons = context.getResources().obtainTypedArray(R.array.network_type_legend_icons);
            holder.timelineMarker.setMarker(icons.getDrawable(connectionTypeSample.getType()));
            icons.recycle();
        }
        else if (connectionTypeSample instanceof ConnectionModeSample){
            TypedArray icons = context.getResources().obtainTypedArray(R.array.connection_mode_legend_icons);
            holder.timelineMarker.setMarker(icons.getDrawable(connectionTypeSample.getType()));
            icons.recycle();
        }
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null ? mFeedList.size() : 0);
    }

}
