package com.example.elixi.c;

import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import static com.example.elixi.c.MainActivity.arr;
import static com.example.elixi.c.MainActivity.fab1;
import static com.example.elixi.c.MainActivity.fab2;
import static com.example.elixi.c.MainActivity.fabCloce;
import static com.example.elixi.c.MainActivity.fabOpen;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link show.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link show#newInstance} factory method to
 * create an instance of this fragment.
 */
public class show extends Fragment {
    DBHelper myDb;

    ListView lv;
    private Toast toast;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public show() {
        // Required empty public constructor
    }


    public static show newInstance(String param1, String param2) {
        show fragment = new show();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_show, container, false);

        myDb = new DBHelper(getActivity());


        lv=(ListView)view.findViewById(R.id.list);
        Log.d(TAG, "onCreateView: "+arr.toString());
        //listview l=new listview();
        lv.setAdapter(new listview(){

        });

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        lv,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    arr.remove(position);
                                    myDb.setdb(arr);
                                    lv.setAdapter(new listview(){
                                    });
                                    if(arr.size()==0) {


                                            Snackbar.make(lv, "History is empty", 1000)
                                                    .setAction("Action", null).show();

                                        new Thread(new Runnable() {

                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(1100);
                                                    String TAG="";
                                                } catch (InterruptedException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                getActivity().runOnUiThread(new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        fab1.startAnimation(fabCloce);
                                                        fab2.startAnimation(fabOpen);
                                                        CameraFragment camerafragment = new CameraFragment();
                                                        FragmentManager manager = getActivity().getSupportFragmentManager();
                                                        manager.beginTransaction().replace(R.id.content_main, camerafragment, camerafragment.getTag()).commit();

                                                    }
                                                });


                                            }
                                        }).start();


                                    }

                                }
                            }
                        });

        lv.setOnTouchListener(touchListener);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    class listview extends BaseAdapter {
        @Override

        public int getCount() {
            return arr.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            view=getActivity().getLayoutInflater().inflate(R.layout.listview,null);
            FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.fab);
            TextView tvPraice=(TextView)view.findViewById(R.id.time);


            tvPraice.setText(arr.get(i).getTime());

            fab.setBackgroundTintList(ColorStateList.valueOf(arr.get(i).getFab()));
            return view;
        }
    }


}
