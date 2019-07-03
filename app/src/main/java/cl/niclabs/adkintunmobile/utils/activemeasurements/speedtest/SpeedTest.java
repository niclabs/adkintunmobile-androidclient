package cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest;

import android.util.Log;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import cl.niclabs.adkintunmobile.data.persistent.Measurement;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import fr.bmartel.speedtest.model.SpeedTestMode;

public class SpeedTest {

    private static final int SOCKET_TIMEOUT = 5000;
    SpeedTestSocket speedTestSocket;
    private final String host;
    private final String port;
    private int fileOctetSize;
    private ISpeedTestListener listener;
    private BigDecimal transferRateOctet ;
    private SpeedTestMode mode;
    private boolean testEnd;


    public SpeedTest(String host, String port, int fileOctetSize, SpeedTestMode mode) {
        speedTestSocket = new SpeedTestSocket();
        speedTestSocket.setSocketTimeout(SOCKET_TIMEOUT);
        this.mode = mode;
        this.host = host;
        this.port = port;
        this.fileOctetSize = fileOctetSize;
        this.testEnd = false;
        setListener(null);
    }

    public void setListener(ISpeedTestListener listener) {
        if (listener == null) {
            this.listener = new ISpeedTestListener() {

                @Override
                public void onCompletion(final SpeedTestReport report) {
                    String mode = report.getSpeedTestMode() == SpeedTestMode.UPLOAD ? "UPLOAD" : "DOWNLOAD";
                    //Log.i("SpeedTestInfo:", mode + " - " + report.getTotalPacketSize()
                    //        + " - " + report.getTransferRateBit() + " - " +
                    //        report.getTransferRateOctet());
                    transferRateOctet = report.getTransferRateOctet();
                    testEnd = true;
                }

                @Override
                public void onError(final SpeedTestError speedTestError, final String errorMessage) {
                    Log.e("SpeedTestError:","Error " + speedTestError + " : " + errorMessage);
                    testEnd = true;
                }

                @Override
                public void onProgress(final float percent, final SpeedTestReport downloadReport) {
                    Log.i("SpeedTestProgress", downloadReport.toString());
                }
            };
        } else {
            this.listener = listener;
        }
        speedTestSocket.addSpeedTestListener(this.listener);
    }

    public Map<String, Object> run() {
        setListener(listener);
        switch (mode) {
            case DOWNLOAD:
                Log.i("SpeedTestDownload", "Downloading");
                speedTestSocket.startDownload(host + "/speedtest/" + (fileOctetSize / 1000000));
                break;

            case UPLOAD:
                Log.i("SpeedTestUpload", "Uploading");
                speedTestSocket.startUpload(host + "/speedtest/", fileOctetSize);
                break;
        }

        while (!testEnd) {
            Log.i("SpeedTestWait", "Waiting for speedtest");
        }
        String mode;

        if (this.mode == SpeedTestMode.UPLOAD) {
            mode = "UPLOAD";
        } else {
            mode = "DOWNLOAD";
        }

        Map<String, Object> map = new HashMap<>();
        map.put(mode, transferRateOctet.toString());
        return map;
    }

    public BigDecimal getTransferRateOctet() {
        return transferRateOctet;
    }

}
