package sns.meme.ual.base;

import android.app.Application;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.parse.Parse;
import com.parse.ParseObject;

import sns.meme.ual.model.UalMember;

/**
 * Created by Meme on 2015-03-13.
 */
public class UalApplication extends Application {

    private UalDao mDao;
    final static private String APP_KEY = "ldiwgq2of5c6p7p";
    final static private String APP_SECRET = "bl0yov8myn9hnmn";
    private DropboxAPI<AndroidAuthSession> mDBApi;

    @Override
    public void onCreate() {
        super.onCreate();

        mDao = new UalDao(this);

        // Enable Parse.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(UalMember.class);
        Parse.initialize(this, "oR56d7okbrgB3IVamRsw2Cea6uIyUKFbvrsqhQpp", "Aom66hxpQLFbCZfWch5fmSF9ltnYr1u0e6SjQkXF");

        // Enable DropBox
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

    }

    public UalDao getDao() {
        return mDao;
    }
    public DropboxAPI<AndroidAuthSession> getDropboxAPI(){
        return mDBApi;
    }
}
