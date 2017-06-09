package com.example.alexnelson.compositionapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Created by Alex Nelson on 21/03/2017.
 */

public class ModSurfaceView extends GLSurfaceView{

    private final ModGLRenderer mRenderer;
    static ScaleGestureDetector.OnScaleGestureListener mScaleListener=new ScaleListener();;
    static ScaleGestureDetector mScaleDetector=new ScaleGestureDetector(AppFragment.appContext, mScaleListener);


    public  ModSurfaceView(Context context, Bitmap bitmap){
        super(context);

        setEGLContextClientVersion(2);

        mRenderer=new ModGLRenderer();

        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mScaleDetector.onTouchEvent(e);
        if (!mScaleDetector.isInProgress()){
        mRenderer.processTouchEvent(e);}//touchstate being what happens when a touch event occurs
        return true;
    }
}
