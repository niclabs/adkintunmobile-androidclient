package cl.niclabs.adkintunmobile.views.activemeasurements;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.Formatter;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.activemeasurements.webpagestest.WebPagesTest;

public class WebPagesTestDialog extends DialogFragment{

    private View view;
    private TextView urlsTime;
    private WebView webView;
    TableLayout tableLayout;
    private int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        view = inflater.inflate(R.layout.fragment_web_pages_test_dialog, container, false);
        getDialog().setTitle("WebPagesTest");

        // Get visual elements
        urlsTime = (TextView) view.findViewById(R.id.urls);
        webView = (WebView) view.findViewById(R.id.webView);
        tableLayout = (TableLayout) view.findViewById(R.id.web_pages_table_layout);

        new WebPagesTest(this, webView).start();

        return view;
    }

    public void onWebPageStarted(int index){
        TableRow pageRow = (TableRow) tableLayout.getChildAt(index + 2);
        ((LinearLayout) pageRow.getChildAt(1)).getChildAt(1).setVisibility(View.VISIBLE);

        //urlsTime.setText("Cargando: " + url);
    }

    public void onWebPageLoaded(int index, long loadingTime, long pageSize){
        TableRow pageRow = (TableRow) tableLayout.getChildAt(index + 2);
        ((TextView)pageRow.getChildAt(2)).setText(loadingTime + "ms");
        ((TextView)pageRow.getChildAt(3)).setText(Formatter.formatFileSize(getContext(), pageSize));

        ((LinearLayout) pageRow.getChildAt(1)).getChildAt(1).setVisibility(View.GONE);
        ((LinearLayout) pageRow.getChildAt(1)).getChildAt(0).setVisibility(View.VISIBLE);
        //pageRow.getChildAt(2).setVisibility(View.VISIBLE);

        //urlsTime.setText("Cargando: " + url);
        //urlsTime.setText(urlsTime.getText() + "\n" + url + ": " + loadingTime + "ms " + Formatter.formatFileSize(getContext(), sizeByte));
    }

    public void setUpTextView(ArrayList<String> names) {
        int index = tableLayout.getChildCount();
        for (String name : names){
            //TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
            int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getContext().getResources().getDisplayMetrics());
            TableRow tableRow = new TableRow(getContext());
            tableRow.setPadding(margin, 0 , margin, 0);
            //tableRow.setLayoutParams(tableRowParams);

            //lp1.weight = 3;

            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3f));
            textView.setText(name);
            tableRow.addView(textView);

            LinearLayout status = new LinearLayout(getContext());
            //status.setLayoutParams(new RelativeLayout.LayoutParams);

            ImageView checkMark = new ImageView(getContext());
            RelativeLayout.LayoutParams checkMarkParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            checkMarkParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            checkMark.setLayoutParams(checkMarkParams);
            //checkMark.setScaleType(ImageView.ScaleType.CENTER);
            //checkMark.setAdjustViewBounds(true);
            checkMark.setVisibility(View.GONE);
            checkMark.setBackgroundResource(R.drawable.ic_done_black_48dp);
            status.addView(checkMark, 50, 50);
            //tableRow.addView(checkMark, 50, 50);

            ProgressBar spinner = new ProgressBar(getContext());
            //spinner.setLayoutParams(new LinearLayout.LayoutParams(margin, margin));
            spinner.setVisibility(View.GONE);
            RelativeLayout.LayoutParams spinnerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            spinnerParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            spinner.setLayoutParams(spinnerParams);
            status.addView(spinner, 50, 50);
            //tableRow.addView(spinner, 50, 50);

            tableRow.addView(status, 50, 50);

            TextView loadingTime = new TextView(getContext());
            loadingTime.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
            //loadingTime.setText("0ms");
            loadingTime.setGravity(Gravity.CENTER);
            tableRow.addView(loadingTime);

            TextView pageSize = new TextView(getContext());
            pageSize.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
            //pageSize.setText("0MB");
            pageSize.setGravity(Gravity.CENTER);
            tableRow.addView(pageSize);

            tableLayout.addView(tableRow, index++);
            //urlsTime.setText(urlsTime.getText() + "\n" + name + ": ");
        }
    }
}
