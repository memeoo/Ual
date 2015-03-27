package sns.meme.ual.base;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import sns.meme.ual.model.UalMember;

/**
 * Created by Meme on 2015-03-13.
 */
public class UalApplication extends Application {

    private UalDao mDao;

    @Override
    public void onCreate() {
        super.onCreate();

        mDao = new UalDao(this);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(UalMember.class);
        Parse.initialize(this, "oR56d7okbrgB3IVamRsw2Cea6uIyUKFbvrsqhQpp", "Aom66hxpQLFbCZfWch5fmSF9ltnYr1u0e6SjQkXF");

    }

    public UalDao getDao() {
        return mDao;
    }
}
