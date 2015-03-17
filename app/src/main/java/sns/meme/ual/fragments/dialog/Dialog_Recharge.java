package sns.meme.ual.fragments.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hellocafe.android.R;

/**
 * Created by jungyongjoo on 14. 11. 17..
 */
public class Dialog_Recharge extends DialogFragment {

    public static Dialog_Recharge newInstance() {
        Dialog_Recharge frag = new Dialog_Recharge();
        return frag;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_recharge, null);
        TextView dia_recharge_msg = (TextView) view.findViewById(R.id.dia_recharge_msg);
//        dia_recharge_msg.setText(getString(R.string.s_017) + getArguments().getString("user_email") + getString(R.string.s_018));
        dia_recharge_msg.setText(getString(R.string.s_017) + getString(R.string.s_018));
        Button dia_recharge_btn = (Button) view.findViewById(R.id.dia_recharge_btn);
        dia_recharge_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }
}
