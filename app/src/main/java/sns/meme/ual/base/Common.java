package sns.meme.ual.base;

import java.text.SimpleDateFormat;
import java.util.Date;

//import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.project.whatthehell.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;

import sns.meme.ual.model.UalMember;

public class Common {
	public static final int PICK_FROM_CAMERA = 0;
	public static final int PICK_FROM_ALBUM = 1;
	public static final int CROP_FROM_CAMERA = 2;
    public static final int AFTER_UPLOAD_QUESTION = 10;
	
	public static final String BASIC_URL = "http://54.199.141.51:9090/";
	public static final String SENDER_ID = "457671552796";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "registration_id";
	
	public static final String QUESTION_KEYWORD = "QUESTION_KEYS";
	public static final String QUESTION_PAGE = "question.do";
	
	public static final String IMGFETCH_KEYWORD = "IMG_NAME_KEYS";
	public static final String IMG_NAME_PAGE = "imgname.do";
	
	public static final String MEMBER_KEYWORD = "MEMBER_KEYS";
	public static final String MEMBER_PAGE = "member.do";
	public static final String TAG_KEYWORD = "TAG_KEYS";
	public static final String TAG_PAGE = "tag.do";
	public static final String FETCH_TAG_PAGE = "fetchtag.do";
	public static final String FETCH_TAG_KEYWORD = "SELECT_TAG_KEYS";
	
	public static String phoneNum = "";
	public static String nickName = "";

    public static UalMember memberMe;
	
//	private static ImageLoaderConfiguration config;
//	private static DisplayImageOptions disOptions;
//	private static ImageLoader imgloader;

	public static final String IMG_FILE_PATH = "http://memehs.cafe24.com/img/";
	public static final String defVal = "defVal";
	
	public static SharedPreferences pref;


	
    // 값 불러오기
    public static String getPreferences(Context context, String key){
    	pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return pref.getString(key, defVal);
    }
     
    // 값 저장하기
    public static void savePreferences(Context context, String key, String value){
        pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }
     
    // 값(Key Data) 삭제하기
    public static void removePreferences(Activity activity, String key){
    	pref = activity.getSharedPreferences("pref", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }
     
    // 값(ALL Data) 삭제하기
    public static void removeAllPreferences(Activity activity){
    	pref = activity.getSharedPreferences("pref", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

	
	public static String getStrNow(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		String strNow = sdf.format(date);
		
		return strNow;
	}

    public static String getStrDateFromDate(Date date){
        SimpleDateFormat transFormat = new SimpleDateFormat("yy.MM.dd HH:mm");
        return  transFormat.format(date).toString();
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
