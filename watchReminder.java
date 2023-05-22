import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;

import java.util.Calendar;

public class ReminderActivity extends WearableActivity {

    private static final String TAG = "ReminderActivity";

    private Vibrator vibrator;
    private Handler handler = new Handler();
    private boolean vibrateEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (vibrateEnabled && shouldVibrateNow()) {
                    vibrator.vibrate(15000); // vibrate for 15 seconds
                    handler.postDelayed(this, 300000); // schedule next vibration in 5 minutes
                }
            }
        }, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    public void stopVibration() {
        this.vibrateEnabled = false;
    }

    public void startVibration() {
        this.vibrateEnabled = true;
    }

    // Check if current time is within vibrate time window
    private boolean shouldVibrateNow() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        // time window 1: 9:30 - 12:00
        if (hour > 9 && hour < 12) {
            return true;
        }
        if (hour == 9 && minute >= 30) {
            return true;
        }

        // time window 2: 14:00 - 21:30
        if (hour > 14 && hour < 21) {
            return true;
        }
        if (hour == 14 && minute >= 0) {
            return true;
        }
        if (hour == 21 && minute < 30) {
            return true;
        }

        return false;
    }
}
