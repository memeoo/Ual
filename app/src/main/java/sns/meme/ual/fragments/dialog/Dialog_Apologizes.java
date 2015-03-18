package sns.meme.ual.fragments.dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import sns.meme.ual.R;

import de.greenrobot.event.EventBus;

/**
 * Created by jungyongjoo on 14. 11. 25..
 */
public class Dialog_Apologizes extends DialogFragment {

    public static Dialog_Apologizes newInstance() {
        Dialog_Apologizes frag = new Dialog_Apologizes();
        return frag;

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_apologize, null);
        Button dia_btn = (Button) view.findViewById(R.id.dia_btn);
//        dia_msg.setText("@!@#@!!#");
        dia_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EventBus.getDefault().post(new MyEvent(""+Constants.DIA_APOLOGIZE));
                EventBus.getDefault().post(new MyEvent(Constants.DIA_APOLOGIZE));
                dismiss();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(MyEvent event) {
    }
}