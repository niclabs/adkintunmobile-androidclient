package cl.niclabs.adkintunmobile.data;

import android.content.Context;
import android.telephony.TelephonyManager;
import com.google.gson.annotations.SerializedName;

public class SimSingleton {
    private static SimSingleton mInstance;

    @SerializedName("mcc")
    private String mMcc;
    @SerializedName("mnc")
    private String mMnc;
    @SerializedName("carrier_id")
    private String mCarrier;
    @SerializedName("serial_number")
    private String mSerialNumber;

    public static SimSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SimSingleton(context);
        }
        return mInstance;
    }

    private SimSingleton(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        this.mCarrier = telephonyManager.getSimOperator();
        this.mSerialNumber = telephonyManager.getSimSerialNumber();

        this.mMcc = this.mCarrier.substring(0, 3);
        this.mMnc = this.mCarrier.substring(3);
    }

    public SimSingleton() {
    }
}