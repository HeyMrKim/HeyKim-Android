package com.example.neeraj.ticketsnew;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class register extends AppCompatActivity implements View.OnClickListener {

    private Button btRegister;
    private EditText etEmail, etPass,name;
    private TextView tvSignIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Integer c=1;
    private SignInButton mGoogleBtn;
    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG="Main_Activity";
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    private RetrofitClient retrofitClient;
    private initMyApi2 initMyApi2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // if(firebaseAuth.getCurrentUser()!=null){
        //start profile activity directly
        //   finish();
        // startActivity(new Intent(this,ProfileActivity.class));
        //}
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuthListner= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    //startActivity(new Intent(register.this,home.class));
                        //sendVerificationEmail();

                }

            }
        };

        progressDialog=new ProgressDialog(this);
        btRegister= findViewById(R.id.btRegister);
        etEmail= findViewById(R.id.etEmail);
        etPass= findViewById(R.id.etPass);
        tvSignIn= findViewById(R.id.tvSignIn);
        name=findViewById(R.id.name);

        mGoogleBtn=findViewById(R.id.googlebtn);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(register.this, "error occured", Toast.LENGTH_SHORT).show();

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        //btRegister.setOnClickListener(this);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupResponse();
            }
        });
        tvSignIn.setOnClickListener(this);

    }

    public void SignupResponse() {
        String userEmail = etEmail.getText().toString().trim();
        String userPassword = etPass.getText().toString().trim();
        String userName = name.getText().toString().trim();

        //loginRequest에 사용자가 입력한 id와 pw를 저장
        SignupRequest signupRequest = new SignupRequest(userEmail, userPassword, userName);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi2 = RetrofitClient.getRetrofitInterface2();

        //loginRequest에 저장된 데이터와 함께 init에서 정의한 getLoginResponse 함수를 실행한 후 응답을 받음
        initMyApi2.getSignupResponse(signupRequest).enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {

                Log.d("TEST", "success1");

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    Log.d("TEST", "success2");

                    //response.body()를 result에 저장
                    SignupResponse result = response.body();

                    //받은 코드 저장
                    String resultCode = result.getResultCode();

                    //받은 토큰 저장
                    String token = result.getToken();

                    String success = "200"; //로그인 성공
                    String error = "401"; //로그인 실패

                    if (resultCode.equals(success)) {
                        String userID = etEmail.getText().toString();
                        String userPassword = etPass.getText().toString();
/*
                        //다른 통신을 하기 위해 token 저장
                        setPreference(token,token);

                        //자동 로그인 여부
                        if (checkBox.isChecked()) {
                            setPreference(autoLoginId, userID);
                            setPreference(autoLoginPw, userPassword);
                        } else {
                            setPreference(autoLoginId, "");
                            setPreference(autoLoginPw, "");
                        }
*/
                        Toast.makeText(register.this, userID + "님 환영합니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(register.this, MainActivity.class);
                        intent.putExtra("userId", userID);
                        startActivity(intent);
                        register.this.finish();

                    } else if (resultCode.equals(error)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                        builder.setTitle("알림")
                                .setMessage("아이디가 존재하지 않거나 비밀번호가 틀렸습니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.1\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();

                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.2\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListner);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else {
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success" + task.isSuccessful());
                            startActivity(new Intent(register.this,MainActivity.class));
                        }
                        // If sign in fails, display a message to the user.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(register.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    private void registerUser()
    {
        String email=etEmail.getText().toString().trim();
        String password= etPass.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return; //stopping the function from executing further

        }

        if(TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        //if validations are ok we will show a progress bar
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    //user is successfully registered
                    UserProfile();
                    Toast.makeText(register.this, "Registered Successfully, Activate account", Toast.LENGTH_SHORT).show();
                    sendVerificationEmail();
                    //finish();
                    //startActivity(new Intent(register.this,login.class));

                }else {
                    Toast.makeText(register.this, "Failed to register. Please try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==btRegister)
        {
            registerUser();
        }
        if(view==tvSignIn)
        {
            //login activity
            startActivity(new Intent(this,login.class));
        }
    }

    private void sendVerificationEmail(){
       final FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "Verification email sent to " + user.getEmail());
                            Toast.makeText(register.this, "verification email sent", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(register.this, login.class));
                            finish();
                        } else {
                            Log.e(TAG, "sendEmailVerification failed!", task.getException());
                            Toast.makeText(register.this, "sendEmailVerification failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void UserProfile() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name.getText().toString().trim())
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TESTING", "User profile updated");
                            }
                        }
                    });
        }
    }
}
