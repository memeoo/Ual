package sns.meme.ual.event;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by meme on 15. 6. 24..
 */
public class UalPushReceiver extends ParsePushBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("meme", "PUSH ACTION ==> " + intent.getAction());
        super.onReceive(context, intent);
    }
}
