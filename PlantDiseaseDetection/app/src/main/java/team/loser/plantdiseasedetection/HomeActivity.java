package team.loser.plantdiseasedetection;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import team.loser.plantdiseasedetection.fragments.AboutFragment;
import team.loser.plantdiseasedetection.fragments.HistoryFragment;
import team.loser.plantdiseasedetection.fragments.PredictionFragment;
import team.loser.plantdiseasedetection.fragments.SolutionFragment;
import team.loser.plantdiseasedetection.models.DiseaseSolution;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private static final int FRAGMENT_PREDICT = 0;
    private static final int FRAGMENT_HISTORY = 1;
    private static final int FRAGMENT_ABOUT = 2;
    private static final int FRAGMENT_SOLUTION = 3;

    public int mCurrentFragment = FRAGMENT_PREDICT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,
                toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //transaction Fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_frame, new PredictionFragment());
        fragmentTransaction.commit();
        mCurrentFragment = FRAGMENT_PREDICT;

        navigationView.getMenu().findItem(R.id.nav_predict).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_predict){
            if(mCurrentFragment != FRAGMENT_PREDICT){
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.content_frame, new PredictionFragment());
                fragmentTransaction.commit();
                mCurrentFragment = FRAGMENT_PREDICT;
            }
        }
        else if(id == R.id.nav_history){
            if(mCurrentFragment != FRAGMENT_HISTORY){
                HistoryFragment historyFragment = new HistoryFragment();
                replaceFragment(historyFragment,historyFragment.HISTORY_FRAGMENT_NAME);
                mCurrentFragment = FRAGMENT_HISTORY;
            }
        }
        else if(id == R.id.nav_about){
            if(mCurrentFragment != FRAGMENT_ABOUT){
                AboutFragment aboutFragment = new AboutFragment();
                replaceFragment(aboutFragment,aboutFragment.ABOUT_FRAGMENT_NAME);
                mCurrentFragment = FRAGMENT_ABOUT;
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {


            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(HomeActivity.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 1500);

        }

    }

    private void replaceFragment(Fragment fragment,String fragmentName){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(fragmentName);
        transaction.commit();
    }
    public void ShowSolutionFragment(DiseaseSolution solution){
        if(mCurrentFragment != FRAGMENT_SOLUTION){
            SolutionFragment solutionFragment = new SolutionFragment(solution);
            replaceFragment(solutionFragment,solutionFragment.SOLUTION_FRAGMENT_NAME);
            mCurrentFragment = FRAGMENT_SOLUTION;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

//    public void onBackPressed() {
////        mCurrentFragment = FRAGMENT_PREDICT;
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
//        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
//            super.onBackPressed();
//        }
//    }




}