# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/diego/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-dontobfuscate

# Do not skip public libraries
-dontskipnonpubliclibraryclasses

# Keep InnerClasses
-keepattributes Exceptions,InnerClasses

# Keep classes mentioned in the manifest
-keep public class cl.niclabs.adkmobile.AdkintunMobileApp{*;}
-keep public class cl.niclabs.adkmobile.monitor.Connectivity{*;}
-keep public class cl.niclabs.adkmobile.monitor.Telephony{*;}
-keep public class cl.niclabs.adkmobile.monitor.Traffic{*;}

# Keep application services
-keep public class cl.niclabs.adkintunmobile.services.SetupSystem{*;}
-keep public class cl.niclabs.adkintunmobile.services.monitors.ConnectivityMonitor{*;}
-keep public class cl.niclabs.adkintunmobile.services.monitors.TelephonyMonitor{*;}
-keep public class cl.niclabs.adkintunmobile.services.monitors.TrafficMonitor{*;}
-keep public class cl.niclabs.adkintunmobile.services.sync.DispatcherDataBroadcastReceiver{*;}
-keep public class cl.niclabs.adkintunmobile.services.sync.Synchronization{*;}
-keep public class cl.niclabs.adkintunmobile.services.sync.SynchronizationBroadcastReceiver{*;}

# Keep descriptor classes
-keep public interface cl.niclabs.adkmobile.monitor.listeners.MonitorListener{*;}
-keep public interface cl.niclabs.adkmobile.monitor.data.Observation{*;}
-keep public class com.orm.SugarRecord{*;}

# Adkintun Middleware Library
-keep public class cl.niclabs.adkmobile.monitor.data.TelephonyObservation{*;}
-keep public class cl.niclabs.adkmobile.monitor.data.ApplicationTrafficSummary{*;}
-keep public class cl.niclabs.adkmobile.monitor.data.NeighborAntenna{*;}
-keep public class cl.niclabs.adkmobile.monitor.data.Sample{*;}
-keep public class cl.niclabs.adkmobile.monitor.data.AccelerometerObservation{*;}
-keep public class cl.niclabs.adkmobile.monitor.data.ClockSynchronizationState{*;}
-keep public class cl.niclabs.adkmobile.monitor.data.ConnectivityObservation{*;}
-keep public class cl.niclabs.adkmobile.monitor.data.LocationObservation{*;}
-keep public class cl.niclabs.adkmobile.monitor.data.StateChange{*;}
-keep public class cl.niclabs.adkmobile.monitor.data.TrafficObservation{*;}
-keep public class cl.niclabs.adkmobile.monitor.data.CdmaObservation{*;}
-keep public class cl.niclabs.adkmobile.monitor.data.GsmObservation{*;}

# Client persistent clases
-keep public class cl.niclabs.adkintunmobile.data.persistent{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.CdmaObservationWrapper{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.ConnectivityObservationWrapper{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.GsmObservationWrapper{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.IpLocation{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.NeighborAntennaWrapper{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.SampleWrapper{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.StateChangeWrapper{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.TelephonyObservationWrapper{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.TrafficObservationWrapper{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionModeSample{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionTypeSample{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.visualization.DailyConnectionModeSummary{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.visualization.DailyConnectionTypeSummary{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.visualization.DailyNetworkTypeSummary{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.visualization.NetworkTypeSample{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.visualization.NewsNotification{*;}


# Do not obfuscate serializable or persistent fields
#-keepclassmembers class * extends cl.niclabs.adkintunmobile.data.persistent {
#    private <fields>;
#}

# Enums
-keep public enum cl.niclabs.adkmobile.monitor.data.constants.NetworkType{*;}
-keep public enum cl.niclabs.adkmobile.monitor.data.constants.SimState{*;}
-keep public enum cl.niclabs.adkmobile.monitor.data.constants.NetworkState{*;}
-keep public enum cl.niclabs.adkmobile.monitor.data.constants.ConnectionType{*;}
-keep public enum cl.niclabs.adkmobile.monitor.data.constants.TelephonyStandard{*;}
-keep public enum cl.niclabs.adkintunmobile.utils.information.SystemSockets.Type{*;}


# Remove logging
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
    public static *** d(...);
}