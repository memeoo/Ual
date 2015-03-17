package sns.meme.ual.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hellocafe.android.R;
import com.hellocafe.android.activities.student.MatchActivity;
import com.hellocafe.android.activities.teacher.CoachSideDetailsActivity;
import com.hellocafe.android.base.Constants;
import com.hellocafe.android.model.Meeting;

/**
 * Created by jungyongjoo on 14. 11. 25..
 */
public class Dialog_CancelMeeting extends DialogFragment {

    private static int mCancelerId;
    private static String mUsername;
    private static boolean mCancelTooLate;
    private static boolean mCancelOnSite;
    private static boolean mIsUpdatedDialog;

    public static Dialog_CancelMeeting newInstance(Bundle bundle) {
        Dialog_CancelMeeting frag = new Dialog_CancelMeeting();
        mCancelerId = bundle.getInt(Meeting.CANCELER_TYPE);
        mUsername = bundle.getString(Constants.INTENT_EXTRA_USERNAME);
        mCancelTooLate = bundle.getBoolean(Constants.INTENT_EXTRA_CANCEL_TOO_LATE);
        mCancelOnSite = bundle.getBoolean(Constants.INTENT_EXTRA_CANCEL_ON_SITE);
        mIsUpdatedDialog = bundle.getBoolean(Constants.INTENT_EXTRA_WAITED_TOO_LONG);
        return frag;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_cancelmeeting, null);
        TextView dia_msg = (TextView) view.findViewById(R.id.dia_msg);
        Button btn1 = (Button) view.findViewById(R.id.dia_btn1);
        Button btn2 = (Button) view.findViewById(R.id.dia_btn2);

        Spanned message = null;
        if (mIsUpdatedDialog) {
            message = Html.fromHtml(getString(R.string.cancel_after_too_long, mUsername));
        } else {
            if (!mCancelTooLate) {
                message = Html.fromHtml(getString(R.string.cancel_without_fine_msg, mUsername));
            } else {
                if (mCancelOnSite) {
                    message = Html.fromHtml(getString(R.string.cancel_without_fine_msg_on_site, mUsername));
                } else {
                    message = Html.fromHtml(getString(R.string.cancel_with_fine_msg, mUsername));
                }
            }
        }

        dia_msg.setText(message);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mCancelerId) {
                    case Meeting.CANCELER_MEMBER:
                        if ((mCancelTooLate && !mCancelOnSite) || mIsUpdatedDialog) {
                            ((MatchActivity) getActivity()).onMemberCancel(false, true);
                        } else {
                            ((MatchActivity) getActivity()).onMemberCancel(false, false);
                        }
                        break;
                    case Meeting.CANCELER_COACH:
                        if ((mCancelTooLate && !mCancelOnSite) || mIsUpdatedDialog) {
                            ((CoachSideDetailsActivity) getActivity()).onCoachCancel(true);
                        } else {
                            ((CoachSideDetailsActivity) getActivity()).onCoachCancel(false);
                        }

                        break;
                }
                dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

}