package sns.meme.ual.base;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Meme on 2015-03-13.
 */
public class UalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "oR56d7okbrgB3IVamRsw2Cea6uIyUKFbvrsqhQpp", "Aom66hxpQLFbCZfWch5fmSF9ltnYr1u0e6SjQkXF");

    }
}
