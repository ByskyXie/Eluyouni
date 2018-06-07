package com.github.byskyxie.eluyouni;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mTelView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createFolder();
        //check login
        if(isLogin()){
            goToIndex();
        }
        // Set up the login form.
        mTelView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button signInButton = findViewById(R.id.button_sign_in);
        signInButton.setBackgroundColor(ContextCompat.getColor(this,R.color.colorGrayBlue));
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mTelView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String tel = mTelView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(tel)) {
            mTelView.setError(getString(R.string.error_field_required));
            focusView = mTelView;
            cancel = true;
        } else if (!isTelValid(tel)) {
            mTelView.setError(getString(R.string.error_invalid_email));
            focusView = mTelView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //登陆
            showProgress(true);
            mAuthTask = new UserLoginTask(tel, password);
            mAuthTask.execute((Void) null);
        }
    }

    private void goToIndex(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private boolean isTelValid(String tel) {
        //TODO: Replace this with your own logic
        return tel.length() == 11;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4 && password.length() <= 18;
    }

    /**
     * Shows the progress UI and hides the login form.
     * 登陆过程的进度条
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }

            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            //显示旋转动画
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }





    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPid;
        private final String mPwd;

        UserLoginTask(String pid, String password) {
            mPid = pid;
            mPwd = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String request = "http://"+IP_SERVER+":8080/"+"eluyouni/patient/login?"+"pid="+mPid+"&pwd="+mPwd;
            URL url = null;
            try {
                //链接服务器请求验证
                url = new URL(request);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStreamReader ins = new InputStreamReader( urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(ins);
                //获得结果
                String line = br.readLine();
                if(line.matches("failed.*")){
                    //说明失败
                    Log.w(".LoginActivity","Login failed");
                    return false;
                }else if(line.matches("accepted.*")){
                    //成功
                    Patient patient = new Patient();
                    for(int i=0;;i++){
                        line = br.readLine();
                        if(line == null)
                            break;
                        switch (i){
                            case 0:
                                patient.setPid(Long.parseLong( line.substring(line.indexOf('=')+1) ));
                                break;
                            case 1:
                                patient.setPsex( Integer.parseInt( line.substring(line.indexOf('=')+1)) );
                                break;
                            case 2:
                                patient.setPname( line.substring(line.indexOf('=')+1 ));
                                break;
                            case 3:
                                patient.setPwd( line.substring(line.indexOf('=')+1 ));
                                break;
                            case 4:
                                String s = line.substring(line.indexOf('=')+1 );
                                patient.setPicon( s );
                                break;
                            case 5:
                                patient.setEcoin( Long.parseLong(line.substring(line.indexOf('=')+1)) );
                                break;
                            case 6:
                                patient.setPscore( Integer.parseInt( line.substring(line.indexOf('=')+1)) );
                                break;
                        }
                    }
                    //保存到数据库
                    userInfo = patient;
                    userInfo.save();
                    if(patient.getPicon()!=null && !patient.getPicon().isEmpty()){
                        if( !downloadPicon(userInfo) ){ //下载头像
                            Log.e("LoginAct","download icon failed"+userInfo.getPicon());
                            downloadPicon(userInfo);
                        }
                    }
                }

            }catch (IOException ioe){
                Log.e(".LoginActivity",ioe.toString());
                return  false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //进入主页
                goToIndex();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

