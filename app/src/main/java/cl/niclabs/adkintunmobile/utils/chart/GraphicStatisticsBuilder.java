package cl.niclabs.adkintunmobile.utils.chart;

import android.view.View;

import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;

/**
 * GraphicStatisticsBuilder interface provides a method to generate graphics using a Statistic information object. 
 */
public interface GraphicStatisticsBuilder {
	/**
	 * Uses an StatisticInformation to extract all the components to build graphics on a view.
	 * @param a StatisticInformation that contains all the necessary information to 
	 * generate graphics.
	 * @return View with all the graphics to show.
	 */
	public View createGraphicStatistic(StatisticInformation a);
}
