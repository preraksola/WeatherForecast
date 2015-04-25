package com.prerak.weather.android.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.prerak.weather.android.R;
import com.prerak.weather.android.adapter.DrawerItemCustomAdapter;
import com.prerak.weather.android.fragment.ForecastListFragment;
import com.prerak.weather.android.fragment.TodayFragment;
import com.prerak.weather.android.view.DrawerItemView;
/*

This is the MainActivity class from where the application starts
 */

public class MainActivity extends ActionBarActivity{

    //Static variables for SharedPreferences
    public static final String APP_PREFERENCES = "AppPreferences";
    public static final String TODAY_DATA = "TODAY_DATA";
    public static final String FORECAST_DATA = "FORECAST_DATA";
    public static final String UNIT = "UNIT";
    public static final String TEMP_UNIT = "TEMP_UNIT";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";

    public static FragmentManager mFragmentManager;
    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    SharedPreferences mPreferences;
    ActionBar mActionBar;
    DrawerItemView[] mDrawerItemView;

    public static void newInstance(int position)
    {
        /*
            This method creates the new fragments based on the option selected from the navigation drawer
        */

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new TodayFragment();

                break;
            case 1:
                fragment = new ForecastListFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.data_fragment, fragment).commit();
        }

        else
        {
            Log.e("MainActivity","Error creating fragment");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#71C6FF")));
        mFragmentManager = getFragmentManager();

        mPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        if (!mPreferences.contains(MainActivity.TEMP_UNIT))
            editor.putString(MainActivity.TEMP_UNIT, "C");

        if (!mPreferences.contains(MainActivity.UNIT))
            editor.putString(MainActivity.UNIT, "metric");

        editor.apply();
        setupNavigationDrawer();
        FragmentManager manager = getFragmentManager();

        if(mDrawerList.getSelectedItemPosition() == 1)
            manager.beginTransaction().replace(R.id.data_fragment, new ForecastListFragment()).commit();

        else {
            manager.beginTransaction().replace(R.id.data_fragment, new TodayFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivityForResult(settingsIntent,RESULT_OK);
                break;

            case R.id.action_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("App by: Prerak Sola\n Awaiting Feedback!").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupNavigationDrawer()
    {
        /*
            Setup the navigation drawer
         */

        mTitle = mDrawerTitle = getTitle();
        mDrawerTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerItemView = new DrawerItemView[2];
        mDrawerItemView[0] = new DrawerItemView(R.drawable.ic_drawer_today_dark, mDrawerTitles[0]);
        mDrawerItemView[1] = new DrawerItemView(R.drawable.ic_drawer_forecast_dark, mDrawerTitles[1]);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.drawer_list_item, mDrawerItemView);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.ic_drawer,R.string.drawer_open,R.string.drawer_close)
        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                getSupportActionBar().setIcon(R.drawable.ic_arrow);
                getSupportActionBar().setLogo(R.drawable.ic_arrow);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            newInstance(position);
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mDrawerTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
}
