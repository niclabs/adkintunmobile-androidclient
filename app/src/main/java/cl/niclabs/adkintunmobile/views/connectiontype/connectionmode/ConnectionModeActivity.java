package cl.niclabs.adkintunmobile.views.connectiontype.connectionmode;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.RelativeLayout;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;
import cl.niclabs.adkintunmobile.utils.display.DoughnutChart;
import cl.niclabs.adkintunmobile.views.connectiontype.ConnectionTypeActivity;

public class ConnectionModeActivity extends ConnectionTypeActivity {

    private final String TAG = "AdkM:ConnectionModeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_mode);
        setBaseActivityParams();
        setUpToolbar();
        setUpDoughnutChart();
    }

    public void setBaseActivityParams() {
        this.title = getString(R.string.view_connection_mode);
        this.context = this;
        this.loadingPanel = (RelativeLayout) findViewById(R.id.loading_panel);
    }

    @Override
    public void loadData(long initialTime) {
        long currentTime = System.currentTimeMillis();
        StatisticInformation statistic = new DailyConnectionModeInformation(context, initialTime, currentTime);
        statistic.setStatisticsInformation();
        this.chart = (DoughnutChart) this.chartBuilder.createGraphicStatistic(statistic);
        this.timeByType = statistic.getTimeByType();
    }

    @Override
    public void refreshLegend(long initialTime){
        TypedArray icons = context.getResources().obtainTypedArray(R.array.connection_mode_legend_icons);
        TypedArray colors = context.getResources().obtainTypedArray(R.array.connection_mode_legend_colors_soft);
        setNewLegend(icons, colors);
    }
}
