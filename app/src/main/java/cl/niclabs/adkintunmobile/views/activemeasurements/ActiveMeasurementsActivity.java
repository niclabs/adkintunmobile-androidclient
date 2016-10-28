package cl.niclabs.adkintunmobile.views.activemeasurements;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveMeasurementsTest;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersDialog;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersTask;
import cl.niclabs.adkintunmobile.utils.activemeasurements.VideoTest.VideoTest;
import cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest.SpeedTest;
import cl.niclabs.adkintunmobile.utils.activemeasurements.webpagestest.WebPagesTest;
import fr.bmartel.speedtest.SpeedTestMode;

public class ActiveMeasurementsActivity extends AppCompatActivity implements ActiveMeasurementsTest{

    private final String TAG = "AdkM:ActiveMeasurActivity";

    protected String title;
    protected Context context;
    protected Toolbar toolbar;

    private GraphView downloadGraph;
    private GraphView uploadGraph;
    private LineGraphSeries<DataPoint> downloadSeries;
    private LineGraphSeries<DataPoint> uploadSeries;
    private TextView downloadTransferRate;
    private TextView uploadTransferRate;
    private TextView urlsTime;
    private EditText serverUrl;
    private EditText editTextFileSize;
    private WebView webView;
    private int i = 0;

    static String[] serversUrl;

    public static String[] getServers() {
        return serversUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_measurements);

        setBaseActivityParams();
        setupToolbar();

        downloadTransferRate = (TextView) findViewById(R.id.downloadTransferRate);
        uploadTransferRate = (TextView) findViewById(R.id.uploadTransferRate);
        editTextFileSize = (EditText) findViewById(R.id.editText);
        serverUrl = (EditText) findViewById(R.id.server_url);
        urlsTime = (TextView) findViewById(R.id.urls);
        webView = (WebView) findViewById(R.id.webView);

        downloadGraph = (GraphView) findViewById(R.id.graph1);
        downloadGraph.getViewport().setXAxisBoundsManual(true);
        downloadGraph.getViewport().setYAxisBoundsManual(true);
        downloadGraph.getViewport().setMinX(0);
        downloadGraph.getViewport().setMaxX(100);

        downloadGraph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        downloadGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        uploadGraph = (GraphView) findViewById(R.id.graph2);
        uploadGraph.getViewport().setXAxisBoundsManual(true);
        uploadGraph.getViewport().setYAxisBoundsManual(true);
        uploadGraph.getViewport().setMinX(0);
        uploadGraph.getViewport().setMaxX(100);

        uploadGraph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        uploadGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
    }
    private void emulateClick(final WebView webview) {
        long delta = 100;
        long downTime = SystemClock.uptimeMillis();
        float x = webview.getLeft() + webview.getWidth()/2; //in the middle of the webview
        float y = webview.getTop() + webview.getHeight()/2;

        final MotionEvent motionEvent = MotionEvent.obtain( downTime, downTime + delta, MotionEvent.ACTION_DOWN, x, y, 0 );
        final MotionEvent motionEvent2 = MotionEvent.obtain( downTime + delta + 1, downTime + delta * 2, MotionEvent.ACTION_UP, x, y, 0 );
        Runnable tapdown = new Runnable() {
            @Override
            public void run() {
                if (webview != null) {
                    webview.dispatchTouchEvent(motionEvent);
                }
            }
        };

        Runnable tapup = new Runnable() {
            @Override
            public void run() {
                if (webview != null) {
                    webview.dispatchTouchEvent(motionEvent2);
                }
            }
        };

        int toWait = 0;
        int delay = 100;
        webview.postDelayed(tapdown, delay);
        delay += 100;
        webview.postDelayed(tapup, delay);

    }
    public void onClickStart(View view) throws Exception {
        if (editTextFileSize.getText().toString().equals("")){
            editTextFileSize.setText("1");
        }
        final int fileOctetSize = Integer.parseInt(editTextFileSize.getText().toString());

        downloadGraph.removeAllSeries();
        uploadGraph.removeAllSeries();

        downloadSeries = new LineGraphSeries<DataPoint>();
        uploadSeries = new LineGraphSeries<DataPoint>();
        downloadGraph.addSeries(downloadSeries);
        uploadGraph.addSeries(uploadSeries);

        urlsTime.setText("");

        selectServer();

        //startSpeedTest(fileOctetSize);
        //startWebPagesTest();
        //startVideoTest();
        View mView = this.getCurrentFocus();
        if (mView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
        }
    }

    private void selectServer() {
        new ActiveServersTask(this).execute();
    }

    public void updateDownloadGraph(final int downloadPercent, final float transferRateBit){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                downloadTransferRate.setText((int) (transferRateBit / 1000) + " ");
                if (downloadSeries.isEmpty()){
                    DataPoint firstPoint = new DataPoint(0, transferRateBit/1000);
                    downloadSeries.appendData(firstPoint, false, 40000);
                }

                DataPoint newPoint = new DataPoint(downloadPercent, transferRateBit/1000);
                downloadSeries.appendData(newPoint, false, 40000);

                downloadGraph.getViewport().setMaxY( (int) (downloadGraph.getViewport().getMaxY(true) + 50) / 50 *50);
            }
        });
    }

    public void updateUploadGraph(final int uploadPercent, final float transferRateBit){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uploadTransferRate.setText((int) (transferRateBit / 1000) + " ");
                if (uploadSeries.isEmpty()){
                    DataPoint firstPoint = new DataPoint(0, transferRateBit/1000);
                    uploadSeries.appendData(firstPoint, false, 40000);
                }

                DataPoint newPoint = new DataPoint(uploadPercent, transferRateBit/1000);
                uploadSeries.appendData(newPoint, false, 4000);

                uploadGraph.getViewport().setMaxY( (int) (uploadGraph.getViewport().getMaxY(true) + 50) / 50 *50);
            }
        });
    }

    public void startSpeedTest(int fileOctetSize, String currentServer) {
        new SpeedTest(this, fileOctetSize, currentServer).start();
    }

    @Override
    public void onSpeedTestFinish() {
        startWebPagesTest();
    }

    @Override
    public void onSpeedTestProgress(SpeedTestMode mode, int progressPercent, float transferRateBit) {
        switch (mode){
            case DOWNLOAD:
                updateDownloadGraph(progressPercent, transferRateBit);
                break;
            case UPLOAD:
                updateUploadGraph(progressPercent, transferRateBit);
                break;
        }
    }

    public void startWebPagesTest() {
        new WebPagesTest(this, webView).start();
    }

    @Override
    public void onWebPageLoaded(String url, long loadingTime, long sizeByte){
        urlsTime.setText(urlsTime.getText() + "\n" + url + ": " + loadingTime + "ms " + Formatter.formatFileSize(this, sizeByte));
    }

    @Override
    public void onWebPageTestFinish() {
        startVideoTest();
    }

    private void startVideoTest() {
        new VideoTest(this, webView).start();
    }

    @Override
    public void onVideoEnded(final String quality, final int timesBuffering, final float loadedFraction, final long totalBytes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                urlsTime.setText(urlsTime.getText() + "\nFor " + quality + ": "
                        + timesBuffering+ "ms buffering, "
                        + (int)(loadedFraction*100) + "% loaded, "
                        + Formatter.formatFileSize(getApplicationContext(), totalBytes));
            }
        });
    }

    @Override
    public void onVideoTestFinish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("about:blank");
            }
        });
    }

    public void setupToolbar(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(this.title);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void setBaseActivityParams(){
        this.title = getString(R.string.view_active_measurements);
        this.context = this;
    }

    public void onActiveServersReceived(String[] serversUrl) {
        this.serversUrl = serversUrl;
        FragmentManager fm = getSupportFragmentManager();
        ActiveServersDialog.showDialogPreference(fm, null);
    }
}
