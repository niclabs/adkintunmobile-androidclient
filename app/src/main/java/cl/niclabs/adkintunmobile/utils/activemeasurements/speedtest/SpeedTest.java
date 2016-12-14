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
    private String server;
    private AsyncTask currentTask;
    SpeedTestReport report;

    public SpeedTest(SpeedTestDialog testDialog) {
        this.testDialog = testDialog;
        report = new SpeedTestReport(testDialog.getContext());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(testDialog.getContext());
        String downloadSizeValue = sharedPreferences.getString(testDialog.getString(R.string.settings_speed_test_download_size_key), "10");
        String uploadSizeValue = sharedPreferences.getString(testDialog.getString(R.string.settings_speed_test_upload_size_key), "10");
        server = sharedPreferences.getString(testDialog.getString(R.string.settings_speed_test_server_key), "");
        downloadSize = Integer.parseInt(downloadSizeValue);
        uploadSize = Integer.parseInt(uploadSizeValue);

        report.host = server;
        report.downloadSize = downloadSize;
        report.uploadSize = uploadSize;
    }

    public void start(){
        startSpeedTest(SpeedTestMode.DOWNLOAD, server, downloadSize);
    }

    private void startSpeedTest(SpeedTestMode mode, String host, int fileOctetSize) {
        currentTask = new SpeedTestTask(this, host, fileOctetSize).execute(mode);
    }

    protected void onSpeedTestTaskFinish(SpeedTestMode mode, fr.bmartel.speedtest.SpeedTestReport finalReport) {
        switch (mode){
            case DOWNLOAD:
                report.downloadSpeed = finalReport.getTransferRateBit();
                report.elapsedDownloadTime = finalReport.getReportTime() - finalReport.getStartTime();
                startSpeedTest(SpeedTestMode.UPLOAD, server, uploadSize);
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
