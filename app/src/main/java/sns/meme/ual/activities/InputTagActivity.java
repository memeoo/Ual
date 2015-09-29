package sns.meme.ual.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;
import com.parse.SaveCallback;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import sns.meme.ual.R;
import sns.meme.ual.base.Common;
import sns.meme.ual.model.UalMember;

//import com.project.whatthehell.R;
//import com.project.whatthehell.activity.InputNickNameActivity.ServerConnectionTask;
//import com.project.whtthehell.common.Common;
//import com.project.whtthehell.common.MakeServerConnection;

public class InputTagActivity extends Activity {

    private Button btnInput, btnFinishTag;
    private EditText edInputTag;
    private LinearLayout llAddedTag;
    private ArrayList<String> tagArr;
    private ParseObject tagParse;
    private ParseQuery tagQuery;
    private String finalTagStr;
//	private MakeServerConnection tagAddConnect, tagFetchConnect;

    // private ProgressDialog mProgress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;

        getWindow().setAttributes(layoutParams);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting);

        edInputTag = (EditText) findViewById(R.id.edInputTag);
        btnInput = (Button) findViewById(R.id.btnInput);
        llAddedTag = (LinearLayout) findViewById(R.id.llAddedInputTag);
        btnFinishTag = (Button) findViewById(R.id.btnFinishTag);

        tagArr = new ArrayList<String>();

//		ArrayList<String> fetchTagInfo = new ArrayList<String>();
//		fetchTagInfo.add(Common.nickName);

        Log.d("meme", " Common.nickNmae => " + Common.nickName);


        tagParse = new ParseObject("Tag");
//		tagFetchConnect = new MakeServerConnection(fetchTagInfo,
//				Common.BASIC_URL + Common.FETCH_TAG_PAGE,
//				Common.FETCH_TAG_KEYWORD);

//		new ServerConnectionTask().execute("fetchTagInfo");
        Log.d("meme", " InputTagActivity Common.memberMe >>>> " + Common.memberMe.getObjectId());

        tagQuery = ParseQuery.getQuery("Tag");
        tagQuery.whereEqualTo("member", Common.memberMe);
        tagQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

                if(e == null) {
                    String tagNotSplited = parseObject.get("tag").toString();
                    String[] eachTags = tagNotSplited.split("#");

                    for (int i = 0; i < eachTags.length; i++) {
                        tagArr.add(eachTags[i]);
                        TextView addedTV = new TextView(
                                InputTagActivity.this);
                        addedTV.setLayoutParams(new LinearLayout.LayoutParams(
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.MATCH_PARENT));
                        addedTV.setTextSize(15);
                        addedTV.setPadding(20, 0, 15, 0);
                        addedTV.setText(eachTags[i]);

                        addedTV.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                llAddedTag.removeView(v);

                                tagArr.remove(((TextView) v).getText().toString());
                            }
                        });

                        llAddedTag.addView(addedTV);
                    }
                }

            }


        });


        btnInput.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView addedTV = new TextView(InputTagActivity.this);
                addedTV.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
                addedTV.setTextSize(15);
                addedTV.setPadding(20, 0, 15, 0);
                addedTV.setText(edInputTag.getText().toString());
                llAddedTag.addView(addedTV);
                tagArr.add(edInputTag.getText().toString());

                addedTV.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llAddedTag.removeView(v);
                        tagArr.remove(((TextView) v).getText().toString());
                    }
                });
                edInputTag.setText("");

            }
        });

        btnFinishTag.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String tagStr = "";
                for (int i = 0; i < tagArr.size(); i++) {
                    tagStr = tagStr + tagArr.get(i) + "#";
                }

                ParseQuery<ParseObject> tagQuery = ParseQuery.getQuery("Tag");
                tagQuery.whereEqualTo("member", Common.memberMe);
                tagQuery.whereEqualTo("isQuestion", "NO");


                finalTagStr = tagStr;
                tagQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {
                            Log.d("meme", " update !! ");

                            parseObject.put("tag", finalTagStr);
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    String [] tags = finalTagStr.split("#");

                                    for(String tag: tags) {
                                        ParsePush.subscribeInBackground(tag);
                                    }

                                    finish();
                                }
                            });

                        } else {
                            Log.d("meme", "exception => " + e.toString());
                            Log.d("meme", " insert !! ");
                            Log.d("meme", " finalTagStr => " + finalTagStr);
                            Log.d("meme", " Common.nickName => " + Common.nickName);
                            Log.d("meme", " insert !!!!!!! ");


                            ParseObject pObj = new ParseObject("Tag");
                            pObj.put("tag", finalTagStr);
                            pObj.put("member", Common.memberMe);
                            pObj.put("isQuestion", "NO");

                            pObj.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    finish();
                                }
                            });
                        }
                    }
                });
//				ArrayList<String> tagInfo = new ArrayList<String>();
//				tagInfo.add(tagStr);
//				tagInfo.add(Common.nickName);
//				tagInfo.add("N"); // isQuestion
//
//				tagAddConnect = new MakeServerConnection(tagInfo,
//						Common.BASIC_URL + Common.TAG_PAGE, Common.TAG_KEYWORD);
//				new ServerConnectionTask().execute("tag");

            }
        });

    };

//	class ServerConnectionTask extends AsyncTask<String, Void, String> {
//		private JSONObject jReader = null;
//		private JSONArray jArray = null;
//		private boolean isNoTag;
//
//		@Override
//		protected String doInBackground(String... urls) {
//
//			String result = "";
//
//			try {
//				// Data Parsing From Server
//				if (urls[0].equalsIgnoreCase("tag")) {
//					result = tagAddConnect.sendData();
//					jReader = new JSONObject(result);
//				} else {
//					result = tagFetchConnect.sendData();
//					jReader = new JSONObject(result);
//					jArray = jReader.getJSONArray("fetchTag");
//
//				}
//				Log.d("meme", " do in background result => " + result);
//
//			}catch (JSONException e) {
//				isNoTag = true;
//			}
//			catch (Exception e) {
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
//				if (result.contains("fetchTag")) {
//					if (!isNoTag) {
//						for (int i = 0; i < jArray.length(); i++) {
//							try {
//								tagArr.add(jArray.getJSONObject(i).optString("tag"));
//
//								TextView addedTV = new TextView(
//										InputTagActivity.this);
//								addedTV.setLayoutParams(new LinearLayout.LayoutParams(
//										LayoutParams.WRAP_CONTENT,
//										LayoutParams.MATCH_PARENT));
//								addedTV.setTextSize(15);
//								addedTV.setPadding(20, 0, 15, 0);
//								addedTV.setText(tagArr.get(i));
//
//								addedTV.setOnClickListener(new OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										llAddedTag.removeView(v);
//										tagArr.remove(((TextView) v).getText().toString());
//									}
//								});
//
//								llAddedTag.addView(addedTV);
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					}
//				} else if (result.contains("inputTag")) {
//					finish();
//				}
//
//			} else {
//				serverResponseMessage = "죄송합니다. 네트워크 및 서버 오류 입니다. 잠시 후 다시 입력 부탁드립니다.";
//			}
//			Log.d("meme", " $$$$$ " + serverResponseMessage);
//		}
//	}

}
