package sns.meme.ual.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import sns.meme.ual.R;
import sns.meme.ual.base.Common;
import sns.meme.ual.base.UalApplication;
import sns.meme.ual.externaltools.RecyclingBitmapDrawable;
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
    private static final String TAG = BoardActivity.class.getSimpleName();

    private Activity activity;
    //    private MakeServerConnection fetchImgNameConnect;
    private ProgressDialog mProgress;
    private String mainShowType;
    private int pageNum;
    private ParseInstallation currentInstallation;
    private int BITMAP_WIDTH = 0, BITMAP_HEIGHT = 0;

    private final int IMAGE_COUNT_TO_SHOW_IN_ONE_SCREEN = 10;
    private final int THREAD_COUNT_TO_USE_FOR_DECODING = 3;

    private BitmapFactory.Options options;
    private ArrayList<byte[]> arrByteList;
    private ArrayList<ImageDecodingTask> ImageDecodingTaskArr;
    private ImageLoader imgLoader;

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


        setContentView(R.layout.activity_board);

        activity = BoardActivity.this;
        edSearch = (EditText) findViewById(R.id.edSearch);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        btnSetting = (Button) findViewById(R.id.btnSetting);

        btnSearch.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnSetting.setOnClickListener(this);

        Common.nickName = Common.getPreferences(getBaseContext(), "nickName");
        Common.phoneNum = Common.getPreferences(getBaseContext(), "phoneNum");

        BITMAP_WIDTH = (int) UalApplication.getScreenSizePix(activity)[0] / 3;
        BITMAP_HEIGHT = (int) UalApplication.getScreenSizePix(activity)[1] / 5;

//        imgFetchInfo = new ArrayList<String>();
//        mainShowType = "all";
//        pageNum = 1;
//        imgFetchInfo.add(pageNum + "");
//        imgFetchInfo.add(mainShowType);

        Log.d("meme", "nickName => " + Common.nickName);
        Log.d("meme", "phoneNum => " + Common.phoneNum);

        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));

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
                Common.memberMe = (UalMember) o;
                Log.d("meme", " BoardActivity Common.memberMe >>>> " + Common.memberMe.getObjectId());

                ParseQuery tagQuery = ParseQuery.getQuery("Tag");
                tagQuery.whereEqualTo("member", Common.memberMe);

                Log.d("meme", " MemberMe >>>> " + Common.memberMe.getObjectId());
                tagQuery.getFirstInBackground(new GetCallback() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {


                    }

                    @Override
                    public void done(Object o, Throwable throwable) {
//                        if (o != null) {
//                            Log.d("meme", " ==== >>>>>>>>>> ===== " + o.getClass());
//                            ParseObject po = (ParseObject) o;
//                            String tagNotSplited = po.getString("tag");
//
//                            String[] eachTags = tagNotSplited.split("#");
//
//                            Log.d("meme", " tags ==> " + tagNotSplited);
//
//                            currentInstallation = ParseInstallation.getCurrentInstallation();
//                            currentInstallation.addAllUnique("channels", Arrays.asList(eachTags));
//                            currentInstallation.saveInBackground(new SaveCallback() {
//                                @Override
//                                public void done(ParseException e) {
//                                    if (e == null) {
//                                        Log.d("meme", " Push Registration Finish !!");
//                                    } else {
//                                        Log.d("meme", " @@@@ e => " + e.toString());
//                                    }
//                                }
//                            });
//
//                        }
                    }
                });
            }
        });


        questionImgArr = new ArrayList<Bitmap>();
        grMain = (GridView) findViewById(R.id.glboard);
        imgFileQuery = ParseQuery.getQuery("Question");

//        UalApplication.showProgressDialog(this, "Loading...");
        imgFileQuery.orderByAscending("createdAt");
        imgFileQuery.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(final List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {

//                    ArrayList<ParseFile> pfList = new ArrayList<ParseFile>();

                    arrByteList = new ArrayList<byte[]>();


                    // =====
                    long startTime = System.currentTimeMillis();
                    for (ParseObject po : parseObjects) {

                        try {
                            ParseFile pf = (ParseFile) po.get("questionImg");
                            Log.d("meme", " Image URL => " + pf.getUrl());

                            // ===============


                            imgLoader.displayImage(pf.getUrl(), new ImageView(getBaseContext()),
                                    new SimpleImageLoadingListener() {
                                        @Override
                                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                            Log.d("meme", " Loaded Image => " + loadedImage);
                                            questionImgArr.add(loadedImage);
//                                            super.onLoadingComplete(imageUri, view, loadedImage);
                                            ImageAdapter imgAdp = new ImageAdapter(getBaseContext(), questionImgArr);
                                            grMain.setAdapter(imgAdp);
                                        }

                                        @Override
                                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                            Log.d("meme", " failReason => " + failReason);
                                            super.onLoadingFailed(imageUri, view, failReason);
                                        }

                                        @Override
                                        public void onLoadingCancelled(String imageUri, View view) {
                                            super.onLoadingCancelled(imageUri, view);
                                        }
                                    });


                            // =========

//                                    arrByteList.add(pf.getData());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            Log.d("meme", "Exception => " + e1.toString());
                        }
                    }

                    options = new BitmapFactory.Options();
//                    options.inSampleSize = Common.calculateInSampleSize(options, BITMAP_WIDTH, BITMAP_HEIGHT);
                    options.inSampleSize = 1;
                    options.inJustDecodeBounds = false;
//                    ImageDecodingTaskArr = new ArrayList<ImageDecodingTask>();

//                    setGrid();
                    // =====

//                    setOnFinishDownload(new OnFinishDownload() {
//                        @Override
//                        public void onFinish() {
//                            Log.d("meme", " onFinish !!");
//                            ImageAdapter imgAdp = new ImageAdapter(getBaseContext(), questionImgArr);
//                            grMain.setAdapter(imgAdp);
//                        }
//                    });
//
//                    ofd.onFinish();
                    grMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getBaseContext(), DetailEachActivity.class);
                            String questionObjId = parseObjects.get(position).getObjectId();
                            intent.putExtra("questionObjId", questionObjId);
                            startActivity(intent);
                        }
                    });

                } else {

                }
//                UalApplication.closeProgressDialog();
            }
        });


    }

    public int getMaxCountOfScreen() {
        int maxCnt = (arrByteList.size() / IMAGE_COUNT_TO_SHOW_IN_ONE_SCREEN) + 1;
        return maxCnt;
    }

    public void setGrid() {

//      int taskCountOfImgDecoding =  getMaxCountOfScreen() * THREAD_COUNT_TO_USE_FOR_DECODING;
        int taskCountOfImgDecoding = arrByteList.size();


        for (int i = 0; i < taskCountOfImgDecoding; i++) {
            ImageDecodingTaskArr.add(new ImageDecodingTask());
            try {
//                            ImageDecodingTaskArr.get(i).execute(arrByteList.get(3 * i), arrByteList.get(3 * i + 1), arrByteList.get(3 * i + 2));
                ImageDecodingTaskArr.get(i).execute(arrByteList.get(i));
            } catch (IndexOutOfBoundsException indexOutOfBounds) {
                Log.d("meme", " Index Out Of Bounds !!");

            }
        }

//        new ImageDecodingTask().execute(arrByteList.get(0));

    }

    public void setImgToGrid() {
        ImageLoader.getInstance().denyNetworkDownloads(false);
        for (int i = 0; i < arrByteList.size(); i++) {

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        String url = "";

        switch (v.getId()) {
            case R.id.btnSearch:

                break;
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
            mCursor = null;
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
            return position;
        }

        public long getItemId(int position) {
            return 0;
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

            imageView.setImageBitmap(mThumbIds.get(position));

//            if(imageView != null)
//            {
//                imageView.setImageBitmap(mThumbIds.get(position));
//                notifyDataSetChanged();  //Calling this helped to solve the problem.
//            }

            return imageView;
        }

    }

    class ImageDecodingTask extends AsyncTask<byte[], Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(byte[]... byteArrays) {

            Bitmap bitmapTo = null;
            try {
                for (int i = 0; i < byteArrays.length; i++) {
                    long startTime = System.currentTimeMillis();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrays[i], 0, byteArrays[i].length, options);
                    long endTime = System.currentTimeMillis();
                    Log.d("meme", "spendingTime Thread 1 => " + (endTime - startTime));
//                    questionImgArr.add(i,bitmap);
                    bitmapTo = bitmap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmapTo;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
//            ImageAdapter imgAdp = new ImageAdapter(getBaseContext(), questionImgArr);
            questionImgArr.add(result);
            ImageAdapter imgAdp = new ImageAdapter(getBaseContext(), questionImgArr);
            grMain.setAdapter(imgAdp);

        }
    }

//    class imgDecodingTask2 extends AsyncTask<byte [], Void, String> {
//
//        @Override
//        protected String doInBackground(byte []... byteArrays) {
//
//            String result = "";
//            try {
//                for(int i=0; i < byteArrays.length; i++) {
//                    long startTime = System.currentTimeMillis();
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrays[i], 0, byteArrays[i].length, options);
//                    long endTime = System.currentTimeMillis();
//                    Log.d("meme", "spendingTime Thread 2 => " + (endTime - startTime));
//
//                    questionImgArr.add(i+3,bitmap);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//
//
//        }
//    }
//
//    class imgDecodingTask3 extends AsyncTask<byte [], Void, String> {
//
//        @Override
//        protected String doInBackground(byte []... byteArrays) {
//
//            String result = "";
//            try {
//                for(int i=0; i < byteArrays.length; i++) {
//                    long startTime = System.currentTimeMillis();
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrays[i], 0, byteArrays[i].length, options);
//                    long endTime = System.currentTimeMillis();
//                    Log.d("meme", "spendingTime Thread 2 => " + (endTime - startTime));
//
//                    questionImgArr.add(i+6,bitmap);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//        }
//    }

}
