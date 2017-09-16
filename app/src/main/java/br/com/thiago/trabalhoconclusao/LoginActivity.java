package br.com.thiago.trabalhoconclusao;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import br.com.thiago.trabalhoconclusao.db.UserDAO;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private TextInputLayout tilUsername, tilPassword;
    private CheckBox cbStaySigned;

    CallbackManager callbackManager;

    private final String KEY_APP_PREFERENCES = "login";
    private final String KEY_LOGIN = "login";
    private final String KEY_PASS = "password";
    private final String KEY_STAY = "staysigned";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(br.com.thiago.trabalhoconclusao.R.layout.activity_login);

        etUsername = (EditText) findViewById(br.com.thiago.trabalhoconclusao.R.id.etUsername);
        etPassword = (EditText) findViewById(br.com.thiago.trabalhoconclusao.R.id.etPassword);
        cbStaySigned = (CheckBox) findViewById(br.com.thiago.trabalhoconclusao.R.id.cbStaySigned);
        tilUsername = (TextInputLayout) findViewById(R.id.tilUsername);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);

        LoginButton loginButton = (LoginButton)
                findViewById(br.com.thiago.trabalhoconclusao.R.id.facebook_login_button);

        callbackManager = CallbackManager.Factory.create();
        if (isFacebookLoggedIn())
            facebookStartApp();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookStartApp();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, getString(R.string.facebook_cancel), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, getString(R.string.facebook_error), Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences pref = getSharedPreferences(KEY_APP_PREFERENCES,MODE_PRIVATE);
        etUsername.setText(pref.getString(KEY_LOGIN, ""));
        etPassword.setText(pref.getString(KEY_PASS, ""));
        cbStaySigned.setChecked(pref.getBoolean(KEY_STAY, false));

        if(isLoginOk()) {
            if (cbStaySigned.isChecked())
                staySigned();
            startApp();
        }

    }


    @Override
    public void onBackPressed() {
        closeApp();
    }

    public boolean isFacebookLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void login(View v)
    {
        if (etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty())
        {
            tilUsername.setError(getString(R.string.error_empty_username));
            tilPassword.setErrorEnabled(false);
        } else if (!etUsername.getText().toString().isEmpty() && etPassword.getText().toString().isEmpty()) {
            tilUsername.setErrorEnabled(false);
            tilPassword.setError(getString(R.string.error_empty_password));
        } else if (etUsername.getText().toString().isEmpty() && etPassword.getText().toString().isEmpty()) {
            tilUsername.setError(getString(R.string.error_empty_username));
            tilPassword.setError(getString(R.string.error_empty_password));
        } else {
            tilUsername.setErrorEnabled(false);
            tilPassword.setErrorEnabled(false);
            if (isLoginOk()) {
                if (cbStaySigned.isChecked())
                    staySigned();
                startApp();
            } else
                Toast.makeText(this, getString(R.string.invalid_login), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isLoginOk(){
        String login = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        UserDAO dao = new UserDAO(this);
        return dao.isValidLogin(login, password);
    }

    private void staySigned(){
        String login = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        Boolean staysigned = cbStaySigned.isChecked();

        SharedPreferences pref = getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_LOGIN, login);
        editor.putString(KEY_PASS, password);
        editor.putBoolean(KEY_STAY, staysigned);
        editor.apply();
    }


    private void startApp(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("Normal_login", etUsername.getText().toString());
        startActivity(intent);
        finish();
    }

    private void facebookStartApp(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("Facebook_login", "Logged-in");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }


    public void closeApp(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.exit_application));
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
