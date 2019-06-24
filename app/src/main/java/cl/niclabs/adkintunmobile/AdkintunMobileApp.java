package cl.niclabs.adkintunmobile;

import com.orm.SugarApp;
import com.orm.SugarContext;


public class AdkintunMobileApp extends SugarApp {
    /**
     * Library version
     */
    public static final String VERSION = "1.3.3b";

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}