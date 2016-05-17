package cl.niclabs.adkintunmobile.views.connectiontype;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    protected TextView dayText;
    protected TextView dateText;
    protected DisplayDateManager dateManager;

    public abstract void loadData(long initialTime);

    public abstract void refreshLegend(long initialTime);

    public TextView createLegendTextView(int icon, int color){
        TextView tv = new TextView(this);
        tv.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
        tv.setBackgroundColor(ContextCompat.getColor(this, color) );
        return tv;
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

}
