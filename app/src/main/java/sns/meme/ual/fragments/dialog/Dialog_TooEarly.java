package sns.meme.ual.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hellocafe.android.R;
import com.hellocafe.android.base.Constants;

/**
 * Created by jungyongjoo on 14. 11. 25..
 */
public class Dialog_TooEarly extends DialogFragment {
    private static String mMemberName;

    public static Dialog_TooEarly newInstance(Bundle bundle) {
        Dialog_TooEarly frag = new Dialog_TooEarly();
        mMemberName = bundle.getString(Constants.INTENT_EXTRA_USERNAME);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_tooearly, null);

        TextView dia_msg = (TextView) view.findViewById(R.id.dia_msg);
        Button btn = (Button) view.findViewById(R.id.dia_btn);
        dia_msg.setText(getResources().getString(R.string.s_045, mMemberName));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

}