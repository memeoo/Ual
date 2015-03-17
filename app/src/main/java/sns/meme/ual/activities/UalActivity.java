package sns.meme.ual.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;

import sns.meme.ual.base.UalApplication;
import sns.meme.ual.base.UalDao;

/**
 * Created by Meme on 2015-03-17.
 */
public class UalActivity extends ActionBarActivity {

    public UalApplication mApp;
    public UalDao mDao;
    protected boolean Is_LocationContinue = false;    //change true when start new activity but continuely tracking more (now login-> detail, detail -> main)
    private UalBroadcastReceiver mLocalReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (UalApplication) getApplication();
        mDao = mApp.getDao();

        mLocalReceiver = new UalBroadcastReceiver(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalReceiver,
                new IntentFilter(UalBroadcastReceiver.LOCAL_RECEIVER_TAG));
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalReceiver);
        super.onDestroy();
    }

    public void buildingGenericErrorDialog(final String title, final String text) {
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_EXTRA_ERROR_DIALOG_TITLE, title);
        bundle.putString(Constants.INTENT_EXTRA_ERROR_DIALOG_MESSAGE, text);
        onShowDialogs(Constants.DIA_ERROR_GENERIC, bundle);
    }

    public static class UalBroadcastReceiver extends BroadcastReceiver {
        public static final String LOCAL_RECEIVER_TAG = UalBroadcastReceiver.class.getSimpleName();
        public static final String EVENT_TYPE_KEY = "eventType";
        public static final int EVENT_TYPE_DEFAULT = -1;


        private UalActivity mContext;

        protected UalBroadcastReceiver(final UalActivity context) {
            mContext = context;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final int eventType = intent.getIntExtra(EVENT_TYPE_KEY, EVENT_TYPE_DEFAULT);
            switch (eventType) {

                default:
                    break;
            }
        }
    }
}
