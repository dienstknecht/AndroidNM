package com.example.android;

import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int secondsRunning;

    public static List<String> workout;
    private Runner runner;
    private static ArrayAdapter arrayAdapter;

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
        workout=new ArrayList<>();
    }

    public void onClickStart(View v){
        Button b = (Button) v;
        if(b.getText().equals(getResources().getString(R.string.startButton))) {
            b.setText(R.string.stopButton);
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            ActivityStartedFragment frag = new ActivityStartedFragment();
            fragmentTransaction.replace(R.id.container,frag).commit();
//            for(ListElement element :workout){
//                for(int i = 0;i<element.getSets();i++){
//                    int dauer = element.getDurationSeconds();
//                    Runner runner = new Runner(3); //spaeter: Sekundenanzahl anhand der Elemente in der Liste berechnen und dort eintragen
//                    runner.execute();//AsyncTask runner stoppen, falls Stop gedrückt - aber wie? und neu starten, wenn wieder start!!
//                }
//            }
            runner = new Runner(10);
            runner.start();
        }else{
            runner.interrupt();

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

    public void updateProgressBar(int percent){
        ProgressBar pb = findViewById(R.id.pbhWorkout);
        pb.setProgress(percent);
    }

    public static class ActivityChainFragment extends Fragment {

        private View rootView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            rootView = inflater.inflate(R.layout.activity_activitychain,container,false);
            Button btPlus = rootView.findViewById(R.id.btPlus);
            btPlus.setOnClickListener(this::onClickPlusButton);
            Button btSave = rootView.findViewById(R.id.btSave);
            btSave.setOnClickListener(this::onClickSaveButton);



            ListView listView = (ListView)rootView.findViewById(R.id.listView);
            workout = new ArrayList<String>();

            arrayAdapter = new ArrayAdapter(this.getContext(),R.layout.custom_list_item,workout);

            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            return rootView;
        }

        public void onClickPlusButton(View v){
            MainActivity.workout.add("Hinzufuegen");
            arrayAdapter.notifyDataSetChanged();
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

    public void startWorkoutThread(){

    }

    public void publishProgress(int progress){
        ProgressBar pb = findViewById(R.id.pbhWorkout);
        if(pb==null){
            Log.i("A","pb is null");
            return;
        }
        pb.setProgress((1<<16)-progress);
        if(pb.getProgress()==0) {
            Button b = findViewById(R.id.btStart);
            b.setText(getResources().getText(R.string.startButton));
            //Textview erstellen und in der Mitte der ProgressBar platzieren, Counter ablaufen lassen
            //Name der Aktivitaet ueber ProgressBar anzeigen
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ActivityChainFragment()).commit();
        }
    }
    public class Runner extends Thread {

        private long passed=0;
        private final long seconds;
        private long start;

        public Runner(long seconds) {
            this.seconds = (long) (seconds * 1e9);
        }

        @Override
        public void interrupt(){
            super.interrupt();
            passed=System.nanoTime()-start;
        }

        @Override
        public void run() {
            this.start = System.nanoTime();
            long current = System.nanoTime();
            while (current - start + passed < seconds) {
                Log.i("A", current - start + passed + "");
                publishProgress((int) (((current - start+passed) * (1<<16) / (seconds))));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                current = System.nanoTime();
            }
            publishProgress(1<<16);
        }

        public void publishProgress(Integer... integers) {
            final int p = integers[0];
            runOnUiThread(()-> MainActivity.this.publishProgress(p));
        }

    }
}
