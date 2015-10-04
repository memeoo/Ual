package sns.meme.ual.base;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

import sns.meme.ual.activities.BoardActivity;
import sns.meme.ual.model.UalMember;

/**
 * Created by Meme on 2015-03-13.
 */
public class UalApplication extends Application {

    private UalDao mDao;
    final static private String APP_KEY = "ldiwgq2of5c6p7p";
    final static private String APP_SECRET = "bl0yov8myn9hnmn";
    public static ProgressDialog progressDialog;
    public static ParseInstallation currentInstallation;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("meme", " ======= Ual Application !! ====== ");
        mDao = new UalDao(this);

        // Enable Parse.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(UalMember.class);
        Parse.initialize(this, "oR56d7okbrgB3IVamRsw2Cea6uIyUKFbvrsqhQpp", "Aom66hxpQLFbCZfWch5fmSF9ltnYr1u0e6SjQkXF");
        PushService.setDefaultPushCallback(this, BoardActivity.class);



        //                    ParsePush.subscribeInBackground("ualPush", new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e == null) {
//                                Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
//                            } else {
//                                Log.e("com.parse.push", "failed to subscribe for push", e);
//                            }
//                        }
//                    });




    }

//    public static void showProgressDialog(Context context){
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//    }


    public static void showProgressDialog(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
    }


    public static void closeProgressDialog() {
        progressDialog.dismiss();
    }

    public UalDao getDao() {
        return mDao;
    }

    public static float[] getScreenSize(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int screenWidth = metrics.widthPixels;
//        int screenHeight = metrics.heightPixels;

        float dipWidth = metrics.widthPixels / metrics.density;
        float dipHeight = metrics.heightPixels / metrics.density;

        float[] dipSizeScreen = {dipWidth, dipHeight};
        return dipSizeScreen;
    }

    public static float[] getScreenSizePix(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int screenWidth = metrics.widthPixels;
//        int screenHeight = metrics.heightPixels;

//        float dipWidth = metrics.widthPixels / metrics.density;
//        float dipHeight = metrics.heightPixels / metrics.density;

        float[] dipSizeScreen = {metrics.widthPixels, metrics.heightPixels};
        return dipSizeScreen;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }
}
