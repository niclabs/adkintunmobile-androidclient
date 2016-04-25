package cl.niclabs.adkintunmobile.views.connectiontype;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.chart.DoughnutChart;
import cl.niclabs.adkintunmobile.utils.chart.DoughnutChartBuilder;
import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;

public class ConnectionTypeFragment extends Fragment {

    private String title;
    private Context context;
    private DoughnutChart chart;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.title = getActivity().getString(R.string.view_connection_type);
        this.context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(this.title);
        view = inflater.inflate(R.layout.fragment_connection_type, container, false);
        DoughnutChart chartAux = (DoughnutChart) view.findViewById(R.id.doughnut);
        float chartDiameter = getResources().getDimension(
                R.dimen.connected_time_doughnut);
					/* create a Builder for our doughnut chart */
        DoughnutChartBuilder chartBuilder = new DoughnutChartBuilder(
                chartAux, chartDiameter);
        long currentTime = System.currentTimeMillis();
        StatisticInformation statistic = new DailyConnectionTypeInformation(context, currentTime, currentTime);
        chart = (DoughnutChart) chartBuilder
                .createGraphicStatistic(statistic);
        setChartVisible(chart);
        return view;
    }
    private void setChartVisible(DoughnutChart chart) {
        DoughnutChart doughnut = ((DoughnutChart) view.findViewById(R.id.doughnut));
        doughnut.setVisibility(View.GONE);
        Animation animationIn = AnimationUtils.loadAnimation(context,
                R.anim.fade_in);
        chart.setRotation(180);
        chart.draw();
        doughnut.setAnimation(animationIn);
        doughnut.setVisibility(View.VISIBLE);
    }

}
