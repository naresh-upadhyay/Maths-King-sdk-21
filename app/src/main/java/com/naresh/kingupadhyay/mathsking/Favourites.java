package com.naresh.kingupadhyay.mathsking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.net.Uri.fromFile;
import static com.naresh.kingupadhyay.mathsking.CourseDetails.expansionButton;
import static com.naresh.kingupadhyay.mathsking.CourseDetails.tempFile;
import static com.naresh.kingupadhyay.mathsking.PDFTools.openPDF;


public class Favourites extends AppCompatActivity{

    private Toolbar toolbar;
    private RecyclerView mBlogList;
    private static DatabaseReference mDatabase;
    public ProgressDialog progDailog;
    public static Drawable draw;
    public static String topic;

    protected String NAME="name";
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        SharedPreferences prefs =getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear().apply();

        MobileAds.initialize(this, "ca-app-pub-6924423095909700~8475665982");
        mAdView = findViewById(R.id.adfav);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid()).child("favourites");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());

        mBlogList=(RecyclerView)findViewById(R.id.fav_myrecyclerview);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));

        // Adding Toolbar to Main screen
        toolbar = (Toolbar) findViewById(R.id.fav_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progDailog = new ProgressDialog(Favourites.this);
        progDailog.setTitle("Data Loading....");
        progDailog.setMessage("Please Wait.....");
        progDailog.setCancelable(false);
        progDailog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progDailog.show();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("favourites")) {

                }else{
                    Toast.makeText(getApplicationContext(), "Favourites is empty", Toast.LENGTH_SHORT).show();
                    progDailog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<dChapter,mChapterViewHolder> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter <dChapter, mChapterViewHolder>
                (dChapter.class,R.layout.favourite_contents,mChapterViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(mChapterViewHolder viewHolder, dChapter model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setAnswer(model.getAnswer());
                viewHolder.setQuestion(model.getQuestion());
                viewHolder.setImage_concept(model.getImage_concept());
                viewHolder.setAnswer_image(model.getAnswer_image());
                viewHolder.setQuestion_image(model.getQuestion_image());

                viewHolder.setConcept_pdf(model.getConcept_pdf());
                viewHolder.setQuestion_pdf(model.getQuestion_pdf());
                viewHolder.setAnswer_pdf(model.getAnswer_pdf());
                progDailog.dismiss();

            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class mChapterViewHolder extends RecyclerView.ViewHolder{
        View mView;
        private RelativeLayout rl_ques;
        private RelativeLayout rl_ans;
        public Button expand_ques;
        public Button expand_ans;
        private String tileText;
        private String questionText;
        private String answerText;
        private String conceptUrl;
        private String questionUrl;
        private String answerUrl;
        private String conceptPdfUrl;
        private String questionPdfUrl;
        private String answerPdfUrl;
        private String newTexttitle;


        private String NAME="name";
        private String KEY;
        final Resources res = itemView.getResources();
        final ImageButton favoriteImageButton = (ImageButton) itemView.findViewById(R.id.fav_add_favorite);



        public mChapterViewHolder(View itemView){
            super(itemView);
            mView=itemView;

            rl_ques=itemView.findViewById(R.id.fav_question_layout);
            rl_ques.setVisibility(View.GONE);
            expand_ques = (Button)itemView.findViewById(R.id.fav_expand_ques);
            expansionButton(rl_ques,expand_ques);

            rl_ans=itemView.findViewById(R.id.fav_answer_layout);
            rl_ans.setVisibility(View.GONE);
            expand_ans = (Button)itemView.findViewById(R.id.fav_expand_ans);
            expansionButton(rl_ans,expand_ans);

            final ImageView concept_Imag=(ImageView)itemView.findViewById(R.id.fav_concept_image);
            concept_Imag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    if ( tempFile(context,"Concept: "+tileText).isFile()) {
                        Intent intent = new Intent(context, PdfViewerActivity.class);
                        intent.putExtra("keyName","Concept: "+tileText+".pdf");
                        context.startActivity(intent);
                        return;
                    }else {

                        Intent intent = new Intent(context, Basic_activity.class);
                        intent.putExtra("pdfUrl", conceptPdfUrl);
                        intent.putExtra("topicN", topic);
                        intent.putExtra("titleNoti",tileText);
                        intent.putExtra("title", "Concept:");
                        context.startActivity(intent);
                    }
                }
            });
            final ImageView question_Imag=(ImageView)itemView.findViewById(R.id.fav_question_image);
            question_Imag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    if(TextUtils.isEmpty(conceptUrl)){
                        newTexttitle = questionText;
                    }
                    if ( tempFile(context,"Question: "+newTexttitle).isFile()) {
                        Intent intent = new Intent(context, PdfViewerActivity.class);
                        intent.putExtra("keyName","Question: "+newTexttitle+".pdf");
                        context.startActivity(intent);
                        return;
                    }else {
                        Intent intent = new Intent(context, Basic_activity.class);
                        intent.putExtra("pdfUrl", questionPdfUrl);
                        intent.putExtra("topicN", topic);
                        intent.putExtra("title", "Question:");
                        intent.putExtra("titleNoti", newTexttitle);
                        context.startActivity(intent);
                    }
                }
            });
            final ImageView answer_Imag=(ImageView)itemView.findViewById(R.id.fav_answer_image);
            answer_Imag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    if(TextUtils.isEmpty(conceptUrl)){
                        newTexttitle = questionText;
                    }
                    if ( tempFile(context,"Answer: "+newTexttitle).isFile()) {
                        Intent intent = new Intent(context, PdfViewerActivity.class);
                        intent.putExtra("keyName","Answer: "+newTexttitle+".pdf");
                        context.startActivity(intent);
                        return;
                        }else {
                            Intent intent = new Intent(context, Basic_activity.class);
                            intent.putExtra("pdfUrl", answerPdfUrl);
                            intent.putExtra("topicN", topic);
                            intent.putExtra("titleNoti", newTexttitle);
                            intent.putExtra("title", "Answer:");
                            context.startActivity(intent);
                        }
                }
            });

            final Activity myActivity=(Activity)(itemView.getContext());
            favoriteImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    KEY = tileText;
                    final SharedPreferences prefs=myActivity.getSharedPreferences(NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor edt = prefs.edit();

                    draw = res.getDrawable(R.drawable.favourite_blank);
                    favoriteImageButton.setImageDrawable(draw);

                    Query applesQuery = mDatabase.orderByChild("title").equalTo(tileText);
                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("CourseDetails", "onCancelled", databaseError.toException());
                        }
                    });

                    edt.putBoolean(KEY,true);
                    edt.apply();

                }
            });


        }

        public void setTitle(String title){
            tileText=title;
            topic=title;
            KEY = tileText;
            newTexttitle = title;
            final Activity myActivity=(Activity)(itemView.getContext());
            final SharedPreferences prefs=myActivity.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor edt = prefs.edit();
            edt.putBoolean(KEY,false);
            edt.apply();
            draw = res.getDrawable(R.drawable.favourite_fill);
            favoriteImageButton.setImageDrawable(draw);
            TextView title_text=(TextView)mView.findViewById(R.id.fav_concept);
            title_text.setText(title);
        }

        public void setQuestion(String question) {
            questionText=question;
            TextView question_text = (TextView)mView.findViewById(R.id.fav_question);
            question_text.setText(question);
        }

        public void setAnswer(String answer) {
            answerText=answer;
            TextView answer_text=(TextView)mView.findViewById(R.id.fav_answer);
            answer_text.setText(answer);
        }

        public void setImage_concept(String image_concept) {
            conceptUrl=image_concept;
            ImageView concept_Image=(ImageView) itemView.findViewById(R.id.fav_concept_image);
            Picasso.get().load(image_concept).into(concept_Image);

        }

        public void setQuestion_image(String question_image) {
            questionUrl=question_image;
            ImageView question_Image=(ImageView)mView.findViewById(R.id.fav_question_image);
            Picasso.get().load(question_image).into(question_Image);


        }
        public void setAnswer_image(String answer_image) {
            answerUrl=answer_image;
            ImageView answer_Image=(ImageView)mView.findViewById(R.id.fav_answer_image);
            Picasso.get().load(answer_image).into(answer_Image);

        }
        private void setConcept_pdf(String conceptPdf){
            conceptPdfUrl=conceptPdf;
        }private void setQuestion_pdf(String questionPdf){
            questionPdfUrl=questionPdf;
        }private void setAnswer_pdf(String answerPdf){
            answerPdfUrl=answerPdf;
        }
    }


}