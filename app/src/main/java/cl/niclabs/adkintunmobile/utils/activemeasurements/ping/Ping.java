package cl.niclabs.adkintunmobile.utils.activemeasurements.ping;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;


public class Ping {
    private String min = "MIN";
    private String average = "AVERAGE";
    private String max = "MAX";
    private int timeOutMillis;
    private int ttl;
    private int count;
    private Map<String, Object> result;

    public Ping(int timeOutMillis, int ttl, int count) {
        this.timeOutMillis = timeOutMillis;
        this.ttl = ttl;
        this.count = count;
    }

    public Map<String, Object> doPing(String address) throws IOException, InterruptedException {

        StringBuilder echo = new StringBuilder();
        Runtime runtime = Runtime.getRuntime();

        int timeoutSeconds = Math.max(timeOutMillis / 1000, 1);
        int ttl = Math.max(this.ttl, 1);

        String pingCommand = "ping";

        InetAddress addr = InetAddress.getByName(address);
        Log.i("IS REACHABLE PING", Boolean.toString(addr.isReachable(1000)));

        Process proc = runtime.exec("/system/bin/" + pingCommand + " -c 1 -W " + timeoutSeconds + " -t " + ttl + " " + address);
        proc.waitFor();
        int exit = proc.exitValue();
        Log.i("ping command", pingCommand + " -c 1 -W " + timeoutSeconds + " " + address);
        switch (exit) {
            case 0:
                InputStreamReader reader = new InputStreamReader(proc.getInputStream());
                BufferedReader buffer = new BufferedReader(reader);
                String line;
                while ((line = buffer.readLine()) != null) {
                    echo.append(line).append("\n");
                }
                return getPingStats(echo.toString());
            case 1:
                Log.e("Ping Error","failed, exit = 1");
                break;
            default:
                Log.e("Ping Error", "error, exit = 2");
                break;
        }
        return null;
    }

    public Map<String, Object> getPingStats(String s) {
        Map<String, Object> map = new HashMap<>();
        if (s.contains("0% packet loss")) {
            int start = s.indexOf("/mdev = ");
            int end = s.indexOf(" ms\n", start);
            if (start == -1 || end == -1) {
                return null;
            } else {
                s = s.substring(start + 8, end);
                String stats[] = s.split("/");
                map.put(this.min, stats[0]);
                map.put(this.average, stats[1]);
                map.put(this.max, stats[2]);
                //map.put(this.min, Float.parseFloat(stats[0]));
                //map.put(this.average, Float.parseFloat(stats[1]));
                //map.put(this.max, Float.parseFloat(stats[2]));
                Log.i("Ping result", stats[0]);
            }
        } else if (s.contains("100% packet loss")) {
            Log.e("Ping Package Loss","100% packet loss");
        } else if (s.contains("% packet loss")) {
            Log.e("Ping Package Loss","partial packet loss");
        } else if (s.contains("unknown host")) {
            Log.e("Ping Package Loss","unknown host");
        } else {
            Log.e("Ping Package Loss","unknown error");
        }
        this.result = map;
        return map;
    }

    public Map<String, Object> getResult() {
        return result;
    }
 }
