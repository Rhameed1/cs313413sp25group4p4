package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class BeepingState implements TimerState {

    private final TimerSMStateView sm;

    public BeepingState(final TimerSMStateView sm) {
        this.sm = sm;
        playAlarm();
    }

    private void playAlarm() {

        System.out.println("BEEPING...");
    }

    private void stopAlarm() {
        System.out.println("ALARM STOPPED");

    }

    @Override
    public void onStartStop() {
        stopAlarm();
        sm.actionReset();
        sm.toStoppedState();
    }

    @Override
    public void onLapReset() {
        stopAlarm();
        sm.actionReset();
        sm.toStoppedState();
    }

    @Override
    public void onTick() {
        // still beeping
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.BEEPING;
    }
}
