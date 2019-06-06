package com.naresh.kingupadhyay.mathsking;


import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public String name;
    public String id;
    private static DatabaseReference favourite;
    private DatabaseReference update;
    private RecyclerView mBlogList;
    protected String NAME="name";
    FirebaseUser firebaseUser;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-6924423095909700~8475665982");

        mAdView = findViewById(R.id.adVmain);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        name=pref.getString("name","Unknown Name");
        id=pref.getString("id","Unknown Id");

        try{
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
            String strDate = sdf.format(c.getTime());
            firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            update=FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid()).child("Last_online");
            update.onDisconnect().setValue(strDate);

        }catch (Exception e){
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        // Create Navigation drawer and inlfate layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mBlogList=(RecyclerView)findViewById(R.id.recycler_view);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));

        //int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem nav_update = menu.findItem(R.id.update);

        // set new title to the MenuItem
        String updateName="Update "+" V "+versionName;
        nav_update.setTitle(updateName);


        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        ImageView profilePic=headerView.findViewById(R.id.profilePic);

        try{

            Picasso.get().load(firebaseUser.getPhotoUrl()).placeholder(R.mipmap.mathsk)
                    .into(profilePic, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError(Exception e) {
                        }
                    });
        }catch (Exception e){
        }

        TextView userName=(TextView)headerView.findViewById(R.id.id_name);
        userName.setText(name);
        TextView phoneNumber = (TextView) headerView.findViewById(R.id.id_phone);
        phoneNumber.setText(id);
        isStoragePermissionGranted();
    }


    @Override
    protected void onStart() {
        super.onStart();

        try{

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("favourites")) {

                        favourite=FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid()).child("favourites");
                        favourite.keepSynced(true);
                        FirebaseRecyclerAdapter<dFavourite,ChapterViewHolder> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter <dFavourite, ChapterViewHolder>
                                (dFavourite.class,R.layout.content_determinants,ChapterViewHolder.class,favourite) {
                            @Override
                            protected void populateViewHolder(ChapterViewHolder viewHolder, dFavourite model, int position) {
                                viewHolder.setTitle(model.getTitle());
                                final SharedPreferences prefs=getSharedPreferences(NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor edt = prefs.edit();
                                edt.putBoolean(model.getTitle(),false);
                                edt.apply();
                            }
                        };
                        mBlogList.setAdapter(firebaseRecyclerAdapter);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }

    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder{
        View mView;
        private String tileText;
        private String NAME="name";
        private String KEY;

        public ChapterViewHolder(View itemView){
            super(itemView);
            mView=itemView;

        }

        public void setTitle(String title){
            tileText=title;
            KEY = tileText;
            final Activity myActivity=(Activity)(itemView.getContext());
            final SharedPreferences prefs=myActivity.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor edt = prefs.edit();
            edt.putBoolean(KEY,false);
            edt.apply();
        }

    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new Course(), "Course");
        adapter.addFragment(new ShortCut(), "Short Notes");
        adapter.addFragment(new Questions(), "Questions");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public  int backpress=0;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backpress = (backpress + 1);
            if(backpress<=1){
                Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
            }else{
                this.finish();
            }
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {

        //} else if (id == R.id.r_books) {

        //} else if (id == R.id.r_sites) {

        } else if (id == R.id.favourite) {
            SharedPreferences pref = getSharedPreferences("skip", Context.MODE_PRIVATE);
            boolean skip=pref.getBoolean("skip",false);

            if(!skip){
                Intent intent = new Intent(MainActivity.this, Favourites.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "Favourites is empty", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.downloaded) {
            Intent intent = new Intent(MainActivity.this, DownloadedList.class);
            startActivity(intent);


        } else if (id == R.id.update) {
            rateAndShare();

        } else if (id == R.id.share) {
            Context context=getApplicationContext();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT,"Boost up your speed in mathematics with 0 cost");
            intent.putExtra(Intent.EXTRA_TEXT,"Click here to get FREE all mathematics formulas,shortcuts,concepts " +
                    "with examples and questions."+"\nhttp://play.google.com/store/apps/details?id=" + context.getPackageName());

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
            startActivity(Intent.createChooser(intent,"Share app"));
        //} else if (id == R.id.settings) {
        } else if (id == R.id.r_us) {
            rateAndShare();

        } else if (id == R.id.a_us) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/naresh.upadhyay.96742"));
            startActivity(browserIntent);
        } else if (id == R.id.help) {
            Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback and Help related query");
            intent.putExtra(Intent.EXTRA_TEXT, "[Edit here...]");
            intent.setData(Uri.parse("mailto:maths.developers@gmail.com")); // or just "mailto:" for blank
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
            try {
                startActivity(Intent.createChooser(intent, "Send email using"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "No email client installed.", Toast.LENGTH_SHORT).show();
            }

        }else if (id ==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            SharedPreferences pref = getSharedPreferences("skip", Context.MODE_PRIVATE);
            SharedPreferences.Editor edt = pref.edit();
            edt.putBoolean("skip",false);
            edt.apply();
            Intent intent = new Intent(MainActivity.this,SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void rateAndShare(){
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

    String TAG = "king";
    public  void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isReadStoragePermissionGranted();
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED ) {
                Log.v(TAG,"Permission is granted");
                //return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                // return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            // return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    public void isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                //return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                //return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            //return true;
        }
    }


}
