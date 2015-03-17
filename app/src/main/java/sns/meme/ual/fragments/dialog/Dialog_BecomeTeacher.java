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

/**
 * Created by jungyongjoo on 14. 11. 25..
 */
public class Dialog_BecomeTeacher extends DialogFragment {

    public static Dialog_BecomeTeacher newInstance(Bundle bundle) {
        Dialog_BecomeTeacher frag = new Dialog_BecomeTeacher();
        return frag;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_becometeacher, null);
        TextView dia_msg = (TextView) view.findViewById(R.id.dia_msg);
        Button btn = (Button) view.findViewById(R.id.dia_btn);
//        dia_msg.setText("dsfsdfsdf");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ;
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

}