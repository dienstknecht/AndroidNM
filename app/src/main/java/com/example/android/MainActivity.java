package com.example.android;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState==null){
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, new ActivityChainFragment()).commit();
        }
        Button btStart = findViewById(R.id.btStart);
        btStart.setOnClickListener(this::onClickStart);
        Button btPlus = findViewById(R.id.btPlus);
    }

    public void onClickStart(View v){
        Button b = (Button) v;
        if(b.getText().equals(getResources().getString(R.string.startButton))) {
            b.setText(R.string.stopButton);
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,new ActivityStartedFragment()).commit();
        }else{
            b.setText(R.string.startButton);
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,new ActivityChainFragment()).commit();
        }

    }

    public static class ActivityStartedFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.activity_activitystarted,container,false);
            return rootView;
        }
    }

    public static class ActivityChainFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.activity_activitychain,container,false);
            Button btPlus = rootView.findViewById(R.id.btPlus);
            btPlus.setOnClickListener(this::onClickPlusButton);
            Button btSave = rootView.findViewById(R.id.btSave);
            btSave.setOnClickListener(this::onClickSaveButton);
            return rootView;

            /*
            ListView listView = (ListView)findViewById(R.id.listView);
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add("test");
            arrayList.add("Niklas");
            arrayList.add("Moritz");

            ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity,android.R.layout.simple_list_item_1);

            MlistView.setAdapter(arrayAdapter);
            */
        }

        public void onClickPlusButton(View v){
            //neues Element in der Liste hinzufügen
        }

        public void onClickSaveButton(View v){
            //Datenbankanbindung?
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void onClickSettings(View v){

    }
}
