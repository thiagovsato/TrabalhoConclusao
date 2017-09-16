package br.com.thiago.trabalhoconclusao;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.facebook.login.LoginManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import br.com.thiago.trabalhoconclusao.crud.Price.PriceInsertActivity;
import br.com.thiago.trabalhoconclusao.crud.Product.ProductViewActivity;
import br.com.thiago.trabalhoconclusao.crud.Store.StoreViewActivity;
import br.com.thiago.trabalhoconclusao.fragments.AboutFragment;
import br.com.thiago.trabalhoconclusao.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String KEY_APP_PREFERENCES = "login";
    private final int PERMISSION_REQUEST_CODE = 0;
    private FirebaseAnalytics mFirebaseAnalytics;
    private RelativeLayout content_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(br.com.thiago.trabalhoconclusao.R.layout.activity_main);

        //Firebase
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseMessaging.getInstance().subscribeToTopic("mob");

        //Navigation
        Toolbar toolbar = (Toolbar) findViewById(br.com.thiago.trabalhoconclusao.R.id.toolbar);
        setSupportActionBar(toolbar);

        content_main = (RelativeLayout) findViewById(R.id.content_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(br.com.thiago.trabalhoconclusao.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, br.com.thiago.trabalhoconclusao.R.string.navigation_drawer_open, br.com.thiago.trabalhoconclusao.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(br.com.thiago.trabalhoconclusao.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new HomeFragment();
        changeFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        closeApp();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent = null;
        Fragment fragment = null;

        switch (id) {
            case br.com.thiago.trabalhoconclusao.R.id.nav_home:
                fragment = new HomeFragment();
                changeFragment(fragment);
                break;

            case br.com.thiago.trabalhoconclusao.R.id.nav_register:
                intent = new Intent(this, PriceInsertActivity.class);
                this.startActivity(intent);
                break;

            case br.com.thiago.trabalhoconclusao.R.id.nav_product_view:
                intent = new Intent(this, ProductViewActivity.class);
                this.startActivity(intent);
                break;

            case br.com.thiago.trabalhoconclusao.R.id.nav_store_view:
                intent = new Intent(this, StoreViewActivity.class);
                this.startActivity(intent);
                break;

            case br.com.thiago.trabalhoconclusao.R.id.nav_about:
                fragment = new AboutFragment();
                changeFragment(fragment);
                break;

            case br.com.thiago.trabalhoconclusao.R.id.nav_share:
                Intent sharingIntent = new Intent();
                sharingIntent.setAction(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message));
                startActivity(Intent.createChooser(sharingIntent,getString(R.string.share_using)));
                break;

            case br.com.thiago.trabalhoconclusao.R.id.nav_logout:
                logout();
                break;

            case br.com.thiago.trabalhoconclusao.R.id.nav_close:
                closeApp();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(br.com.thiago.trabalhoconclusao.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeFragment(Fragment fragment) {
        FrameLayout layout = (FrameLayout) findViewById(R.id.flContainer);
        layout.removeAllViewsInLayout();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContainer, fragment)
                .disallowAddToBackStack()
                .commit();
    }

    public void viewMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        this.startActivity(intent);
    }

    public void callMe(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:33858010"));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
        } else {
            this.startActivity(intent);
        }

    }

    public void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.logout_confirmation));
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                LoginManager.getInstance().logOut();
                                getApplicationContext().getSharedPreferences(KEY_APP_PREFERENCES, 0).edit().clear().apply();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                MainActivity.this.startActivity(intent);
                                finish();
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
