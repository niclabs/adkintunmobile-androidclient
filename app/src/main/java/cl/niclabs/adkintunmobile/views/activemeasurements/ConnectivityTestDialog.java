package cl.niclabs.adkintunmobile.views.activemeasurements;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest.ConnectivityTest;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class ConnectivityTestDialog extends DialogFragment{

    private View view;
    private WebView webView;
    private TableLayout tableLayout;
    private ConnectivityTest connectivityTest;

    public void onWebPageStarted(int index){
        TableRow tableRow = (TableRow) tableLayout.getChildAt(index);
        tableRow.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
    }

    public void onWebPageLoaded(int index, long loadingTime, long pageSize){
        TableRow tableRow = (TableRow) tableLayout.getChildAt(index);
        tableRow.findViewById(R.id.progress_bar).setVisibility(View.GONE);
        ImageView status = (ImageView) tableRow.findViewById(R.id.ic_done);
        status.setVisibility(View.VISIBLE);
        if (loadingTime == 0){
            status.setImageResource(R.drawable.ic_clear_black);
            return;
        }
        status.setImageResource(R.drawable.ic_done_black);

        String time;
        if (loadingTime > 1000)
            time = String.format(Locale.getDefault(), "%.2f s", loadingTime/1000.0);
        else
            time = String.format(Locale.getDefault(), "%d ms", loadingTime);
        ((TextView)tableRow.findViewById(R.id.loading_time)).setText(time);
        ((TextView)tableRow.findViewById(R.id.downloaded_size)).setText(Network.formatBytes(pageSize));
    }

    public void setUpTextView(ArrayList<String> names) {
        int index = tableLayout.getChildCount();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        for (String name : names){
            TableRow tableRow = (TableRow) inflater.inflate(R.layout.web_pages_dialog_row, null);
            ((TextView)tableRow.findViewById(R.id.web_page_name)).setText(name);

            tableLayout.addView(tableRow, index++);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        connectivityTest.cancelTask();
                    }
                });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_connectivity_test_dialog, null);

        // Get visual elements
        webView = (WebView) view.findViewById(R.id.webView);
        tableLayout = (TableLayout) view.findViewById(R.id.web_pages_table_layout);
        setCancelable(false);
        connectivityTest = new ConnectivityTest(this, webView);
        connectivityTest.start();

        builder.setView(view);
        return builder.create();
    }

}
