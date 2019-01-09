package cl.niclabs.adkintunmobile.commons;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import cl.niclabs.adkintunmobile.commons.utils.Scheduler;

import com.orm.SugarApp;

/**
 * Base class for NIC Labs Applications.
 *
 * It must be added as <code>android:name</code> in the manifest of application using the library
 * in order to use the data storage implemented in the library.
 *
 * Even though this class extends SugarApp, usage of the superclass methods is not recommended
 * since the underlying implementation of data storage may change in the future.
 *
 * <code>
 * <application android:label="@string/app_name" android:icon="@drawable/icon" android:name="cl.niclabs.adkintunmobile.commons.NicLabsApp">
 * 		<!-- Configures persistence status (enabled by default) -->
 * 		<meta-data android:name="PERSISTENT" android:value="false" />
 * </application>
 * </code>
 *
 * @author Felipe Lalanne <flalanne@niclabs.cl>
 */
public class NicLabsApp extends SugarApp {
    /**
     * Debugging status of the application
     */
    public static boolean DEBUG = true;

    /**
     * Library version
     */
    public static final String VERSION = "1.3.2b";

    /**
     * Define persistence state for the whole application
     */
    public static boolean PERSISTENCE_ENABLED = true;

    /**
     * Library settings file
     */
    public static final String LIBRARY_SETTINGS = "cl.niclabs.adkmobile.settings";

    /**
     * Application context, will be null if the application is not added to the manifest
     */
    private static NicLabsApp nicLabsContext;

    /**
     * Get application context (null if the application is not added in manifest)
     * @return
     */
    public static Context getContext() {
        return nicLabsContext;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // Shutdown the scheduler
        Scheduler.getInstance().shutdown();
    }

    /**
     * Get PERSISTENT key value from manifest metadata if available
     * @return
     */
    private boolean isPersistenceEnabled() {
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            return ai.metaData.getBoolean("PERSISTENT", PERSISTENCE_ENABLED);
        } catch (NameNotFoundException e) {
        }
        return PERSISTENCE_ENABLED;
    }

    /**
     * Get ADK_DEBUG ley from manifest metadata if available, otherwise use default
     * value in variable DEBUG
     * @return
     */
    private boolean inDebugEnabled() {
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            return ai.metaData.getBoolean("ADK_DEBUG", DEBUG);
        } catch (NameNotFoundException e) {
        }
        return DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PERSISTENCE_ENABLED = isPersistenceEnabled();
        DEBUG = inDebugEnabled();

        /* Set context (it will be null if application is not added to manifest_ */
        nicLabsContext = this;
    }


    /**
     *
     * @return true if persistence is available for this application
     */
    public static boolean isPersistenceAvailable() {
        return nicLabsContext != null && PERSISTENCE_ENABLED;
    }
}
