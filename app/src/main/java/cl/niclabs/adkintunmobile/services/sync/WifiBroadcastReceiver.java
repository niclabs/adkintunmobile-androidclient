package cl.niclabs.adkintunmobile.services.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import cl.niclabs.adkintunmobile.utils.volley.VolleySingleton;

public class WifiBroadcastReceiver extends BroadcastReceiver{
    public void onReceive(Context context, Intent intent) {
        NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

        if (netInfo != null && netInfo.isConnected()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            VolleySingleton.getInstance(context).getRequestQueue().start();
        }
    }
}
