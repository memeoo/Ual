package sns.meme.ual.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sns.meme.ual.R;
import sns.meme.ual.base.Common;
import sns.meme.ual.model.Question;
import sns.meme.ual.model.UalMember;


public class BoardActivity extends UalActivity implements View.OnClickListener {

    private EditText edSearch;
    private Uri mImageCaptureUri, cropUri;
    private Button btnSearch, btnRefresh, btnCamera, btnGallery, btnSetting;
    private GridView grMain;
    private String imgURL;
    private ArrayList<String> imgFetchInfo;
    private ArrayList<Bitmap> questionImgArr;
    boolean isFromGallery;
    private ParseQuery imgFileQuery;

//    private MakeServerConnection fetchImgNameConnect;
    private ProgressDialog mProgress;
    private String mainShowType;
    private int pageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        edSearch = (EditText) findViewById(R.id.edSearch);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        btnSetting = (Button)findViewById(R.id.btnSetting);

        btnSearch.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnSetting.setOnClickListener(this);

        Common.nickName = Common.getPreferences(getBaseContext(),"nickName");
        Common.phoneNum = Common.getPreferences(getBaseContext(),"phoneNum");

//		imgURL = Common.IMG_FILE_PATH + phoneNum + "/" + regId + "/_";

        imgFetchInfo = new ArrayList<String>();
        mainShowType = "all";
        pageNum = 1;
        imgFetchInfo.add(pageNum+"");
        imgFetchInfo.add(mainShowType);

        if(Common.getPreferences(this,"DropBoxAccessToken") == null){
            mApp.getDropboxAPI().getSession().startOAuth2Authentication(BoardActivity.this);
        }


        ParseQuery meQuery = ParseQuery.getQuery("UalMember");
        meQuery.whereEqualTo("nickName", Common.nickName);
        meQuery.getFirstInBackground(new GetCallback() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Log.d("meme", " getFirstInBackground 11111 ");

            }

            @Override
            public void done(Object o, Throwable throwable) {
                Log.d("meme", " getFirstInBackground 22222 ");
                Common.memberMe = (UalMember)o;
                Log.d("meme", " BoardActivity Common.memberMe >>>> " + Common.memberMe.getObjectId());
            }
        });


        questionImgArr = new ArrayList<Bitmap>();
//        Common.setImgLoader(this);
//        fetchImgNameConnect = new MakeServerConnection(imgFetchInfo, Common.BASIC_URL
//                + Common.IMG_NAME_PAGE, Common.IMGFETCH_KEYWORD);
//        new ServerConnectionTask().execute("imgFetch");
        grMain = (GridView) findViewById(R.id.glboard);

//        ParseCloud.callFunctionInBackground("hello", new HashMap<String, Object>(), new FunctionCallback<String>() {
//            public void done(String result, ParseException e) {
//                if (e == null) {
//                    Toast.makeText(getBaseContext(),result,Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        String url = "";

        switch (v.getId()) {
            case R.id.btnSearch:

                break;
            case R.id.btnRefresh:

                break;
            case R.id.btnCamera:
                // 카메라를 호출한다.
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                url = "camera_" + String.valueOf(System.currentTimeMillis())+Common.nickName
                        + ".jpg";
                mImageCaptureUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), url));
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                        mImageCaptureUri);

                Log.d("meme", " mImageCaptureUri => " + mImageCaptureUri.toString());

                startActivityForResult(intent, Common.PICK_FROM_CAMERA);

                break;
            case R.id.btnGallery:
                // 갤러리에서 가져온다.
                intent = new Intent(Intent.ACTION_PICK);

                url = "gallery_" + String.valueOf(System.currentTimeMillis())+ Common.nickName
                        + ".jpg";
//                url = "tmp_" + String.valueOf(System.currentTimeMillis())
//                        + ".jpg";

                mImageCaptureUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), url));

                Log.d("meme", " mImageCaptureUri => " + mImageCaptureUri.toString());

                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, Common.PICK_FROM_ALBUM);
                break;
            case R.id.btnSetting:
                intent = new Intent(BoardActivity.this, InputTagActivity.class);
                startActivity(intent);

                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case Common.CROP_FROM_CAMERA:

                final Bundle extras = data.getExtras();
                Log.d("meme", "CROP_FROM_CAMERA ");
                if (extras != null) {
                    Intent moveIntent = new Intent(this, QuestionActivity.class);

                    if(isFromGallery) {
                        Log.d("meme", " Gallery cropUri.getPath() >>>>>> " + cropUri.getPath());
                        Log.d("meme", " Gallery mImageCaptureUri.getPath() >>>>>> " + mImageCaptureUri.getPath());

                        moveIntent.putExtra("crop", cropUri.getPath());
                    }else{
//                        Log.d("meme", " Camera cropUri.getPath() >>>>>> " + cropUri.getPath());
                        Log.d("meme", " Camera mImageCaptureUri.getPath() >>>>>> " + mImageCaptureUri.getPath());
                        moveIntent.putExtra("crop", mImageCaptureUri.getPath());
                    }

                    moveIntent.putExtra("uri", mImageCaptureUri);
                    startActivity(moveIntent);
                    finish();
                }
                break;
            case Common.PICK_FROM_ALBUM: {
                // 이후의 처리가 카메라와 같으므로 일단 break없이 진행합니다.
                Log.d("meme", "PICK_FROM_ALBUM ");
                mImageCaptureUri = data.getData();
                File original_file = getImageFile(mImageCaptureUri);
                Log.d("meme", "original_file =>  " + original_file.toString());
                isFromGallery = true;
                cropUri = createSaveCropFile();
                File copy_file = new File(cropUri.getPath());

                Log.d("meme", "cpoy_file =>  " + copy_file.toString());
                // SD카드에 저장된 파일을 이미지 Crop을 위해 복사한다.
                boolean isCopySuccess = copyFile(original_file, copy_file);

                Log.d("meme", "isCopySuccess =>  " + isCopySuccess);
            }

            case Common.PICK_FROM_CAMERA: {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
                Log.d("meme", "PICK_FROM_CAMERA ");
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");
                intent.putExtra("output", mImageCaptureUri);
                startActivityForResult(intent, Common.CROP_FROM_CAMERA);

                break;
            }
        }
    }

    public static boolean copyFile(File srcFile, File destFile) {
        boolean result = false;
        try {
            InputStream in = new FileInputStream(srcFile);
            try {
                result = copyToFile(in, destFile);
            } finally {
                in.close();
            }
        } catch (IOException e) {
            Log.d("meme", " Exception e >>>> " + e.toString());
            result = false;
        }
        return result;
    }

    private static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            OutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.close();
            }
            return true;
        } catch (IOException e) {
            Log.d("meme", " IOException e >>>> " + e.toString());
            return false;
        }
    }

    private Uri createSaveCropFile() {
        Uri uri;
        String url = "tmp_" + String.valueOf(System.currentTimeMillis())+Common.nickName
                + ".jpg";
        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                url));
        return uri;
    }

    private File getImageFile(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor mCursor = getContentResolver().query(uri, projection, null,
                null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (mCursor == null || mCursor.getCount() < 1) {
            return null; // no cursor or no record
        }
        int column_index = mCursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();

        String path = mCursor.getString(column_index);

        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }

        return new File(path);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mApp.getDropboxAPI().getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mApp.getDropboxAPI().getSession().finishAuthentication();

                String accessToken = mApp.getDropboxAPI().getSession().getOAuth2AccessToken();

                Common.savePreferences(this,"DropBoxAccessToken", accessToken);

            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<Bitmap> mThumbIds;

        public ImageAdapter(Context c, ArrayList<Bitmap>bitmapList){
            mContext = c;
            mThumbIds = bitmapList;
        }
        public int getCount(){
            return mThumbIds.size();
        }
        public Object getItem(int position){
            return null;
        }
        public long getItemId(int position){
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ImageView imageView;
            if(convertView == null){
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            }
            else{
                imageView = (ImageView) convertView;
            }

            imageView.setImageBitmap(mThumbIds.get(position));
            return imageView;
        }


    }

    class ServerConnectionTask extends AsyncTask<String, Void, String> {

        private ArrayList<Question>questionArr;
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            try {
                // Data Parsing From Server

                imgFileQuery = ParseQuery.getQuery("Question");
                imgFileQuery.findInBackground(new FindCallback() {
                    @Override
                    public void done(List list, ParseException e) {
                        for(int i=0 ; i < list.size(); i++) {
                            list.get(i).getClass();
                        }
                    }

                    @Override
                    public void done(Object o, Throwable throwable) {

                    }
                });

                File file = new File("/magnum-opus.txt");
                FileOutputStream outputStream = new FileOutputStream(file);
                DropboxAPI.DropboxFileInfo info = mApp.getDropboxAPI().getFile("/magnum-opus.txt", null, outputStream, null);
                Log.d("DbExampleLog", "The file's rev is: " + info.getMetadata().rev);

                questionArr = new ArrayList<Question>();


                for(int i=0; i < jArray.length(); i++){

                    Question question = new Question(
                            jArray.getJSONObject(i).getString("qId"),
                            jArray.getJSONObject(i).getString("qText"),
                            jArray.getJSONObject(i).getString("questioner"),
                            jArray.getJSONObject(i).getString("qTime"),
                            jArray.getJSONObject(i).getString("aTime"),
                            jArray.getJSONObject(i).getString("answerer"),
                            jArray.getJSONObject(i).getString("imgName"));

                    questionArr.add(question);

                    questionImgArr.add(Common.getImgFromServer(getBaseContext(), Common.IMG_FILE_PATH + jArray.getJSONObject(i).getString("imgName")));
                }

                // Fetch images ==================


                // ====================

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
                Toast.makeText(BoardActivity.this, "Hello !!",
                        Toast.LENGTH_SHORT).show();

                // 여기
                grMain.setAdapter(new ImageAdapter(getBaseContext(), questionImgArr));
                grMain.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), DetailEachActivity.class);
                        startActivity(intent);

                    }
                });

            } else {
                serverResponseMessage = "죄송합니다. 네트워크 및 서버 오류 입니다. 잠시 후 다시 입력 부탁드립니다.";
            }
            Log.d("meme", " $$$$$ " + serverResponseMessage);
        }
    }

}
