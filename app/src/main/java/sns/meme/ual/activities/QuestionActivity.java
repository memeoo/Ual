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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import sns.meme.ual.R;
import sns.meme.ual.base.Common;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.project.whatthehell.R;
//import com.project.whatthehell.ui.MyAlertDialog;
//import com.project.whtthehell.common.Common;
//import com.project.whtthehell.common.FtpConnect;
//import com.project.whtthehell.common.MakeServerConnection;

public class QuestionActivity extends Activity {

	private int requestCode;
	// private Uri mImageCaptureUri;
	private String savePath;
	private ImageView imgQuestion;
	private EditText edTag, edQuestion;
	private Button btnQuestion, btnInput;
//	private MakeServerConnection questionRegisterConnect;
	private ProgressDialog mProgress;
	private ArrayList<String> qustionInfo;
	private ArrayList<File> photoToUploadArr;
	private Uri mImageCaptureUri;
	private File photoFile;
	private LinearLayout llAddedTag;
	private ArrayList<String> tagArr;
    private ParseObject questionObj;

	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		imgQuestion = (ImageView) findViewById(R.id.imgQuestion);

		savePath = getIntent().getStringExtra("crop");
		mImageCaptureUri = getIntent().getParcelableExtra("uri");
		
		Log.d("meme", " mImageCaptureUri => " + mImageCaptureUri);
		
		Bitmap photo = BitmapFactory.decodeFile(savePath);
		imgQuestion.setImageBitmap(photo);
		
		photoToUploadArr = new ArrayList<File>();
		
		edTag = (EditText)findViewById(R.id.edTag);
		edQuestion = (EditText)findViewById(R.id.edQuestion);
		btnQuestion = (Button)findViewById(R.id.btnQuestion);
		btnInput = (Button)findViewById(R.id.btnInput);
		
		llAddedTag = (LinearLayout)findViewById(R.id.lladded);
		tagArr = new ArrayList<String>();
		
		mProgress = new ProgressDialog(QuestionActivity.this);
		mProgress.setTitle("FTP Status");
		mProgress.setMessage("Uploading...");
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		mProgress.setButton(-1, "Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						mProgress.dismiss();
					}

				});
		
		btnInput.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView addedTV = new TextView(QuestionActivity.this);
				addedTV.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
				addedTV.setTextSize(15);
				addedTV.setPadding(20, 0, 15, 0);
				addedTV.setText(edTag.getText().toString());
				llAddedTag.addView(addedTV);
				tagArr.add(edTag.getText().toString());
				
				addedTV.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						llAddedTag.removeView(v);
						tagArr.remove(((TextView) v).getText().toString());
					}
				});
				edTag.setText("");
				
			}
		});
		
		btnQuestion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				try {
					String tagStr = "";
					for(int i=0; i < tagArr.size() ; i++){
						tagStr = tagArr.get(i) + "#";
					}

                    questionObj = new ParseObject("Question");
                    questionObj.put("Question", edQuestion.getText().toString());
                    questionObj.put("Questioner",Common.nickName);
                    questionObj.put("QuestionTag", tagStr);
//                    questionObj.put("")



//					String questionStr = edQuestion.getText().toString();
//
//					qustionInfo = new ArrayList<String>();
//					qustionInfo.add(questionStr);
//					qustionInfo.add(tagStr);
//					qustionInfo.add(Common.nickName);   // Need to change ID after finishing login logic.
//					qustionInfo.add(Common.getStrNow() + "_"+ Common.phoneNum + "_");
//
//					questionRegisterConnect = new MakeServerConnection(qustionInfo, Common.BASIC_URL
//							+ Common.QUESTION_PAGE, Common.QUESTION_KEYWORD);
					
//					new ServerConnectionTask().execute("question");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
		
	}
	
	
	
	class ServerConnectionTask extends AsyncTask<String, Void, String> {
		private JSONObject jReader = null;

		@Override
		protected String doInBackground(String... urls) {

			String result = "";
			try {
				// Data Parsing From Server
//				result = questionRegisterConnect.sendData();
//				jReader = new JSONObject(result);
//				Log.d("meme", " do in background result => " + result);
//
//				String qId = jReader.getJSONArray("question").getJSONObject(0)
//						.get("qId").toString();
//
//				if (mImageCaptureUri.getPath().contains("/external/")) {
//					photoFile = new File(getRealPathFromURI(mImageCaptureUri));
//					Log.d("meme", " xxxxxxx 1 => " + photoFile.toURI());
//				} else {
//					photoFile = new File(mImageCaptureUri.getPath());
//					Log.d("meme", " xxxxxxx 2 => " + photoFile.toURI());
//				}
//
//				photoToUploadArr.add(photoFile);
//				FtpConnect ftpCon = new FtpConnect(photoToUploadArr);
//
//				if (ftpCon.openFTP(Common.phoneNum, qId )) {
//					Log.d("meme", " FTP OPEN SUCCEED !! ");
//					for (int i = 0; i < photoToUploadArr.size(); i++) {
//						if (ftpCon.uploadFTP(photoToUploadArr.get(i), "_" + Integer.toString(i + 1) + ".jpg")) {
//							Log.d("meme", " FTP UPLOAD SUCCEED !! ");
//						} else {
//							Log.d("meme", " FTP UPLOAD FAIL !! ");
//						}
//					}
//				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			String serverResponseMessage = "";
			Log.d("meme", " result >>> " + result);

			if (result.contains("succeed")) {
				serverResponseMessage = "성공적으로 입력되었습니다."; // DB 입력 완료 후

				mProgress.dismiss();
				Toast.makeText(QuestionActivity.this, "업로드 완료 되었습니다.",
						Toast.LENGTH_SHORT).show();

				// 여기

				Intent intent = new Intent(QuestionActivity.this,
						BoardActivity.class);

				startActivity(intent);
				finish();

			} else {
				serverResponseMessage = "죄송합니다. 네트워크 및 서버 오류 입니다. 잠시 후 다시 입력 부탁드립니다.";
			}
			Log.d("meme", " $$$$$ " + serverResponseMessage);
		}
	}
	
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
}
