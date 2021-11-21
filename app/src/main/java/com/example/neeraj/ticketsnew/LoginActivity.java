package com.example.neeraj.ticketsnew;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    CheckBox checkBox;
    EditText idText, passwordText;

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;
    private BackPressCloseHandler backPressCloseHandler;
    String autoLoginId, autoLoginPw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //뒤로 가기 버튼 2번 클릭시 종료
        backPressCloseHandler = new BackPressCloseHandler(this);

        idText= findViewById(R.id.insert_id);
        passwordText = findViewById(R.id.insert_password);
        btn_login = findViewById(R.id.btn_login);
        checkBox = findViewById(R.id.autoLogin);


        //자동 로그인을 선택한 유저
        if (!getPreferenceString(autoLoginId).equals("") && !getPreferenceString(autoLoginPw).equals("")) {
            checkBox.setChecked(true);
            checkAutoLogin(getPreferenceString(autoLoginId));
        }


        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String id = idText.getText().toString();
                String pw = passwordText.getText().toString();
                hideKeyboard();

                //로그인 정보 미입력 시
                if (id.trim().length() == 0 || pw.trim().length() == 0 || id == null || pw == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("알림")
                            .setMessage("로그인 정보를 입력바랍니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    //로그인 통신
                    LoginResponse();
                }
            }
        });
    }

    public void LoginResponse() {
        String userID = idText.getText().toString().trim();
        String userPassword = passwordText.getText().toString().trim();

        //loginRequest에 사용자가 입력한 id와 pw를 저장
        LoginRequest loginRequest = new LoginRequest(userID, userPassword);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        //loginRequest에 저장된 데이터와 함께 init에서 정의한 getLoginResponse 함수를 실행한 후 응답을 받음
        initMyApi.getLoginResponse(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                Log.d("TEST", "success1");

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    Log.d("TEST", "success2");

                    //response.body()를 result에 저장
                    LoginResponse result = response.body();

                    //받은 코드 저장
                    String resultCode = result.getResultCode();

                    //받은 토큰 저장
                    String token = result.getToken();

                    String success = "200"; //로그인 성공
                    String error = "401"; //로그인 실패

                    if (resultCode.equals(success)) {
                        String userID = idText.getText().toString();
                        String userPassword = passwordText.getText().toString();

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

                        Toast.makeText(LoginActivity.this, userID + "님 환영합니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userId", userID);
                        startActivity(intent);
                        LoginActivity.this.finish();

                    } else if (resultCode.equals(error)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("알림")
                                .setMessage("아이디가 존재하지 않거나 비밀번호가 틀렸습니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.2\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    //데이터를 내부 저장소에 저장하기
    public void setPreference(String key, String value){
        SharedPreferences pref = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //내부 저장소에 저장된 데이터 가져오기
    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        return pref.getString(key, "");
    }


    //키보드 숨기기
    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(idText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordText.getWindowToken(), 0);
    }

    //화면 터치 시 키보드 내려감
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //자동 로그인 유저
    public void checkAutoLogin(String id){

        //Toast.makeText(this, id + "님 환영합니다.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    //뒤로 가기 버튼 2번 클릭시 종료
    @Override public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}