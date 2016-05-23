package cl.niclabs.adkintunmobile.views.connectiontype;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;
import cl.niclabs.adkintunmobile.utils.display.DisplayManager;
import cl.niclabs.adkintunmobile.utils.display.DoughnutChart;
import cl.niclabs.adkintunmobile.utils.display.DoughnutChartBuilder;

public abstract class ConnectionTypeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    protected String title;
    protected Context context;
    protected Toolbar toolbar;
    protected RelativeLayout loadingPanel;

    protected DoughnutChart chart;
    protected DoughnutChartBuilder chartBuilder;
    protected long[] timeByType;
    protected TextView dayText;
    protected TextView dateText;
    protected DisplayDateManager dateManager;

    public abstract void loadData(long initialTime);

    public abstract void refreshLegend(long initialTime);

    public TextView createLegendTextView(int icon, int color, String text){
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
        tv.setBackgroundColor(ContextCompat.getColor(this, color));
        int marginHorizontal = 0;
        int marginVertical = (int)getResources().getDimension(R.dimen.separation_little);
        tv.setPadding(marginHorizontal, marginVertical, marginHorizontal, marginVertical);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        return tv;
    }

    public void setNewLegend(TypedArray icons, TypedArray colors){
        TableLayout tableLayout = (TableLayout) findViewById(R.id.time_info_table_layout);
        tableLayout.removeAllViews();
        ArrayList<TimeLegend> timeLegend = new ArrayList<>();

        for (int i=0; i<timeByType.length; i++){
            long hours = timeByType[i]/(3600*1000);
            long minutes = (timeByType[i] - hours*3600*1000)/(60*1000);

            if (hours != 0 || minutes != 0){
                String legend = hours + " h " + minutes + " min";
                TextView legendTextView = createLegendTextView(icons.getResourceId(i,0), colors.getResourceId(i, 0), legend);
                timeLegend.add(new TimeLegend(legendTextView, timeByType[i]) );
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

        icons.recycle();
        colors.recycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.connection_type, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_date_picker_btn:
                DisplayManager.makeDateDialog(context, this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUpDoughnutChart(){
        dayText = (TextView) findViewById(R.id.text_day);
        dateText = (TextView) findViewById(R.id.text_date);
        dateManager = new DisplayDateManager(context);

        Typeface tf1 = Typeface.createFromAsset(context.getAssets(),
                getString(R.string.font_text_view));
        dayText.setTypeface(tf1);
        dateText.setTypeface(tf1);

        DoughnutChart chartElement = (DoughnutChart) findViewById(R.id.doughnut);

        float chartDiameter = getResources().getDimension(
                R.dimen.connected_time_doughnut);
        this.chartBuilder = new DoughnutChartBuilder(chartElement, chartDiameter);
        chartElement.setRotation(180f);

        // Cargar datos de tipo de conexion de las últimas 24 horas
        (new Thread(){
            @Override
            public void run() {
                final long currentTime = System.currentTimeMillis();

                loadData(currentTime);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLegend(currentTime);
                        dateManager.refreshDate(dayText, dateText, currentTime);
                        chart.draw();
                        DisplayManager.dismissLoadingPanel(loadingPanel, context);
                    }
                });
            }
        }).start();
    }

    public void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(this.title);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        final long initTime = c.getTimeInMillis();

        if (initTime > System.currentTimeMillis()){
            Toast.makeText(this.context, getString(R.string.view_connection_mode_bad_date), Toast.LENGTH_LONG).show();
            return;
        }

        DisplayManager.enableLoadingPanel(this.loadingPanel);
        (new Thread(){
            @Override
            public void run() {
                // Acá la lógica de recuperar datos para el día seleccionado en initTime
                loadData(initTime);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLegend(initTime);
                        //Acá la lógica para modificar el donutchart con los nuevos datos
                        dateManager.refreshDate(dayText, dateText, initTime);
                        chart.draw();
                        DisplayManager.dismissLoadingPanel(loadingPanel, context);
                    }
                });
            }
        }).start();
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
