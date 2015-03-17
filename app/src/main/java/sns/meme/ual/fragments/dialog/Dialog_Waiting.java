package sns.meme.ual.fragments.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.hellocafe.android.R;

/**
 * Created by jungyongjoo on 14. 11. 25..
 */
public class Dialog_Waiting extends DialogFragment {

    public static Dialog_Waiting newInstance() {
        Dialog_Waiting frag = new Dialog_Waiting();
        return frag;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        View view = inflater.inflate(R.layout.dialog_waiting, container, false);
        return view;
    }

}