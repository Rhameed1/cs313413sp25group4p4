package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import android.media.MediaPlayer;
import android.content.Context;

class BeepingState implements TimerState {

    private final TimerSMStateView sm;
    private MediaPlayer mediaPlayer;
    private final Context appContext;

    public BeepingState(final TimerSMStateView sm, final Context context) {
        this.sm = sm;
        this.appContext = context.getApplicationContext();
        playAlarm();
    }

    private void playAlarm() {

        try {
            // Create and configure MediaPlayer
            mediaPlayer = MediaPlayer.create(appContext, R.raw.alarm_beep);
            if (mediaPlayer != null) {
                // Set looping to ensure it plays indefinitely
                mediaPlayer.setLooping(true);
                // Set media player listener for completion
                mediaPlayer.setOnCompletionListener(mp -> {
                    // This will be called if looping is false
                    // For safety, we'll restart the sound here, but with looping=true
                    // this should not be necessary
                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                });
                // Start playing
                mediaPlayer.start();
                System.out.println("BEEPING... (Alarm started)");
            } else {
                System.out.println("Failed to create MediaPlayer");
            }
        } catch (Exception e) {
            System.out.println("Error playing alarm: " + e.getMessage());
        }
    }

    private void stopAlarm() {

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            System.out.println("ALARM STOPPED");
        }
    }

    @Override
    public void onStartStop() {
        stopAlarm();
        sm.actionReset();
        sm.toStoppedState();
    }

    //@Override
    //public void onLapReset() {
       // stopAlarm();
      //  sm.actionReset();
       // sm.toStoppedState();
   // }

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
