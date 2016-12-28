package cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.SpeedTestReport;
import cl.niclabs.adkintunmobile.views.activemeasurements.SpeedTestDialog;
import fr.bmartel.speedtest.SpeedTestMode;

public class SpeedTest {
    private SpeedTestDialog testDialog;
    private int downloadSize, uploadSize;
    private String serverHost;
    private String serverPort;
    private AsyncTask currentTask;
    SpeedTestReport report;

    public SpeedTest(SpeedTestDialog testDialog) {
        this.testDialog = testDialog;
        report = new SpeedTestReport();
        report.setUpReport(testDialog.getContext());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(testDialog.getContext());
        String downloadSizeValue = sharedPreferences.getString(testDialog.getString(R.string.settings_speed_test_download_size_key), "1000000");
        String uploadSizeValue = sharedPreferences.getString(testDialog.getString(R.string.settings_speed_test_upload_size_key), "1000000");
        serverHost = sharedPreferences.getString(testDialog.getString(R.string.settings_speed_test_server_host_key), "");
        serverPort = sharedPreferences.getString(testDialog.getString(R.string.settings_speed_test_server_port_key), "");
        downloadSize = Integer.parseInt(downloadSizeValue);
        uploadSize = Integer.parseInt(uploadSizeValue);

        report.host = serverHost;
        report.downloadSize = downloadSize;
        report.uploadSize = uploadSize;
    }

    public void start(){
        startSpeedTest(SpeedTestMode.DOWNLOAD, serverHost, serverPort, downloadSize);
    }

    private void startSpeedTest(SpeedTestMode mode, String host, String port, int fileOctetSize) {
        currentTask = new SpeedTestTask(this, host, port, fileOctetSize).execute(mode);
    }

    protected void onSpeedTestTaskFinish(SpeedTestMode mode, fr.bmartel.speedtest.SpeedTestReport finalReport) {
        switch (mode){
            case DOWNLOAD:
                report.downloadSpeed = finalReport.getTransferRateBit();
                report.elapsedDownloadTime = finalReport.getReportTime() - finalReport.getStartTime();
                startSpeedTest(SpeedTestMode.UPLOAD, serverHost, serverPort, uploadSize);
                break;
            case UPLOAD:
                report.uploadSpeed = finalReport.getTransferRateBit();
                report.elapsedUploadTime = finalReport.getReportTime() - finalReport.getStartTime();
                testDialog.onSpeedTestFinish();
                report.save();
                break;
        }
    }

    protected void onProgress(SpeedTestMode mode, int progressPercent, fr.bmartel.speedtest.SpeedTestReport report) {
        testDialog.onSpeedTestProgress(mode, progressPercent, report.getTransferRateBit());
        if (progressPercent == 100)
            onSpeedTestTaskFinish(mode, report);
    }

    public void cancelTask() {
        currentTask.cancel(true);
    }
}
