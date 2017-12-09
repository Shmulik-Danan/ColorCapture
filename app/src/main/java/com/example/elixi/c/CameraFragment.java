package com.example.elixi.c;

import android.content.Context;
import android.content.res.ColorStateList;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.elixi.c.MainActivity.arr;


public class CameraFragment extends Fragment implements View.OnTouchListener,
        CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "";
    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat mRgba;
    private Scalar mBlobColorHsv;
    private Scalar mBlobColorRgba;
    long startTime;
    int rowStart = 0;//rowStart
    int rowEnd = 0;//rowEnd
    int colStart = 0;//colStart
    int colEnd = 0;//colEnd
    int PlusdisRow=0;
    int PlusdisCol=0;

    float x = 0;
    float y = 0;
    float fabWidth;
    float fabHeight;

    float PrevDistance=0f;

    FloatingActionButton fab;
    //Square square;
    private boolean isOnClick =false;
    private boolean isItOK =false;
    private boolean doWork =false;
    private Object lock=new Object();

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getActivity()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(CameraFragment.this);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CameraFragment() {
        // Required empty public constructor
    }

    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
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
        Log.d(TAG, "onCreate: ");
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_camera, container, false);
        startTime = System.nanoTime();

        mOpenCvCameraView = (CameraBridgeViewBase) view.findViewById(R.id.opencv_tutorial_activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

       // square=(Square)view.findViewById(R.id.Square);

        mOpenCvCameraView.setOnTouchListener(this);
        fab=(FloatingActionButton)view.findViewById(R.id.fab);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        x =width-width/5;
        y=height/3;
        fabWidth= fab.getWidth();
        fabHeight=fab.getHeight();
        Log.d(TAG, "onCreateView: "+fab.getHeight());
       return view;
    }

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

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba= inputFrame.rgba();
        //int arr[]=getsize();

        rowStart = mRgba.rows()/3-PlusdisRow;//rowStart
        rowEnd = mRgba.rows()-mRgba.rows()/3+PlusdisRow;//rowEnd

        colStart = mRgba.cols()/3-PlusdisCol;//colStart
        colEnd = mRgba.cols()-mRgba.cols()/3+PlusdisCol;//colEnd

        fab.show();

        if(mRgba.submat(rowStart, rowEnd, colStart, colEnd)!=null) {

            Mat mat = mRgba.submat(rowStart, rowEnd, colStart, colEnd);
            Size wsize = mat.size();
            Imgproc.rectangle(mat, new Point(1, 1), new Point(wsize.width - 2, wsize.height - 2), new Scalar(0, 0, 0, 0), 7);
            mat.release();
               if (doWork) {
                   doWork = false;

                   Mat RegionRgba = mRgba.submat(rowStart, rowEnd, colStart, colEnd);
                   Mat RegionHsv = new Mat();
                   Imgproc.cvtColor(RegionRgba, RegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

                   mBlobColorHsv = Core.sumElems(RegionHsv);
                   int pointCount = RegionRgba.height() * RegionRgba.width();
                   for (int i = 0; i < mBlobColorHsv.val.length; i++)
                       mBlobColorHsv.val[i] /= pointCount;


                   mBlobColorRgba = convertScalarHsv2Rgba(mBlobColorHsv);

                   DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                   Calendar cal = Calendar.getInstance();

                   arr.add(new DB(dateFormat.format(cal.getTime()),
                           Color.rgb((int) mBlobColorRgba.val[0],
                           (int) mBlobColorRgba.val[1],
                           (int) mBlobColorRgba.val[2])));
                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                        public void run() {
                           fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb((int) mBlobColorRgba.val[0],
                                (int) mBlobColorRgba.val[1],(int) mBlobColorRgba.val[2])));


                        }
                   });
               }
        fab.setX(x - (fab.getWidth() / 2));
        fab.setY(y - (fab.getHeight() / 2));

        }
        return mRgba;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if((event.getAction()&MotionEvent.ACTION_MASK)==MotionEvent.ACTION_MOVE){
            //int SumRow=0,SumCol=0;
            if(event.getPointerCount()==2){
                float xx=event.getX(0)-event.getX(1);
                float yy=event.getY(0)-event.getY(1);
                float NewDistance= (float) Math.sqrt(xx * xx + yy * yy);
                if(NewDistance>PrevDistance){

                    if( (rowEnd-rowStart) +50<mRgba.rows()){
                        Log.d(TAG, "onTouch: (rowEnd-rowStart) +10 = "+((rowEnd-rowStart) +10)+
                                " <mRgba.rows() = "+ mRgba.rows()+"\n rowStart = "+rowStart+" rowEnd = "+rowEnd);
                        // SumRow+=5;
                        PlusdisRow+=5;
                    }
                    if((colEnd-colStart)+50< mRgba.cols()){///(colEnd-colStart)+10< mRgba.cols()
                        Log.d(TAG, "onTouch: (colEnd-colStart)+10 = "+((colEnd-colStart)+10)+
                                " <mRgba.cols() = "+ mRgba.cols()+"\n colStart = "+colStart+" colEnd = "+colEnd);
                        //SumCol+=5;
                        PlusdisCol+=5;//h
                    }

                }else {
                    if((colEnd-colStart)>mRgba.rows()+50){
                        if((colEnd-colStart)-10> mRgba.cols()- (mRgba.cols()- mRgba.cols()/10)){
                            //  SumCol-=5;
                            PlusdisCol-=5;//h
                        }
                    }
                    else {
                        if( (rowEnd-rowStart) -10>mRgba.rows()-(mRgba.rows()-mRgba.rows()/10)){
                            // SumRow-=5;
                            PlusdisRow-=5;
                        }
                        if((colEnd-colStart)-10> mRgba.cols()- (mRgba.cols()- mRgba.cols()/10)){
                            //SumCol-=5;
                            PlusdisCol-=5;//h
                        }

                    }

                }
                PrevDistance=NewDistance;

                // SetPlusdis(SumRow,SumCol);
            }
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                if(event.getPointerCount()==1){
                    if(event.getX()<x+fab.getWidth()/2&event.getX()>x-fab.getWidth()/2&
                            event.getY()<y+fab.getHeight()/2&event.getY()>y-fab .getHeight()/2     ){
                        isOnClick =true;

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(event.getPointerCount()==1&&isOnClick){
                    if(event.getX()<x+fab.getWidth()/2&event.getX()>x-fab.getWidth()/2&
                            event.getY()<y+fab.getHeight()/2&event.getY()>y-fab .getHeight()/2     ){
                        isOnClick =false;
                        doWork = true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:

                if(event.getPointerCount()==1){
                    Log.d(TAG, "onTouch: x = "+x+" y = "+y);
                    if(event.getX()<x+fab.getWidth()/2&event.getX()>x-fab.getWidth()/2&
                            event.getY()<y+fab.getHeight()/2&event.getY()>y-fab.getHeight()/2     ){
                        x=event.getX();
                        y=event.getY();
                        isOnClick =false;
                        // isItOK=true;
                    }
                }
                break;
            case MotionEvent.ACTION_BUTTON_PRESS:
                break;

        }
        return true;
    }

    @Override
    public void onPause() {
        fabWidth= fab.getWidth();
        fabHeight=fab.getHeight();
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    @Override
    public void onResume() {

        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, getActivity(), mLoaderCallback);


        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        }

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private Scalar convertScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }
    public void SetPlusdis( int SumRow,int SumCol){
        synchronized(lock) {
            PlusdisRow += SumRow;
            PlusdisCol += SumCol;
        }

    }
    public synchronized int[] getsize(){
        int arr[]=new int[2];
        synchronized(lock){
             arr[0]=PlusdisRow;
             arr[1]=PlusdisCol;

        }
    return arr;
    }
}
