package cl.niclabs.adkintunmobile.utils.display;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import java.util.Calendar;
import java.util.TimeZone;

import cl.niclabs.adkintunmobile.R;

public class DisplayManager {

    // TODO: Pasar a Strings.xml
    static public String[] dayNames = {"", "Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};
    static public String[] monthNames = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    static public void enableLoadingPanel(RelativeLayout loadingPanel) {
        loadingPanel.setVisibility(View.VISIBLE);
    }

    static public void dismissLoadingPanel(RelativeLayout loadingPanel, Context context) {
        Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        loadingPanel.startAnimation(fadeOut);
        loadingPanel.setVisibility(View.GONE);
    }

    public static void makeDateDialog(Context context, DatePickerDialog.OnDateSetListener listener){
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                listener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }
}
