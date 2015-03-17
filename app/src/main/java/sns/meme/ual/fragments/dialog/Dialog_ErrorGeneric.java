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

public class Dialog_ErrorGeneric extends DialogFragment {
    private static String mTitle;
    private static String mMessage;

    public static Dialog_ErrorGeneric newInstance(Bundle bundle) {
        Dialog_ErrorGeneric frag = new Dialog_ErrorGeneric();
        mTitle = bundle.getString(Constants.INTENT_EXTRA_ERROR_DIALOG_TITLE);
        mMessage = bundle.getString(Constants.INTENT_EXTRA_ERROR_DIALOG_MESSAGE);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_error_generic, null);

        TextView dia_title = (TextView) view.findViewById(R.id.dia_title);
        TextView dia_msg = (TextView) view.findViewById(R.id.dia_msg);
        Button btn = (Button) view.findViewById(R.id.dia_btn);

        dia_title.setText(mTitle);
        dia_msg.setText(mMessage);
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
