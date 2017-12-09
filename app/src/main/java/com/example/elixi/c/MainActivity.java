package com.example.elixi.c;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements CameraFragment.OnFragmentInteractionListener,View.OnClickListener ,show.OnFragmentInteractionListener{
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);

    static FloatingActionButton fab1;
    static FloatingActionButton fab2;

    static Animation fabOpen;
    static Animation fabCloce;

    static boolean isOpen=false;
    DBHelper myDb;
    static ArrayList<DB > arr;

    private static final String TAG ="";
    static{
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "static initializer: OpenCV not loaded");
        }
        else{
            Log.d(TAG, "static initializer: OpenCV loaded");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fabOpen= AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabCloce= AnimationUtils.loadAnimation(this,R.anim.fab_close);

        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        fab1.startAnimation(fabCloce);
        fab2.startAnimation(fabOpen);

        arr=new ArrayList<DB>();
        myDb = new DBHelper(this);
        myDb.setarr();

        onClick(fab1);
    }

    static void animatefab(){

        if(isOpen){

            fab1.startAnimation(fabCloce);
            fab2.startAnimation(fabCloce);



            fab1.setClickable(false);
            fab2.setClickable(false);


            isOpen=false;
        }
        else{

            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);


            fab1.setClickable(true);
            fab2.setClickable(true);

            isOpen=true;
        }
    }




    @Override
    public void onFragmentInteraction(Uri uri) {

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.fab1):
                fab1.startAnimation(fabCloce);
                fab2.startAnimation(fabOpen);
                CameraFragment camerafragment =new CameraFragment();
                FragmentManager manager=getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.content_main,camerafragment,camerafragment.getTag()).commit();

                break;
            case (R.id.fab2):



                if(!arr.isEmpty()){
                    fab2.startAnimation(fabCloce);
                    fab1.startAnimation(fabOpen);
                    show show=new show();
                    FragmentManager manager2=getSupportFragmentManager();
                    manager2.beginTransaction().replace(R.id.content_main,show,show.getTag()).commit();


                }


                else{

                    Snackbar.make(fab1, "History is empty", 1000)
                            .setAction("Action", null).show();

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {

                                Thread.sleep(1100);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            MainActivity.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                  //  fab.setEnabled(true);

                                }
                            });
                        }
                    }).start();

                    // Toast.makeText(this, "History is empty", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }
}
