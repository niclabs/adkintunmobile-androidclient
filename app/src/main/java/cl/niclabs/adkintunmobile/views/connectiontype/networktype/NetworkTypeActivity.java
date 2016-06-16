package cl.niclabs.adkintunmobile.views.connectiontype.networktype;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.facebook.shimmer.ShimmerFrameLayout;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;
import cl.niclabs.adkintunmobile.utils.display.DoughnutChart;
import cl.niclabs.adkintunmobile.views.connectiontype.ConnectionTypeActivity;

public class NetworkTypeActivity extends ConnectionTypeActivity {

    private final String TAG = "AdkM:NetworkTypeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_type);
        setBaseActivityParams();
        setUpToolbar();
        //setUpDoughnutChart();
    }

    public void setBaseActivityParams() {
        this.title = getString(R.string.view_network_type);
        this.context = this;
        this.loadingPanel = (RelativeLayout) findViewById(R.id.loading_panel);
        ShimmerFrameLayout container =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        container.startShimmerAnimation();
    }

    @Override
    public void loadData(long initialTime) {
        long currentTime = System.currentTimeMillis();
        StatisticInformation statistic = new DailyNetworkTypeInformation(context, initialTime, currentTime);
        statistic.setStatisticsInformation();
        this.chart = (DoughnutChart) this.chartBuilder.createGraphicStatistic(statistic);
        this.timeByType = statistic.getTimeByType();
    }

    @Override
    public void refreshLegend(long initialTime){
        TypedArray icons = context.getResources().obtainTypedArray(R.array.network_type_legend_icons);
        TypedArray colors = context.getResources().obtainTypedArray(R.array.network_type_legend_colors_soft);
        setNewLegend(icons, colors);
    }
}
