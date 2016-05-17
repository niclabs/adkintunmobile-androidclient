package cl.niclabs.adkintunmobile.views.connectiontype.connectionmode;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;
import cl.niclabs.adkintunmobile.data.persistent.visualization.DailyConnectionModeSummary;
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
        this.chart = (DoughnutChart) this.chartBuilder.createGraphicStatistic(statistic);
    }

    @Override
    public void refreshLegend(long initialTime){
        int[] icons = {R.drawable.ic_01_wifi,
                R.drawable.ic_02_mobile,
                R.drawable.ic_03_nowifi};

        int[] colors = {R.color.doughnut_wifi,
                R.color.doughnut_mobile,
                R.color.doughnut_no_connection};

        TableLayout tableLayout = (TableLayout) findViewById(R.id.time_info_table_layout);
        tableLayout.removeAllViews();

        long [] totalTimeByType = DailyConnectionModeSummary.getTimeByTypeSummary(initialTime);
        long aux = totalTimeByType[2];
        totalTimeByType[2] = totalTimeByType[0];
        totalTimeByType[0] = aux;

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        for (int i=0; i<3; i++){
            long hours = totalTimeByType[i]/(1000*3600);
            long minutes = (totalTimeByType[i] - hours*1000*3600)/(1000*60);

            if (hours != 0 || minutes != 0){
                TextView legendTextView = createLegendTextView(icons[i], colors[i]);
                legendTextView.setText(hours + " Horas, " + minutes + " Min.");
                tableRow.addView(legendTextView);
            }
        }
        tableLayout.addView(tableRow, new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
    }
}
