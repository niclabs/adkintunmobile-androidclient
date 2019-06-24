package cl.niclabs.adkintunmobile.utils.activemeasurements.ping;

import android.util.Log;

import java.io.IOException;
import java.net.*;


public class Ping {
    private static int TIMEOUT = 5000;

    public static boolean sendPingRequest(String ipAdress) throws UnknownHostException, IOException {
        boolean ping = false;
        try {
            InetAddress addr = InetAddress.getByName(ipAdress);
            ping = addr.isReachable(TIMEOUT);
        } catch (Exception e) {
            Log.e("Ping Error", e.getMessage());
        }
        return ping;
    }
}
