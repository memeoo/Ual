package sns.meme.ual.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sns.meme.ual.R;
import sns.meme.ual.base.Common;
import sns.meme.ual.base.UalApplication;
import sns.meme.ual.model.UalMember;

public class DetailEachActivity extends Activity {

    private TextView tvQuestion, tvQuestionedAt;
    private EditText edAnswer;
    private Button btnAnswer;

    private String question;
    private Date questionedAt;

    private ParseFile pf;
    private ImageView imgPhoto;
    private LinearLayout llAddAnswer;

    private String qObjId;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvQuestion = (TextView)findViewById(R.id.tvQuestion);
        tvQuestionedAt = (TextView)findViewById(R.id.tvQuestionedAt);
        imgPhoto = (ImageView)findViewById(R.id.imgQuestion);
        llAddAnswer = (LinearLayout)findViewById(R.id.llAddAnswer);
        btnAnswer = (Button)findViewById(R.id.btnAnswer);
        edAnswer = (EditText)findViewById(R.id.edAnswer);
        edAnswer.setHint("Wating your answer ...");


        qObjId = getIntent().getStringExtra("questionObjId");
        ParseQuery questionQry = ParseQuery.getQuery("Question");
        questionQry.whereEqualTo("objectId", qObjId);
        questionQry.getFirstInBackground(new GetCallback() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Log.d("meme", " !!! ======= ");

            }

            @Override
            public void done(Object o, Throwable throwable) {
                Log.d("meme", " @@@ ======= ");
                ParseObject parseObject = (ParseObject)o;
                question = parseObject.getString("Question");
                pf = parseObject.getParseFile("questionImg");

                pf.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        if(e == null) {
                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 4;
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                            imgPhoto.setImageBitmap(bitmap);
//                            if(bitmap != null){
//                                bitmap.recycle();
//                            }
                        }
                    }
                });



                questionedAt = parseObject.getUpdatedAt();
                tvQuestion.setText(question);
//                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                tvQuestionedAt.setText(Common.getStrDateFromDate(questionedAt));
                getAnswerFromParse();
            }
        });

        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnswerToParse();
            }
        });
	}

    public void getAnswerFromParse(){
        ParseQuery answerQuery = ParseQuery.getQuery("Answer");
        answerQuery.whereEqualTo("questionObjId", qObjId);
        answerQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    addAnswerResult(list);
                } else {
                    Toast.makeText(DetailEachActivity.this, " Network나 통신 환경에 문제가 있습니다. " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void addAnswerResult(){
        TextView addedAnswer = new TextView(
                DetailEachActivity.this);
        addedAnswer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        addedAnswer.setTextSize(15);
        addedAnswer.setPadding(20, 0, 15, 0);
        addedAnswer.setText(Common.nickName + ": "+ edAnswer.getText() +"    " + Common.getStrNow());
        llAddAnswer.addView(addedAnswer);

        edAnswer.setText("");
    }

    public void addAnswerResult(List<ParseObject> list){

        for( ParseObject answer : list) {
//            TextView addedAnswer = new TextView(
//                    DetailEachActivity.this);
//            addedAnswer.setLayoutParams(new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT));
//            addedAnswer.setTextSize(15);
//            addedAnswer.setPadding(20, 5, 15, 5);


            ParseObject ualMember = (UalMember)answer.getParseObject("answerer");
//            Log.d("meme", " nickName ===> " + ualMember.getString("nickName"));
//            Log.d("meme", " answer ===> " + answer.getString("answer"));


//            addedAnswer.setText(ualMember.getString("nickName") + ": " + answer.getString("answer") + "    " + Common.getStrDateFromDate(answer.getUpdatedAt()));

            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.answer_row, null, false);
            LinearLayout addedRow = (LinearLayout)view.findViewById(R.id.llAdded_row);
            TextView ansContent = (TextView)addedRow.findViewById(R.id.tvAnswer);
            TextView answerer = (TextView)addedRow.findViewById(R.id.tvAnswerer);
            TextView upDatedAt = (TextView)addedRow.findViewById(R.id.tvUpdatedAt);

            try {
                ansContent.setText(answer.fetchIfNeeded().getString("answer"));
                answerer.setText(ualMember.fetchIfNeeded().getString("nickName") );
            } catch (ParseException e) {
                e.printStackTrace();
            }

            upDatedAt.setText(Common.getStrDateFromDate(answer.getUpdatedAt()));

            llAddAnswer.addView(addedRow);
        }

        edAnswer.setText("");
    }

    public void sendAnswerToParse(){
        UalApplication.showProgressDialog(DetailEachActivity.this, "Uploading...");
        ParseObject answerObj = new ParseObject("Answer");
        answerObj.put("answer", edAnswer.getText().toString());
        answerObj.put("answerer", Common.memberMe);
        answerObj.put("questionObjId", qObjId);
        answerObj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                UalApplication.closeProgressDialog();
                if(e==null){
                    addAnswerResult();
                }else{
                    Toast.makeText(DetailEachActivity.this," Network나 통신 환경에 문제가 있습니다. " + e.toString() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
