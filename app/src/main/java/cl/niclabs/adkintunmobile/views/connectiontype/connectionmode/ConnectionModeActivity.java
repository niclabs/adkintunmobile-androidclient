package cl.niclabs.adkintunmobile.views.connectiontype.connectionmode;

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
import cl.niclabs.adkintunmobile.utils.display.DoughnutChart;
import cl.niclabs.adkintunmobile.utils.display.ShowCaseTutorial;
import cl.niclabs.adkintunmobile.views.connectiontype.ConnectionTypeActivity;

public class ConnectionModeActivity extends ConnectionTypeActivity {

    private final String TAG = "AdkM:ConnectionModeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_mode);
        setBaseActivityParams();
        setUpToolbar();
        setUpTimeLine();
        //setUpDoughnutChart();
    }

    public void setBaseActivityParams() {
        this.title = getString(R.string.view_connection_mode);
        this.context = this;
        this.loadingPanel = (RelativeLayout) findViewById(R.id.loading_panel);
        ShimmerFrameLayout container =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        container.startShimmerAnimation();
    }

    @Override
    public void loadData(long initialTime) {
        long currentTime = System.currentTimeMillis();
        DailyConnectionModeInformation statistic = new DailyConnectionModeInformation(context, initialTime, currentTime);
        statistic.setStatisticsInformation();
        this.chart = (DoughnutChart) this.chartBuilder.createGraphicStatistic(statistic);
        this.timeByType = statistic.getTimeByType();

        timelineAdapter.updateData(statistic.getSamples());
    }

    @Override
    public void refreshLegend(long initialTime){
        TypedArray icons = context.getResources().obtainTypedArray(R.array.connection_mode_legend_icons);
        TypedArray colors = context.getResources().obtainTypedArray(R.array.connection_mode_legend_colors_soft);
        setNewLegend(icons, colors);
    }

    @Override
    public TypedArray getSaturatedColors(){
        return  context.getResources().obtainTypedArray(R.array.connection_mode_legend_colors);
    }

    @Override
    public TypedArray getSoftColors(){
        return context.getResources().obtainTypedArray(R.array.connection_mode_legend_colors_soft);
    }

    private int helpCounter;
    private ShowcaseView showcaseView;

    @Override
    public void showTutorial() {
        helpCounter = 0;
        final String[] tutorialTitle = getResources().getStringArray(R.array.tutorial_connection_mode_title);
        final String[] tutorialBody = getResources().getStringArray(R.array.tutorial_connection_mode_body);
        Target firstTarget = new ViewTarget(toolbar.getChildAt(0));
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpCounter++;
                Target mTarget;

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

                    default:
                        showcaseView.hide();
                        return;
                }
                showcaseView.setContentTitle(tutorialTitle[helpCounter]);
                showcaseView.setContentText(tutorialBody[helpCounter]);
                showcaseView.setShowcase(mTarget, true);
            }
        };

        showcaseView = ShowCaseTutorial.createViewTutorial(this, firstTarget, tutorialTitle, tutorialBody, onClickListener);
    }
}
