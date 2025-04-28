package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class StoppedZeroState implements TimerState {

    public StoppedZeroState(final TimerSMStateView sm) {
        this.sm = sm;
    }

    private final TimerSMStateView sm;

    @Override
    public void onStartStop() {
        if (sm.getRuntime() < 99) {
            sm.actionInc();
            sm.toWaitingState();
        } else {
            sm.toRunningState();
        }
    }

    //@Override
   // public void onLapReset() {
      //  sm.actionReset();
       // sm.toStoppedState();
    //}

    @Override
    public void onTick() {
        // Do nothing when timer is at zero and stopped
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.STOPPED;
    }
}
