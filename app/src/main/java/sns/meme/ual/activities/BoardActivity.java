package sns.meme.ual.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SendCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import sns.meme.ual.R;
import sns.meme.ual.base.Common;
import sns.meme.ual.base.UalApplication;
import sns.meme.ual.model.UalMember;


public class BoardActivity extends UalActivity implements View.OnClickListener {

    private EditText edSearch;
    private Uri mImageCaptureUri, cropUri;
    private Button btnSearch, btnRefresh, btnSetting;
    private LinearLayout btnCamera, btnGallery;
    private GridView grMain;
    private ArrayList<Bitmap> questionImgArr;
    private ArrayList<String> objIdArr;
    boolean isFromGallery;
    private ParseQuery imgFileQuery;
    private static final String TAG = BoardActivity.class.getSimpleName();

    private Activity activity;

    private ParseInstallation currentInstallation;
    private int BITMAP_WIDTH = 0, BITMAP_HEIGHT = 0;


    private ImageLoader imgLoader;
    private static final int FIRST_SHOW_COUNT = 18;
    private static final int IMG_CNT_PER_ONE_SCROLL = 18;
    private static final int MAX_CNT_TOSHOW = 120;

    private ImageAdapter imgAdp;
    private int currentQuriedCnt, stackedQuriedCnt;
    private int scrollCnt = 1;
    private String searchKeyword="";
    private int activityIndex;

    interface OnFinishDownload {
        void onFinish();
    }

    private OnFinishDownload ofd;

    public void setOnFinishDownload(OnFinishDownload of) {
        ofd = of;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setImageLoader();
        setCommonVarilable();
        meQuery();
        imgFileQuery(0,FIRST_SHOW_COUNT);
    }

    public void setView() {
        activity = BoardActivity.this;
        setContentView(R.layout.activity_board);
        activityIndex = 0; // onBackPressed 에서 활용: 백버튼 눌렀을떼 종료 처리 하기 위해

        edSearch = (EditText) findViewById(R.id.edSearch);
        edSearch.setHint("검색어로 찾아 보세요");
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        btnCamera = (LinearLayout) findViewById(R.id.btnCamera);
        btnGallery = (LinearLayout) findViewById(R.id.btnGallery);
        btnSetting = (Button) findViewById(R.id.btnSetting);

        btnSearch.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        edSearch.setOnClickListener(this);

        grMain = (GridView) findViewById(R.id.glboard);

        BITMAP_WIDTH = (int) UalApplication.getScreenSizePix(activity)[0] / 3;
        BITMAP_HEIGHT = (int) UalApplication.getScreenSizePix(activity)[1] / 5;
        questionImgArr = new ArrayList<Bitmap>();
        objIdArr = new ArrayList<String>();

    }

    public void setImageLoader() {
        imgLoader = ImageLoader.getInstance();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_five_fables) // resource or drawable
                .showImageForEmptyUri(R.drawable.ic_launcher) // resource or drawable
                .showImageOnFail(R.drawable.ic_launcher) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();

        final int memClass = ((ActivityManager) this.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new LruMemoryCache(memClass * 1024 * 1024 / 8))
                .memoryCacheSize(4 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .defaultDisplayImageOptions(options)
                .build();

        imgLoader.init(config);
    }

    public void setCommonVarilable() {
        Common.nickName = Common.getPreferences(getBaseContext(), "nickName");
        Common.phoneNum = Common.getPreferences(getBaseContext(), "phoneNum");
    }

    public void meQuery() {
        ParseQuery meQuery = ParseQuery.getQuery("UalMember");
        meQuery.whereEqualTo("nickName", Common.nickName);
        meQuery.getFirstInBackground(new GetCallback() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

            }

            @Override
            public void done(Object o, Throwable throwable) {
                Common.memberMe = (UalMember) o;
            }
        });
    }

    public boolean hasMoreQuestions(int compareCnt){
        boolean hasMore = false;
        Log.d("meme", " currentQuriedCnt => " + currentQuriedCnt + " compareCnt => " + compareCnt);
        if(compareCnt >= currentQuriedCnt){
            hasMore = true;
        }
        return hasMore;
    }

    public void imgFileQuery(int skipCnt, int limitCnt) {
        imgFileQuery = ParseQuery.getQuery("Question");
        imgFileQuery.orderByDescending("createdAt");

        if(!searchKeyword.equals("")) {
            Log.d("meme", "searchKeyword => " + searchKeyword);
            imgFileQuery.whereContains("QuestionTag", searchKeyword);
            searchKeyword="";
        }

//        try {
//            currentQuriedCnt = imgFileQuery.count();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        Log.d("meme", " skipCnt = " + skipCnt);
        Log.d("meme", " limitCnt = " + limitCnt);

        imgFileQuery.setSkip(skipCnt);
        imgFileQuery.setLimit(limitCnt);
        final long startTime = System.currentTimeMillis();
        UalApplication.showProgressDialog(BoardActivity.this, "Loading...");

        imgFileQuery.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(final List<ParseObject> parseObjects, ParseException e) {
                long endTime = System.currentTimeMillis();
                Log.d("meme", " time fast => " + (endTime - startTime) / 100.0f + "");

                if (e == null) {
                    currentQuriedCnt = parseObjects.size();
                    final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == 1) {
                                Log.d("meme", " what == 1");
                                imgAdp = new ImageAdapter(getBaseContext(), questionImgArr);
                                grMain.setAdapter(imgAdp);

                            } else if (msg.what == 2) {
                                Log.d("meme", " what == 2");
                                imgAdp.notifyDataSetChanged();
                            }
                            UalApplication.closeProgressDialog();
                        }
                    };

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (ParseObject po : parseObjects) {

                                try {
                                    final ParseFile pf = (ParseFile) po.get("questionImg");
                                    Log.d("meme", " Image URL => " + pf.getUrl());

                                    Log.d("meme", " questionImgArr.size() before = " + questionImgArr.size());
                                    questionImgArr.add(imgLoader.loadImageSync(pf.getUrl()));
                                    objIdArr.add(po.getObjectId());
                                    Log.d("meme", " questionImgArr.size() after = " + questionImgArr.size());
                                    Log.d("meme", " parseObjects.size() = " + parseObjects.size());



                                    Log.d("meme", " handler 0 scrollCnt = " + FIRST_SHOW_COUNT * scrollCnt);

                                    if (questionImgArr.size() == Math.floor(FIRST_SHOW_COUNT * scrollCnt / 2)) {    //절반값에 도달하면
                                        Log.d("meme", " handler 1 scrollCnt = " + scrollCnt);
                                        handler.sendEmptyMessage(1); // adapter set 한다.
                                    }
//

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    Log.d("meme", "Exception => " + e1.toString());
                                }
                            }
                            stackedQuriedCnt = stackedQuriedCnt + parseObjects.size();
                            Log.d("meme", "stackedQuriedCnt = " + stackedQuriedCnt);
                            if( questionImgArr.size() == stackedQuriedCnt){
                                Log.d("meme", " handler 2 scrollCnt = " + scrollCnt);
                                handler.sendEmptyMessage(2); // adapter notifyDataSetChange 한다.
                            }
                        }
                    }).start();

                    grMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getBaseContext(), DetailEachActivity.class);
                            String questionObjId = objIdArr.get(position);
                            intent.putExtra("questionObjId", questionObjId);
                            startActivity(intent);
                        }
                    });

                } else {
                    Log.d("meme", " exception e => " +e.toString());
                    UalApplication.closeProgressDialog();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        String url = "";

        switch (v.getId()) {

            case R.id.btnRefresh:
                Log.d("meme", "Sending push .... ");
                ParsePush push = new ParsePush();
                ArrayList<String> channels = new ArrayList<String>();
                channels.add("movie");
                channels.add("love");
                channels.add("trust");
                channels.add("drama");
                push.setChannels(channels); // Notice we use setChannels not setChannel
                Log.d("meme", "channels => " + channels.toString());
                push.setMessage("A new question is wating your answer... !!");
                push.sendInBackground(new SendCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("meme", " Sending Push Succeed !!");
                        } else {
                            Log.d("meme", " Sending Push Exception => " + e.toString());
                        }
                    }
                });

                Log.d("meme", "Push sent .... ");
                break;
            case R.id.btnCamera:
                // 카메라를 호출한다.
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                url = "camera_" + String.valueOf(System.currentTimeMillis()) + Common.nickName
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

                url = "gallery_" + String.valueOf(System.currentTimeMillis()) + Common.nickName
                        + ".jpg";

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

            case R.id.btnSearch:
                searchKeyword = edSearch.getText().toString();
                if(searchKeyword.equals("") || searchKeyword == null){
                    Toast.makeText(this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                }else{
                    setDataClear();

                    InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);

                    imgFileQuery(0, FIRST_SHOW_COUNT);
                    activityIndex = 1;
                    edSearch.setText("");

                }
                break;
            default:
                break;
        }

    }

    public void setDataClear(){
        stackedQuriedCnt = 0;
        questionImgArr.clear();
        objIdArr.clear();
    }
    @Override
    public void onBackPressed() {
        if(activityIndex == 1){
            searchKeyword="";
            setDataClear();
            activityIndex = 0;
            imgFileQuery(0, FIRST_SHOW_COUNT);
        }else if(activityIndex == 0){
            UalApplication.showYesNoDialog(this, "정말 끝내시게?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   finish();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    UalApplication.dialog.dismiss();
                }
            });
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

                    if (isFromGallery) {
                        moveIntent.putExtra("crop", cropUri.getPath());
                    } else {
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
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + Common.nickName
                + ".jpg";
        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                url));
        return uri;
    }

    private File getImageFile(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
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
        }

        return new File(path);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<Bitmap> mThumbIds;
        private float[] screenSize;
        boolean isOnceCalled = false;

        public ImageAdapter(Context c, ArrayList<Bitmap> bitmapList) {
            mContext = c;
            mThumbIds = bitmapList;
            screenSize = UalApplication.getScreenSize(BoardActivity.this);

        }

        public int getCount() {
            Log.d("meme", "mThumbIds.size() => " + mThumbIds.size());
            return mThumbIds.size();
        }

        public Object getItem(int position) {
            return mThumbIds.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams((int) UalApplication.getScreenSizePix(activity)[0] / 3, (int) UalApplication.getScreenSizePix(activity)[1] / 5));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(8, 8, 8, 8);

            } else {
                imageView = (ImageView) convertView;
            }

            Log.d("meme", " position = " + position + " mThumbIds " + mThumbIds.size());



            Log.d("meme", " position = " + position + " FIRST_SHOW_COUNT * scrollCnt " + FIRST_SHOW_COUNT * scrollCnt);
            if (position == FIRST_SHOW_COUNT * scrollCnt -1) {     // 위치가 끝에 이르면
                Log.d("meme", " position !! => " + position + " scrollCnt => " + scrollCnt + " FIRST_SHOW_COUNT * scrollCnt => " + FIRST_SHOW_COUNT * scrollCnt);
                scrollCnt++;
                isOnceCalled = true;
                Log.d("meme", " position !! => " + position + " scrollCnt => " + scrollCnt);
                int newQuerylimit = FIRST_SHOW_COUNT * scrollCnt;

                imgFileQuery(currentQuriedCnt, newQuerylimit);


            }

//            else if(position < FIRST_SHOW_COUNT * scrollCnt && isOnceCalled){
//                Log.d("meme", " else if !!");
//                scrollCnt--;
//            }

            if (position < mThumbIds.size()) {
                imageView.setImageBitmap(mThumbIds.get(position));
            }


            return imageView;
        }

    }

}
