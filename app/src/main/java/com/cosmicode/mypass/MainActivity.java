package com.cosmicode.mypass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cosmicode.mypass.view.MainFingerprintFragment;
import com.cosmicode.mypass.view.MainHomeFragment;
import com.cosmicode.mypass.view.MainOptionsFragment;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainFingerprintFragment.OnFingerprintListener, BottomNavigationView.OnNavigationItemSelectedListener, MainOptionsFragment.OnFragmentInteractionListener {

    // Variables
    private static String TAG = "MainActivity";
    @BindView(R.id.navigation_view)
    BottomNavigationView navigationView;
    @BindView(R.id.navigation_layout)
    ConstraintLayout navigationLayout;


    public static final Intent clearTopIntent(Context from) {
        Intent intent = new Intent(from, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigationView.setOnNavigationItemSelectedListener(this);
        openFragment(MainHomeFragment.newInstance(), "up");

    }

    @Override
    public void onResume() {
        super.onResume();
        challengeUser(true);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_view_home:
                MainHomeFragment homeFragment = MainHomeFragment.newInstance();
                openFragment(homeFragment, "left");
                return true;
            case R.id.navigation_view_options:
                MainOptionsFragment optionsFragment = MainOptionsFragment.newInstance();
                openFragment(optionsFragment, "right");
                return true;
            default:
                MainHomeFragment defaultFragment = MainHomeFragment.newInstance();
                openFragment(defaultFragment, "up");
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void openFragment(Fragment fragment, String start) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (start) {
            case "left":
                transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
                break;
            case "right":
                transaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, 0, 0);
                break;
            case "up":
        }
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public final void performLogout() {
        try {
            GoogleSignInOptions gso = (new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)).requestServerAuthCode(getString(R.string.default_web_client_id)).requestEmail().build();
            GoogleSignIn.getClient(this, gso).signOut();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        getJhiUsers().logout();
        startActivity(LoginActivity.clearTopIntent(this));
    }

    private void challengeUser(Boolean show) {
        if (show) {
            navigationLayout.setVisibility(View.INVISIBLE);
            openFragment(MainFingerprintFragment.newInstance(), "up");
        } else {
            navigationLayout.setVisibility(View.VISIBLE);
            openFragment(MainHomeFragment.newInstance(), "right");
        }
    }

    @Override
    public void onFingerprintSuccess(String string) {
        challengeUser(false);
    }

    @Override
    public void onFingerprintError(String error) {

    }
}
