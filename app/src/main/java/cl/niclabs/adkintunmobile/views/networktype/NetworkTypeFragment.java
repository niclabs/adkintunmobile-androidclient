package cl.niclabs.adkintunmobile.views.networktype;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NetworkTypeSample;
import cl.niclabs.adkintunmobile.utils.display.DigitalClock;
import cl.niclabs.adkintunmobile.utils.display.DisplayManager;
import cl.niclabs.adkintunmobile.utils.display.DoughnutChart;
import cl.niclabs.adkintunmobile.utils.display.DoughnutChartBuilder;
import cl.niclabs.adkintunmobile.views.connectiontype.DailyConnectionTypeInformation;
import cl.niclabs.android.utils.Time;

public class NetworkTypeFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private final String TAG = "AdkM:AppNetwTypeFragment";

    private String title;
    private Context context;

    private DoughnutChart chart;
    private RelativeLayout loadingPanel;
    private StatisticInformation statistic;
    private DoughnutChartBuilder chartBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.title = getActivity().getString(R.string.view_network_type);
        this.context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(this.title);
        final View view = inflater.inflate(R.layout.fragment_network_type, container, false);
        this.loadingPanel = (RelativeLayout) view.findViewById(R.id.loading_panel);

        DigitalClock digitalClock = (DigitalClock) view.findViewById(R.id.digital_clock);
        TextView dayText = (TextView) view.findViewById(R.id.text_day);
        Typeface tf1 = Typeface.createFromAsset(context.getAssets(),
                getString(R.string.font_text_view));
        digitalClock.setTypeface(tf1);
        dayText.setTypeface(tf1);

        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String[] dayOfWeek = getResources().getStringArray(R.array.day_of_week);

		/* set text view to show the name of the day of week */
        dayText.setText(dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1]);


        setHasOptionsMenu(true);

        DoughnutChart chartElement = (DoughnutChart) view.findViewById(R.id.doughnut);

        ImageView imageClock = (ImageView) view.findViewById(R.id.image_clock);

        float chartDiameter = getResources().getDimension(
                R.dimen.connected_time_doughnut);
					/* create a Builder for our doughnut chart */
        this.chartBuilder = new DoughnutChartBuilder(chartElement, chartDiameter);


        // Cargar datos de tipo de conexion de las últimas 24 horas
        (new Thread(){
            @Override
            public void run() {
                final long currentTime = System.currentTimeMillis();

                loadConnectionTypeData(currentTime);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        setChartVisible(chart);
                        DisplayManager.dismissLoadingPanel(loadingPanel, context);
                    }
                });

            }
        }).start();
        return view;
    }

    private void loadConnectionTypeData(long initialTime) {
        long currentTime = System.currentTimeMillis();
        this.statistic = new DailyNetworkTypeInformation(context, initialTime, currentTime);
        this.chart = (DoughnutChart) this.chartBuilder.createGraphicStatistic(statistic);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.network_type, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_date_picker_btn:
                makeDateDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setChartVisible(DoughnutChart chart) {
        //DoughnutChart doughnut = ((DoughnutChart) view.findViewById(R.id.doughnut));
        //doughnut.setVisibility(View.GONE);
        //Animation animationIn = AnimationUtils.loadAnimation(context,
        //        R.anim.fade_in);
        chart.setRotation(0);
        chart.draw();
        //doughnut.setAnimation(animationIn);
        //doughnut.setVisibility(View.VISIBLE);
    }

    private void makeDateDialog(){
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this.context,
                this,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
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
                loadConnectionTypeData(initTime);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Acá la lógica para modificar el donutchart con los nuevos datos
                        setChartVisible(chart);
                        DisplayManager.dismissLoadingPanel(loadingPanel, context);
                    }
                });
            }
        }).start();


    }

}