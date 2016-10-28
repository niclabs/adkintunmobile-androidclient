package cl.niclabs.adkintunmobile.views.activemeasurements;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest.SpeedTest;
import fr.bmartel.speedtest.SpeedTestMode;

public class SpeedTestDialog extends DialogFragment{
    protected static int fileOctetSize;
    protected static String serverUrl;

    private View view;
    private GraphView downloadGraph;
    private GraphView uploadGraph;
    private LineGraphSeries<DataPoint> downloadSeries;
    private LineGraphSeries<DataPoint> uploadSeries;
    private TextView downloadTransferRate;
    private TextView uploadTransferRate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        view = inflater.inflate(R.layout.fragment_speed_test_dialog, container, false);
        getDialog().setTitle("SpeedTest");

        // Get visual elements
        downloadTransferRate = (TextView) view.findViewById(R.id.downloadTransferRate);
        uploadTransferRate = (TextView) view.findViewById(R.id.uploadTransferRate);
        downloadGraph = (GraphView) view.findViewById(R.id.download_graph);
        downloadGraph.getViewport().setXAxisBoundsManual(true);
        downloadGraph.getViewport().setYAxisBoundsManual(true);
        downloadGraph.getViewport().setMinX(0);
        downloadGraph.getViewport().setMaxX(100);

        downloadGraph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        downloadGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        uploadGraph = (GraphView) view.findViewById(R.id.upload_graph);
        uploadGraph.getViewport().setXAxisBoundsManual(true);
        uploadGraph.getViewport().setYAxisBoundsManual(true);
        uploadGraph.getViewport().setMinX(0);
        uploadGraph.getViewport().setMaxX(100);

        uploadGraph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        uploadGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        downloadSeries = new LineGraphSeries<DataPoint>();
        uploadSeries = new LineGraphSeries<DataPoint>();
        downloadGraph.addSeries(downloadSeries);
        uploadGraph.addSeries(uploadSeries);

        new SpeedTest(this, fileOctetSize, serverUrl).start();

        return view;
    }

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

    public void updateDownloadGraph(final int downloadPercent, final float transferRateBit){
        getActivity().runOnUiThread(new Runnable() {
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
        getActivity().runOnUiThread(new Runnable() {
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

    public void setSpeedTestParams(int fileOctetSize, String serverUrl){
        this.fileOctetSize = fileOctetSize;
        this.serverUrl = serverUrl;
    }
}
