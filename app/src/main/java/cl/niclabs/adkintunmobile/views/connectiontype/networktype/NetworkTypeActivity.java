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
        final String[] tutorialTitle = getResources().getStringArray(R.array.tutorial_network_type_title);
        final String[] tutorialBody = getResources().getStringArray(R.array.tutorial_network_type_body);

        showcaseView = new ShowcaseView.Builder(this)
                .setTarget(Target.NONE)
                .setContentTitle(tutorialTitle[helpCounter])
                .setContentText(tutorialBody[helpCounter])
                .setStyle(R.style.CustomShowcaseTheme)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helpCounter++;
                        Target mTarget = Target.NONE;

                        switch (helpCounter) {
                            case 1:
                                mTarget = new ViewTarget(findViewById(R.id.layout_time));
                                break;

                            case 2:
                                View mView = ((TableLayout) findViewById(R.id.time_info_table_layout)).getChildAt(0);
                                mTarget = new ViewTarget(((TableRow) mView).getChildAt(0));
                                break;

                            case 3:
                                mTarget = new ViewTarget(findViewById(R.id.menu_date_picker_btn));
                                showcaseView.setButtonText(getString(R.string.tutorial_close));
                                break;

                            case 4:
                                showcaseView.hide();
                                return;
                        }
                        showcaseView.setContentTitle(tutorialTitle[helpCounter]);
                        showcaseView.setContentText(tutorialBody[helpCounter]);
                        showcaseView.setShowcase(mTarget, true);
                    }
                })
                .withNewStyleShowcase()
                .build();
        showcaseView.setButtonText(getString(R.string.tutorial_next));
    }
}
