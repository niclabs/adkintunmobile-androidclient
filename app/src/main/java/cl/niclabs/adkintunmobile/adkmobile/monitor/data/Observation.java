package cl.niclabs.adkintunmobile.adkmobile.monitor.data;

/**
 * Defines the structure for all data objects
 * generated by Monitor classes
 *
 * @author Felipe Lalanne <flalanne@niclabs.cl>
 */
public interface Observation {
    /**
     * @return the identifier of the event, according to the Monitor class
     */
    public Integer getEventType();

    /**
     * @return the timestamp at which the event took place (in milliseconds)
     */
    public Long getTimestamp();
}