package com.example.k.superbag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.k.superbag.Fragment.FirstPageFragment;
import com.example.k.superbag.Fragment.MemoFragment;
import com.example.k.superbag.activity.EditActivity;
import com.example.k.superbag.activity.SettingsActivity;
import com.example.k.superbag.utils.SuperbagDatabaseHelper;

import java.io.FileNotFoundException;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private Fragment[] fragments = new Fragment[3];
    private android.support.v4.app.FragmentManager fm;
    private int currentIndex = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        initDatabase();
        fm  = getSupportFragmentManager();
        showFragment(0);

    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //-----
        toolbar.getBackground().setAlpha(0);
    }

    private void initListener(){
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    //初始化数据库
    private void initDatabase(){
        SuperbagDatabaseHelper dbHelper = new SuperbagDatabaseHelper(this,"superbag.db",null,1);
        SQLiteDatabase sbDatabase = dbHelper.getWritableDatabase();
        //------测试插入数据
        dbHelper.insertToDB(sbDatabase,"电影","独立日",false,2,"6-28","");
    }

    //用于显示
    private void showFragment(int index){
        if (currentIndex == index){
            return;
        }
        currentIndex = index;
        FragmentTransaction transaction = fm.beginTransaction();
        hideAllFragment(transaction);
        Log.d("已隐藏","ssss");
        Log.d("现在是",currentIndex+"");
        switch (index){
            case 0:
                if (fragments[0] == null){
                    fragments[0] = new FirstPageFragment();
                    transaction.add(R.id.main_ll,fragments[0]);
                }
                transaction.show(fragments[0]);
                transaction.commit();
                Log.d("已显示","sss");
                break;
            case 1:
                if (fragments[1] == null){
                    fragments[1] = new MemoFragment();
                    transaction.add(R.id.main_ll,fragments[1]);
                }
                transaction.show(fragments[1]);
                transaction.commit();
                break;
            case 2:

                break;
        }
    }

    private void hideAllFragment(FragmentTransaction ft){
        for (Fragment fragment:fragments){
            if (fragment != null){
                ft.hide(fragment);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_firstPage) {
            showFragment(0);
        } else if (id == R.id.nav_memo) {
            showFragment(1);
        } else if (id == R.id.nav_diary) {

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_quit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
