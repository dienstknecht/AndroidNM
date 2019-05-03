package com.example.android;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, new ActivityChainFragment()).commit();
        }
        Button btStart = findViewById(R.id.btStart);
        btStart.setOnClickListener(this::onClickStart);
        Button btPlus = findViewById(R.id.btPlus);
        btStart.setOnClickListener(this::onClickPlus);
        Button btSave = findViewById(R.id.btSave);
        btStart.setOnClickListener(this::onClickSave);
    }

    public void onClickStart(View v){

    }

    public void onClickPlus(View v){

    }

    public void onClickSave(View v){

    }

    public static class ActivityChainFragment extends Fragment {
        public ActivityChainFragment(){

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.activity_activitychain,container,false);
            return rootView;
        }

        public void onClickPlusButton(View v){

        }

        public void onClickSaveButton(View v){

        }
    }
}
