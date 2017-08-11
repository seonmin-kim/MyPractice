package com.example.tacademy.mypractice.Kakao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.tacademy.mypractice.R;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class KakaoLogin extends AppCompatActivity {

    private Button loginBtn, logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login);
        init();
    }
    public void init(){
        loginBtn = (Button)findViewById(R.id.kakao_login_btn);
        logoutBtn = (Button)findViewById(R.id.kakao_logout_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), KakaoSimpleLogin.class));
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogout();
            }
        });
        }
    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                KakaoSignupActivity();
            }
        });
    }
    protected void KakaoSignupActivity() {
        final Intent intent = new Intent(this, KakaoSignUpActivity.class);
        startActivity(intent);
        finish();
    }

}
