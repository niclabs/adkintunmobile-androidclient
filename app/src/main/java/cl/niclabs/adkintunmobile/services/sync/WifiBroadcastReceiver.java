package cl.niclabs.adkintunmobile.services.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cl.niclabs.adkintunmobile.utils.volley.VolleySingleton;

public class WifiBroadcastReceiver extends BroadcastReceiver{
    public void onReceive(Context context, Intent intent) {
        VolleySingleton.getInstance(context).getRequestQueue().start();
    }
}
