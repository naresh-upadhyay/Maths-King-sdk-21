package com.naresh.kingupadhyay.mathsking;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import static android.net.Uri.fromFile;
import static com.naresh.kingupadhyay.mathsking.CourseDetails.tempFile;
import static com.naresh.kingupadhyay.mathsking.PDFTools.openPDF;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PdfViewerActivity extends AppCompatActivity {


    private Toolbar toolbar;
    public File imageFile;
    private String pdfUri;
    private RelativeLayout relativeLayout;
    private AdView mAdView;
    String path;
    private ZoomableImageView imgView;
    Button btnPrevious, btnNext;
    int pageIndex;
    PdfRenderer pdfRenderer;
    PdfRenderer.Page curPage;
    ParcelFileDescriptor descriptor;
    private String name;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        MobileAds.initialize(this, "ca-app-pub-6924423095909700~8475665982");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.screenShot);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.appbarWebView).setOnTouchListener(mDelayHideTouchListener);
        findViewById(R.id.toolbar).setOnTouchListener(mDelayHideTouchListener);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        relativeLayout=findViewById(R.id.nameKing);
        relativeLayout.setVisibility(View.GONE);

        Intent intent = getIntent();
        name = intent.getStringExtra("keyName");
        //path = intent.getStringExtra("keyPath");
        path = new File( this.getExternalFilesDir( Environment.DIRECTORY_DOWNLOADS ), name ).toString();

        setTitle(name);
        TextView titletab = findViewById(R.id.titleb);
        titletab.setText(name);

        //initializing the views
        imgView = (ZoomableImageView)findViewById(R.id.pdfViewer);
        btnPrevious = (Button)findViewById(R.id.prev);
        btnNext = (Button)findViewById(R.id.next);


        //set click listener on buttons
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = curPage.getIndex()-1;
                displayPage(index);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = curPage.getIndex()+1;
                displayPage(index);
            }
        });
        btnPrevious.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_previous_item, 0, 0, 0);
        btnNext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_next_item, 0);


        ImageButton back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mVisible){
                    hide();
                    fab.setImageResource(R.drawable.arrow_collaps);

                }else{
                    show();
                    fab.setImageResource(R.drawable.arrow_expanded);
                }
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //delayedHide(2000);
        show();
    }


    @Override
    public void onStart() {
        super.onStart();
        try {
            openPdfRenderer();
            displayPage(pageIndex);
        } catch (Exception e) {
            Toast.makeText(PdfViewerActivity.this, "Sorry! This pdf is protected with password.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        try {
            closePdfRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }


    void openPdfRenderer(){
        File file = new File(path);
        descriptor = null;
        pdfRenderer = null;
        try {
            descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(descriptor);
        } catch (Exception e) {
            Toast.makeText(PdfViewerActivity.this, "There's some error", Toast.LENGTH_LONG).show();
        }

    }

    private void closePdfRenderer() throws IOException {
        if (curPage != null)
            curPage.close();
        if (pdfRenderer != null)
            pdfRenderer.close();
        if(descriptor !=null)
            descriptor.close();
    }

    void displayPage(int index){
        if(pdfRenderer.getPageCount() <= index)
            return;
        //close the current page
        if(curPage != null)
            curPage.close();
        //open the specified page
        curPage = pdfRenderer.openPage(index);
        //get page width in points(1/72")
        int pageWidth = curPage.getWidth();
        //get page height in points(1/72")
        int pageHeight = curPage.getHeight();
        //returns a mutable bitmap

        Bitmap bitmap = Bitmap.createBitmap(
                getResources().getDisplayMetrics().densityDpi * curPage.getWidth() / 72,
                getResources().getDisplayMetrics().densityDpi * curPage.getHeight() / 72,
                Bitmap.Config.ARGB_8888
        );
        //render the page on bitmap
        curPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        //display the bitmap
        imgView.setImageBitmap(bitmap);
        //enable or disable the button accordingly
        int pageCount = pdfRenderer.getPageCount();
        btnPrevious.setEnabled(0 != index);
        btnNext.setEnabled(index + 1 < pageCount);
    }



    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }




    private void takeScreenShot(View v,boolean shareCheck){
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        try {
            relativeLayout.setVisibility(View.VISIBLE);
            v.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
            v.setDrawingCacheEnabled(false);
            String filename= now +".jpeg";
            imageFile = new File(this.getExternalCacheDir(), filename);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            //setting screenshot in imageview
            popUpShareImage(imageFile,shareCheck);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            relativeLayout.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    private void share(final File imageFile){
        Uri uri = Uri.fromFile(imageFile);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent .setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Boost up your speed in mathematics with 0 cost");
        intent.putExtra(Intent.EXTRA_TEXT,"Click here to get FREE all mathematics formulas,shortcuts,concepts with examples and questions.  \nhttp://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        intent .putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(Intent.createChooser(intent,"Share Image"));
    }

    private void sendMail(final File imageFile){
        Uri uri = Uri.fromFile(imageFile);
        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check The Error ");
        intent.putExtra(Intent.EXTRA_TEXT, "There is an error present in this concept/question/answer please recheck it."+"\n[Edit here to say something about the problem...");
        intent.setData(Uri.parse("mailto:maths.developers@gmail.com")); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        intent .putExtra(Intent.EXTRA_STREAM, uri);

        try {
            startActivity(Intent.createChooser(intent, "Report feedback"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(PdfViewerActivity.this, "No email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    private void popUpShareImage(final File imageFile, final boolean shareCheck) {
        final Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.share_image);
        myDialog.setCancelable(false);
        final Button share = (Button) myDialog.findViewById(R.id.shareHere);
        Button cancel = (Button) myDialog.findViewById(R.id.cancelHere);
        ImageView imageView=(ImageView)myDialog.findViewById(R.id.shareImage);
        Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        imageView.setImageBitmap(ssbitmap);
        myDialog.show();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shareCheck)
                    share(imageFile);
                else
                    sendMail(imageFile);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
                myDialog.dismiss();
            }
        });
    }
    public void rateUs(){
        Context context=getApplicationContext();
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pdf_viewer_offline_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.last_share){
            RelativeLayout relativeLayout= findViewById(R.id.screenShot);
            takeScreenShot(relativeLayout,true);
            return true;
        }else if (id==R.id.rate_us){
            rateUs();
            return true;
        }else if (id==R.id.last_report){
            RelativeLayout relativeLayout= findViewById(R.id.screenShot);
            takeScreenShot(relativeLayout,false);
            return true;
        }else if (id==R.id.remove_file){
            File tempFile = new File( PdfViewerActivity.this.getExternalFilesDir( Environment.DIRECTORY_DOWNLOADS ), name );
            tempFile.delete();
            onBackPressed();

            return true;
    }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
