package cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.SpeedTestDialog;
import fr.bmartel.speedtest.SpeedTestMode;

public class SpeedTest {
    private SpeedTestDialog testDialog;
    private int downloadSize, uploadSize;
    private String server;
    private AsyncTask currentTask;

    public SpeedTest(SpeedTestDialog testDialog) {
        this.testDialog = testDialog;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(testDialog.getContext());
        String downloadSizeValue = sharedPreferences.getString(testDialog.getString(R.string.settings_speed_test_download_size_key), "10");
        String uploadSizeValue = sharedPreferences.getString(testDialog.getString(R.string.settings_speed_test_upload_size_key), "10");
        server = sharedPreferences.getString(testDialog.getString(R.string.settings_speed_test_server_key), "");
        downloadSize = Integer.parseInt(downloadSizeValue);
        uploadSize = Integer.parseInt(uploadSizeValue);
    }

    public void start(){
        startSpeedTest(SpeedTestMode.DOWNLOAD, server, downloadSize);
    }

    private void startSpeedTest(SpeedTestMode mode, String host, int fileOctetSize) {
        currentTask = new SpeedTestTask(this, host, fileOctetSize).execute(mode);
    }

    protected void onSpeedTestTaskFinish(SpeedTestMode mode) {
        switch (mode){
            case DOWNLOAD:
                startSpeedTest(SpeedTestMode.UPLOAD, server, uploadSize);
                break;
            case UPLOAD:
                testDialog.onSpeedTestFinish();
                break;
        }
    }

    protected void onProgress(SpeedTestMode mode, int progressPercent, float transferRateBit) {
        testDialog.onSpeedTestProgress(mode, progressPercent, transferRateBit);
    }

    public void cancelTask() {
        currentTask.cancel(true);
    }
}
