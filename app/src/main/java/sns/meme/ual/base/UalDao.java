package sns.meme.ual.base;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Meme on 2015-03-17.
 */
public class UalDao {

    private UalApplication mApplications;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public UalDao(UalApplication apps) {
        this.mApplications = apps;
        this.mSharedPreferences = getSharedPreferences();
        this.mEditor = mSharedPreferences.edit();
    }

    private SharedPreferences getSharedPreferences() {
        if (mSharedPreferences == null)
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mApplications);
        return mSharedPreferences;
    }

}
