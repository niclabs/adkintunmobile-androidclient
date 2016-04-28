package cl.niclabs.adkintunmobile.utils.display;

import android.view.View;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;

/**
 * DoughnutChartBuilder creates a DoughnutChart object using the parameters that
 * an StatisticInformation object stored.
 *
 */
public class DoughnutChartBuilder implements GraphicStatisticsBuilder {
	private DoughnutChart chart;
	private float chartDiameter;

	/**
	 * Creates a DoughnutChartBuilder. It requires a DoughnutChart to place the
	 * final graphics, and its diameter. The FatBar doesn't need to have any
	 * parameter set.
	 * 
	 * @param chart
	 *            DoughnutChart to place the final view
	 * @param chartDiameter
	 *            diamater of the chart
	 */
	public DoughnutChartBuilder(DoughnutChart chart, float chartDiameter) {
		this.chart = chart;
		this.chartDiameter = chartDiameter;
	}

	/**
	 * Uses an StatisticInformation to extract all the components to build a
	 * DoughnutChart.
	 */
	@Override
	public View createGraphicStatistic(StatisticInformation info) {
		info.setStatisticsInformation();
		final ArrayList<Integer> colors = info.getColors();
		final ArrayList<Float> values = info.getValues();
		/* this sets the start of the chart for a day that starts at 12am */
		chart.setOffset(88f);
		chart.setDiameter(chartDiameter);
		chart.setColors(colors);
		chart.setValues(values);
		return chart;
	}

}