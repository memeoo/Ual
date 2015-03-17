package sns.meme.ual.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.hellocafe.android.R;

/**
 * Created by jungyongjoo on 14. 11. 17..
 */
public class Dialog_NetworkEnable extends DialogFragment {

    public static Dialog_NetworkEnable newInstance() {
        Dialog_NetworkEnable frag = new Dialog_NetworkEnable();
        return frag;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_network_enable, null);
        Button dia_network_btn = (Button) view.findViewById(R.id.dia_network_btn);
        dia_network_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }
}
