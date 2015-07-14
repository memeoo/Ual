package sns.meme.ual.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import sns.meme.ual.R;
import sns.meme.ual.base.Common;
import sns.meme.ual.model.UalMember;


//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.project.whatthehell.R;
//import com.project.whatthehell.activity.QuestionActivity.ServerConnectionTask;
//import com.project.whatthehell.ui.MyAlertDialog;
//
//import com.project.whtthehell.common.FtpConnect;
//import com.project.whtthehell.common.MakeServerConnection;

public class InputNickNameActivity extends Activity {

    private Button btnDone, btnLogin;
    private EditText edInputNick, edInputNickLogin;
    private String nickName;

    //	private MakeServerConnection memberAddConnect;
//	private ProgressDialog mProgress;
    private ArrayList<String> memberInfo;

    //	private GoogleCloudMessaging gcm;
    private AtomicInteger msgId = new AtomicInteger();
    private SharedPreferences prefs;
    private Context context;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String regid;
    private UalMember memberObject;

    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_nick);

        edInputNick = (EditText) findViewById(R.id.edInputNick);
        btnDone = (Button) findViewById(R.id.btnDone);
        memberObject = new UalMember();

        edInputNickLogin = (EditText) findViewById(R.id.edInputNickLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnDone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                nickName = edInputNick.getText().toString();


                Log.d("meme", " phoneNum => " + Common.phoneNum);
                Log.d("meme", " nickName => " + Common.nickName);

                ParseQuery memberQuery = ParseQuery.getQuery("UalMember");
                memberQuery.whereEqualTo("nickName", nickName);
                memberQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {

                        if (e == null) {
                            Log.d("meme", " list Size => " + list.size());
                            if (list.size() > 0) {
                                Toast.makeText(getBaseContext(), "This ID is already being used ", Toast.LENGTH_SHORT).show();
                            } else {

                                Common.savePreferences(getBaseContext(), "nickName", nickName);
                                Common.nickName = Common.getPreferences(getBaseContext(), "nickName");

                                TelephonyManager telManager = (TelephonyManager) getBaseContext().getSystemService(getBaseContext().TELEPHONY_SERVICE);
                                Common.phoneNum = telManager.getLine1Number().substring(1, telManager.getLine1Number().length());

                                memberObject.put("phoneNum", Common.phoneNum);
                                memberObject.put("nickName", Common.nickName);

                                memberObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            Log.d("meme", " e => " + e.toString());
                                        } else {

                                            Common.savePreferences(getBaseContext(), "phoneNum", Common.phoneNum);
                                            Common.savePreferences(getBaseContext(), "nickName", Common.nickName);

                                            // Push 등록
//                                            ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
//                                            currentInstallation.put("phoneNum", Common.phoneNum);
//                                            currentInstallation.saveInBackground();
                                            // ==========

                                            Intent intent = new Intent(getBaseContext(), BoardActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.d("meme", " e=> " + e.toString());
                        }
                    }

//                    @Override
//                    public void done(Object o, Throwable throwable) {
//                        Log.d("meme", " throwable => ");
//                    }

                });

            }
        });

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String memberId = edInputNickLogin.getText().toString();
                ParseQuery memberQuery = ParseQuery.getQuery("UalMember");
                memberQuery.whereEqualTo("nickName", memberId);
                memberQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if(e == null){
                            Common.savePreferences(getBaseContext(), "nickName", memberId);
                            Common.nickName = Common.getPreferences(getBaseContext(), "nickName");

                            TelephonyManager telManager = (TelephonyManager) getBaseContext().getSystemService(getBaseContext().TELEPHONY_SERVICE);
                            Common.phoneNum = telManager.getLine1Number().substring(1, telManager.getLine1Number().length());

                            Common.savePreferences(getBaseContext(), "phoneNum", Common.phoneNum);
                            Common.savePreferences(getBaseContext(), "nickName", Common.nickName);

                            Intent intent = new Intent(getBaseContext(), BoardActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(getBaseContext(),"해당 ID가 없습니다. 다시 입력해 주시거나 ID를 등록해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

            }
        });

    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


}
