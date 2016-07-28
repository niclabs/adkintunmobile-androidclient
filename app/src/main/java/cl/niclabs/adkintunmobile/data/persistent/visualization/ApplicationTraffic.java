package cl.niclabs.adkintunmobile.data.persistent.visualization;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import cl.niclabs.adkintunmobile.data.persistent.TrafficObservationWrapper;
import cl.niclabs.android.data.Persistent;

public class ApplicationTraffic extends Persistent<ApplicationTraffic>{

    public final static int MOBILE = 1;
    public final static int WIFI = 6;

    @SerializedName("uid")
    public Integer uid;
    @SerializedName("timestamp")
    public long timestamp;
    @SerializedName("network_type")
    public int networkType;
    @SerializedName("rx_bytes")
    public long rxBytes;
    @SerializedName("tx_bytes")
    public long txBytes;
    @SerializedName("tcp_rx_bytes")
    public Long tcpRxBytes;
    @SerializedName("tcp_tx_bytes")
    public Long tcpTxBytes;

    public ApplicationTraffic() {

    }

    public ApplicationTraffic(TrafficObservationWrapper trafficObservationWrapper){
        // Actualiza forma de guardar, ahora los timestamp serán diarios
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(trafficObservationWrapper.timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Log.d("KEP", trafficObservationWrapper.uid+"");

        this.uid = trafficObservationWrapper.uid;
        //this.timestamp = trafficObservationWrapper.timestamp;
        this.timestamp = calendar.getTimeInMillis();
        this.networkType = trafficObservationWrapper.networkType;
        this.rxBytes = trafficObservationWrapper.rxBytes;
        this.txBytes = trafficObservationWrapper.txBytes;
        this.tcpRxBytes = trafficObservationWrapper.tcpRxBytes;
        this.tcpTxBytes = trafficObservationWrapper.tcpTxBytes;
    }

    /**
     * @return Arreglo con [rxData, txData] de datos móviles del mes actual
     */
    static public long[] getMonthlyMobileConsumption(){
        long[] dataUsage = new long[2];
        long rxMobile = 0L;
        long txMobile = 0L;

        Date today = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Iterator<ApplicationTraffic> iterator = ApplicationTraffic.findAsIterator(
                ApplicationTraffic.class, "network_type = ? and timestamp >= ?",
                Integer.toString(ApplicationTraffic.MOBILE),
                Long.toString(calendar.getTimeInMillis()));

        while (iterator.hasNext()){
            ApplicationTraffic current = iterator.next();
            rxMobile += current.rxBytes;
            txMobile += current.txBytes;
        }

        dataUsage[0] = rxMobile;
        dataUsage[1] = txMobile;

        return dataUsage;
    }

    /**
     *
     * @param appTrafficType Seleccionar ApplicationTraffic.MOBILE o ApplicationTraffic.WIFI
     * @param initialTimestamp Tiempo desde el cual recuperar datos
     * @return Arreglo con [DownloadedBytes, UploadedBytes]
     */
    public static long[] getTransferedData(int appTrafficType, long initialTimestamp){
        long rxData = 0, txData = 0;

        Iterator<ApplicationTraffic> iterator = findAsIterator(
                ApplicationTraffic.class, "network_type = ? and timestamp >= ?",
                Integer.toString(appTrafficType),
                Long.toString(initialTimestamp));

        while (iterator.hasNext()){
            ApplicationTraffic current = iterator.next();
            rxData += current.rxBytes;
            txData += current.txBytes;
        }

        long[] ret = new long[2];
        ret[0] = rxData;
        ret[1] = txData;
        return ret;
    }
}
