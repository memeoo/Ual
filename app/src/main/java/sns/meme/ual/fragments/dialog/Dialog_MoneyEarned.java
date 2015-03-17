package sns.meme.ual.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hellocafe.android.R;
import com.hellocafe.android.model.Meeting;

public class Dialog_MoneyEarned extends DialogFragment {
    private static String mPrice;

    public static Dialog_MoneyEarned newInstance(Bundle bundle) {
        Dialog_MoneyEarned frag = new Dialog_MoneyEarned();
        mPrice = bundle.getString(Meeting.PRICE);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_money_earned, null);

        TextView dia_msg = (TextView) view.findViewById(R.id.dia_msg);
        Button btn = (Button) view.findViewById(R.id.dia_btn);
        dia_msg.setText(Html.fromHtml(getString(R.string.you_just_earned, mPrice)));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                getActivity().finish();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }
}
