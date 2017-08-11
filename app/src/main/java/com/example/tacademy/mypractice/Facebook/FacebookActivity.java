package com.example.tacademy.mypractice.Facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.tacademy.mypractice.R;
import com.example.tacademy.mypractice.Util.LogUtil;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class FacebookActivity extends AppCompatActivity {
    private final String TAG ="";
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private ProfileTracker profileTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        init();
        accessToken();
        profileTrack();
    }
    public void init(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(getBaseContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                getUserInfo(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getBaseContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                LogUtil.log(exception.getMessage().toString());
            }
        });
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
    }
    private void accessToken(){
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
    }
    private void profileTrack(){
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    public void getUserInfo(LoginResult loginResult){
        AccessToken accessToken = loginResult.getAccessToken();
        LogUtil.log("Token : " + accessToken.getToken());
        LogUtil.log("ApplicationId : " + accessToken.getApplicationId());
        LogUtil.log("UserId : " + accessToken.getUserId());
        LogUtil.log("Source : " + accessToken.getUserId());
        LogUtil.log("Token : " + "toString : " + accessToken.toString());
        LogUtil.log("Token : " + "Expires : " + accessToken.getExpires());

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        LogUtil.log( "getUserInfo(LoginResult) onCompleted(JSONObject, GraphResponse) : " + response.toString());
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
