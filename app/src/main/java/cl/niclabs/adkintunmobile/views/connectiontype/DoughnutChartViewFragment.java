package cl.niclabs.adkintunmobile.views.connectiontype;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;
import cl.niclabs.adkintunmobile.utils.display.DoughnutChart;
import cl.niclabs.adkintunmobile.utils.display.DoughnutChartBuilder;

public abstract class DoughnutChartViewFragment extends ConnectionTypeViewFragment {

    protected DoughnutChart chart;
    protected DoughnutChartBuilder chartBuilder;
    protected long[] timeByType;
    protected TextView dayText;
    protected TextView dateText;
    protected HashMap<Integer, Integer> colorHashMap;
    protected DisplayDateManager dateManager;


    /* ¿Qué debe implementar cada clase que extienda de NewDonutchartViewFragment? */

    // 1.- Cómo actualizar la leyenda
    public abstract void refreshLegend();

    // 2.- Cómo obtener los colores saturados
    public abstract TypedArray getSaturatedColors();

    // 3.- Cómo obtener los colores suaves
    public abstract TypedArray getSoftColors();


    // Responsabilidad de la herencia
    // Métodos a implementar de NewConnectionTypeViewFragment
    @Override
    public void updateView(DailyConnectionTypeInformation statistic) {

        setUpDoughnutChart();
        this.chart = (DoughnutChart) this.chartBuilder.createGraphicStatistic(statistic);
        this.timeByType = statistic.getTimeByType();

        refreshLegend();
        dateManager.refreshDate(dayText, dateText, statistic.initialTime);
        chart.draw();
        setUpHashMap();
    }

    /* Android Lifecycle */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getContext();
    }


    // Utilitarios

    public DoughnutChartViewFragment() {
    }

    private void setUpDoughnutChart() {
        //TODO: getView() puede dar null si se sale rápido de la vista
        View mView = getView();
        if (mView != null){
            dayText = (TextView) mView.findViewById(R.id.text_day);
            dateText = (TextView) mView.findViewById(R.id.text_date);
            dateManager = new DisplayDateManager(context);

            Typeface tf1 = Typeface.createFromAsset(context.getAssets(),
                    getString(R.string.font_text_view));
            dayText.setTypeface(tf1);
            dateText.setTypeface(tf1);

            DoughnutChart chartElement = (DoughnutChart) mView.findViewById(R.id.doughnut);

            float chartDiameter = getResources().getDimension(
                    R.dimen.connected_time_doughnut);
            this.chartBuilder = new DoughnutChartBuilder(chartElement, chartDiameter);
            chartElement.setRotation(180f);
        }
    }

    private void setUpHashMap() {
        TypedArray saturatedColors = getSaturatedColors();
        TypedArray softColors = getSoftColors();

        colorHashMap = new HashMap<>();
        colorHashMap.put(ContextCompat.getColor(context, R.color.doughnut_no_info),
                ContextCompat.getColor(context, R.color.doughnut_no_info));
        colorHashMap.put(ContextCompat.getColor(context, R.color.doughnut_start),
                ContextCompat.getColor(context, R.color.doughnut_start));
        for (int i = 0; i < saturatedColors.length(); i++) {
            colorHashMap.put(ContextCompat.getColor(context, saturatedColors.getResourceId(i, 0)),
                    (ContextCompat.getColor(context, R.color.doughnut_no_info)));
        }
    }

    public TextView createLegendTextView(int icon, int color, String text) {
        TextView tv = new TextView(this.context);
        tv.setText(text);
        tv.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
        tv.setBackgroundColor(ContextCompat.getColor(this.context, color));
        int marginHorizontal = 0;
        int marginVertical = (int) getResources().getDimension(R.dimen.separation_little);
        tv.setPadding(marginHorizontal, marginVertical, marginHorizontal, marginVertical);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        Typeface tf1 = Typeface.createFromAsset(context.getAssets(),
                getString(R.string.font_text_view));
        tv.setTypeface(tf1);
        tv.setTextColor(ContextCompat.getColor(context, android.R.color.white));

        return tv;
    }

    public void setNewLegend(TypedArray icons, TypedArray colors) {
        TableLayout tableLayout = (TableLayout) getView().findViewById(R.id.time_info_table_layout);
        tableLayout.removeAllViews();
        ArrayList<TimeLegend> timeLegend = new ArrayList<>();

        for (int i = 0; i < timeByType.length; i++) {
            long hours = timeByType[i] / (3600 * 1000);
            long minutes = (timeByType[i] - hours * 3600 * 1000) / (60 * 1000);

            if (hours != 0 || minutes != 0) {
                String legend = "";
                legend += hours > 0 ? +hours + " Hr. " : "";
                legend += minutes > 0 ? minutes + " Min." : "";
                TextView legendTextView = createLegendTextView(icons.getResourceId(i, 0), colors.getResourceId(i, 0), legend);
                legendTextView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_DOWN:
                                int typeColor = ((ColorDrawable) v.getBackground()).getColor();
                                TypedArray saturatedColors = getSaturatedColors();
                                TypedArray softColors = getSoftColors();
                                int index;

                                for (index = 0; index < softColors.length(); index++) {
                                    if (typeColor == ContextCompat.getColor(context, softColors.getResourceId(index, 0))) {
                                        typeColor = ContextCompat.getColor(context, saturatedColors.getResourceId(index, 0));
                                        //v.setBackgroundColor(typeColor);
                                        break;
                                    }
                                }
                                ArrayList<Integer> chartColors = new ArrayList<>();
                                ArrayList<Integer> newColors = chart.getColors();
                                for (int i = 0; i < newColors.size(); i++) {
                                    chartColors.add(newColors.get(i));

                                    if (newColors.get(i) != typeColor) {
                                        int newColor = colorHashMap.get(newColors.get(i));
                                        newColors.remove(i);
                                        newColors.add(i, newColor);
                                    }
                                }
                                chart.setColors(newColors);
                                chart.draw();
                                chart.setColors(chartColors);
                                return true;
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                chart.draw();
                                return true;

                        }
                        return false;
                    }
                });
                timeLegend.add(new TimeLegend(legendTextView, timeByType[i]));
            }
        }
        Collections.sort(timeLegend, new Comparator<TimeLegend>() {
            @Override
            public int compare(TimeLegend lhs, TimeLegend rhs) {
                return (int) (rhs.getTotalTime() - lhs.getTotalTime());
            }
        });

        int rowWidth = 3;
        if (timeLegend.size() == 4 || timeLegend.size() == 5) {
            rowWidth = 2;
        }
        TableRow tableRow = new TableRow(this.context);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < timeLegend.size(); i++) {
            tableRow.addView(timeLegend.get(i).getLegendTextView(), new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            if (tableRow.getChildCount() == rowWidth) {
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                tableRow = new TableRow(this.context);
                tableRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                if (rowWidth == 2)
                    ++rowWidth;
            }
        }
        if (!timeLegend.isEmpty())
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        else {
            TextView empty = new TextView(this.context);
            empty.setGravity(Gravity.CENTER_HORIZONTAL);
            empty.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.Large));
            empty.setText(R.string.view_applications_traffic_item_no_data);
            empty.setTextColor(ContextCompat.getColor(this.context, R.color.textColorPrimary));
            tableRow.addView(empty, new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.setHorizontalGravity(View.TEXT_ALIGNMENT_CENTER);
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        }

        icons.recycle();
        colors.recycle();
    }

    protected class TimeLegend{
        private TextView legendTextView;
        private long totalTime;


        public TimeLegend(TextView legendTextView, long totalTime) {
            this.legendTextView = legendTextView;
            this.totalTime = totalTime;
        }

        public TextView getLegendTextView() {
            return legendTextView;
        }

        public long getTotalTime() {
            return totalTime;
        }
    }
}
