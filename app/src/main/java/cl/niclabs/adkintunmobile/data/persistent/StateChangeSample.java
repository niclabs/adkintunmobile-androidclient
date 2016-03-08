package cl.niclabs.adkintunmobile.data.persistent;

import cl.niclabs.adkmobile.monitor.data.StateChange;
import cl.niclabs.android.data.Persistent;

public class StateChangeSample extends Persistent<StateChangeSample>{

    private int state;
    private Integer stateType;

    public StateChangeSample(){}

    public StateChangeSample(StateChange stateChange){
        this.setState(stateChange.getState());
        this.setStateType(stateChange.getStateType().value());
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Integer getStateType() {
        return stateType;
    }

    public void setStateType(Integer stateType) {
        this.stateType = stateType;
    }
}
