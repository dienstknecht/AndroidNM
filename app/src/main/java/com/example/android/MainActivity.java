package com.example.android;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
            ActivityStartedFragment frag = new ActivityStartedFragment();
            fragmentTransaction.replace(R.id.container,frag).commit();
            Runner runner = new Runner(3); 
            runner.execute();
        }else{
            b.setText(R.string.startButton);
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,new ActivityChainFragment()).commit();
        }
    }

    public static class ActivityStartedFragment extends Fragment{

        private View rootView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.activity_activitystarted,container,false);
            this.rootView=rootView;
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
        }

        public void onClickPlusButton(View v){

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

    public class Runner extends AsyncTask<Void,Integer,Void>{

        private final long seconds;

        public Runner(int seconds){
            this.seconds = (long) (seconds*1e9);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            long start = System.nanoTime();
            long current = System.nanoTime();
            while(current-start<seconds){
                publishProgress((int)(((current-start)*100/seconds)));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                current=System.nanoTime();
            }
            publishProgress(100);
            return null;
        }

        @Override
        public void onProgressUpdate(Integer... integers){
            ProgressBar pb = findViewById(R.id.pbhWorkout);
            pb.setProgress(100-integers[0],true);
            if(pb.getProgress()==0){
                Button b = findViewById(R.id.btStart);
                b.setText(getResources().getText(R.string.startButton));
                //Textview erstellen und in der Mitte der ProgressBar platzieren, Counter ablaufen lassen
                //Name der Aktivitaet ueber ProgressBar anzeigen
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new ActivityChainFragment()).commit();
            }
        }
    }
}
