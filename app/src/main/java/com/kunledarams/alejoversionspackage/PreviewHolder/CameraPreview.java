package com.kunledarams.alejoversionspackage.PreviewHolder;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by ok on 9/22/2018.
 */
@SuppressWarnings("deprecation")

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Context mcontext;
    private Camera mCamera;
    private SurfaceHolder mHolder;
    public CameraPreview(Context context, Camera camera) {
        super(context);
          this.mCamera= camera;
        // to get notification when the surfaceholder is created or destroy
        mHolder=getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Since the surface have being created, now we tell the camera where to draw the preview

        try{

            if(mCamera==null){

                mCamera.startPreview();
                mCamera.setPreviewDisplay(holder);

            }

        }catch (IOException e){
            Log.e(e.getMessage(),"Error with image capture");

        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //

       RefreshCamera(mCamera);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        mCamera.stopPreview();
        mCamera.release();
//        mCamera.startPreview();


    }

    public void RefreshCamera(Camera camera){
        if(mHolder.getSurface()==null){
            //preview surface does not exist
            return;
        }
        try {
            mCamera.stopPreview();
        }catch (Exception e){

        }

         setCamera(camera);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }catch (Exception e){

        }
    }

    private void setCamera(Camera camera) {

        mCamera=camera;
    }
}
