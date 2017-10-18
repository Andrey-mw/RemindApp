package com.android.dron.remindapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.dron.remindapp.fragments.FragmentTask;
import com.android.dron.remindapp.model.Note;
import com.android.dron.remindapp.setting.SettingActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Ok
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private FragmentTask fragmentTask;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private SharedPreferences sharedPreferences;
    private final String TAG = "Методы Activity: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String color = sharedPreferences.getString("color", "");
        Toast.makeText(getApplicationContext(), color + "", Toast.LENGTH_LONG).show();
        try {
            int id = Integer.parseInt(color);
            switch (id) {
                case 0:
                    setTheme(R.style.AppTheme_Red);
                    break;
                case 1:
                    setTheme(R.style.AppTheme_Pink);
                    break;
                case 3:
                    setTheme(R.style.AppTheme_Purple);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "No change!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        Log.e(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        initToolbar();
        initNavigationItemMenu();
        initFragment();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop();");

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState();");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy();");

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void initNavigationItemMenu() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.inflateMenu(R.menu.menu_navigation_item);
        navigationView.inflateHeaderView(R.layout.navigation_header);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.menu_add:
                        fragmentTask.openDialogFragmentTask();
                        break;

                    case R.id.menu_sort:
                        Cursor sort = fragmentTask.db.getSort();
                        refreshCursor(sort);
                        break;

                    case R.id.menu_setting:
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        break;
                }
                fragmentTask.adapter.notifyDataSetChanged();
                drawerLayout.closeDrawers();
                return true;

            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_toolbar_main_activity);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_toolbar_add:
                        fragmentTask.openDialogFragmentTask();
                        break;
                }
                return false;
            }
        });
    }

    private void initFragment() {
        fragmentTask = new FragmentTask();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.container, fragmentTask);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_main_activity, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_toolbar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor cursor;
                cursor = fragmentTask.db.getSearchList(newText);
                refreshCursor(cursor);
                return false;
            }
        });
        return true;
    }

    public void refreshCursor(Cursor cursor) {
        if (cursor != null) {
            if (fragmentTask.list == null) {
                fragmentTask.list = new ArrayList<>();
            } else {
                fragmentTask.list.clear();
            }
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                fragmentTask.list.add(new Note(cursor.getString(1), cursor.getString(2)));
                cursor.moveToNext();
            }
            cursor.close();
            fragmentTask.adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), "Опаньки!", Toast.LENGTH_LONG).show();
            fragmentTask.list.clear();
            fragmentTask.adapter.notifyDataSetChanged();
        }
    }
}
