package sns.meme.ual.activities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import sns.meme.ual.R;
import sns.meme.ual.base.Common;


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
	
	private Button btnDone;
	private EditText edInputNick;
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
	
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		layoutParams.dimAmount = 0.7f;
		getWindow().setAttributes(layoutParams);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_nick);
		
		edInputNick = (EditText)findViewById(R.id.edInputNick);
		btnDone = (Button)findViewById(R.id.btnDone);
		
//		if(checkPlayServices()){
//			Log.d("meme", "Keep Going~ !");
//
////			gcm = GoogleCloudMessaging.getInstance(this);
//            regid = getRegistrationId(getBaseContext());
//
//            if (regid.isEmpty()) {
//                registerInBackground();
//            }
//
//		}else{
//			MyAlertDialog mDailog = new MyAlertDialog(InputNickNameActivity.this,
//					"구를  Play App을 다운 받으셔야 합니다.",
//					"확인",
//					new DialogInterface.OnClickListener(){
//						public void onClick(DialogInterface dialog,int whichButton){
//							InputNickNameActivity.this.finish();
//						}
//				}
//			);
//			mDailog.getDialog().show();
//		}
		 
		btnDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nickName = edInputNick.getText().toString();
				Common.savePreferences(getBaseContext(), "nickName", nickName);
				Common.nickName = Common.getPreferences(getBaseContext(), "nickName"); 
				
				TelephonyManager telManager = (TelephonyManager)getBaseContext().getSystemService(getBaseContext().TELEPHONY_SERVICE); 
				Common.phoneNum = telManager.getLine1Number().substring(1, telManager.getLine1Number().length());
                Common.savePreferences(getBaseContext(), "phoneNum", Common.phoneNum);

				Log.d("meme", " phoneNum => " + Common.phoneNum);
				Log.d("meme", " nickName => " + Common.nickName);

                ParseObject memberObject = new ParseObject("Member");

                memberObject.put("phoneNum", Common.phoneNum);
                memberObject.put("nickName", Common.nickName);


                memberObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null){
                            if(e.getCode() == 111){
                                Toast.makeText(getBaseContext(),"Already Registered !!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Intent intent = new Intent(getBaseContext(), BoardActivity.class);
                            startActivity(intent);
                        }
                    }
                });

//                ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
//                // Retrieve the object by id
//                query.getInBackground("UKwqrIBUII", new GetCallback<ParseObject>() {
//                    public void done(ParseObject member, ParseException e) {
//                        if (e == null) {
//                           Log.d("meme", " nickName => " + member.get("nickName"));
//
//                        }else{
//
//                        }
//                    }
//                });

				
//				memberInfo = new ArrayList<String>();
//				memberInfo.add(nickName);
//				memberInfo.add(Common.phoneNum);   // Need to change ID after finishing login logic.
//				memberInfo.add(regid);            // gcmID input
//
//				memberAddConnect = new MakeServerConnection(memberInfo, Common.BASIC_URL
//						+ Common.MEMBER_PAGE, Common.MEMBER_KEYWORD);
//
//				new ServerConnectionTask().execute("member");
				
				
			}
		});

	};
	
//	private boolean checkPlayServices() {
//	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//	    if (resultCode != ConnectionResult.SUCCESS) {
//	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
//	        } else {
//	            Log.i("meme", "This device is not supported.");
//	            finish();
//	        }
//	        return false;
//	    }
//	    return true;
//	}
	
//	@SuppressLint("NewApi")
//	private String getRegistrationId(Context context) {
//	    final SharedPreferences prefs = getGCMPreferences(context);
//	    String registrationId = prefs.getString(Common.PROPERTY_REG_ID, "");
//
//	    if (registrationId.isEmpty()) {
//	        Log.i("meme", "Registration not found.");
//	        return "";
//	    }
//	    // Check if app was updated; if so, it must clear the registration ID
//	    // since the existing regID is not guaranteed to work with the new
//	    // app version.
//	    int registeredVersion = prefs.getInt(Common.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
//	    int currentVersion = getAppVersion(context);
//	    if (registeredVersion != currentVersion) {
//	        Log.i("meme", "App version changed.");
//	        return "";
//	    }
//	    return registrationId;
//	}
//
//	private SharedPreferences getGCMPreferences(Context context) {
//	    // This sample app persists the registration ID in shared preferences, but
//	    // how you store the regID in your app is up to you.
//	    return getSharedPreferences(QuestionActivity.class.getSimpleName(),
//	            Context.MODE_PRIVATE);
//	}
	
//	private void registerInBackground() {
//
//		new AsyncTask<String, Void, String>() {
//			@Override
//			protected String doInBackground(String... params) {
//				String msg = "";
//				Context context = getApplicationContext();
//				try {
//					if (gcm == null) {
//						gcm = GoogleCloudMessaging.getInstance(context);
//					}
//
//					regid = gcm.register(Common.SENDER_ID);
//					msg = "Device registered, registration ID=" + regid;
//
////					sendRegistrationIdToBackend();
//
//					storeRegistrationId(context, regid);
//				} catch (IOException ex) {
//					msg = "Error :" + ex.getMessage();
//				}
//				return msg;
//			}
//
//			protected void onPostExecute(String result) {
//				Log.d("meme", " msg => " + result);
//				// msgText.append(result.toString() + "\n");
//			};
//		}.execute(null, null, null);
//	}
	
//	private void sendRegistrationIdToBackend() {
//	    // Your implementation here.
//	}
	
//	private void storeRegistrationId(Context context, String regId) {
//	    final SharedPreferences prefs = getGCMPreferences(context);
//
//	    int appVersion = getAppVersion(context);
//
//	    Log.i("meme", "Saving regId on app version " + appVersion);
//	    SharedPreferences.Editor editor = prefs.edit();
//	    editor.putString(Common.PROPERTY_REG_ID, regId);
//	    editor.putInt(Common.PROPERTY_APP_VERSION, appVersion);
//	    editor.commit();
//	}
	
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
	
//	class ServerConnectionTask extends AsyncTask<String, Void, String> {
//		private JSONObject jReader = null;
//
//		@Override
//		protected String doInBackground(String... urls) {
//
//			String result = "";
//			try {
//				// Data Parsing From Server
//				result = memberAddConnect.sendData();
//				jReader = new JSONObject(result);
//				Log.d("meme", " do in background result => " + result);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//
//			String serverResponseMessage = "";
//			Log.d("meme", " result >>> " + result);
//
//			if (result.contains("succeed")) {
//				serverResponseMessage = "성공적으로 입력되었습니다."; // DB 입력 완료 후
//
//				Common.savePreferences(getBaseContext(), "phoneNum", Common.phoneNum);
//				Common.savePreferences(getBaseContext(), "nickName", Common.nickName);
//
////				mProgress.dismiss();
////				Toast.makeText(InputNickNameActivity.this, "업로드 완료 되었습니다.", Toast.LENGTH_SHORT).show();
//
//				// 여기
//				Intent intent = new Intent(InputNickNameActivity.this,BoardActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//				finish();
//
//			} else {
//				serverResponseMessage = "죄송합니다. 네트워크 및 서버 오류 입니다. 잠시 후 다시 입력 부탁드립니다.";
//			}
//			Log.d("meme", " $$$$$ " + serverResponseMessage);
//		}
//	}

}
