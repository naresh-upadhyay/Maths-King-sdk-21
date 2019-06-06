package com.naresh.kingupadhyay.mathsking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity {

    public EditText fieldName;
    public EditText fieldPhoneNumber;
    public EditText fieldVerificationCode;
    public Button buttonStartVerification;
    public Button buttonVerifyPhone;
    public Button buttonResend;
    public Button buttonHelp;
    public TextView time;
    public TextView time_otp;
    public Spinner spinner;
    public String phoneNumber;
    public String name;
    private FirebaseAuth mAuth;
    public boolean check=true;
    public CountDownTimer timer;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public String mVerificationId;
    private static final String TAG = "LoginActivity";
    int ErrorCheck=0;
    private DatabaseReference DatabaseRef;
    protected String NAME="name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences pref = getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.clear().apply();

        DatabaseRef = FirebaseDatabase.getInstance().getReference().child("users");
        DatabaseRef.keepSynced(true);

        spinner=(Spinner) findViewById(R.id.spinnerCountries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, CountryData.countryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        fieldName=findViewById(R.id.name);
        fieldPhoneNumber=findViewById(R.id.number);
        fieldVerificationCode=findViewById(R.id.otp);
        buttonStartVerification=findViewById(R.id.send_code);
        buttonVerifyPhone=findViewById(R.id.verify);
        buttonResend=findViewById(R.id.resend_code);
        buttonHelp=findViewById(R.id.help);
        time=findViewById(R.id.time);
        time_otp=findViewById(R.id.time_otp);

        buttonStartVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=fieldName.getText().toString().trim();
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String number = fieldPhoneNumber.getText().toString().trim();
                if (name.isEmpty()){
                    fieldName.setError("Valid name is required");
                    fieldName.requestFocus();
                    return;
                }
                if (number.isEmpty() || number.length() < 10) {
                    fieldPhoneNumber.setError("Valid number is required");
                    fieldPhoneNumber.requestFocus();
                    return;
                }

                if(!isNetworkAvailable()){
                    Toast.makeText(LoginActivity.this, "Network not available ", Toast.LENGTH_LONG).show();
                    return;
                }
                phoneNumber = "+" + code + number;
                getOtp(phoneNumber);
                startTimer(60000,1000);
                buttonStartVerification.setVisibility(View.INVISIBLE);
                time_otp.setVisibility(View.VISIBLE);
                ErrorCheck=5;
            }
        });

        buttonVerifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = fieldVerificationCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    fieldVerificationCode.setError("Cannot be empty.");
                    return;
                }
                if (TextUtils.isEmpty(mVerificationId)){
                    Toast.makeText(LoginActivity.this, "OTP not received ", Toast.LENGTH_LONG).show();
                    return;
                }
                verifyPhoneNumberWithCode(mVerificationId, code);

            }
        });

        buttonResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check=false;
                String currentNumber=fieldPhoneNumber.getText().toString();
                if(currentNumber.equals(phoneNumber)){
                    Toast.makeText(LoginActivity.this, "Mobile Number changed ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                resendVerificationCode(phoneNumber, mResendToken);
                buttonResend.setVisibility(View.INVISIBLE);
                startTimer(60000,1000);
                ErrorCheck=6;

            }
        });

        fieldPhoneNumber.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() < 4 )
                {
                  buttonStartVerification.setVisibility(View.VISIBLE);
                  time_otp.setVisibility(View.INVISIBLE);
                  if(ErrorCheck==6 || ErrorCheck==5){
                      timer.cancel();
                      time.setVisibility(View.INVISIBLE);
                      buttonResend.setVisibility(View.INVISIBLE);
                  }
                  ErrorCheck=7;
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String number = fieldPhoneNumber.getText().toString().trim();
                if (number.isEmpty() || number.length() < 10) {
                    fieldPhoneNumber.setError("Valid number is required");
                    fieldPhoneNumber.requestFocus();
                    return;
                }
                if(!isNetworkAvailable()){
                    Toast.makeText(LoginActivity.this, "Network not available ", Toast.LENGTH_LONG).show();
                    return;
                }
                phoneNumber = "+" + code + number;

                sendMail();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d("", "onVerificationCompleted:" + credential);
                String code = credential.getSmsCode();
                if (code != null) {
                    fieldVerificationCode.setText(code);
                }
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                buttonStartVerification.setVisibility(View.VISIBLE);
                time.setVisibility(View.INVISIBLE);
                timer.cancel();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    fieldPhoneNumber.setError("Invalid country code or mobile number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {

                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d("", "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addUserDatabase(name,phoneNumber);
                            SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edt = pref.edit();
                            edt.putString("name",name);
                            edt.putString("id",phoneNumber);
                            edt.apply();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            timer.cancel();
                            finish();
                        }else if (ErrorCheck==7){
                            Toast.makeText(LoginActivity.this,"Mobile Number changed on run time", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            timer.cancel();
                            time.setVisibility(View.INVISIBLE);
                            buttonResend.setVisibility(View.VISIBLE);
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                fieldVerificationCode.setError("Invalid code.");
                            }
                        }
                    }
                });
    }

    public void addUserDatabase(String name,String id){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        ref.child("a_name").setValue(name);
        ref.child("b_id").setValue(id);

    }

    private void getOtp(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }


    public void startTimer(final long finish, long tick) {

        timer = new CountDownTimer(finish, tick) {

            public void onTick(long millisUntilFinished) {
               // buttonStartVerification.setVisibility(View.INVISIBLE);
                long remainedSecs = millisUntilFinished / 1000;
                if(check){
                    time.setVisibility(View.VISIBLE);
                    time.setText("" + (remainedSecs / 60) + ":" + (remainedSecs % 60));// manage it accordign to you
                }else{
                    time.setVisibility(View.VISIBLE);
                    time_otp.setVisibility(View.VISIBLE);
                    time_otp.setText("" + (remainedSecs / 60) + ":" + (remainedSecs % 60));// manage it accordign to you
                    time.setText("Wait...");
                }
            }

            public void onFinish() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser==null){
                    Toast.makeText(LoginActivity.this,"check messenger or wait for massage", Toast.LENGTH_LONG).show();
                    buttonResend.setVisibility(View.VISIBLE);
                    time.setVisibility(View.INVISIBLE);
                    time_otp.setVisibility(View.INVISIBLE);

                }
                cancel();
            }
        }.start();
    }

    public void sendMail(){

        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Unable to Login");
        intent.putExtra(Intent.EXTRA_TEXT, "Help me to login in MathsKing using "+phoneNumber+"\n[Edit here to say something about the problem...]");
        intent.setData(Uri.parse("mailto:maths.developers@gmail.com")); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.

        try {
            startActivity(Intent.createChooser(intent, "Send email using"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(LoginActivity.this, "No email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

}