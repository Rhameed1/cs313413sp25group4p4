package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import android.os.Handler;
import edu.luc.etl.cs313.android.simplestopwatch.R;

class WaitingState implements TimerState {

    private final TimerSMStateView sm;
    private final Handler handler = new Handler();
    private long lastPressTime;

    public WaitingState(final TimerSMStateView sm) {
        this.sm = sm;
        lastPressTime = System.currentTimeMillis();
        scheduleTimeoutCheck();
    }

    @Override
    public void onStartStop() {
        lastPressTime = System.currentTimeMillis();
        if (sm.getRuntime() < 99) {
            sm.actionInc();
        }
        sm.updateUIRuntime();
    }

    //@Override
    //public void onLapReset() {
      //  sm.actionReset();
      //  sm.toStoppedState();
    //}

    @Override
    public void onTick() {
        // Do nothing on tick — handled by the timeout handler
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.WAITING;
    }

    private void scheduleTimeoutCheck() {
        handler.postDelayed(() -> {
            long elapsed = System.currentTimeMillis() - lastPressTime;
            if (elapsed >= 3000 || sm.getRuntime() == 99) {
                sm.actionStart();
                sm.toRunningState();
            } else {
                scheduleTimeoutCheck();
            }
        }, 500);
    }
}
