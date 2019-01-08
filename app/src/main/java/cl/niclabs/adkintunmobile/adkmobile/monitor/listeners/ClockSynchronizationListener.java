package cl.niclabs.adkintunmobile.adkmobile.monitor.listeners;


import cl.niclabs.adkintunmobile.adkmobile.monitor.data.ClockSynchronizationState;

/**
 * Listens for ClockSynchronization events
 *
 * @author Felipe Lalanne <flalanne@niclabs.cl>.
 */
public interface ClockSynchronizationListener extends MonitorListener {
    /**
     * Inform the listener of a telephony change
     * @param screenState the new screen state data
     */
    public void onClockStateChange(ClockSynchronizationState clockState);
}