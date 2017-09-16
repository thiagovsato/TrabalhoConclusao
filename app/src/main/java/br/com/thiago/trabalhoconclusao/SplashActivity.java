package br.com.thiago.trabalhoconclusao;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import br.com.thiago.trabalhoconclusao.login.ApiUtils;
import br.com.thiago.trabalhoconclusao.login.LoginAPI;
import br.com.thiago.trabalhoconclusao.model.User;
import br.com.thiago.trabalhoconclusao.db.UserDAO;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private LoginAPI mService;
    private static final String TAG = SplashActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(br.com.thiago.trabalhoconclusao.R.layout.activity_splash);
        getLoginData();
        load();
    }

    private void load() {
        Animation anim = AnimationUtils.loadAnimation(this, br.com.thiago.trabalhoconclusao.R.anim.animation_splash);
        anim.reset();
        ImageView iv = (ImageView) findViewById(br.com.thiago.trabalhoconclusao.R.id.ivLogoSplash);
        if (iv != null) {
            iv.clearAnimation();
            iv.startAnimation(anim);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void getLoginData() {
        mService = ApiUtils.getLoginAPI();
        final UserDAO dao = new UserDAO(this);

        mService.getUser()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Completed.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getUser failed.");
                    }

                    @Override
                    public void onNext(User user) {
                        if (!dao.doesItExist(user)){
                            if (dao.insert(user)) {
                                Log.d(TAG, "User insert: " + user.getUsername());
                            }else{
                                Log.d(TAG, "User error: " + user.getUsername());
                            }
                        }
                        else{
                            Log.d(TAG, "User already exists: "+user.getUsername());
                        }
                    }
                });
    }
}
