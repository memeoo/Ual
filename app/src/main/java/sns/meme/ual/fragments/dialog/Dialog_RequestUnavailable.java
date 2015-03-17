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

public class Dialog_RequestUnavailable extends DialogFragment {
    private static String mReason;
    private static boolean mFinishAcivity;

    public static Dialog_RequestUnavailable newInstance(Bundle bundle) {
        Dialog_RequestUnavailable frag = new Dialog_RequestUnavailable();
        mReason = bundle.getString(Constants.INTENT_EXTRA_REQUEST_UNAVAILABLE_REASON);
        mFinishAcivity = bundle.getBoolean(Constants.INTENT_EXTRA_FINISH_ACTIVITY_AFTERWARDS);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_requestunavailable, null);

        TextView dia_msg = (TextView) view.findViewById(R.id.dia_msg);
        Button btn = (Button) view.findViewById(R.id.dia_btn);
        String message = null;
        if (mReason.equals(Constants.REQUEST_UNAVAILABLE_ACCEPTED)) {
            message = getResources().getString(R.string.request_unavailable_accepted);
        } else if (mReason.equals(Constants.REQUEST_UNAVAILABLE_CANCELED)) {
            message = getResources().getString(R.string.request_unavailable_canceled);
        } else if (mReason.equals(Constants.REQUEST_UNAVAILABLE_GENERIC)) {
            message = getResources().getString(R.string.request_unavailable_generic_error);
        }
        dia_msg.setText(message);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mFinishAcivity) {
                    getActivity().finish();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

}
