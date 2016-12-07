package cl.niclabs.adkintunmobile.views.activemeasurements;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
    private View view;
    private GraphView downloadGraph;
    private GraphView uploadGraph;
    private LineGraphSeries<DataPoint> downloadSeries;
    private LineGraphSeries<DataPoint> uploadSeries;
    private TextView downloadTransferRate;
    private TextView uploadTransferRate;
    private SpeedTest speedTest;
    private AlertDialog alert;
    private AlertDialog.Builder builder;

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                speedTest.cancelTask();
            }
        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_speed_test_dialog, null);

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

        downloadSeries = new LineGraphSeries<>();
        uploadSeries = new LineGraphSeries<>();
        downloadGraph.addSeries(downloadSeries);
        uploadGraph.addSeries(uploadSeries);

        setCancelable(false);

        speedTest = new SpeedTest(this);
        speedTest.start();

        builder.setView(view);
        alert = builder.create();
        return alert;
    }

    public void onSpeedTestFinish() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener()
                {
                    @Override
                    public void onShow(DialogInterface dialog)
                    {
                        if(true)
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
                    }
                });
                ((ViewGroup) view.getParent()).removeAllViews();
                dialog.show();
            }
        });
    }
}
