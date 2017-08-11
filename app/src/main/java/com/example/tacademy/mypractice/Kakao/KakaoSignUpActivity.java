package com.example.tacademy.mypractice.Kakao;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.inputmethod.InputMethod;

import com.example.tacademy.mypractice.MainActivity;
import com.example.tacademy.mypractice.R;
import com.example.tacademy.mypractice.Util.LogUtil;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import java.util.HashMap;
import java.util.Map;

public class KakaoSignUpActivity extends Activity {

    private InputMethod.SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_sign_up);

        requestMe();
        onClickUnlink();
    }

    public void onClickSignup() {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("nickname", "leo");
        properties.put("age", "33");

        UserManagement.requestSignup(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                redirectMainActivity();
            }
            @Override
            public void onSessionClosed(final ErrorResult errorResult) {
                redirectLoginActivity();
            }
            @Override
            public void onFailure(final ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                LogUtil.log(getClass() + " requesetMe() onFailure(ErrorResult) " + message + errorResult.getException());

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }
        }, properties);
    }
    private void onClickUnlink() {
        final String appendMessage = getString(R.string.com_kakao_confirm_unlink);
        new AlertDialog.Builder(this)
                .setMessage(appendMessage)
                .setPositiveButton(getString(R.string.com_kakao_ok_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        Logger.e(errorResult.toString());
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        redirectLoginActivity();
                                    }

                                    @Override
                                    public void onNotSignedUp() {
                                        redirectLoginActivity();
                                    }

                                    @Override
                                    public void onSuccess(Long userId) {
                                        redirectLoginActivity();
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.com_kakao_cancel_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

    }
    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                redirectLoginActivity();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Logger.d("UserProfile : " + userProfile);
                redirectMainActivity();
            }

            @Override
            public void onNotSignedUp() {
//                showSignup();
            }
        });
    }
    private void redirectMainActivity(){
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void redirectLoginActivity(){
        final Intent intent = new Intent(this, KakaoLogin.class);
        startActivity(intent);
        finish();
    }
}
