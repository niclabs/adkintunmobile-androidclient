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

# Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

# Do not skip public libraries
-dontskipnonpubliclibraryclasses

# Keep InnerClasses
-keepattributes Exceptions,InnerClasses

# Keep Exceptions
-keep public class cl.niclabs.adkintunmobile.utils.information.Connections.SystemSocketException{*;}

# Keep classes mentioned in the manifest
-keep public class cl.niclabs.adkintunmobile.adkmobile.AdkintunMobileApp{*;}
-keep public interface cl.niclabs.adkmobile.monitor.events.MonitorEvent{*;}

# Keep services
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.Connectivity{*;}
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.Telephony{*;}
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.Traffic{*;}

# Keep application services
-keep public class cl.niclabs.adkintunmobile.services.SetupSystem{*;}
-keep public class cl.niclabs.adkintunmobile.services.monitors.ConnectivityMonitor{*;}
-keep public class cl.niclabs.adkintunmobile.services.monitors.TelephonyMonitor{*;}
-keep public class cl.niclabs.adkintunmobile.services.monitors.TrafficMonitor{*;}
-keep public class cl.niclabs.adkintunmobile.services.sync.DispatcherDataBroadcastReceiver{*;}
-keep public class cl.niclabs.adkintunmobile.services.sync.Synchronization{*;}
-keep public class cl.niclabs.adkintunmobile.services.sync.SynchronizationBroadcastReceiver{*;}
-keep public class cl.niclabs.adkintunmobile.services.notifications.DailyNotificationBroadcastReceiver{*;}

# Keep descriptor classes
-keep public interface cl.niclabs.adkintunmobile.adkmobile.monitor.listeners.MonitorListener{*;}
-keep public interface cl.niclabs.adkintunmobile.adkmobile.monitor.data.Observation{*;}
-keep public class com.orm.SugarRecord{*;}

# Adkintun Middleware Library
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.data.TelephonyObservation{*;}
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.data.ApplicationTrafficSummary{*;}
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.data.NeighborAntenna{*;}
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.data.Sample{*;}
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.data.AccelerometerObservation{*;}
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.data.ClockSynchronizationState{*;}
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.data.ConnectivityObservation{*;}
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.data.LocationObservation{*;}
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.data.StateChange{*;}
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.data.TrafficObservation{*;}
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.data.CdmaObservation{*;}
-keep public class cl.niclabs.adkintunmobile.adkmobile.monitor.data.GsmObservation{*;}

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
-keep public class cl.niclabs.adkintunmobile.data.persistent.activemeasurement.ActiveMeasurement{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.activemeasurement.ConnectivityTestReport{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.activemeasurement.MediaTestReport{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.activemeasurement.NetworkInterface{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.activemeasurement.SiteResult{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.activemeasurement.SpeedTestReport{*;}
-keep public class cl.niclabs.adkintunmobile.data.persistent.activemeasurement.VideoResult{*;}

# ActiveMeasurements
-keep public class cl.niclabs.adkintunmobile.utils.activemeasurements{*;}
-keep public class cl.niclabs.adkintunmobile.utils.activemeasurements.mediatest{*;}
-keep public class cl.niclabs.adkintunmobile.utils.activemeasurements.mediatest.MediaTest{*;}
-keep public class cl.niclabs.adkintunmobile.utils.activemeasurements.mediatest.MediaTestJavascriptInterface{*;}
-keep public class cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest{*;}
-keep public class cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest{*;}


# Do not obfuscate serializable or persistent fields
#-keepclassmembers class * extends cl.niclabs.adkintunmobile.data.persistent {
#    private <fields>;
#}

# Enums
-keep public enum cl.niclabs.adkintunmobile.adkmobile.monitor.data.constants.NetworkType{*;}
-keep public enum cl.niclabs.adkintunmobile.adkmobile.monitor.data.constants.SimState{*;}
-keep public enum cl.niclabs.adkintunmobile.adkmobile.monitor.data.constants.NetworkState{*;}
-keep public enum cl.niclabs.adkintunmobile.adkmobile.monitor.data.constants.ConnectionType{*;}
-keep public enum cl.niclabs.adkintunmobile.adkmobile.monitor.data.constants.TelephonyStandard{*;}
-keep public enum cl.niclabs.adkintunmobile.adkintunmobile.utils.information.Connections.Connections.Type{*;}


# Remove logging
#-assumenosideeffects class android.util.Log {
#    public static boolean isLoggable(java.lang.String, int);
#    public static int v(...);
#    public static int i(...);
#    public static int w(...);
#    public static int d(...);
#    public static int e(...);
#}
-assumenosideeffects class android.util.Log { *; }