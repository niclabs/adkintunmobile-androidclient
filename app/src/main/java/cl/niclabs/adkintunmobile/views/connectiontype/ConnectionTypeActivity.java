package cl.niclabs.adkintunmobile.views.connectiontype;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import java.util.Calendar;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.display.DisplayManager;

public abstract class ConnectionTypeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    protected String title;
    protected Context context;
    protected Toolbar toolbar;
    protected RelativeLayout loadingPanel;

    protected long timestampToQuery;

    protected DailyConnectionTypeInformation statistic;
    protected ConnectionTypeViewPagerAdapter mViewPagerAdapter;
    protected ViewPager mViewPager;

    /* ¿Qué debe implementar cada clase que extienda de ConnectionTypeActivity? */

    // 1.- Cómo cargar los datos para el día especificado
    protected abstract void loadData(long initialTime);

    // 2.- El tutorial
    protected abstract void showTutorial();

    // 3.- Seteo de elementos basicos de la vista
    protected abstract void setBaseActivityParams();

    // 4.- Preparar el ViewPager
    protected abstract void setUpViewPager();


    /* Android Lifecycle */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.timestampToQuery = System.currentTimeMillis();
    }


    /* Android UI */

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
            case R.id.menu_info_btn:
                showTutorial();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        timestampToQuery = c.getTimeInMillis();

        startUpdateUiThread();
    }

    /* Métodos provistos desde acá para construcción de la actividad */

    public void setUpToolbar(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(this.title);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void updateFragmentsAttached() {
        for (int i=0; i<this.mViewPagerAdapter.getCount(); i++){
            ConnectionTypeViewFragment mFragment = (ConnectionTypeViewFragment) this.mViewPagerAdapter.getItem(i);
            mFragment.updateView(this.statistic);
        }
    }

    protected void startUpdateUiThread(){
        DisplayManager.enableLoadingPanel(this.loadingPanel);
        (new Thread(){
            @Override
            public void run() {
                loadData(timestampToQuery);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateFragmentsAttached();
                        DisplayManager.dismissLoadingPanel(loadingPanel, context);
                    }
                });
            }
        }).start();
    }
}
