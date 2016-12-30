package cl.niclabs.adkintunmobile.views.connectiontype;

import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class ConnectionTypeViewFragment extends Fragment {

    protected Context context;
    protected String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /* ¿Qué debe implementar cada clase que extienda de NewConnectionTypeViewFragment? */

    // 1.- Cómo actualizar su vista
    public abstract void updateView(DailyConnectionTypeInformation statistic);
}
