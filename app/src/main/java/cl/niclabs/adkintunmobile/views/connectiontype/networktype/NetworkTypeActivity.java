package cl.niclabs.adkintunmobile.views.connectiontype.networktype;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;
import cl.niclabs.adkintunmobile.data.persistent.visualization.DailyNetworkTypeSummary;
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
        setUpDoughnutChart();
    }

    public void setBaseActivityParams() {
        this.title = getString(R.string.view_network_type);
        this.context = this;
        this.loadingPanel = (RelativeLayout) findViewById(R.id.loading_panel);
    }

    @Override
    public void loadData(long initialTime) {
        long currentTime = System.currentTimeMillis();
        StatisticInformation statistic = new DailyNetworkTypeInformation(context, initialTime, currentTime);
        this.chart = (DoughnutChart) this.chartBuilder.createGraphicStatistic(statistic);
    }

    @Override
    public void refreshLegend(long initialTime){
        int[] icons = {R.drawable.ic_10_nored,
                R.drawable.ic_04_g,
                R.drawable.ic_05_edge,
                R.drawable.ic_06_3g,
                R.drawable.ic_07_h,
                R.drawable.ic_08_hp,
                R.drawable.ic_09_4g};

        int[] colors = {R.color.network_type_unknown_soft,
                R.color.network_type_G_soft,
                R.color.network_type_E_soft,
                R.color.network_type_3G_soft,
                R.color.network_type_H_soft,
                R.color.network_type_Hp_soft,
                R.color.network_type_4G_soft};

        TableLayout tableLayout = (TableLayout) findViewById(R.id.time_info_table_layout);
        tableLayout.removeAllViews();

        long [] totalTimeByType = DailyNetworkTypeSummary.getTimeByTypeSummary(initialTime);

        ArrayList<TimeLegend> timeLegend = new ArrayList<>();

        for (int i=0; i<7; i++){
            long hours = totalTimeByType[i]/(1000*3600);
            long minutes = (totalTimeByType[i] - hours*1000*3600)/(1000*60);

            if (hours != 0 || minutes != 0){
                String leyend = hours + " h " + minutes + " min";
                TextView legendTextView = createLegendTextView(icons[i], colors[i], leyend);
                timeLegend.add(new TimeLegend(legendTextView, totalTimeByType[i]));
            }
        }

        Collections.sort(timeLegend, new Comparator<TimeLegend>() {
            @Override
            public int compare(TimeLegend lhs, TimeLegend rhs) {
                return (int) (rhs.getTotalTime() - lhs.getTotalTime());
            }
        });

        int rowWidth = 3;
        if (timeLegend.size() == 4){
            rowWidth = 2;
        }
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        for (int i=0; i<timeLegend.size(); i++){
            tableRow.addView( timeLegend.get(i).getLegendTextView() );
            if (tableRow.getChildCount() == rowWidth){
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
            }
        }

        if (!timeLegend.isEmpty())
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
    }
}
