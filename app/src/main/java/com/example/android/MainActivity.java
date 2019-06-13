package com.example.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<ListItem> workout;
    private static ArrayAdapter arrayAdapter;
    private boolean finished;

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
        //TODO Liste aus DB laden
        workout=new ArrayList<>();
    }

    public void onClickStart(View v){
        ExecutorService e = Executors.newSingleThreadExecutor();
        Button b = (Button) v;
        ArrayList<ListItem> workout2 = new ArrayList<>();
        workout2.addAll(workout);
        if(b.getText().equals(getResources().getString(R.string.startButton))) {
            finished=false;
            b.setText(R.string.stopButton);
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            ActivityStartedFragment frag = new ActivityStartedFragment();
            fragmentTransaction.replace(R.id.container,frag).commit();
            List<Runner> runners = new ArrayList<>();
            for(int i = 0;i<workout.size();i++){
                runners.add(new Runner(workout.get(i).getDuration()));
            }
            for(Runner r : runners) {
                e.submit(r);
            }
            e.submit(new Runnable() {
                @Override
                public void run() {
                    workout=workout2;
                    finished=true;
                }
            });
            //Wieder hinzufuegen von workout-items in die liste nach ausfuehrung, und stopp-start fixen
        }else{
            e.submit(new Runnable() {
                @Override
                public void run() {
                    workout=workout2;
                    finished=true;
                }
            });
            e.shutdownNow();
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

        private View rootView;
        long itemId;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            rootView = inflater.inflate(R.layout.activity_activitychain,container,false);
            Button btPlus = rootView.findViewById(R.id.btPlus);
            btPlus.setOnClickListener(this::onClickPlusButton);
            Button btSave = rootView.findViewById(R.id.btSave);
            btSave.setOnClickListener(this::onClickSaveButton);
            Button btMinus = rootView.findViewById(R.id.btMinus);
            btMinus.setOnClickListener(this::onClickMinusButton);


            ListView listView = (ListView)rootView.findViewById(R.id.listView);

            arrayAdapter = new MyListAdapter(this.getContext(),R.layout.custom_list_item,workout);

            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            if(workout.isEmpty()) {
                itemId=0;
            }
            else{
                itemId = workout.get(workout.size()-1).getId();
            }
            return rootView;
        }

        public void onClickOkayButton(View v,EditText titleBox, EditText descriptionBox,AlertDialog dialog){
            if(titleBox.getText().toString().equals("")||descriptionBox.getText().toString().equals("")) {
                Toast.makeText(this.getContext(), "please specify a workout name and a duration", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            MainActivity.workout.add(new ListItem(titleBox.getText().toString(), Integer.parseInt(descriptionBox.getText().toString()),itemId));
            arrayAdapter.notifyDataSetChanged();
            dialog.dismiss();
        }

        public void onClickCancelButton(View v, EditText titleBox, EditText desriptionBox,AlertDialog dialog){
            dialog.dismiss();
        }

        public void onClickPlusButton(View v){
            LayoutInflater inflater = getLayoutInflater();
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_addworkout,null);
            layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
            final EditText titleBox = layout.findViewById(R.id.etworkoutname);
            // Notice this is an add method

// Add another TextView here for the "Description" label
            final EditText descriptionBox = layout.findViewById(R.id.etseconds);
            // Another add method
            AlertDialog.Builder dialog=new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Light_Dialog_Alert);
            dialog.setView(layout);
            // Again this is a set method, not add
            AlertDialog alertDialog = dialog.show();
            Button btOkay = layout.findViewById(R.id.btOkay);
            btOkay.setOnClickListener((view)->onClickOkayButton(view,titleBox,descriptionBox,alertDialog));
            Button btCancel = layout.findViewById(R.id.btCancel);
            btCancel.setOnClickListener((view)->onClickCancelButton(view,titleBox,descriptionBox,alertDialog));
            //neues Element in der Liste hinzufügen
        }

        public void onClickMinusButton(View v) {
            if (!workout.isEmpty()) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                builder.setTitle("Are you sure you want to delete your workout?");

                //if the response is positive in the alert
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //removing the item
                        workout.clear();

                        //reloading the list
                        arrayAdapter.notifyDataSetChanged();
                    }
                });

                //if response is negative nothing is being done
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                //creating and displaying the alert dialog
                android.support.v7.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();

                arrayAdapter.notifyDataSetChanged();
            }
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

    public void publishProgress(int progress,double seconds){
        TextView tvsekunden = findViewById(R.id.tvSekunden);
        ProgressBar pb = findViewById(R.id.pbhWorkout);
        TextView tvWorkout = findViewById(R.id.tvWorkoutName);
        if(pb==null||tvsekunden==null||tvWorkout==null){
            Log.i("A","pb or tv is null");
            return;
        }
        pb.setProgress((1<<16)-progress);
        tvsekunden.setText(String.valueOf((long)(seconds-progress/(1<<16))));
        if(!MainActivity.workout.isEmpty()){
            tvWorkout.setText(MainActivity.workout.get(0).getName());
        }
        if(pb.getProgress()==0&&!finished){
            workout.remove(0);
        }
        if(pb.getProgress()==0&&finished) {
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
            while (current - start + passed < seconds&&!Thread.currentThread().isInterrupted()) {
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
            final double seconds = this.seconds/1e9-(System.nanoTime()-start)/1e9+1;
            runOnUiThread(()-> MainActivity.this.publishProgress(p,seconds));
        }

    }

}
