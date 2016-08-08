package cl.niclabs.adkintunmobile.views.connectiontype;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

public class TimelineViewFragment extends ConnectionTypeViewFragment{

    protected RecyclerView timeLineView;
    protected TimelineAdapter timelineAdapter;

    protected TextView dayText, dateText;

    // Responsabilidad de la herencia
    // Métodos a implementar de NewConnectionTypeViewFragment
    @Override
    public void updateView(DailyConnectionTypeInformation statistic) {
        this.timelineAdapter.updateData(statistic.getSamples());
        DisplayDateManager dateManager = new DisplayDateManager(context);
        dateManager.refreshDate(this.dayText, this.dateText, statistic.initialTime);

        TextView tvEmpty = (TextView) getView().findViewById(R.id.empty);
        if (this.timelineAdapter.getItemCount() == 0){
            tvEmpty.setVisibility(View.VISIBLE);
        }else{
            tvEmpty.setVisibility(View.GONE);
        }
    }


    /* Android Lifecycle */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_view, container, false);
        setUpTimeline(view);
        this.dayText = (TextView) view.findViewById(R.id.tv_timeline_date_day);
        this.dateText = (TextView) view.findViewById(R.id.tv_timeline_date_full);

        Typeface tf1 = Typeface.createFromAsset(context.getAssets(),
                context.getString(R.string.font_text_view));
        this.dayText.setTypeface(tf1);
        this.dateText.setTypeface(tf1);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getContext();
    }


    // Herramientas
    public void setUpTimeline(View view) {
        this.timeLineView = (RecyclerView) view.findViewById(R.id.timeline);
        this.timeLineView.setHasFixedSize(true);
        this.timeLineView.setLayoutManager(new LinearLayoutManager(this.context));
        this.timelineAdapter = new TimelineAdapter(this.context);
        this.timeLineView.setAdapter(this.timelineAdapter);
    }
}
