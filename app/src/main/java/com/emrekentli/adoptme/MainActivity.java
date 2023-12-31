package com.emrekentli.adoptme;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.emrekentli.adoptme.database.TokenManager;
import com.emrekentli.adoptme.fragments.AdsFragment;
import com.emrekentli.adoptme.fragments.InboxFragment;
import com.emrekentli.adoptme.fragments.NewAdd_Fragment;
import com.emrekentli.adoptme.fragments.ProfileFragment;
import com.emrekentli.adoptme.fragments.WelcomeFragment;
import com.emrekentli.adoptme.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    FrameLayout fragmentLayout;
    private TokenManager tokenManager;
    ImageButton newAddButton;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    BottomNavigationView navigation;
    LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        versionControl();
        tokenManager = new TokenManager(this);
        if (tokenManager.getToken() == null) {
            redirectToLogin();
        }
        fragmentLayout = findViewById(R.id.fragmentLayout);
        newAddButton = findViewById(R.id.newAddCursor);


        navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        newAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                replaceFragments(NewAdd_Fragment.class);

            }
        });
        replaceFragments(AdsFragment.class);
    }

    private void redirectToLogin() {
        replaceFragments(WelcomeFragment.class);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragments(AdsFragment.class);
                    navigation.setClickable(false);

                    break;
                case R.id.navigation_dashboard:
                    replaceFragments(ProfileFragment.class);
                    navigation.setClickable(false);
                    break;
                case R.id.navigation_notifications:
                    replaceFragments(InboxFragment.class);
                    navigation.setClickable(false);
                    break;
            }
            //  loadFragment(selectedFragment);
            return true;
        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                loading = findViewById(R.id.loading);
                loading.setVisibility(View.GONE);
            }
        }

    };


    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment)
                .commit();
    }

    public void replaceFragmentsSearch(String value, Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        Bundle args = new Bundle();
        args.putString("searchValue", value);
        args.putString("city", "Hepsi");
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragmentLayout, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    public void update(String duyuru) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(" \n " + "YENİLİKLER \n \n" + duyuru + "\n" + "Uygulamayı güncellemeniz gerekmektedir. \n")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("GÜNCELLE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation


                        finish();
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }


                    }
                })

                .setNegativeButton("KAPAT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation

                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    public void versionControl() {
        handler.sendEmptyMessageDelayed(1, 3000);
    }

}