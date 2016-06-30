package cl.niclabs.adkintunmobile.views.connectiontype.networktype;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

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

    @Override
    public TypedArray getSaturatedColors(){
        return  context.getResources().obtainTypedArray(R.array.network_type_legend_colors);
    }

    @Override
    public TypedArray getSoftColors(){
        return context.getResources().obtainTypedArray(R.array.network_type_legend_colors_soft);
    }

    private int helpCounter;
    private ShowcaseView showcaseView;

    @Override
    public void showTutorial() {
        helpCounter = 0;
        showcaseView = new ShowcaseView.Builder(this)
                .setTarget(Target.NONE)
                .setContentTitle(getString(R.string.view_network_type_tutorial_1_title))
                .setContentText(getString(R.string.view_network_type_tutorial_1_body))
                .setStyle(R.style.CustomShowcaseTheme)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (helpCounter) {
                            case 0:
                                Target mTarget0 = new ViewTarget(findViewById(R.id.layout_time));
                                showcaseView.setShowcase(mTarget0, true);
                                showcaseView.setContentTitle(getString(R.string.view_network_type_tutorial_2_title));
                                showcaseView.setContentText(getString(R.string.view_network_type_tutorial_2_body));
                                break;

                            case 1:
                                View mView = ((TableLayout) findViewById(R.id.time_info_table_layout)).getChildAt(0);
                                Target mTarget1 = new ViewTarget(((TableRow) mView).getChildAt(0));
                                showcaseView.setShowcase(mTarget1, true);
                                showcaseView.setContentTitle(getString(R.string.view_network_type_tutorial_3_title));
                                showcaseView.setContentText(getString(R.string.view_network_type_tutorial_3_body));
                                break;

                            case 2:
                                Target mTarget2 = new ViewTarget(findViewById(R.id.menu_date_picker_btn));
                                showcaseView.setShowcase(mTarget2, true);
                                showcaseView.setContentTitle(getString(R.string.view_network_type_tutorial_4_title));
                                showcaseView.setContentText(getString(R.string.view_network_type_tutorial_4_body));
                                showcaseView.setButtonText(getString(R.string.tutorial_close));
                                break;

                            case 3:
                                showcaseView.hide();
                                break;
                        }
                        helpCounter++;
                    }
                })
                .withNewStyleShowcase()
                .build();
        showcaseView.setButtonText(getString(R.string.tutorial_next));
    }
}
