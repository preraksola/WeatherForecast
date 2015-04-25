package com.prerak.weather.android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.prerak.weather.android.R;


public class SettingsActivity extends ActionBarActivity {

    SharedPreferences mPreferences;
    RadioGroup mRadioGroupTemp, mRadioGroupUnit;
    RadioButton mCelsiusRadioButton, mFahrenheitRadioButton, mMetricRadioButton, mImperialRadioButton;
    Button mButton;
    ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, MODE_PRIVATE);
        setContentView(R.layout.activity_settings);
        mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#71C6FF")));
        mButton = (Button)findViewById(R.id.button);
        mRadioGroupTemp = (RadioGroup)findViewById(R.id.tempSettings);
        mCelsiusRadioButton = (RadioButton)findViewById(R.id.unitC);
        mFahrenheitRadioButton = (RadioButton)findViewById(R.id.unitF);
        mRadioGroupUnit = (RadioGroup)findViewById(R.id.unitSettings);
        mImperialRadioButton = (RadioButton)findViewById(R.id.unitI);
        mMetricRadioButton = (RadioButton)findViewById(R.id.unitM);

        if(mPreferences.contains(MainActivity.TEMP_UNIT)) {
            if (mPreferences.getString(MainActivity.TEMP_UNIT, null).equals("C"))
                mRadioGroupTemp.check(R.id.unitC);

            else if (mPreferences.getString(MainActivity.TEMP_UNIT, null).equals("F"))
                mRadioGroupTemp.check(R.id.unitF);
        }

        if(mPreferences.contains(MainActivity.UNIT)) {
            if (mPreferences.getString(MainActivity.UNIT, null).equals("metric"))
                mRadioGroupUnit.check(R.id.unitM);

            else if (mPreferences.getString(MainActivity.UNIT, null).equals("imperial"))
                mRadioGroupUnit.check(R.id.unitI);
        }

        mButton.setOnClickListener(mButtonClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up mButton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int selectedId = mRadioGroupTemp.getCheckedRadioButtonId();
            int selectedId1 = mRadioGroupUnit.getCheckedRadioButtonId();
            SharedPreferences.Editor editor = mPreferences.edit();

            if (selectedId == R.id.unitF)
                editor.putString(MainActivity.TEMP_UNIT, "F");

            else
                editor.putString(MainActivity.TEMP_UNIT, "C");

            if (selectedId1 == R.id.unitM)
                editor.putString(MainActivity.UNIT, "metric");

            else
                editor.putString(MainActivity.UNIT, "imperial");

            editor.apply();

            setResult(RESULT_OK);
            finishActivity(RESULT_OK);

            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            startActivity(mainIntent);
        }
    };

}
