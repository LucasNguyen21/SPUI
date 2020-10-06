package Broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.federation.funf_test.MainActivity;

public class OnBootBroadcaster extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) || Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(intent.getAction())){
            Intent toMainActivityIntent = new Intent(context, MainActivity.class);
            toMainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(toMainActivityIntent);
        }
    }
}
