package cl.niclabs.adkintunmobile.views.connectiontype.connectionmode;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.connectiontype.DoughnutChartViewFragment;

public class DoughnutChartConnectionModeViewFragment extends DoughnutChartViewFragment {

    // Responsabilidad de la herencia
    // Métodos a implementar de NewDonutchartViewFragment
    @Override
    public void refreshLegend() {
        TypedArray icons = context.getResources().obtainTypedArray(R.array.connection_mode_legend_icons);
        TypedArray colors = context.getResources().obtainTypedArray(R.array.connection_mode_legend_colors_soft);
        setNewLegend(icons, colors);
    }

    @Override
    public TypedArray getSaturatedColors() {
        return  context.getResources().obtainTypedArray(R.array.connection_mode_legend_colors);
    }

    @Override
    public TypedArray getSoftColors() {
        return context.getResources().obtainTypedArray(R.array.connection_mode_legend_colors_soft);
    }


    // Utilitarios

    public DoughnutChartConnectionModeViewFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donutchart_view, container, false);
    }

}
