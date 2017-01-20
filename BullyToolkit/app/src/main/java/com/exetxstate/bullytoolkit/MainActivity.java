package com.exetxstate.bullytoolkit;

import android.graphics.Typeface;
import android.support.design.widget.TabItem;
import android.support.v4.app.FragmentManager;
//import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import android.app.FragmentTransaction;
//import android.app.actionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import layout.FragmentPageAdapter;
import layout.fragment1;
import layout.fragment2;
import layout.fragment3;

public class MainActivity extends AppCompatActivity {

    public Boolean incompleteField;
    public Button saveButton;
    public Button generateButton;
    public Toolbar toolBar;
    public ViewPager viewPager;
    private TabLayout tablayout;
    private FragmentPageAdapter adapter;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // getSupportActionBar().hide();
       // ActionBar = getActionBar();
        incompleteField = true;
        tablayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        toolBar = (Toolbar)findViewById(R.id.toolbar);
        adapter = new FragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);

        final TabLayout.Tab generateTab = tablayout.newTab();
        final TabLayout.Tab savedTab = tablayout.newTab();
        final TabLayout.Tab featuresTab = tablayout.newTab();
        final TabLayout.Tab settingsTab = tablayout.newTab();

        setSupportActionBar(toolBar);
//        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Bully.ttf");
//        TabItem generateTab = (TabItem) findViewById(R.id.generateTab);
//        TextView customFont = new TextView(this);
//        customFont.setText("Test");
//        customFont.setTypeface(tf);
//        generateTab.setCustomView(customFont);
        //generateTab.setText("Generate");
//        savedTab.setText("Saved");
//        featuresTab.setText("features");
//        settingsTab.setText("settings");

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Bully.otf");
        tablayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        tablayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.tab_indicator));
        tablayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        TextView[] tv = new TextView[tablayout.getTabCount()];
        tv[0] = (TextView)findViewById(R.id.textViewFont1);
        tv[1] = (TextView)findViewById(R.id.textViewFont2);
        tv[2] = (TextView)findViewById(R.id.textViewFont3);
        tv[3] = (TextView)findViewById(R.id.textViewFont4);
        for (int i = 0; i < tablayout.getTabCount(); i++) {
            tv[i].setText(adapter.getPageTitle(i));
            tv[i].setTypeface(tf);
            tablayout.getTabAt(i).setCustomView(tv[i]);
        }


//        Intent intent = getIntent();
//        String test;
//        //ArrayList<String> savedSentence = new ArrayList<>();
//        test = intent.getStringExtra("newList");
//        Log.d(TAG, test);

        //////////// Sending Data to saved tab //////////////////////
//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList("newList", savedSentence);
//         // Set fragment class arguments
//        fragment2 fragmentTwo = new fragment2();
//        fragmentTwo.setArguments(bundle);


//        tablayout.addTab(generateTab, 0);
//        tablayout.addTab(savedTab, 1);
//        tablayout.addTab(featuresTab, 2);
//        tablayout.addTab(settingsTab,3);
//
//
//        TextView generateText = (TextView)findViewById(R.id.textViewFont1);
//        TextView savedText = (TextView)findViewById(R.id.textViewFont2);
//        TextView featuresText = (TextView)findViewById(R.id.textViewFont3);
//        TextView settingsText = (TextView)findViewById(R.id.textViewFont4);
//
//
//        generateText.setText("Generate");
//        savedText.setText("Saved");
//        featuresText.setText("Features");
//        settingsText.setText("Settings");
//
//
//        savedText.setTypeface(tf);
//        generateText.setTypeface(tf);
//        featuresText.setTypeface(tf);
//        settingsText.setTypeface(tf);
//
//        savedTab.setCustomView(savedText);
//        generateTab.setCustomView(generateText);
//        featuresTab.setCustomView(featuresText);
//        settingsTab.setCustomView(settingsText);

        /*saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivityivty.this, secondActivity.this));
            }



        });
        */
//
//            fragment1 fragmentOne = new fragment1();
//            FragmentManager manager = getSupportFragmentManager();
//            FragmentTransaction transaction = manager.beginTransaction();
//            transaction.add(R.id.activity_main, fragmentOne, "firstFrag");
//            transaction.commit();


    }


//    public Boolean isIncomplete() {
//
//        if (ethnicityField.getText().toString().length() < 3) {
//            return true;
//        } else {
//            return false;
//        }
//
//    }


//    public void makeToast(View v) {
//        incompleteField = isIncomplete();
//        if (isIncomplete()) {
//            Toast.makeText(getApplicationContext(), "Needs more than 3 chars", Toast.LENGTH_LONG);
//        }
//
//    }



}


