package cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest;

import android.os.AsyncTask;
import android.util.Log;

import fr.bmartel.speedtest.ISpeedTestListener;
import fr.bmartel.speedtest.SpeedTestError;
import fr.bmartel.speedtest.SpeedTestMode;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;

public class SpeedTestTask extends AsyncTask<SpeedTestMode, Void, Void> {
    private int progressPercent = -1;
    private SpeedTest speedTest;
    private final String host;
    private int fileOctetSize;

    public SpeedTestTask(SpeedTest speedTest, String host, int fileOctetSize){
        this.speedTest = speedTest;
        this.host = host;
        this.fileOctetSize = fileOctetSize;
    }

    @Override
    protected Void doInBackground(final SpeedTestMode... params) {
        final SpeedTestMode mode = params[0];
        final SpeedTestSocket speedTestSocket = new SpeedTestSocket();
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onDownloadPacketsReceived(long packetSize,
                                                  float transferRateBitPerSeconds,
                                                  float transferRateOctetPerSeconds) {
                Log.i("speed-test-app","download transfer rate  : " + transferRateBitPerSeconds + "Bps");
            }

            @Override
            public void onDownloadError(SpeedTestError errorCode, String message) {
                Log.i("speed-test-app","Download error " + errorCode + " occured with message : " + message);
            }

            @Override
            public void onUploadPacketsReceived(long packetSize,
                                                float transferRateBitPerSeconds,
                                                float transferRateOctetPerSeconds) {
                Log.i("speed-test-app","upload transfer rate  : " + transferRateOctetPerSeconds + "Bps");
            }

            @Override
            public void onUploadError(SpeedTestError errorCode, String message) {
                Log.i("speed-test-app","Upload error " + errorCode + " occured with message : " + message);
            }

            @Override
            public void onDownloadProgress(final float percent, final SpeedTestReport downloadReport) {
                Log.i("speed-test-app","percentdown"+ percent);
                if (Math.round(percent) > progressPercent) {
                    progressPercent = Math.round(percent);
                    speedTest.onProgress(mode, progressPercent, downloadReport);
                }
            }

            @Override
            public void onUploadProgress(float percent,final SpeedTestReport uploadReport) {
                Log.i("speed-test-app", "percentup" + percent);
                if (Math.round(percent) > progressPercent) {
                    progressPercent = Math.round(percent);
                    speedTest.onProgress(mode, progressPercent, uploadReport);
                }
            }
        });

        switch (mode){
            case DOWNLOAD:
                speedTestSocket.startDownload(host, 5000, "/speedtest/" + (fileOctetSize/1000000));
                break;
            case UPLOAD:
                speedTestSocket.startUpload(host, 5000, "/speedtest/", fileOctetSize);
                break;
        }

        return null;
    }
}
