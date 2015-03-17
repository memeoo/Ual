package sns.meme.ual.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.hellocafe.android.R;

/**
 * Created by jungyongjoo on 14. 11. 17..
 */
public class Dialog_LocationEnable extends DialogFragment {

    public static Dialog_LocationEnable newInstance() {
        Dialog_LocationEnable frag = new Dialog_LocationEnable();
        return frag;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_location_enable, null);
        Button dia_loc_btn = (Button) view.findViewById(R.id.dia_loc_btn);
        dia_loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

}
