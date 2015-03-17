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
import com.hellocafe.android.events.MyEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by jungyongjoo on 14. 12. 16..
 */
public class Dialog_Common extends DialogFragment {

    public static Dialog_Common newInstance(Bundle bundle) {
        Dialog_Common frag = new Dialog_Common();
        frag.setArguments(bundle);
        return frag;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_common, null);
        TextView dia_title = (TextView) view.findViewById(R.id.dia_title);
        TextView dia_msg1 = (TextView) view.findViewById(R.id.dia_msg1);
        TextView dia_msg2 = (TextView) view.findViewById(R.id.dia_msg2);
        Button dia_btn1 = (Button) view.findViewById(R.id.dia_btn1);
        Button dia_btn2 = (Button) view.findViewById(R.id.dia_btn2);

        if (getArguments().getString(Constants.DIA_BUNDLE_TITLE) != null)
            dia_title.setText(getArguments().getString(Constants.DIA_BUNDLE_TITLE));
        if (getArguments().getString(Constants.DIA_BUNDLE_MSG1) != null)
            dia_msg1.setText(getArguments().getString(Constants.DIA_BUNDLE_MSG1));
        if (getArguments().getString(Constants.DIA_BUNDLE_MSG2) != null)
            dia_msg2.setText(getArguments().getString(Constants.DIA_BUNDLE_MSG2));

        if (getArguments().getInt(Constants.DIA_BUNDLE_BTNCNT) == 0) {
            dia_btn1.setVisibility(View.GONE);
            dia_btn2.setVisibility(View.GONE);

        } else if (getArguments().getInt(Constants.DIA_BUNDLE_BTNCNT) == 1) {
            dia_btn1.setText(getArguments().getString(Constants.DIA_BUNDLE_BTN1MSG));
            dia_btn1.setTextAppearance(getActivity(), getArguments().getInt(Constants.DIA_BUNDLE_BTN1STYLE));
            dia_btn1.setVisibility(View.VISIBLE);
            dia_btn2.setVisibility(View.INVISIBLE);

        } else if (getArguments().getInt(Constants.DIA_BUNDLE_BTNCNT) == 2) {
            dia_btn1.setText(getArguments().getString(Constants.DIA_BUNDLE_BTN1MSG));
            dia_btn1.setTextAppearance(getActivity(), getArguments().getInt(Constants.DIA_BUNDLE_BTN1STYLE));
            dia_btn2.setText(getArguments().getString(Constants.DIA_BUNDLE_BTN2MSG));
            dia_btn2.setTextAppearance(getActivity(), getArguments().getInt(Constants.DIA_BUNDLE_BTN2STYLE));
            dia_btn1.setVisibility(View.VISIBLE);
            dia_btn2.setVisibility(View.VISIBLE);
        }


        dia_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyEvent event = new MyEvent(getArguments().getInt(Constants.DIA_BUNDLE_TYPE), Constants.DIA_RESULT_LEFT);
                EventBus.getDefault().post(event);
                dismiss();
            }
        });

        dia_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyEvent event = new MyEvent(getArguments().getInt(Constants.DIA_BUNDLE_TYPE), Constants.DIA_RESULT_RIGHT);
                EventBus.getDefault().post(event);
                dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }
}