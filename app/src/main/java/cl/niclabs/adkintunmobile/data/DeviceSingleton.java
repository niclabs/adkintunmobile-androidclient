package cl.niclabs.adkintunmobile.data;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import com.google.gson.annotations.SerializedName;

public class DeviceSingleton {
    private static DeviceSingleton mInstance;

    @SerializedName("device_id")
    private String mDeviceId;
    @SerializedName("board")
    private String mBoard;
    @SerializedName("brand")
    private String mBrand;
    @SerializedName("device")
    private String mDevice;
    @SerializedName("build_id")
    private String mBuildId;
    @SerializedName("hardware")
    private String mHardware;
    @SerializedName("manufacturer")
    private String mManufacturer;
    @SerializedName("model")
    private String mModel;
    @SerializedName("product")
    private String mProduct;
    @SerializedName("release")
    private String mRelease;
    @SerializedName("release_type")
    private String mReleaseType;
    @SerializedName("sdk")
    private Integer mSdk;

    public static DeviceSingleton getInstance(Context context) {
        if (mInstance == null){
            mInstance = new DeviceSingleton(context);
        }
        return mInstance;
    }

    private DeviceSingleton(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mDeviceId = telephonyManager.getDeviceId();
        mBoard = Build.BOARD;
        mBrand = Build.BRAND;
        mDevice = Build.DEVICE;
        mBuildId = Build.DISPLAY;
        mHardware = Build.HARDWARE;
        mManufacturer = Build.MANUFACTURER;
        mModel = Build.MODEL;
        mProduct = Build.PRODUCT;
        mRelease = Build.VERSION.RELEASE;
        mReleaseType = Build.TYPE;
        mSdk = Build.VERSION.SDK_INT;
    }

    public DeviceSingleton() {
    }
}
