package sns.meme.ual.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.parse.ParseInstallation;

import sns.meme.ual.R;
import sns.meme.ual.base.Common;

public class LoadingActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_main);


        Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                if(!Common.getPreferences(LoadingActivity.this, "phoneNum").equalsIgnoreCase(Common.defVal)){
                    Intent intent = new Intent(LoadingActivity.this,BoardActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(LoadingActivity.this, InputNickNameActivity.class);
                    startActivity(intent);
                    finish();
                }

                super.handleMessage(msg);
            }

        };

        handler.sendEmptyMessageDelayed(0, 700);

    }
}

