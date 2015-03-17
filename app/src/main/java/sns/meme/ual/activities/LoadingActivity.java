package sns.meme.ual.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Meme on 2015-03-17.
 */
public class LoadingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_main);

        Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                Log.d("meme", " sharedPreference => " + Common.getPreferences(LoadingActivity.this, "phoneNum"));
                Log.d("meme", " defVal => " + Common.defVal);
                Log.d("meme", " PROPERTY_REG_ID => " + Common.PROPERTY_REG_ID);

                if(!Common.getPreferences(LoadingActivity.this, "phoneNum").equalsIgnoreCase(Common.defVal)){
                    Intent intent = new Intent(LoadingActivity.this,BoardActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(LoadingActivity.this, InputNickNameActivity.class);
                    startActivity(intent);

                }

                super.handleMessage(msg);
            }

        };

        handler.sendEmptyMessageDelayed(0, 700);

    }
}

