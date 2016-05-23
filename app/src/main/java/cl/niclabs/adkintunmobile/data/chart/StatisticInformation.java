package cl.niclabs.adkintunmobile.data.chart;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * StatisticInformation groups every element that contains statistic data that will be 
 * shown as graphics later. This class is meant to be called by a Builder that will
 * construct the final view using the data contained in this class.
 * @author ivana
 *
 */
public abstract class StatisticInformation {
	private ArrayList<Object> information;
	private long[] timeByType;
	/**
	 * Creates an StatisticInformation with no info.
	 */
	public StatisticInformation(){
		information=null;
	}
	/**
	 * Generates all the statistics information
	 */
	public abstract void setStatisticsInformation();
	/**
	 * From the information generated with setStatisticsInformation() returns the colors for the graphics.  
	 * @return colors to be used on graphic. If there are no colors return null.
	 */
	public abstract ArrayList<Integer> getColors();
	/**
	 * From the information generated with setStatisticsInformation() returns the values for the graphics.  
	 * @return values to be used on graphic. If there are no values return null.
	 */
	public abstract ArrayList<Float> getValues();
	/**
	 * From the information generated with setStatisticsInformation() returns an adapter for the graphics (ListView type graphics).  
	 * @return adatpter to be used on a ListView. If there is no adapter return null.
	 */
	public abstract ArrayAdapter<Integer> getAdapter();

	public ArrayList<Object> getInformation(){
		return  information;
	}

	public void setInformation(ArrayList<Object> information){
		this.information = information;
	}

	public void setTimeByType(long[] timeByType){
		this.timeByType = timeByType;
	}

	public long[] getTimeByType(){
		return timeByType;
	}
	
}
