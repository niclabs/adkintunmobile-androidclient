package cl.niclabs.adkintunmobile.utils.information;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.Locale;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionModeSample;

public class Network {

    static public final String  NOTAVAILABLE = "Sin red";


    /**
     * 2G, 3G, 4G, No Disponible
     * @param context
     * @return
     */
    static public String getNetworkType(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return Network.NOTAVAILABLE;
        }
    }


    /**
     * Retorna el tipo actual de la conexión
     * @param context
     * @return ConnectionModeSample.MOBILE, ConnectionModeSample.WIFI o ConnectionModeSample.NONE
     */
    static public int getActiveNetwork(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        if (activeNetwork == null){
            return ConnectionModeSample.NONE;
        }else{
            switch (activeNetwork.getType()){
                case ConnectivityManager.TYPE_MOBILE:
                    return ConnectionModeSample.MOBILE;
                case ConnectivityManager.TYPE_WIFI:
                    return ConnectionModeSample.WIFI;
                default:
                    return ConnectionModeSample.NONE;
            }
        }
    }

    // retorna arreglo con 3 strings:
    // 0.- grande: ssid o tipo red
    // 1.- chico: connected o subtipo de red
    static public String[] getConnectionDetails(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        String[] ret = new String[2];

        if(activeNetwork == null){
            ret[0] = context.getString(R.string.view_connection_mode_disconnected);
            ret[1] = Network.NOTAVAILABLE;
        }else if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
            ret[0] = Network.getNetworkType(context);
            ret[1] = activeNetwork.getSubtypeName();
        }else {

            ret[0] = activeNetwork.getExtraInfo();
            ret[0] = ret[0]==null ? activeNetwork.getTypeName() : ret[0].replace("\"","");
            ret[1] = activeNetwork.getState().name();
        }
        return ret;
    }


    /**
     * Int resource de operador al que estamos conectados
     * @param context
     * @return
     */
    static public int getConnectedCarrierIntRes(Context context){
        String operator = Network.getConnectedCarrrier(context).toLowerCase();
        return Network.getIntRes(operator, context);
    }

    /**
     * Int resource de la sim actual
     * @param context
     * @return
     */
    static public int getSIMIntRes(Context context){
        String operator = Network.getSimCarrier(context).toLowerCase();
        return Network.getIntRes(operator, context);
    }

    static private int getIntRes(String operator, Context context) {
        String[] operators = context.getResources().getStringArray(R.array.mobile_operators);
        int i;
        for (i = 0; i < operators.length; ++i){
            if (operator.toLowerCase().contains(operators[i]))
                break;
        }
        switch (i){
            case 0:
                return R.mipmap.operator_claro;
            case 1:
                return R.mipmap.operator_entel;
            case 2:
                return R.mipmap.operator_falabella;
            case 3:
                return R.mipmap.operator_gtd;
            case 4:
                return R.mipmap.operator_movistar;
            case 5:
                return R.mipmap.operator_nextel;
            case 6:
                return R.mipmap.operator_virgin;
            case 7:
                return R.mipmap.operator_vtr;
            case 8:
                return R.mipmap.operator_wom;
            default:
                return R.mipmap.operator_other;
        }
    }


    /**
     * Nombre del operador de nuestra SIM
     * @param context
     * @return
     */
    static public String getSimCarrier(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        String res = mTelephonyManager.getSimOperatorName();
        return res.isEmpty() ? context.getString(R.string.view_status_no_sim) : res.toLowerCase();
    }

    /**
     * Nombre de operador al que estamos conectados
     * @param context
     * @return
     */
    static public String getConnectedCarrrier(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        String res = mTelephonyManager.getNetworkOperatorName();
        return res.isEmpty() ? context.getString(R.string.view_status_no_antenna) : res.toLowerCase();
    }

    /**
     * Entrega string con tamaño de bytes formateado en human readable
     * @param bytes
     * @return
     */
    static public String formatBytes(long bytes) {
        int unit = 1000;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = ("kMGTPE").charAt(exp - 1) + "";
        return String.format(Locale.getDefault(), "%.1f %sB",
                bytes / Math.pow(unit, exp), pre);
    }
}
