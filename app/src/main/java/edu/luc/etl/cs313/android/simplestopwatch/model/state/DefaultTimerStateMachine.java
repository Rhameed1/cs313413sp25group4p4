package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;
import android.content.Context;

import static edu.luc.etl.cs313.android.simplestopwatch.common.Constants.SEC_PER_TICK;

/**
 * An implementation of the state machine for the stopwatch.
 *
 * @author laufer
 */
public class DefaultTimerStateMachine implements TimerStateMachine {

    public DefaultTimerStateMachine(final TimeModel timeModel, final ClockModel clockModel, final Context context) {
        this.timeModel = timeModel;
        this.clockModel = clockModel;
        this.appContext = context.getApplicationContext();
        this.BEEPING = new BeepingState(this, appContext);
    }


    private final TimeModel timeModel;

    private final ClockModel clockModel;

    private final Context appContext;

    /**
     * The internal state of this adapter component. Required for the State pattern.
     */
    private TimerState state;

    protected void setState(final TimerState state) {
        this.state = state;
        listener.onStateUpdate(state.getId());
    }

    private StopwatchModelListener listener;

    @Override
    public void setModelListener(final StopwatchModelListener listener) {

        this.listener = listener;
    }

    // forward event uiUpdateListener methods to the current state
    // these must be synchronized because events can come from the
    // UI thread or the timer thread
    @Override public synchronized void onStartStop() { state.onStartStop(); }
    //@Override public synchronized void onLapReset()  { state.onLapReset(); }
    @Override public synchronized void onTick()      { state.onTick(); }

    @Override public void updateUIRuntime() { listener.onTimeUpdate(timeModel.getRuntime()); }
    @Override public void updateUILaptime() { listener.onTimeUpdate(timeModel.getLaptime()); }

    // known states
    private final TimerState STOPPED     = new StoppedZeroState(this);
    private final TimerState RUNNING     = new RunningState(this);
    //private final TimerState LAP_RUNNING = new LapRunningState(this);
    //private final TimerState LAP_STOPPED = new LapStoppedState(this);
    private final TimerState WAITING = new WaitingState(this);
    private TimerState BEEPING;


    // transitions
    @Override public void toRunningState()    { setState(RUNNING); }
    @Override public void toStoppedState()    { setState(STOPPED); }
    //@Override public void toLapRunningState() { setState(LAP_RUNNING); }
   //@Override public void toLapStoppedState() { setState(LAP_STOPPED); }
    @Override public void toWaitingState() { setState(WAITING); }
    @Override public void toBeepingState() { setState(BEEPING); }


    // actions
    @Override public void actionInit()       { toStoppedState(); actionReset(); }
    @Override public void actionReset()      { timeModel.resetRuntime(); actionUpdateView(); }
    @Override public void actionStart()      { clockModel.start(); }
    @Override public void actionStop()       { clockModel.stop(); }
    //@Override public void actionLap()        { timeModel.setLaptime(); }
    @Override public void actionInc()        { timeModel.incRuntime(); actionUpdateView(); }
    @Override public void actionDec() { timeModel.decRuntime(); actionUpdateView();}
    @Override public void actionUpdateView() { state.updateView(); }

    @Override
    public int getRuntime() {
        return timeModel.getRuntime();
    }
}
