package cl.niclabs.adkintunmobile.data.persistent;

import android.content.Context;
import android.telephony.TelephonyManager;
import com.google.gson.annotations.SerializedName;
import cl.niclabs.android.data.Persistent;

public class SimSingleton extends Persistent<SimSingleton> {
    private static SimSingleton mInstance;

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
        mCarrier = telephonyManager.getSimOperator();
        mSerialNumber = telephonyManager.getSimSerialNumber();
    }

    public String getmSerialNumber() {
        return mSerialNumber;
    }

    public void setmSerialNumber(String mSerialNumber) {
        this.mSerialNumber = mSerialNumber;
    }

    public String getmCarrier() {
        return mCarrier;
    }

    public void setmCarrier(String mCarrier) {
        this.mCarrier = mCarrier;
    }

    public SimSingleton() {
    }
}